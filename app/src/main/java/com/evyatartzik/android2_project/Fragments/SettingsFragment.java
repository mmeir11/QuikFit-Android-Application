package com.evyatartzik.android2_project.Fragments;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Adapters.GlobalApplication;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.LoginRegister;
import com.evyatartzik.android2_project.UI.MenuActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {

    Context context;
    private FirebaseAuth mAuth;
    DatabaseReference users;
    List<User> userList;
    FirebaseDatabase database;
    TextView textViewTest1;
    Button buttonTest1;
    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = GlobalApplication.getAppContext();
        View root =inflater.inflate(R.layout.settings_fragment, container, false);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        users = database.getReference("users");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userList = new ArrayList<>();


        textViewTest1 = root.findViewById(R.id.test1);
        textViewTest1.setText(user.getEmail());

        buttonTest1 = root.findViewById(R.id.test2);
        buttonTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                updateUI();
            }
        });




        return root;
    }

    private void updateUI() {
        Intent intent = new Intent(getActivity(), LoginRegister.class);
        startActivity(intent);

    }


}