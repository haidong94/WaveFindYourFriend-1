package vinsoft.com.wavefindyourfriend.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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
import java.util.HashMap;
import java.util.Map;

import vinsoft.com.wavefindyourfriend.R;
import vinsoft.com.wavefindyourfriend.adapter.MessageApdapter;
import vinsoft.com.wavefindyourfriend.model.ChatMessage;
import vinsoft.com.wavefindyourfriend.myinterface.IDeleteMessage;
import vinsoft.com.wavefindyourfriend.untils.MethodUntil;
import vinsoft.com.wavefindyourfriend.untils.StringFormatUntil;

public class ChatActivity extends AppCompatActivity {

    private static final int MY_NOTIFICATION_ID = 12345;

    ImageView imgChatSend;
    EditText edtMessageEdit;
    RecyclerView rlvMessageContainer;

    ArrayList<ChatMessage> chatHistory;
    LinearLayoutManager linearLayoutManager;
    MessageApdapter messageApdapter;
    Calendar cal = Calendar.getInstance();

    DatabaseReference mData, msgReference;
    FirebaseDatabase fireData;
    DatabaseReference dataOffline;

    static boolean isVisiable;

    String userID, groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initWidget();

        isVisiable = true;
        chatHistory = new ArrayList<>();
        userID = "ABC";
        groupId=getIntent().getStringExtra("groupId");

        setDatabase();

        checkNetwork();

        initParams(userID);

        setEvent(userID);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isVisiable = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisiable = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisiable = false;
    }

    public void initWidget() {
        imgChatSend = (ImageView) findViewById(R.id.img_chat_send);
        edtMessageEdit = (EditText) findViewById(R.id.edt_message_edit);
        rlvMessageContainer = (RecyclerView) findViewById(R.id.rlv_messages_container);
        //RelativeLayout container = (RelativeLayout) findViewById(R.id.container);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        rlvMessageContainer.setLayoutManager(linearLayoutManager);

    }

    public void displayMessage(ChatMessage message) {
        messageApdapter.addMessage(message);

        rlvMessageContainer.scrollToPosition(messageApdapter.getItemCount() - 1);

    }

    public void setEvent(final String sendId) {
        imgChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isVisiable = true;
                String messageText = edtMessageEdit.getText().toString();
                if (TextUtils.isEmpty(messageText)) {
                    return;
                }
                ChatMessage msg = new ChatMessage();

                DateFormat dateFormat = new SimpleDateFormat(StringFormatUntil.DATE_FORMAT);
                DateFormat timeFormat = new SimpleDateFormat(StringFormatUntil.DAY_FORMAT);

                if (MethodUntil.isNetworkConnected(ChatActivity.this)) {
                    String keyId = msgReference.push().getKey();
                    msg.setMessageID(keyId);
                    msg.setDateSend(dateFormat.format(cal.getTime()));
                    msg.setTimeSend(timeFormat.format(cal.getTime()));
                    msg.setContentMessage(messageText);
                    msg.setPersonSendID(sendId);
                    msg.setMessageVision("NULL");
                    msgReference.push().setValue(msg);
                } else {
                    String keyId = dataOffline.push().getKey();
                    msg.setMessageID(keyId);
                    msg.setDateSend(dateFormat.format(cal.getTime()));
                    msg.setTimeSend(timeFormat.format(cal.getTime()));
                    msg.setContentMessage(messageText);
                    msg.setPersonSendID(sendId);
                    msg.setMessageVision("NULL");
                    dataOffline.push().setValue(msg);
                }

                edtMessageEdit.setText("");

            }
        });

        msgReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                displayMessage(message);
                if (!ChatActivity.this.isVisiable) {
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ChatActivity.this);
                    mBuilder.setSmallIcon(R.drawable.ic_msg_notification);
                    mBuilder.setContentTitle("Wave");
                    mBuilder.setContentText(message.getContentMessage());
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setSound(alarmSound);

                    NotificationManager notificationService = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

                    Notification notification = mBuilder.build();
                    notificationService.notify(MY_NOTIFICATION_ID, notification);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
                //displayMessage(message);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                ChatMessage message = dataSnapshot.getValue(ChatMessage.class);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void initParams(final String sendId) {
        messageApdapter = new MessageApdapter(chatHistory, getApplicationContext(), sendId, new IDeleteMessage() {
            @Override
            public void onDeteteMessage(String messageId) {
                msgReference.orderByChild("messageID").equalTo(messageId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            ChatMessage msg = child.getValue(ChatMessage.class);
                            if (msg.getMessageVision().equals("NULL")) {
                                Map<String, Object> msgUpdates = new HashMap<String, Object>();
                                msgUpdates.put("messageVision", userID);
                                msgReference.child(child.getKey()).updateChildren(msgUpdates);
                            } else {
                                msgReference.child(child.getKey()).removeValue();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        rlvMessageContainer.setAdapter(messageApdapter);
    }

    public void checkNetwork() {
        if (!MethodUntil.isNetworkConnected(getApplicationContext())) {
            MethodUntil.makeToast(ChatActivity.this, getString(R.string.no_network), 1);
        }
    }

    public void setDatabase() {
        final Object[] ss = {null};
        final String[] key = {null};
        fireData = FirebaseDatabase.getInstance();
        mData = fireData.getReference();
      /*  mData.child(StringFormatUntil.REF_GROUP_NAME).child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot mGroup : dataSnapshot.getChildren()){
                    ss[0] =mGroup.getValue();
                    key[0] = mGroup.getKey();
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
        });*/
        msgReference=mData.child(StringFormatUntil.REF_MESSAGE_NAME).child(groupId);
        dataOffline = fireData.getReference(StringFormatUntil.REF_MESSAGE_NAME).child(groupId);
    }
}
