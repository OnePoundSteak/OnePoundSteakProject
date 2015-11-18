package honda.onepoundsteakproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class ViewActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        int money = 1;
        int time = 120;
        double latitude = 34.986047;
        double longitude = 135.758826;

        Intent intent = getIntent();
        if(intent != null && intent.hasExtra("money")) {
            money = intent.getIntExtra("money", -1);
            time = intent.getIntExtra("time", -1);
            latitude = intent.getDoubleExtra("lat", 0);
            longitude = intent.getDoubleExtra("lon", 0);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contents);
        if(fragment == null){
            fragment = new InputFragment();
//            fragment = new PreviewActivityFragment();
//            Bundle args = new Bundle();
//            args.putInt("money", money);
//            args.putInt("time", time);
//            args.putDouble("lon", longitude);
//            args.putDouble("lat", latitude);
//            fragment.setArguments(args);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.contents, fragment);
            fragmentTransaction.commit();
        }

    }

    public int gethoge(){
        return -1;
    }

}
