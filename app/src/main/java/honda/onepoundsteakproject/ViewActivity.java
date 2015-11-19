package honda.onepoundsteakproject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class ViewActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contents);
        if(fragment == null){
            fragment = new InputFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.contents, fragment);
            fragmentTransaction.commit();
        }

    }

}
