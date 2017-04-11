package vinsoft.com.wavefindyourfriend.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.adapter.InviteContacAdapter;
import vinsoft.com.wavefindyourfriend.model.Contact;

public class InviteFriendActivity extends AppCompatActivity {

    public static int REQUEST_CODE_PICK_CONTACTS =1911;

    private RecyclerView rlvInviteFriend;
    private TextView tvv;

    List<Contact> contactList;
    InviteContacAdapter contacAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friend);
        initWidget();

        initParams();

        checkIsOnline();
    }

    public void initWidget() {
        tvv = (TextView) findViewById(R.id.tvv);
        rlvInviteFriend = (RecyclerView) findViewById(R.id.rlv_invite_friend);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        rlvInviteFriend.setLayoutManager(linearLayoutManager);
        rlvInviteFriend.addItemDecoration(itemDecoration);

    }

    public void initParams(){
        contactList = new ArrayList<>();
        loadAllContacts();

        contacAdapter = new InviteContacAdapter(contactList,this);
        rlvInviteFriend.setAdapter(contacAdapter);
    }

    public void loadAllContacts() {
        ContentResolver cr = getContentResolver();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        Cursor cursor = cr.query(uri, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        //listContact.clear();
        while (cursor.moveToNext()) {
            String nameConTact = ContactsContract.Contacts.DISPLAY_NAME;
            String phoneContact = ContactsContract.CommonDataKinds.Phone.NUMBER;

            int locationName = cursor.getColumnIndex(nameConTact);
            int locationPhone = cursor.getColumnIndex(phoneContact);

            String name = cursor.getString(locationName);
            String phone = cursor.getString(locationPhone);

            Contact contact = new Contact(name, phone);
            if(!contactList.contains(contact)){
                contactList.add(contact);
            }

        }
        cursor.close();
    }

    public void checkIsOnline(){
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                   tvv.setText("True");
                } else {
                    tvv.setText("False");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                tvv.setText("Error");
            }
        });
    }


}

