package vinsoft.com.wavefindyourfriend.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

/**
 * Created by kienmit95 on 05/04/17.
 */

public class ConversationFragment extends Fragment {

    View view;
    RecyclerView rlvGroup;

    DatabaseReference mData, groupData;
    FirebaseDatabase fireData;

    Calendar cal = Calendar.getInstance();
    List<Group> groupList;
    GroupAdapter groupAdapter;

    Context context;

    static String userId;

    public ConversationFragment() {
    }

    public static ConversationFragment newInstance(String id){
        ConversationFragment fragment = new ConversationFragment();
        userId=id;
        return fragment;
    }

    public GroupAdapter getGroupAdapter() {
        return groupAdapter;
    }

    public void setGroupAdapter(GroupAdapter groupAdapter) {
        this.groupAdapter = groupAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_conversation_layout, container, false);
        initWidget(view);

        initParams();

        setEvent();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //initParams();
        //setEvent();
    }

    public void initWidget(View itemView) {
        context=itemView.getContext();

        rlvGroup = (RecyclerView) itemView.findViewById(R.id.rlv_group);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        rlvGroup.addItemDecoration(itemDecoration);
        rlvGroup.setLayoutManager(linearLayoutManager);

    }

    public void initParams(){
        groupList = new ArrayList<>();
        groupAdapter = new GroupAdapter(groupList,context);
        rlvGroup.setAdapter(groupAdapter);


        fireData = FirebaseDatabase.getInstance();
        mData = fireData.getReference();
        groupData = mData.child(StringFormatUntil.REF_GROUP_NAME).child(userId);
    }

    public void setEvent() {
        groupData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Group group = dataSnapshot.getValue(Group.class);
                if (!TextUtils.isEmpty(group.getLastMessage()))
                    groupAdapter.addGroup(group);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Group group = dataSnapshot.getValue(Group.class);
                boolean kt = false;
                for (Group temp : groupList){
                    if(temp.getGroupId().equals(group.getGroupId())){
                        temp.setLastMessage(group.getLastMessage());
                        groupAdapter.notifyDataSetChanged();

                        kt=true;

                        break;
                    }
                }
                if(!kt)
                    groupAdapter.addGroup(group);
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
