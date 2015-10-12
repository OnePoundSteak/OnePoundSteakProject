package honda.onepoundsteakproject;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;

/**
 * A placeholder fragment containing a simple view.
 */
public class PreviewActivityFragment extends Fragment {
    private static final String TAG = PreviewActivityFragment.class.getSimpleName();
    private String API_KEY = "AIzaSyBV6AkKjK5ZUYbU-ntP5-qKhSJMVuSJufY";

    private UserInf mUserInf;

    //検索で絞ったリスト
    private ArrayList<SpotInf> mSpotList;
    //履歴用のリスト
    private ArrayList<SpotInf> mCheckedSpotList;
    //現在表示しているもの
    private SpotInf mSelectSpotInf;

    private TextView mSpotNameTextView;
    private TextView mSpotTimeTextView;
    private TextView mSpotFareTextView;
    private ProgressDialog mpDialog;
    private NetworkImageView mSpotImageView;

    private Boolean mSpotListLoaded;
    private Boolean mRouteListLoaded;
    private Boolean mImageURLLoaded;
    private Boolean mLoading;
    private long mStartTime;
    private ImageLoader mImageLoader;


    public PreviewActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCheckedSpotList = new ArrayList();
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
        mpDialog.setCancelable(false);
        mpDialog.setMessage("検索中です...");
        mSpotListLoaded = false;
        mRouteListLoaded = false;
        mImageURLLoaded = false;
        mLoading = false;
        mStartTime = System.currentTimeMillis();

        mImageLoader = new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache());

        spotListRequest(mUserInf.lon, mUserInf.lat, mUserInf.time);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_preview, container, false);

        mSpotNameTextView = (TextView) view.findViewById(R.id.spotNameText);
        mSpotTimeTextView = (TextView) view.findViewById(R.id.spotTimeText);
        mSpotFareTextView = (TextView) view.findViewById(R.id.spotFareText);
        mSpotImageView = (NetworkImageView) view.findViewById(R.id.spotImageView);
        mSpotImageView.setDefaultImageResId(android.R.drawable.spinner_background);
        mSpotImageView.setErrorImageResId(android.R.drawable.ic_dialog_alert);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ViewActivityFragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.contents, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                /*
                GoogleMapとの連携
                 */
                try{
                    Uri uri = Uri.parse(
                            "geo:0.0?q=" + mSelectSpotInf.lat + "," + mSelectSpotInf.lon
                    );
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    //アプリがなかったときのエラー処理
                    //Toast.makeText(getActivity(), "画像がないです", Toast.LENGTH_LONG).show();
                }
            }
        });

        view.findViewById(R.id.changeSpotButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((System.currentTimeMillis() - mStartTime) > 600) {
                    mStartTime = System.currentTimeMillis();
                    if (mSpotList.size() != 0) {
                        Log.d("行ける！", "");
                        if (mSelectSpotInf != null) {
                            mCheckedSpotList.add(mSelectSpotInf);
                        }
                        mSelectSpotInf = mSpotList.get(0);
                        mSpotList.remove(0);

                        mRouteListLoaded = false;
                        mImageURLLoaded = false;
                        routeListRequest(mUserInf.lon, mUserInf.lat, mSelectSpotInf.lon, mSelectSpotInf.lat);
                    } else {
                        Log.d("みつからんやで...", "");
                        Toast.makeText(getActivity(), "見つかりませんでした...", Toast.LENGTH_LONG).show();
                    }
                }
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
        // TODO ロード中表示
        if(!mLoading) {
            mpDialog.show();
            mLoading = true;
        }
        mSpotListLoaded = false;
        mRouteListLoaded = false;
        mImageURLLoaded = false;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mSpotList = parseJSONtoSpotList(response);
                        Log.d("spotList:", "loading comp!");

                        if (mSpotList.size() != 0) {
                            if(mSelectSpotInf != null){
                                mCheckedSpotList.add(mSelectSpotInf);
                            }
                            mSelectSpotInf = mSpotList.get(0);
                            mSpotList.remove(0);

                            // 経路情報の取得
                            routeListRequest(mUserInf.lon, mUserInf.lat, mSelectSpotInf.lon, mSelectSpotInf.lat);
                        }else{
                            // みつかりませんでした
                            mSpotNameTextView.setText("ごめんなさい。見つかりませんでした。");
                            mSpotFareTextView.setText("");
                            mSpotTimeTextView.setText("");
                            mSelectSpotInf = null;
                            // TODO: 画像を削除
                        }

                        mSpotListLoaded = true;
                        if (mSpotListLoaded && mRouteListLoaded && mImageURLLoaded) {
                            mpDialog.hide();
                            mLoading = false;
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
        if(!mLoading) {
            mpDialog.show();
            mLoading = true;
        }
        mRouteListLoaded = false;
        mImageURLLoaded = false;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<RouteInf> routeList = parseJSONtoRouteList(response);
                        Log.d("routeList:", ""+routeList.size());

                        // 一件も見つからなかった時
                        if(routeList.size() == 0){
                            Log.d("ルートが一件も", "みつからん");
                            if(mSpotList.size() != 0) {
                                mSelectSpotInf = mSpotList.get(0);
                                mSpotList.remove(0);
                                // 再度、検索をかける
                                routeListRequest(mUserInf.lon, mUserInf.lat, mSelectSpotInf.lon, mSelectSpotInf.lat);
                            }else{
                                // みつかりませんでした
                                mSpotNameTextView.setText("ごめんなさい。見つかりませんでした。");
                                mSpotFareTextView.setText("");
                                mSpotTimeTextView.setText("");
                                mSelectSpotInf = null;
                                // TODO: 画像を削除
                            }
                        }else{
                            // TODO
                            mSelectSpotInf.fare = routeList.get(0).fare;
                            mSelectSpotInf.duration = routeList.get(0).duration;
                            for (int i = 1; i < routeList.size(); i++) {
                                if (mSelectSpotInf.fare > routeList.get(i).fare) {
                                    mSelectSpotInf.fare = routeList.get(i).fare;
                                }
                                if (mSelectSpotInf.duration > routeList.get(i).duration) {
                                    mSelectSpotInf.duration = routeList.get(i).duration;
                                }
                            }
                            mRouteListLoaded = true;

                            if (mSelectSpotInf.fare > mUserInf.money && mSelectSpotInf.duration > mUserInf.time) {
                                // 条件を満たしていない
                                Log.d("みたして", "いない");
                                if (mSpotList.size() != 0) {
                                    mSelectSpotInf = mSpotList.get(0);
                                    mSpotList.remove(0);
                                    // 再度、検索をかける
                                    routeListRequest(mUserInf.lon, mUserInf.lat, mSelectSpotInf.lon, mSelectSpotInf.lat);
                                } else {
                                    // みつかりませんでした
                                    mSpotNameTextView.setText("ごめんなさい。見つかりませんでした。");
                                    mSpotFareTextView.setText("");
                                    mSpotTimeTextView.setText("");
                                    mSelectSpotInf = null;
                                    // TODO: 画像を削除
                                    mRouteListLoaded = true;
                                    if (mSpotListLoaded && mRouteListLoaded && mImageURLLoaded) {
                                        mpDialog.hide();
                                        mLoading = false;
                                    }
                                }
                            } else {
                                // 条件を満たしている
                                mSpotNameTextView.setText(mSelectSpotInf.name);
                                mSpotFareTextView.setText(mSelectSpotInf.fare + "円");
                                mSpotTimeTextView.setText(mSelectSpotInf.duration + "分");
                                imgURLRequest(mSelectSpotInf.name);
                                mRouteListLoaded = true;
                                if (mSpotListLoaded && mRouteListLoaded && mImageURLLoaded) {
                                    mpDialog.hide();
                                    mLoading = false;
                                }
                            }
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

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
        // スペースは「%20」へ置換する
        keyword = keyword.replace(" ", "%20");
        keyword = keyword.replace("　", "%20");
        //keyword.replace("", "%20");
        String url = "https://www.googleapis.com/customsearch/v1?"
                + "key=" + API_KEY
                + "&cx=" + "004164534463101160377:waf7n6e8twc"
                + "&searchType=image"
                + "&imgSize=large"
                + "&imgType=photo"
                + "&q=" + keyword;
        Log.d("Access URL:", url);



        // ロード中表示
        if(!mLoading) {
            mpDialog.show();
            mLoading = true;
        }
        mImageURLLoaded = false;

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
                            //
                            mSpotImageView.setImageUrl(mSelectSpotInf.imageURL, mImageLoader);
                            //new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache())

                            // 画像取得処理
                            //imageLoader.get(mSelectSpotInf.imageURL, ImageLoader.getImageListener(mSpotImageView, R.drawable.loadingimage, R.drawable.errorimage));
                        }
                        mImageURLLoaded = true;
                        if (mSpotListLoaded && mRouteListLoaded && mImageURLLoaded) {
                            mpDialog.hide();
                            mLoading = false;
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
                Log.d("", "11111");
                JSONObject route = routes.getJSONObject(i);
                JSONArray legs = route.getJSONArray("legs");
                JSONObject leg = legs.getJSONObject(0);
                //JSONArray steps = leg.getJSONArray("steps");
                //JSONObject step = steps.getJSONObject(0);
                // --
                Log.d("", "2222");
                JSONObject distance = leg.getJSONObject("distance");
                JSONObject duration = leg.getJSONObject("duration");
                int fare;
                if (leg.has("fare")) {
                    fare = leg.getJSONObject("fare").getInt("value");
                } else {
                    fare = 0;
                }
                JSONObject startLoc = leg.getJSONObject("start_location");
                JSONObject endLoc = leg.getJSONObject("end_location");
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
        if(ret.size()!=0) {
            Log.d("", "RET!!" + ret.get(0).fare);
        }
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
