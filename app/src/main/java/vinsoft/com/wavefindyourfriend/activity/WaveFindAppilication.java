package vinsoft.com.wavefindyourfriend.activity;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kienmit95 on 2/04/17.
 */

public class WaveFindAppilication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
