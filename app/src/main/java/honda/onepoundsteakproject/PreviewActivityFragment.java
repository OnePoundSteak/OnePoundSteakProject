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
    private TextView mSpotNameTextView;
    private SpotInf mSpotInf;
    private String mSpotImageURL;
    private NetworkImageView mSpotImageView;
    private int mMoney;
    private int mTime;
    private double mLat;
    private double mLon;

    public PreviewActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpotInf = null;
        mSpotNameTextView = null;
        mSpotImageView = null;
        mSpotImageURL="";
        mMoney = getArguments().getInt("money");
        mTime = getArguments().getInt("time");
        mLon = getArguments().getDouble("lon");
        mLat = getArguments().getDouble("lat");

        spotListRequest((float) mLat, (float) mLon, mTime);
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

        if(mSpotInf != null){
            mSpotNameTextView.setText(mSpotInf.name);
        }

        if(!mSpotImageURL.equals("")){
            AppController.getInstance().getRequestQueue();
            mSpotImageView.setImageUrl(mSpotImageURL, new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache()));
        }

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

    private void spotDirectionRequest(double orgLat, double orgLon, double destLat, double destLon){
        String API_KEY = "AIzaSyBV6AkKjK5ZUYbU-ntP5-qKhSJMVuSJufY";
        String url = "https://maps.googleapis.com/maps/api/directions/json?"+
                "origin=" + orgLat +"," + orgLon +
                "&destination=" + destLat + "," + destLon +
                "&key=" + API_KEY +
                "&mode=transit&alternatives=true" +
                "&avoid=tolls|highways|ferries" +
                "&language=ja" +
                "&units=metric" +
                "&departure_time=" + "now";
        Log.d("Access URL:", url);
    }

    private void spotListRequest(double lat, double lon, float time) {
        String tag_json_obj = "json_obj_req";
        String url = "https://gentle-basin-2840.herokuapp.com/place/" + lat + "/" + lon + "/" + time;
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
                            // 本用はソートするべき
                            mSpotInf = mSpotList.get(5);
                            mSpotNameTextView.setText(mSpotInf.name);
                        }

                        SpotInf tmpSpotInf = mSpotInf;
                        spotDirectionRequest(mLat, mLon, tmpSpotInf.lat, tmpSpotInf.lon); // 入れ子になるね

                        mSpotImageURL = "http://www.jalan.net/jalan/img/9/spot/0109/KL/26106aa1020109571_1.jpg";
                        AppController.getInstance().getRequestQueue();
                        mSpotImageView.setImageUrl(mSpotImageURL, new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache()));

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