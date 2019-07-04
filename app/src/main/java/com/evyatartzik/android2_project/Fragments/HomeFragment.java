package com.evyatartzik.android2_project.Fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.evyatartzik.android2_project.Adapters.ActivityRvAdapter;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener, ActivityRvAdapter.ObjectListener  {


    View root;
    FloatingActionButton floatingActionButton;
    private ActivityRvAdapter activityRvAdapter;
    private RecyclerView NearByActivitysRv;
    private RecyclerView MyActivityRv;

    /*Firebase*/
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference ref , databaseUsers,dataBaseActivity;

    /**Arraylist of activity**/
    ArrayList<User> userArrayList;
    ArrayList<Activity> activities;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*View*/ root = inflater.inflate(R.layout.home_fragment, container, false);

//        root =  inflater.inflate(R.layout.activity_fragment, container, false);

        activities = new ArrayList<>();

        activityRvAdapter = new ActivityRvAdapter(getActivity(), activities);
        NearByActivitysRv = root.findViewById(R.id.recycler_near_activates);
        NearByActivitysRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        NearByActivitysRv.setAdapter(activityRvAdapter);


        MyActivityRv = root.findViewById(R.id.recycler_my_activitys);
        MyActivityRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        MyActivityRv.setAdapter(activityRvAdapter);




        activityRvAdapter.setListener(this);

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


    /* open display activity fragment */
    @Override
    public void onObjectClicked(int pos, View view) {

    }



/*    private void RequestNewGroup() {

        View dialog_createGroup = getLayoutInflater().inflate(R.layout.dialog_create_activity, null, false);
        final EditText titleEt = dialog_createGroup.findViewById(R.id.groupName_Et);
        final EditText countryLocationEt = dialog_createGroup.findViewById(R.id.country_Et);
        final EditText cityLocationEt = dialog_createGroup.findViewById(R.id.city_Et);
        final EditText activityTypeEt = dialog_createGroup.findViewById(R.id.activityType_Et);
        final Button timePickerBtn = dialog_createGroup.findViewById(R.id.timePickerBtn);
        final Button datePickerBtn = dialog_createGroup.findViewById(R.id.datePickerBtn);

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String myFormat = "MM/dd/yy"; //In which you need put here
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                        datePickerBtn.setText(sdf.format(myCalendar.getTime()));
                    }

                };

                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timePickerBtn.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext(),R.style.AlertDialog);
        builder.setTitle("New activity");
        builder.setView(dialog_createGroup);


        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String title = titleEt.getText().toString();
                String county = countryLocationEt.getText().toString();
                String city = cityLocationEt.getText().toString();
                String activityType = activityTypeEt.getText().toString();
                String dateStr = datePickerBtn.getText().toString();
                String time = timePickerBtn.getText().toString();

                Location location = null;


                Activity activity = new Activity(title,location, activityType, dateStr, "",null );

                if(title.isEmpty() || county.isEmpty() || city.isEmpty() || activityType.isEmpty() *//*|| date.isEmpty()*//* || time.isEmpty()){
                    Toast.makeText(getContext(), "Please fill all the  filed", Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateNewGroup(activity);
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }*/



}