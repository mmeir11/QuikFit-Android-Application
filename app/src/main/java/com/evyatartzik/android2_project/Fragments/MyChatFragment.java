package com.evyatartzik.android2_project.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evyatartzik.android2_project.Adapters.ChatsListRvAdapter;
import com.evyatartzik.android2_project.Adapters.UsersChatAdapter;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.Chat;
import com.evyatartzik.android2_project.Models.ChatActivity;
import com.evyatartzik.android2_project.Models.ChatMessage;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class MyChatFragment extends Fragment implements ChatsListRvAdapter.ObjectListener {


    private RecyclerView recyclerView_chat;
    private ChatsListRvAdapter chatsListRvAdapter;
    private ArrayList<Chat> chatsList;

    private UsersChatAdapter usersChatAdapter;
    private List<User> userList;

//    FirebaseUser fuser;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser; //fuser

    private DatabaseReference mChatNameRef;


    private List<String> usersList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.all_chat_fragment, container, false);


        recyclerView_chat = view.findViewById(R.id.recycler_view_chat);
        recyclerView_chat.setHasFixedSize(true);
        recyclerView_chat.setLayoutManager(new LinearLayoutManager(getContext()));

        chatsList = new ArrayList<>();
        chatsListRvAdapter = new ChatsListRvAdapter(getContext(), chatsList);
        recyclerView_chat.setAdapter(chatsListRvAdapter);

        chatsListRvAdapter.setListener(this);



        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        retrieveAndDisplayActivitys();





        return view;
    }




    private void retrieveAndDisplayActivitys()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("database");
        DatabaseReference activitysRef = ref.child("activity");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        activitysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chatsList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Activity activitysRef = postSnapshot.getValue(Activity.class);
                    if(activitysRef.getUsersIDList()!=null && activitysRef.getUsersIDList().contains(currentUser.getUid()))
                    {

                        String currentChatName = activitysRef.getTitle();
//                        ArrayList<String> usersIDList = activitysRef.getUsersIDList();

                        mChatNameRef = FirebaseDatabase.getInstance().getReference("database/chats").child(currentChatName);

//                        ArrayList<ChatMessage> chatMessagesList = getMessage(mChatNameRef);

                        Chat chat = new Chat(currentChatName);
                        chatsList.add(chat);

                    }
                }

                chatsListRvAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }


    @Override
    public void onActivityObjectClicked(int pos, View view) {

        String chatName = chatsList.get(pos).getTitle();

        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("groupName", chatName);
        startActivity(intent);


    }
}