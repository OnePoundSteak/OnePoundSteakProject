package honda.onepoundsteakproject;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PreviewActivityFragment extends Fragment {
    private static final String TAG = PreviewActivityFragment.class.getSimpleName();
    private String API_KEY = "AIzaSyBV6AkKjK5ZUYbU-ntP5-qKhSJMVuSJufY";

    private UserInf mUserInf;

    private ArrayList<SpotInf> mSpotList;
    private ArrayList<SpotInf> mCheckedSpotList;
    private SpotInf mSelectSpotInf;

    private TextView mSpotNameTextView;
    private TextView mSpotTimeTextView;
    private TextView mSpotFareTextView;
    private ProgressDialog mpDialog;
    private NetworkImageView mSpotImageView;

    private Boolean mSpotListLoaded;
    private Boolean mRouteListLoaded;
    private Boolean mImageURLLoaded;


    public PreviewActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSelectSpotInf = null;

        mSpotNameTextView = null;
        mSpotImageView = null;
        mSpotTimeTextView = null;
        mSpotFareTextView = null;

        mUserInf = new UserInf(
                getArguments().getDouble("lon"),
                getArguments().getDouble("lat"),
                getArguments().getInt("money"),
                getArguments().getInt("time"));

        mpDialog = new ProgressDialog(getActivity());
        mpDialog.setMessage("Loading...");
        mpDialog.show();

        mSpotListLoaded = false;
        mRouteListLoaded = false;
        mImageURLLoaded = false;

        spotListRequest(mUserInf.lon, mUserInf.lat, mUserInf.time);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);

        mSpotNameTextView = (TextView) view.findViewById(R.id.spotNameText);
        mSpotTimeTextView = (TextView) view.findViewById(R.id.spotTimeText);
        mSpotFareTextView = (TextView) view.findViewById(R.id.spotFareText);
        mSpotImageView = (NetworkImageView) view.findViewById(R.id.spotImageView);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewActivityFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.contents, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        view.findViewById(R.id.changeSpotButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RequestDialogFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.contents, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        if (mSelectSpotInf != null) {
            mSpotNameTextView.setText(mSelectSpotInf.name);
            mSpotTimeTextView.setText(mSelectSpotInf.duration + "分");
            mSpotFareTextView.setText(mSelectSpotInf.fare + "円");

            if (!mSelectSpotInf.imageURL.equals("")) {
                AppController.getInstance().getRequestQueue();
                mSpotImageView.setImageUrl(mSelectSpotInf.imageURL, new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache()));
            }
        }
        return view;
    }


    private void spotListRequest(double lon, double lat, float time) {
        String tag_json_obj = "json_obj_req";
        String url = "https://gentle-basin-2840.herokuapp.com/place/" + lat + "/" + lon + "/" + time;
        Log.d("Access URL:", url);
        // ロード中表示
        mpDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mSpotList = parseJSONtoSpotList(response);
                        Log.d("spotList:", "loading comp!");

                        if (mSpotNameTextView != null && mSpotList.size() != 0) {
                            // 本用はソートするべき
                            mSelectSpotInf = mSpotList.get(15);
                        }

                        // 経路情報の取得
                        routeListRequest(mUserInf.lon, mUserInf.lat, mSelectSpotInf.lon, mSelectSpotInf.lat);

                        mSpotListLoaded = true;
                        if (mSpotListLoaded && mRouteListLoaded && mImageURLLoaded) {
                            mpDialog.hide();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mpDialog.hide(); // TODO ここもしっかり書き分けるべき

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        });

        // シングルトンクラスで実行
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void routeListRequest(double orgLon, double orgLat, double destLon, double destLat) {
        String tag_json_obj = "DirectionJson_obj_req";
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + orgLat + "," + orgLon +
                "&destination=" + destLat + "," + destLon +
                "&key=" + API_KEY +
                "&mode=transit&alternatives=true" +
                "&avoid=tolls|highways|ferries" +
                "&language=ja" +
                "&units=metric" +
                "&departure_time=" + "now";
        Log.d("Access URL:", url);

        // ロード中表示
        mpDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<RouteInf> routeList = parseJSONtoRouteList(response);
                        Log.d("spotList:", "loading comp!");
                        Log.d(">>>>>>>", routeList.get(0).distance + ", "
                                + routeList.get(0).duration + ", "
                                + routeList.get(0).fare + ", "
                                + routeList.get(0).startLat + ", "
                                + routeList.get(0).startLon + ", "
                                + routeList.get(0).endLat + ", "
                                + routeList.get(0).endLon);
                        // TODO 平均値にするなりなんなり
                        mSelectSpotInf.fare = routeList.get(0).fare;
                        mSelectSpotInf.duration = routeList.get(0).duration;
                        mRouteListLoaded = true;

                        // 画面に適用
                        mSpotNameTextView.setText(mSelectSpotInf.name);
                        mSpotFareTextView.setText(mSelectSpotInf.fare + "円");
                        mSpotTimeTextView.setText(mSelectSpotInf.duration + "分");

                        imgURLRequest(mSelectSpotInf.name);

                        if (mSpotListLoaded && mRouteListLoaded && mImageURLLoaded) {
                            mpDialog.hide();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mpDialog.hide(); // TODO ここもしっかり書き分けるべき

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        });

        // シングルトンクラスで実行
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private void imgURLRequest(String keyword) {
        String tag_json_obj = "imgSearch_obj_req";
        String url = "https://www.googleapis.com/customsearch/v1?"
                + "key=" + API_KEY
                + "&cx=" + "004164534463101160377:waf7n6e8twc"
                + "&searchType=image"
                + "&imgSize=large"
                + "&imgType=photo"
                + "&q=" + keyword;
        Log.d("Access URL:", url);

        // ロード中表示
        mpDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("よんだ", "いめーじぱーさー");
                        mSelectSpotInf.imageURL = parseJSONtoImageURL(response);
                        Log.d("imgURL:", "loading comp!");
                        // 画像のロード
                        if (!mSelectSpotInf.imageURL.equals("")) {
                            AppController.getInstance().getRequestQueue();
                            mSpotImageView.setImageUrl(mSelectSpotInf.imageURL, new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache()));
                        }
                        mImageURLLoaded = true;

                        if (mSpotListLoaded && mRouteListLoaded && mImageURLLoaded) {
                            mpDialog.hide();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mpDialog.hide(); // TODO ここもしっかり書き分けるべき

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                }
            }
        });

        // シングルトンクラスで実行
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private ArrayList<SpotInf> parseJSONtoSpotList(JSONObject jsondata) {
        ArrayList<SpotInf> ret = new ArrayList<SpotInf>();

        try {
            JSONArray spots = jsondata.getJSONArray("spots");
            for (int i = 0; i < spots.length(); i++) {
                JSONObject spot = spots.getJSONObject(i);
                ret.add(new SpotInf(
                        Integer.parseInt(spot.getString("data_id")),
                        spot.getString("name"),
                        Float.parseFloat(spot.getString("lat")),
                        Float.parseFloat(spot.getString("lon")),
                        Float.parseFloat(spot.getString("rate")),
                        Float.parseFloat(spot.getString("distance"))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("ListSize:", "" + ret.size());

        return ret;
    }

    private ArrayList<RouteInf> parseJSONtoRouteList(JSONObject jsondata) {
        ArrayList<RouteInf> ret = new ArrayList();
        // stateでOKかどうかも確認したほうがいいかも
        try {
            JSONArray routes = jsondata.getJSONArray("routes");

            for (int i = 0; i < routes.length(); i++) {
                JSONObject route = routes.getJSONObject(i);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                JSONArray steps = leg.getJSONArray("steps");
                JSONObject step = steps.getJSONObject(0);
                // --
                JSONObject distance = step.getJSONObject("distance");
                JSONObject duration = step.getJSONObject("duration");
                int fare;
                if (step.has("fare")) {
                    fare = step.getJSONObject("fare").getInt("value");
                } else {
                    fare = 0;
                }
                JSONObject startLoc = step.getJSONObject("start_location");
                JSONObject endLoc = step.getJSONObject("end_location");
                // --
                ret.add(new RouteInf(
                        distance.getDouble("value") / 1000, // mで返されているのでkmに換算
                        duration.getInt("value") / 60, // 秒で返されているので分に換算
                        fare,
                        startLoc.getDouble("lat"),
                        startLoc.getDouble("lng"),
                        endLoc.getDouble("lat"),
                        endLoc.getDouble("lng")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("ListSize:", "" + ret.size());

        return ret;
    }

    private String parseJSONtoImageURL(JSONObject jsondata) {
        String ret = "";

        try {
            JSONObject item = jsondata.getJSONArray("items").getJSONObject(0);
            ret = item.getString("link");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("imgURL:", ret);

        return ret;
    }

}
