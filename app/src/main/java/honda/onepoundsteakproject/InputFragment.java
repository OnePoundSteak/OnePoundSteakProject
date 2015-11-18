package honda.onepoundsteakproject;


import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class InputFragment extends Fragment {
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
        final View view = inflater.inflate(R.layout.fragment_input, container, false);
        final Activity activity = getActivity();

        mLocationManager = (LocationManager)activity.getSystemService(Service.LOCATION_SERVICE);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        int gpsPermission1 = activity.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, activity.getPackageName());
        int gpsPermission2 = activity.getPackageManager().checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, activity.getPackageName());
        if( isNetworkEnabled
                && gpsPermission1 == PackageManager.PERMISSION_GRANTED
                && gpsPermission2 == PackageManager.PERMISSION_GRANTED ){
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME,
                    LOCATION_UPDATE_MIN_DISTANCE,
                    new LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            mLatitude = location.getLatitude();
                            mLongitude = location.getLongitude();
                            Log.d(TAG, ""+mLatitude+","+mLongitude);
                        }

                        @Override
                        public void onStatusChanged(String provider, int status, Bundle bundle) {
                            // TODO: 未実装になっている
                        }

                        @Override
                        public void onProviderEnabled(String s) { }

                        @Override
                        public void onProviderDisabled(String s) { }
                    }
            );
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                mLatitude = location.getLatitude();//緯度を取得する
                mLongitude = location.getLongitude();//経度を取得する
            }
        }else{
            Toast.makeText(activity, "GPS機能が無効になっています,ONにしてください", Toast.LENGTH_LONG).show();
        }


        // リスナーの登録
        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(activity, "ボタンが押されました！", Toast.LENGTH_LONG).show();
                int time = time1*60+time2;
                Log.d(TAG, "onClick "+ time + "," + mMoney );
                Fragment fragment = new PreviewActivityFragment();
                Bundle args = new Bundle();
                args.putInt("money", mMoney);
                args.putInt("time", time);
                args.putDouble("lon", mLongitude);
                args.putDouble("lat", mLatitude);
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.contents, fragment);
                fragmentTransaction.commit();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);

        Activity activity = getActivity();

        Spinner time1Spinner = (Spinner) activity.findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(activity.getBaseContext(), android.R.layout.simple_spinner_item, spinnerTime1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time1Spinner.setAdapter(adapter1);
        time1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                String item = (String) spinner.getSelectedItem();
                if (item.equals("0")) {
                    time1 = 0;
                } else if (item.equals("1")) {
                    time1 = 1;
                } else if (item.equals("2")) {
                    time1 = 2;
                } else {
                    time1 = 3;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) { }
        });

        Spinner time2Spinner = (Spinner) activity.findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(activity.getBaseContext(), android.R.layout.simple_spinner_item, spinnerTime2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time2Spinner.setAdapter(adapter2);
        time2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                String item = (String) spinner.getSelectedItem();
                if (item.equals("0")) {
                    time2 = 0;
                } else if (item.equals("10")) {
                    time2 = 10;
                } else if (item.equals("20")) {
                    time2 = 20;
                } else if (item.equals("30")) {
                    time2 = 30;
                } else if (item.equals("40")) {
                    time2 = 40;
                } else if (item.equals("50")) {
                    time2 = 50;
                } else {
                    time2 = 60;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        Spinner moneySpinner = (Spinner) activity.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(activity.getBaseContext(), android.R.layout.simple_spinner_item, spinnerMoney);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        moneySpinner.setAdapter(adapter3);
        moneySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                String item = (String) spinner.getSelectedItem();
                if (item.equals("気にしない")) {
                    mMoney = -1;
                } else if (item.equals("500")) {
                    mMoney = 500;
                } else if (item.equals("1000")) {
                    mMoney = 1000;
                } else if (item.equals("2000")) {
                    mMoney = 2000;
                } else if (item.equals("3000")) {
                    mMoney = 3000;
                } else if (item.equals("4000")) {
                    mMoney = 4000;
                } else if (item.equals("5000")) {
                    mMoney = 5000;
                } else {
                    time2 = 0; //TODO ??????
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

}