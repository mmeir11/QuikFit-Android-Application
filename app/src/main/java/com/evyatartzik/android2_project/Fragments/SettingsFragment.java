package com.evyatartzik.android2_project.Fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evyatartzik.android2_project.Classes.User;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;


public class SettingsFragment extends Fragment {

    private FirebaseAuth mAuth;
    DatabaseReference users;
    List<User> userList;
    FirebaseDatabase database;
    TextView textViewTest;
    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =inflater.inflate(R.layout.settings_fragment, container, false);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        users = database.getReference("users");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        textViewTest = root.findViewById(R.id.username);
        textViewTest.setText(user.getEmail());



        return root;
    }

}