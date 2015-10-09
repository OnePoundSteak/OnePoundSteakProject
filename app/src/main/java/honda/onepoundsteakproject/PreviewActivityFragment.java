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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PreviewActivityFragment extends Fragment {

    private static final String TAG = PreviewActivityFragment.class.getSimpleName();
    private ArrayList<String> mSpotList;
    private int image = -1;

    public PreviewActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //request( (float)34.986047, (float)135.758826, 60);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);

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

    private void setSpotList(ArrayList<String> spotList){
        mSpotList = spotList;
    }

    /**
     * リクエスト処理
     */
    private void request(float lon, float lat, float time) {
        String tag_json_obj = "json_obj_req";
        String url = "https://gentle-basin-2840.herokuapp.com/place/" + lon + "/" + lat + "/" + time;

        // ロード中表示
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ArrayList<String> newSpotList = new ArrayList<String>();
                        Log.d(TAG, response.toString());
                        try {
                            JSONArray spots = response.getJSONArray("spots");
                            for(int i=0; i<spots.length(); i++) {
                                newSpotList.add(spots.getJSONObject(i).getString("name"));
                            }
                            setSpotList(newSpotList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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