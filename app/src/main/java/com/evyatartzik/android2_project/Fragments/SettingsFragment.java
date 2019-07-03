package com.evyatartzik.android2_project.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Adapters.GlobalApplication;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {


    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    DatabaseReference databaseUsers;
    ArrayList<User> users;

    private View rootView;
    Context context;
    private TextView userName;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //context = GlobalApplication.getAppContext();
        rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        initFirebase();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI

                User post = null;
                try{
                    post = dataSnapshot.getValue(User.class);
                }
                catch (Exception ex)
                {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                }
                if(post!=null && !(post.getName().isEmpty())) {
                    initLayoutByID();
                    userName.setText(post.getName());
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
        databaseUsers.addValueEventListener(postListener);



        return rootView;
    }

    private void initLayoutByID() {
        userName = rootView.findViewById(R.id.username);
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseUsers =ref.child("users").child(currentUser.getUid());

    }
}
