package honda.onepoundsteakproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class OutputMain extends AppCompatActivity {
    private Button b1,b2,b3,b4,b5;
    public final static String EXTRA_MYLONGITUDE = "honda.onepoundsteakproject.MYLONGITUDE";
    //経度のKey
    public final static String EXTRA_MYLAITUDE = "honda.onepoundsteakproject.MYLAITUDE";
    //経度のKey
    public final static String EXTRA_LONGITUDE = "honda.onepoundsteakproject.LONGITUDE";
    //Listからの経度のKey
    public final static String EXTRA_LAITUDE = "honda.onepoundsteakproject.LAITUDE";
    //Listからの経度のKey
    public double mylatitude,mylongitude,latitude1,longitude1,latitude2,
            longitude2,latitude3,longitude3,latitude4,longitude4,latitude5,longitude5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_output_main);

        Intent intent = getIntent();
        int myTime = intent.getIntExtra(InputMain.EXTRA_MYTIME, 0);
        //InputMainから遷移した時に「時間」を取得
        int myMoney = intent.getIntExtra(InputMain.EXTRA_MYMONEY, 0);
        //InputMainから遷移した時に「お金」を取得
        mylongitude = intent.getDoubleExtra(InputMain.EXTRA_MYLONGITUDE, 0.0);
        //InputMainから遷移した時に「経度」を取得
        mylatitude = intent.getDoubleExtra(InputMain.EXTRA_MYLAITUDE, 0.0);
        //InputMainから遷移した時に「緯度」を取得

        OperateSpotInf osi=new OperateSpotInf();
        //arrayListのメソッドを持つクラスの作成
        ArrayList<SpotInf> list= osi.makeList();
        //listの作成

//        list = getSortList(myTime,myMoney,mylaitude,mylongitude,list){
//
//        }

        b1 = (Button)findViewById(R.id.spotButton1);
        b2 = (Button)findViewById(R.id.spotButton2);
        b3 = (Button)findViewById(R.id.spotButton3);
        b4 = (Button)findViewById(R.id.spotButton4);
        b5 = (Button)findViewById(R.id.spotButton5);

        b1.setText(list.get(0).name);
        b2.setText(list.get(1).name);
        b3.setText(list.get(2).name);
        b4.setText(list.get(3).name);
        b5.setText(list.get(4).name);

        latitude1=list.get(0).lat;
        latitude2=list.get(1).lat;
        latitude3=list.get(2).lat;
        latitude4=list.get(3).lat;
        latitude5=list.get(4).lat;

        longitude1=list.get(0).lon;
        longitude2=list.get(1).lon;
        longitude3=list.get(2).lon;
        longitude4=list.get(3).lon;
        longitude5=list.get(4).lon;

    }
    //    private ArrayList getSortList(int myTime, int myMoney, double mylaitude, double mylongitude) {
    //    SortしたListの取得
    //    ArrayList a = new ArrayList() ;
    //    return a;
    //    }
    public void pushButton(View view){
        double latitude,longtitude;
        Intent intent = new Intent(this, MapsActivity.class);
        switch(view.getId()){
            case R.id.spotButton1:
                intent.putExtra(EXTRA_LAITUDE, latitude1);
                intent.putExtra(EXTRA_LONGITUDE, longitude1);
                intent.putExtra(EXTRA_MYLONGITUDE, mylongitude);
                intent.putExtra(EXTRA_MYLAITUDE, mylatitude);
                break;
            case R.id.spotButton2:
                intent.putExtra(EXTRA_LAITUDE, latitude2);
                intent.putExtra(EXTRA_LONGITUDE, longitude2);
                intent.putExtra(EXTRA_MYLONGITUDE, mylongitude);
                intent.putExtra(EXTRA_MYLAITUDE, mylatitude);
                break;
            case R.id.spotButton3:
                intent.putExtra(EXTRA_LAITUDE, latitude3);
                intent.putExtra(EXTRA_LONGITUDE, longitude3);
                intent.putExtra(EXTRA_MYLONGITUDE, mylongitude);
                intent.putExtra(EXTRA_MYLAITUDE, mylatitude);
                break;
            case R.id.spotButton4:
                intent.putExtra(EXTRA_LAITUDE, latitude4);
                intent.putExtra(EXTRA_LONGITUDE, longitude4);
                intent.putExtra(EXTRA_MYLONGITUDE, mylongitude);
                intent.putExtra(EXTRA_MYLAITUDE, mylatitude);
                break;
            case R.id.spotButton5:
                intent.putExtra(EXTRA_LAITUDE, latitude5);
                intent.putExtra(EXTRA_LONGITUDE, longitude5);
                intent.putExtra(EXTRA_MYLONGITUDE, mylongitude);
                intent.putExtra(EXTRA_MYLAITUDE, mylatitude);
                break;
        }
        startActivity(intent);
    }
}
