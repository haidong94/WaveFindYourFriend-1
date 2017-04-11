package vinsoft.com.wavefindyourfriend.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.activity.ChatActivity;
import vinsoft.com.wavefindyourfriend.adapter.FriendAdapter;
import vinsoft.com.wavefindyourfriend.model.Friend;
import vinsoft.com.wavefindyourfriend.model.Group;
import vinsoft.com.wavefindyourfriend.model.UserAccount;
import vinsoft.com.wavefindyourfriend.myinterface.IOnCreateGroup;
import vinsoft.com.wavefindyourfriend.untils.StringFormatUntil;

/**
 * Created by kienmit95 on 4/10/17.
 */

public class FriendFragment extends Fragment implements IOnCreateGroup {
    View view;
    RecyclerView rlvFriend;

    List<Friend> friendList;
    FriendAdapter friendAdapter;
    static String userId;

    DatabaseReference friendData, accountData, msgData, groupData;

    public static FriendFragment newInstance(String id){
        FriendFragment fragment = new FriendFragment();
        userId=id;
        return fragment;
    }

    @Override
    public void onCreateGroup(String friendId, final String name) {
        List<String> idList = new ArrayList<>();
        idList.add(friendId);
        idList.add(userId);
        Collections.sort(idList);

        String groupId = "";
        for (String id : idList){
            groupId+=id;
        }

        final String finalGroupId = groupId;
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(StringFormatUntil.DATE_FORMAT);

        Group group = new Group();
        group.setGroupId(groupId);
        group.setGroupName(name);
        group.setLastMessage("");
        group.setGroupCreateDate(dateFormat.format(calendar.getTime()));

        updateGroup(userId,groupId,group);
        updateGroup(friendId,groupId,group);

        /*groupData.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child(finalGroupId).exists()){

                    Calendar calendar = Calendar.getInstance();
                    DateFormat dateFormat = new SimpleDateFormat(StringFormatUntil.DATE_FORMAT);

                    Group group = new Group();
                    group.setGroupId(finalGroupId);
                    group.setGroupName(name);
                    group.setLastMessage("");
                    group.setGroupCreateDate(dateFormat.format(calendar.getTime()));

                    groupData.child(userId).child(finalGroupId).setValue(group);

                    groupData.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        Activity activity = getActivity();
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("groupId",groupId);
        activity.startActivity(intent);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_friend_layout, container, false);

        initWidget(view);

        initParams();

        setEvent();

        return view;
    }

    public void setEvent() {
        friendData.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    String friendId = data.getKey();
                    final String date = data.getValue().toString();

                    accountData.child(friendId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserAccount account = dataSnapshot.getValue(UserAccount.class);

                            Friend friend = new Friend();
                            friend.setUserAccount(account);
                            friend.setCreateFriendDate(date);

                            friendAdapter.addFriend(friend);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

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

    public void initParams() {
        friendList = new ArrayList<>();

        friendAdapter = new FriendAdapter(friendList,getContext(),this);
        rlvFriend.setAdapter(friendAdapter);

        friendData = FirebaseDatabase.getInstance().getReference().child(StringFormatUntil.REF_FRIEND_NAME).child(userId);
        accountData=FirebaseDatabase.getInstance().getReference().child(StringFormatUntil.REF_ACCOUNT_NAME);
        groupData=FirebaseDatabase.getInstance().getReference().child(StringFormatUntil.REF_GROUP_NAME);
        msgData=FirebaseDatabase.getInstance().getReference().child(StringFormatUntil.REF_MESSAGE_NAME);
        /*UserAccount account = new UserAccount("Dong","1911","09692601895","sasaffas","13-08-2019",1,1);
        accountData.child(account.getUserPhone()).setValue(account);*/

    }


    public void initWidget(View view) {
        Context context = view.getContext();
        rlvFriend = (RecyclerView) view.findViewById(R.id.rlv_friend);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(context,DividerItemDecoration.VERTICAL);
        rlvFriend.addItemDecoration(itemDecoration);
        rlvFriend.setLayoutManager(linearLayoutManager);
    }

    public void updateGroup(final String accountId, final String groupId, final Group group){
        groupData.child(accountId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child(groupId).exists()){



                    groupData.child(accountId).child(groupId).setValue(group);

                    groupData.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
