package com.evyatartzik.android2_project.Models;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Adapters.MessageAdapter;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageButton mSendMessageBtn;
    private EditText mUserMessage;
    private ScrollView mScrollView;
    private TextView mDisplayMessages;

    private String currentChatName;

    private String currentUserId, currentUserName;
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private DatabaseReference mChatNameRef;
    private DatabaseReference mGroupMessageRef;

    private String currentDate, currentTime;

    private MessageAdapter messageAdapter;
    List<ChatMessage> mChatMessage;

    RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        //=========================================
        recyclerView = findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        //=========================================



        mAuth = FirebaseAuth.getInstance();


        currentUserId = mAuth.getCurrentUser().getUid();
        mReference = FirebaseDatabase.getInstance().getReference().child("users");

        //get the name of the current user
        FirebaseDatabase.getInstance().getReference("database/users").child(currentUserId).child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        currentChatName = getIntent().getExtras().get("groupName").toString();

//        mChatNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentChatName);
        mChatNameRef = FirebaseDatabase.getInstance().getReference("database/chats").child(currentChatName);

        initFields();

        GetUserInfo();

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMessageInToDatabase();

                mUserMessage.setText("");

//                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });


    }

    private void SaveMessageInToDatabase() {

        String message = mUserMessage.getText().toString();
        String messageKey = mChatNameRef.push().getKey();
        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "Please write message first...", Toast.LENGTH_SHORT).show();

        }else{
            Calendar calendarForDate = Calendar.getInstance();
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            currentDate = currentDateFormat.format(calendarForDate.getTime());

            Calendar calendarForTime = Calendar.getInstance();
            SimpleDateFormat currentTimeFormat = new SimpleDateFormat("hh:mm:ss");
            currentTime = currentTimeFormat.format(calendarForTime.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();
            mChatNameRef.updateChildren(groupMessageKey);
            mGroupMessageRef = mChatNameRef.child(messageKey);


            //ChatMessage(String date, String message, String name, String time,  String uuid)
            ChatMessage chatMessage = new ChatMessage(currentDate, message, currentUserName,  currentTime,  currentUserId);

//            mGroupMessageRef.updateChildren(chatMessage);
            mGroupMessageRef.setValue(chatMessage);

            //Save to DB
         /*   HashMap<String, Object> messageInfoMap = new HashMap<>();
            messageInfoMap.put("name", currentUserName);
            messageInfoMap.put("message",message);
            messageInfoMap.put("date", currentDate);
            messageInfoMap.put("time", currentTime);

            mGroupMessageRef.updateChildren(messageInfoMap);*/
            // notify to all the users with thier Uids.




        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mChatNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DisplayMessages(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



     /*   mChatNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }



    private void GetUserInfo() {
        mReference.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    currentUserName = dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initFields() {

        mToolbar = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(currentChatName);

        mSendMessageBtn = findViewById(R.id.send_message_btn);
        mUserMessage = findViewById(R.id.input_group_message);
//        mScrollView = findViewById(R.id.my_scroll_view);
//        mDisplayMessages = findViewById(R.id.group_chat_display);
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        mChatMessage = new ArrayList<>();
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chats");

        mChatMessage.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
            mChatMessage.add(chatMessage);
        }

        messageAdapter = new MessageAdapter(ChatActivity.this, mChatMessage);
        recyclerView.setAdapter(messageAdapter);

       /* Iterator iterator = dataSnapshot.getChildren().iterator();
        while (iterator.hasNext()){

            String chatDate = (String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage = (String)((DataSnapshot)iterator.next()).getValue();
            String chatName = (String)((DataSnapshot)iterator.next()).getValue();
            String chatTime = (String)((DataSnapshot)iterator.next()).getValue();


            mDisplayMessages.append(chatName +"  "+chatTime+"    "+chatDate+":\n"+ chatMessage+"\n\n"  );
            mScrollView.fullScroll(ScrollView.FOCUS_DOWN);

        }*/
    }



    private void readMessage(final String myId, final String userId, final String imageUrl) {
        mChatMessage = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatMessage.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = snapshot.getValue(ChatMessage.class);
                    if (chatMessage.getReceiver().equals(myId) && chatMessage.getSender().equals(userId) ||
                            chatMessage.getReceiver().equals(userId) && chatMessage.getSender().equals(myId)) {
                        mChatMessage.add(chatMessage);
                    }

                    messageAdapter = new MessageAdapter(ChatActivity.this, mChatMessage);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }















}
