package com.evyatartzik.android2_project.UI;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
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
import com.evyatartzik.android2_project.Interfaces.APIService;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.ChatMessage;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.Notifictions.Client;
import com.evyatartzik.android2_project.Notifictions.Data;
import com.evyatartzik.android2_project.Notifictions.MyResponse;
import com.evyatartzik.android2_project.Notifictions.Sender;
import com.evyatartzik.android2_project.Notifictions.Token;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    Activity currentActivity;

    RecyclerView recyclerView;

    APIService apiService;

    boolean notify = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        currentChatName = getIntent().getExtras().getString("groupName");
        mChatNameRef = FirebaseDatabase.getInstance().getReference("database/chats").child(currentChatName);
        getCurrentActivty();

        apiService = Client.getClient("http://fcm.googleapis.com/").create(APIService.class);



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

        initFields();

        GetUserInfo();

        mSendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                SaveMessageInToDatabase();

                mUserMessage.setText("");
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


            ChatMessage chatMessage = new ChatMessage(currentDate, message, currentUserName,  currentTime,  currentUserId);

            mGroupMessageRef.setValue(chatMessage);

            final String msg = message;


     // להתראות הצאט =============================
/*            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("database/users")*//*.child(currentUserId)*//*;

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<String> UsersIDList = currentActivity.getUsersIDList();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if(currentActivity != null && UsersIDList != null && UsersIDList.contains(user.getuID()) ) {
                            if (notify) {
                                sendNotification(user.getuID(),currentUserName , msg);
//                                notify = false;
                            }

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/

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

    }




    private void sendNotification(String reciver, final String username, final String message)
    {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("database/Token");   //"database/Token"
        Query query = tokens.orderByKey().equalTo(reciver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(currentUserId, R.mipmap.ic_launcher, username +": " + message, "News Message",
                            currentUserId);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().success != 1){
                                            Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getCurrentActivty(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("database");
        DatabaseReference databaseCurrentActivity = ref.child("activity").child(currentChatName);

        databaseCurrentActivity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentActivity = dataSnapshot.getValue(Activity.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }







}
