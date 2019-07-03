package com.evyatartzik.android2_project.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.LoginRegister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ActivityFragment extends Fragment implements View.OnClickListener {

    View root;
    FloatingActionButton floatingActionButton;

    /*Firebase*/
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference ref , databaseUsers,dataBaseActivity;

    /**Arraylist of activity**/
    ArrayList<User> userArrayList;
    ArrayList<Activity> activities;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        root =  inflater.inflate(R.layout.activity_fragment, container, false);

        initDataStructe();
        initFragmentByID();
        initFirebase();
        floatingActionButton.setOnClickListener(this);


/*
        ValueEventListener activityListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User post = null;
                try
                {
                    post = dataSnapshot.getValue(User.class);
                }
                catch (Exception ex)
                {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
                if(post!=null ){
                    initFragmentByID();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                // ...
            }
        };
*/

        ValueEventListener userListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User post = null;
                try
                {

                    post = dataSnapshot.getValue(User.class);
                    userArrayList.add(post);

                }
                catch (Exception ex)
                {
                    Toast.makeText(getActivity(), R.string.failure_task, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };
        databaseUsers.addValueEventListener(userListener);
        //dataBaseActivity.addValueEventListener(activityListener);


        return root;
    }

    private void initDataStructe() {
        userArrayList = new ArrayList<>();
    }

    private void initFragmentByID() {
        floatingActionButton = root.findViewById(R.id.add_fb);
    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseUsers =ref.child("users").child(currentUser.getUid());
        dataBaseActivity = ref.child("activity");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.add_fb:
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
                AlertDialog alertDialog;
                final View view = getLayoutInflater().inflate(R.layout.dialog_create_activity, null);
                final EditText activityNameET = view.findViewById(R.id.groupName_Et);
                final EditText activityLocationET = view.findViewById(R.id.city_Et);
                final EditText activitiyType = view.findViewById(R.id.activityType_Et);
                final Button buttonDatePicker = view.findViewById(R.id.datePickerBtn);
                final Button buttonTimePicker = view.findViewById(R.id.timePickerBtn);
                final Button buttonActivitySave = view.findViewById(R.id.save_activitiy_btn);
                buttonActivitySave.setOnClickListener(this);
                buttonDatePicker.setOnClickListener(this);
                buttonTimePicker.setOnClickListener(this);

                mBuilder.setView(view);
                alertDialog = mBuilder.create();
                alertDialog.show();
                break;
            case R.id.timePickerBtn:
                break;
            case R.id.datePickerBtn:
                break;
            case R.id.save_activitiy_btn: /*Created dummy activitiy object*/
                Location location = new Location("LocationManager.GPS_PROVIDER");
                Activity activity = new Activity("test",location,"test","123456","test",userArrayList);
                dataBaseActivity.child(activity.getTitle()).setValue(activity);

                /**update the database**/
                break;
            default:
                Toast.makeText(getActivity(), R.string.failure_task, Toast.LENGTH_SHORT).show();
                break;


        }
    }
}
