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
    private ArrayList<SpotInf> mSpotList;
    private int image = -1;
    private TextView mSpotNameTextView;
    private NetworkImageView mSpotImageView;

    public PreviewActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpotNameTextView = null;
        mSpotImageView = null;

        request((float) 34.986047, (float) 135.758826, 60);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);

        mSpotNameTextView = (TextView)view.findViewById(R.id.spotNameText);
        mSpotImageView = (NetworkImageView)view.findViewById(R.id.spotImageView);

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
        return view;
    }

    private void setSpotList(ArrayList<SpotInf> spotList) {
        mSpotList = spotList;
    }

    private ArrayList<SpotInf> parseJSONtoSpotList(JSONObject jsondata){
        ArrayList<SpotInf> ret = new ArrayList<SpotInf>();

        try {
            JSONArray spots = jsondata.getJSONArray("spots");
            for(int i=0; i<spots.length(); i++) {
                JSONObject spot = spots.getJSONObject(i);
                ret.add(new SpotInf(
                        Integer.parseInt(spot.getString("data_id")),
                        spot.getString("name"),
                        Float.parseFloat(spot.getString("lon")),
                        Float.parseFloat(spot.getString("lat")),
                        Float.parseFloat(spot.getString("rate")),
                        Float.parseFloat(spot.getString("distance"))));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("ListSize:", ""+ret.size());

        return ret;
    }

    private void request(float lon, float lat, float time) {
        String tag_json_obj = "json_obj_req";
        String url = "https://gentle-basin-2840.herokuapp.com/place/" + lon + "/" + lat + "/" + time;
        Log.d("Access URL:", url);
        // ロード中表示
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<SpotInf> newSpotList = parseJSONtoSpotList(response);
                        setSpotList(newSpotList);
                        Log.d("spotList:", "loading comp!");

                        if(mSpotNameTextView != null && mSpotList.size() != 0){
                            mSpotNameTextView.setText(mSpotList.get(0).name);
                        }

                        AppController.getInstance().getRequestQueue();
                        String hogeurl = "http://www.jalan.net/jalan/img/9/spot/0109/KL/26106aa1020109571_1.jpg";
                        mSpotImageView.setImageUrl(hogeurl, new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache()));

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        pDialog.hide();

                        if( error instanceof NetworkError) {
                        } else if( error instanceof ServerError) {
                        } else if( error instanceof AuthFailureError) {
                        } else if( error instanceof ParseError) {
                        } else if( error instanceof NoConnectionError) {
                        } else if( error instanceof TimeoutError) {
                        }
                    }
        });

        // シングルトンクラスで実行
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void imageRequest() {
        String tag_json_obj = "json_obj_req";
        String url = "http://bluemark.info/wp-content/uploads/2013/02/3a4465fcdc9a8bb92e40ac1456d52d6f.jpg";
        Log.d("Access URL:", url);
        // ロード中表示
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<SpotInf> newSpotList = parseJSONtoSpotList(response);
                        setSpotList(newSpotList);
                        Log.d("spotList:", "loading comp!");

                        if(mSpotNameTextView != null && mSpotList.size() != 0){
                            mSpotNameTextView.setText(mSpotList.get(0).name);
                        }

                        AppController.getInstance().getRequestQueue();
                        String hogeurl = "http://www.gundam.info/uploads/image/thumbnail/20120418150816-25907.jpg";
                        mSpotImageView.setImageUrl(hogeurl, new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache()));

                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();

                if( error instanceof NetworkError) {
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                } else if( error instanceof TimeoutError) {
                }
            }
        });

        // シングルトンクラスで実行
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

}