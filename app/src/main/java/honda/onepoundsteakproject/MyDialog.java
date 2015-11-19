package honda.onepoundsteakproject;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by ain on 15/11/19.
 */
public class MyDialog {
    private ProgressDialog mDialog;
    private Boolean mVisibleFlag;

    public MyDialog(Activity activity, String message){
        mDialog = new ProgressDialog(activity);
        mDialog.setCancelable(false);
        mDialog.setMessage(message);
        mVisibleFlag = false;
    }

    public void show(){
        if (!mVisibleFlag) {
            mDialog.show();
        }
    }

    public void hide(){
        if (mVisibleFlag) {
            mDialog.hide();
        }
    }
}
