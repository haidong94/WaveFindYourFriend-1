package vinsoft.com.wavefindyourfriend.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.adapter.ZonePhoneAdapter;
import vinsoft.com.wavefindyourfriend.model.ZonePhone;
import vinsoft.com.wavefindyourfriend.myinterface.ISetZonePhone;
import vinsoft.com.wavefindyourfriend.myinterface.LoginListener;
import vinsoft.com.wavefindyourfriend.untils.StringFormatUntil;

import static vinsoft.com.wavefindyourfriend.R.id.edt_number_phone;
import static vinsoft.com.wavefindyourfriend.R.id.edt_pass;

public class SignInActivity extends AppCompatActivity {

    EditText edtZonePhone, edtNumberPhone, edtPass;
    Button btnContinue;
    RecyclerView rlvZonePhone;
    CheckBox cbShowPass;
    TextView tvRegister;
    TextInputLayout tilPhone, tilPass;

    List<ZonePhone> zonePhoneList;
    ZonePhoneAdapter adapter;

    String phone, pass;

    LoginListener loginListener;

    DatabaseReference accountRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initWidget();

        if (autoLogin()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("UserID",phone);
            startActivity(intent);
            finish();
        }

        getSupportActionBar().hide();

        setParams();

        setEvent();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //accountRef.removeEventListener(loginListener);
    }

    @Override
    public void onBackPressed() {

        if (rlvZonePhone.getVisibility() == View.VISIBLE) {
            rlvZonePhone.setVisibility(View.GONE);
            btnContinue.setVisibility(View.VISIBLE);
        } else
            super.onBackPressed();
    }

    public void initWidget() {
        edtNumberPhone = (EditText) findViewById(edt_number_phone);
        edtZonePhone = (EditText) findViewById(R.id.edt_zone_phone);
        edtZonePhone.setInputType(InputType.TYPE_NULL);
        edtPass = (EditText) findViewById(edt_pass);

        tvRegister = (TextView) findViewById(R.id.tv_register);

        cbShowPass = (CheckBox) findViewById(R.id.cb_show_password);

        btnContinue = (Button) findViewById(R.id.btn_continue);

        tilPass = (TextInputLayout) findViewById(R.id.til_pass);
        tilPhone = (TextInputLayout) findViewById(R.id.til_phone);

        rlvZonePhone = (RecyclerView) findViewById(R.id.rlv_zone_phone);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rlvZonePhone.setLayoutManager(layoutManager);
    }

    public void setEvent() {
        cbShowPass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtPass.setTransformationMethod(null);
                    edtPass.setSelection(edtPass.getText().length());
                } else {
                    edtPass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtPass.setSelection(edtPass.getText().length());
                }
            }
        });

        /*edtZonePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlvZonePhone.setVisibility(View.VISIBLE);
                btnContinue.setVisibility(View.GONE);
            }
        });*/

        edtNumberPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rlvZonePhone.setVisibility(View.GONE);
                    btnContinue.setVisibility(View.VISIBLE);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtNumberPhone.getWindowToken(), 0);
                }

            }
        });

        edtPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rlvZonePhone.setVisibility(View.GONE);
                    btnContinue.setVisibility(View.VISIBLE);
                } else {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtPass.getWindowToken(), 0);
                }

            }
        });

        edtZonePhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    rlvZonePhone.setVisibility(View.VISIBLE);
                    btnContinue.setVisibility(View.GONE);
                }


            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void setParams() {
        accountRef = FirebaseDatabase.getInstance().getReference().child(StringFormatUntil.REF_ACCOUNT_NAME);

        zonePhoneList = new ArrayList<>();

        zonePhoneList.add(new ZonePhone("Afghanistan", "93"));
        zonePhoneList.add(new ZonePhone("Argentina", "54"));
        zonePhoneList.add(new ZonePhone("Australia", "61"));
        zonePhoneList.add(new ZonePhone("Belarus", "375"));
        zonePhoneList.add(new ZonePhone("Belgium", "32"));
        zonePhoneList.add(new ZonePhone("Canada", "1"));
        zonePhoneList.add(new ZonePhone("China", "86"));
        zonePhoneList.add(new ZonePhone("Croatia", "385"));
        zonePhoneList.add(new ZonePhone("Cuba", "53"));
        zonePhoneList.add(new ZonePhone("Czech Republic", "420"));
        zonePhoneList.add(new ZonePhone("Denmark", "45"));
        zonePhoneList.add(new ZonePhone("France", "33"));
        zonePhoneList.add(new ZonePhone("Germany", "49"));
        zonePhoneList.add(new ZonePhone("Indonesia", "62"));
        zonePhoneList.add(new ZonePhone("Italy", "39"));
        zonePhoneList.add(new ZonePhone("Japan", "81"));
        zonePhoneList.add(new ZonePhone("Laos", "856"));
        zonePhoneList.add(new ZonePhone("Myanmar", "95"));
        zonePhoneList.add(new ZonePhone("Slovenia", "386"));
        zonePhoneList.add(new ZonePhone("South Korea", "82"));
        zonePhoneList.add(new ZonePhone("Vietnam", "84"));

        adapter = new ZonePhoneAdapter(zonePhoneList, new ISetZonePhone() {
            @Override
            public void onSetZonePhone(ZonePhone zonePhone) {
                edtZonePhone.setText(zonePhone.getCountryName() + " +" + zonePhone.getCountryPhone());
                rlvZonePhone.setVisibility(View.GONE);
                btnContinue.setVisibility(View.VISIBLE);
            }
        });

        rlvZonePhone.setAdapter(adapter);
    }

    private void login() {

        phone = edtNumberPhone.getText().toString();
        pass = edtPass.getText().toString();
        if (TextUtils.isEmpty(phone))
            tilPhone.setError(getResources().getString(R.string.txt_empty_phone));
        else
            tilPhone.setErrorEnabled(false);
        if (TextUtils.isEmpty(pass))
            tilPass.setError(getResources().getString(R.string.txt_empty_pass));
        else
            tilPass.setErrorEnabled(false);

        if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(pass)) {
            loginListener = new LoginListener(this, phone, pass);
            accountRef.addValueEventListener(loginListener);
        }

    }

    public boolean autoLogin() {

        SharedPreferences preferences = getSharedPreferences(StringFormatUntil.PREFER_ACCOUNT_NAME, MODE_PRIVATE);
        if (preferences.getString("isLogin", "0").equals("1")) {
            phone = preferences.getString("phone", "NULL");
            return true;
        } else return false;
    }
}
