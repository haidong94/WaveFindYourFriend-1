package vinsoft.com.wavefindyourfriend.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import vinsoft.com.wavefindyourfriend.R;

public class WelcomActivity extends AppCompatActivity {

    RelativeLayout rtlGoIt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        initWidget();

        getSupportActionBar().hide();

        setEvent();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length == 2 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(WelcomActivity.this, "Permisions is Granted", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(WelcomActivity.this, "Permisions is Denied. Grant it now!", Toast.LENGTH_LONG).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void initPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                    | checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                //Permisson don't granted
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    //Toast.makeText(WelcomActivity.this, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
                }
                // Permisson don't granted and dont show dialog again.
                else {
                    //Toast.makeText(WelcomActivity.this, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
                }
                //Register permission
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            }
        }
    }

    public void initWidget(){
        rtlGoIt= (RelativeLayout) findViewById(R.id.rtl_go_it);
    }

    public void setEvent(){
        rtlGoIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPermission();

            }
        });
    }

    public void openIntentNext(){

    }

}
