package honda.onepoundsteakproject;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ViewActivityFragment extends Fragment {

    private ArrayList<SpotInf> mSpotList;
    private TextView mSpotNameTextView;
    private NetworkImageView mSpotImageView;


    public ViewActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSpotNameTextView = null;
        mSpotImageView = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);

        mSpotNameTextView = (TextView)view.findViewById(R.id.spotNameText);
        mSpotImageView = (NetworkImageView)view.findViewById(R.id.spotImageView);

        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getFragmentManager().popBackStack();
            }
        });

        AppController.getInstance().getRequestQueue();
        String hogeurl = "http://www.jalan.net/jalan/img/9/spot/0109/KL/26106aa1020109571_1.jpg";
        mSpotImageView.setImageUrl(hogeurl, new ImageLoader(AppController.getInstance().getRequestQueue(), new BitmapCache()));

        return view;
    }
}
