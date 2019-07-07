package com.evyatartzik.android2_project.Fragments;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Adapters.GlobalApplication;
import com.evyatartzik.android2_project.Adapters.UserPrefAdapter;
import com.evyatartzik.android2_project.Adapters.UserPreferencesAdapter;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.LoginRegister;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;


public class ProfileFragment extends Fragment {

    /*Layout Element*/
    TextView textViewUserName;
    TextView textViewUserLocation;
    ImageView imageViewProfilePicture;

    User user;

    /*Firebase*/
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference mStorageRef;
    private FirebaseDatabase database;
    DatabaseReference databaseUsers;
    private DatabaseReference ref;
    ArrayList<User> userArrayList;
    ArrayList<UserPreferences> userPreferences;
    private View rootView;
    private DatabaseReference databaseUserPref;
    private UserPrefAdapter userPreferencesAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView =  inflater.inflate(R.layout.profile_fragment, container, false);
        user = (User) getArguments().getSerializable("user");



        initFirebase();


        initLayoutByID();

        textViewUserName.setText(user.getName());

        textViewUserLocation.setText(user.getLocation_string());

        if(!user.getProfile_pic_path().equals("profile.image"))
        {
            Picasso.get().load(user.getProfile_pic_path()).rotate(90).into(imageViewProfilePicture);

        }
        userPreferences = user.getUserPreferences();

        recyclerView = rootView.findViewById(R.id.user_preferences_logged_in);
        layoutManager = new GridLayoutManager(getActivity(),4, LinearLayoutManager.VERTICAL, false);
        userPreferencesAdapter = new UserPrefAdapter(userPreferences);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(userPreferencesAdapter);
        userPreferencesAdapter.notifyDataSetChanged();

        return rootView;


    }

    private String getLocation(float longitude, float latitude) {
        return "Holon, IL";
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseUsers =ref.child("users").child(currentUser.getUid());
        databaseUserPref = ref.child("users").child(currentUser.getUid()).child("userPreferences");
    }

    private void initLayoutByID() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = rootView.findViewById(R.id.user_preferences_logged_in);
        textViewUserName = rootView.findViewById(R.id.user_full_name);
        textViewUserLocation = rootView.findViewById(R.id.user_address);
        imageViewProfilePicture = rootView.findViewById(R.id.user_profile_picture);
    }

}