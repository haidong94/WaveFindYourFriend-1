package vinsoft.com.wavefindyourfriend.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.adapter.GroupAdapter;
import vinsoft.com.wavefindyourfriend.model.Group;
import vinsoft.com.wavefindyourfriend.untils.StringFormatUntil;

public class ConversationActivity extends AppCompatActivity {

    RecyclerView rlvGroup;

    DatabaseReference mData, groupData;
    FirebaseDatabase fireData;

    Calendar cal = Calendar.getInstance();
    List<Group> groupList;
    GroupAdapter groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initWidget();

        initParams();

        setEvent();
    }

    public void initWidget() {
        rlvGroup = (RecyclerView) findViewById(R.id.rlv_group);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        rlvGroup.addItemDecoration(itemDecoration);
        rlvGroup.setLayoutManager(linearLayoutManager);
    }

    public void initParams() {

        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupList,ConversationActivity.this);
        rlvGroup.setAdapter(groupAdapter);

        fireData = FirebaseDatabase.getInstance();
        mData = fireData.getReference();
        groupData = mData.child(StringFormatUntil.REF_GROUP_NAME).child("ABC");
    }

    public void setEvent() {
        groupData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Group group = dataSnapshot.getValue(Group.class);
                groupAdapter.addGroup(group);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
