package vinsoft.com.wavefindyourfriend.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.adapter.ZonePhoneAdapter;
import vinsoft.com.wavefindyourfriend.model.ZonePhone;
import vinsoft.com.wavefindyourfriend.myinterface.ISetZonePhone;

public class SignInActivity extends AppCompatActivity {

    EditText edtZonePhone, edtNumberPhone;
    Button btnContinue;
    RecyclerView rlvZonePhone;

    List<ZonePhone> zonePhoneList;
    ZonePhoneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initWidget();

        getSupportActionBar().hide();

        setParams();

        setEvent();
    }

    public void initWidget(){
       edtNumberPhone= (EditText) findViewById(R.id.edt_number_phone);
        edtZonePhone= (EditText) findViewById(R.id.edt_zone_phone);

        btnContinue= (Button) findViewById(R.id.btn_continue);
        
        rlvZonePhone= (RecyclerView) findViewById(R.id.rlv_zone_phone);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rlvZonePhone.setLayoutManager(layoutManager);
    }

    public void  setEvent(){
        edtZonePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rlvZonePhone.setVisibility(View.VISIBLE);
                btnContinue.setVisibility(View.GONE);
            }
        });
    }
    public void setParams(){
        zonePhoneList=new ArrayList<>();

        zonePhoneList.add(new ZonePhone("Afghanistan","93"));
        zonePhoneList.add(new ZonePhone("Argentina","54"));
        zonePhoneList.add(new ZonePhone("Australia","61"));
        zonePhoneList.add(new ZonePhone("Belarus","375"));
        zonePhoneList.add(new ZonePhone("Belgium","32"));
        zonePhoneList.add(new ZonePhone("Canada","1"));
        zonePhoneList.add(new ZonePhone("China","86"));
        zonePhoneList.add(new ZonePhone("Croatia","385"));
        zonePhoneList.add(new ZonePhone("Cuba","53"));
        zonePhoneList.add(new ZonePhone("Czech Republic","420"));
        zonePhoneList.add(new ZonePhone("Denmark","45"));
        zonePhoneList.add(new ZonePhone("France","33"));
        zonePhoneList.add(new ZonePhone("Germany","49"));
        zonePhoneList.add(new ZonePhone("Indonesia","62"));
        zonePhoneList.add(new ZonePhone("Italy","39"));
        zonePhoneList.add(new ZonePhone("Japan","81"));
        zonePhoneList.add(new ZonePhone("Laos","856"));
        zonePhoneList.add(new ZonePhone("Myanmar","95"));
        zonePhoneList.add(new ZonePhone("Slovenia","386"));
        zonePhoneList.add(new ZonePhone("South Korea","82"));
        zonePhoneList.add(new ZonePhone("Vietnam","84"));

        adapter = new ZonePhoneAdapter(zonePhoneList, new ISetZonePhone() {
            @Override
            public void onSetZonePhone(ZonePhone zonePhone) {
                edtZonePhone.setText(zonePhone.getCountryName()+" +"+zonePhone.getCountryPhone());
                rlvZonePhone.setVisibility(View.GONE);
                btnContinue.setVisibility(View.VISIBLE);
            }
        });

        rlvZonePhone.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

        if(rlvZonePhone.getVisibility()==View.VISIBLE){
            rlvZonePhone.setVisibility(View.GONE);
            btnContinue.setVisibility(View.VISIBLE);
        }else
            super.onBackPressed();
    }
}
