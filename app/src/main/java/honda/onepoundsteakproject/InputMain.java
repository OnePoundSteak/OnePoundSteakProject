package honda.onepoundsteakproject;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class InputMain extends AppCompatActivity implements LocationListener, View.OnClickListener{
    public final static String EXTRA_MYTIME = "honda.onepoundsteakproject.MYTIME";
    //時間のKey
    public final static String EXTRA_MYMONEY = "honda.onepoundsteakproject.MYMONEY";
    //お金のKey
    public final static String EXTRA_MYLONGITUDE = "honda.onepoundsteakproject.MYLONGITUDE";
    //経度のKey
    public final static String EXTRA_MYLAITUDE = "honda.onepoundsteakproject.MYLAITUDE";
    //経度のKey
    private static final String TAG = InputMain.class.getSimpleName();
    //Log用
    private static final int LOCATION_UPDATE_MIN_TIME = 0;
    //おおよその更新時間
    private static final int LOCATION_UPDATE_MIN_DISTANCE = 1;
    //おおよその最小距離
    private LocationManager mLocationManager;
    //LocationManager型のインスタンスの定義
    private double mLongitude;
	//遷移後に送る経度
    private double mLatitude;
	//遷移後に送る緯度
    private int mMoney;
	//遷移後に送るお金
    private int mTime;
	//最終的に送る時間(分)
    private Spinner time1Spinner, time2Spinner, moneySpinner;
	//時間、分、お金のspinner
    private String spinnerTime1[] = {"3", "2", "1", "0"};
	//一つ目のスピナーの要素(何時間以内か)
    private String spinnerTime2[] = {"0", "10", "20", "30", "40", "50", "60"};
	//二つ目のスピナーの要素(何分以内か)
    private String spinnerMoney[] = {"気にしない", "500", "1000", "2000", "3000", "4000", "5000"};
	//三つ目のスピナーの要素(お金)
    private int time1, time2;
	//(スピナーに格納する時間(time1)、分(time2))

    /*一番初めに呼び出されるメソッド. LocationManager型のオブジェクトを作成
    getLatLongitude()メソッドの呼び出しを行う*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_main);

        // リスナーの登録
        findViewById(R.id.sendButton).setOnClickListener(this);

        mLocationManager = (LocationManager) this.getSystemService(Service.LOCATION_SERVICE);
        getLatLongitude();
        //initialize,id付け
		time1=3;
        time2=0;
        time1Spinner = (Spinner) findViewById(R.id.spinner1);
        time2Spinner = (Spinner) findViewById(R.id.spinner2);
        moneySpinner = (Spinner) findViewById(R.id.spinner3);


        //1つめのすぴなーの登録
        // リソースからアイテムを取得
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTime1);

        // Adapterをセット
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //ListViewにAdapterの関連付け
        time1Spinner.setAdapter(adapter1);

        //2つめのすぴなーの登録
        // リソースからアイテムを取得
        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerTime2);

        // Adapterをセット
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        //ListViewにAdapterの関連付け
        time2Spinner.setAdapter(adapter2);

        //3つめのすぴなーの登録
        // リソースからアイテムを取得
        ArrayAdapter<String> adapter3 =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerMoney);

        // Adapterをセット
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //ListViewにAdapterの関連付け
        moneySpinner.setAdapter(adapter3);

		//1つめのスピナーのアイテムクリック時のメソッド
        time1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                String item = (String) spinner.getSelectedItem();
                if (item.equals("0")) {
                    setTime1(0);
                } else if (item.equals("1")) {
                    setTime1(1);
                } else if (item.equals("2")) {
                    setTime1(2);
                } else {
                    setTime1(3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
		//2つめのスピナーのアイテムクリック時のメソッド
        time2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                String item = (String) spinner.getSelectedItem();
                if (item.equals("0")) {
                    setTime2(0);
                } else if (item.equals("10")) {
                    setTime2(10);
                } else if (item.equals("20")) {
                    setTime2(20);
                } else if (item.equals("30")) {
                    setTime2(30);
                }else if (item.equals("40")) {
                    setTime2(40);
                }else if (item.equals("50")) {
                    setTime2(50);
                }else{
                    setTime2(60);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

		//3つめのスピナーのアイテムクリック時のメソッド
        moneySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Spinner spinner = (Spinner) parent;
                // 選択されたアイテムを取得します
                String item = (String) spinner.getSelectedItem();
                if (item.equals("気にしない")) {
                    setmMoney(-1);
                } else if (item.equals("500")) {
                    setmMoney(500);
                } else if (item.equals("1000")) {
                    setmMoney(1000);
                } else if (item.equals("2000")) {
                    setmMoney(2000);
                }else if (item.equals("3000")) {
                    setmMoney(3000);
                }else if (item.equals("4000")) {
                    setmMoney(4000);
                }else if (item.equals("5000")){
                    setmMoney(5000);
                }else{
                    setTime2(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    /*Buttonをクリックしたときのメソッド
        Intentを使い次の画面に遷移させる.
		お金の気にしないという選択の場合は-1が帰るためその時の処理は遷移後のview_activityで行う
     */
    @Override
    public void onClick(View v) {
        // 送信ボタンが押された場合
        if (v.getId() == R.id.sendButton) {
            Toast.makeText(this, "ボタンが押されました！", Toast.LENGTH_LONG).show();

            //Intent intent = new Intent(InputMain.this, OutputMain.class);
          	Intent intent = new Intent(InputMain.this, ViewActivity.class);
			//ViewActivityに遷移させるためintentの生成
            // デバッグ用の変数代入
            mMoney = getmMoney();
            mTime = getTime1()*60+getTime2();
            //スピナーで取得してきた時間を分に直しmTimeに格納
			//mLatitude = 34.986047;
            //mLongitude = 135.758826;
			//KEYとそれぞれの変数を次のactivityに渡す
            intent.putExtra("money", mMoney);
            intent.putExtra("time", mTime);
            intent.putExtra("lon", mLongitude);
            intent.putExtra("lat", mLatitude);
			//遷移
            startActivity(intent);
        }
    }

	/*setter及びgetterの処理*/
    public int getmMoney() {
        return mMoney;
    }

    public void setmMoney(int mMoney) {
        this.mMoney = mMoney;
    }

    public int getTime1() {
        return time1;
    }

    public int getTime2() {
        return time2;
    }

    public void setTime1(int time1) {
        this.time1 = time1;
    }

    public void setTime2(int time2) {
        this.time2 = time2;
    }

    /*LocationListenerのオブジェクトからGPSの位置情報を取得するメソッド
            GPSが利用可能な場合は,latitude,longitudeに現在地の緯度、経度を代入する.
            GPSが取得できない場合は,エラー文を出力するようにしている.
            */
    public void getLatLongitude() {
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        //GPSが利用可能の場合
        if (isNetworkEnabled) {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME,
                    LOCATION_UPDATE_MIN_DISTANCE,
                    this
            );
            Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                mLatitude = location.getLatitude();//緯度を取得する
                mLongitude = location.getLongitude();//経度を取得する
            }
        } else {
            String message = "GPS機能が無効になっています,ONにしてください";
            showMessage(message);
        }
    }
    /*GPSでの位置情報が変更した時に呼び出されるメソッド.
    getLatLongitudeメソッドを呼び出す
    */
    @Override
    public void onLocationChanged(Location location) {
        getLatLongitude();
    }
    /*GPSの位置情報の状態を受け取るメソッド, 利用不可, 一時的に利用不可,利用可能の三種類を持つ.
    利用可能の状態の場合getLatLongitudeメソッドを呼び出す
    */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.e(TAG, "onStatusChanged");
        switch (status) {
            case LocationProvider.OUT_OF_SERVICE:
                //訳:プロバイダー(GPSやネットワーク)が圏外のとき、近い未来変更が期待できない場合
                //今回は保留
                String outOfServiceMessage = provider + "が圏外になっていて取得できません。";
                showMessage(outOfServiceMessage);
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                //訳:プロバイダー(GPSやネットワーク)が一時的に利用不可能の場合
                //今回は保留
                String temporarilyUnavaliableMessage = "一時的に" + provider + "が利用できません。";
                showMessage(temporarilyUnavaliableMessage);
                break;
            case LocationProvider.AVAILABLE:
                //訳:利用可能状態の場合
                if (provider.equals(LocationManager.GPS_PROVIDER)) {
                    getLatLongitude();
                }
                break;
        }
    }
    /*TextViewにメッセージを表示させるメソッド
    エラー文などを表示させる
    */
    private void showMessage(String message) {
        TextView textView = (TextView) findViewById(R.id.message);
        textView.setText(message);
    }
    /*GPSが利用可能なときに呼びだされるメソッド.
    getLatLongitudeメソッドを呼び出す
    */
    @Override
    public void onProviderEnabled(String provider) {
        Log.e(TAG, "onProviderEnabled");
        getLatLongitude();
    }
    /*GPSが利用不可なときに呼びだされるメソッド.
    エラー文を表示させる.
    */
    @Override
    public void onProviderDisabled(String provider) {
        Log.e(TAG, "onProviderDisabled");
    }
}
