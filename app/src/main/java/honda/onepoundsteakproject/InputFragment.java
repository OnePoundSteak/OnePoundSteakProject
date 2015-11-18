package honda.onepoundsteakproject;


import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputFragment extends android.support.v4.app.Fragment {
    public final static String EXTRA_MYTIME = "honda.onepoundsteakproject.MYTIME";
    public final static String EXTRA_MYMONEY = "honda.onepoundsteakproject.MYMONEY";
    public final static String EXTRA_MYLONGITUDE = "honda.onepoundsteakproject.MYLONGITUDE";
    public final static String EXTRA_MYLAITUDE = "honda.onepoundsteakproject.MYLAITUDE";
    private static final String TAG = InputMain.class.getSimpleName();
    private static final int LOCATION_UPDATE_MIN_TIME = 0;
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 1;
    private LocationManager mLocationManager;
    private double mLongitude;
    private double mLatitude;
    private int mMoney;
    private int mTime;
    private Spinner time1Spinner, time2Spinner, moneySpinner;
    private String spinnerTime1[] = {"0", "1", "2", "3"};
    private String spinnerTime2[] = {"0", "10", "20", "30", "40", "50", "60"};
    private String spinnerMoney[] = {"気にしない", "500", "1000", "2000", "3000", "4000", "5000"};
    private int time1, time2;

    public InputFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_input, container, false);
    }


}
