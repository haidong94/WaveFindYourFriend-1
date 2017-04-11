package vinsoft.com.wavefindyourfriend.myinterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.activity.MainActivity;
import vinsoft.com.wavefindyourfriend.model.UserAccount;
import vinsoft.com.wavefindyourfriend.untils.MethodUntil;


/**
 * Created by kienmit95 on 4/10/17.
 */

public class LoginListener implements ValueEventListener {

    UserAccount userAccount;
    Activity activity;
    String phone,pass;

    public LoginListener(Activity activity, String phone, String pass) {
        this.activity = activity;
        this.phone = phone;
        this.pass = pass;
        this.userAccount=new UserAccount();
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        if(!dataSnapshot.child(phone).exists())
            MethodUntil.makeToast(activity,activity.getResources().getString(R.string.txt_login_failed),0);
        else {
            DataSnapshot receiveSnapshot = dataSnapshot.child(phone);
            Log.d("REF",receiveSnapshot.child("userPass").getValue().toString());
            if (receiveSnapshot.child("userPass").getValue().toString().equals(pass)) {

                SharedPreferences preferences = activity.getSharedPreferences("MyAccount", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("phone",phone);
                editor.putString("pass",pass);
                editor.putString("isLogin","1");
                editor.commit();

                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("UserID",phone);
                activity.startActivity(intent);
                activity.finish();


            } else
                MethodUntil.makeToast(activity, activity.getResources().getString(R.string.txt_login_failed), 0);
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}

