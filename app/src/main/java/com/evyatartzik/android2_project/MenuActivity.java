package com.evyatartzik.android2_project;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    TextView textView;
    private FirebaseAuth mAuth;
    DatabaseReference users;
    List<User> userList;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        textView = findViewById(R.id.user);

        database = FirebaseDatabase.getInstance();
        userList = new ArrayList<>();
        
        
        
        mAuth = FirebaseAuth.getInstance();
        users = database.getReference("users");

        /*get registered/logged-in user*/
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            textView.setText(email);
        }

        /*get all users in database*/
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshotIt : dataSnapshot.getChildren()) {
                    String email = snapshotIt.child("email").getValue().toString();
                    String password = snapshotIt.child("password").getValue().toString();
                    String name = snapshotIt.child("name").getValue().toString();

                    User user = new User(name, email, password);
                    userList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });




    }
}
