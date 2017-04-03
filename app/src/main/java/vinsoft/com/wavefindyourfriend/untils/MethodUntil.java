package vinsoft.com.wavefindyourfriend.untils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * Created by kienmit95 on 2/04/17.
 */

public class MethodUntil {
    //check internet
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectManager.getActiveNetworkInfo() != null;
    }

    //make toast

    public static void makeToast(Context context, String content, int duration){
        Toast.makeText(context,content,duration).show();
    }
}
