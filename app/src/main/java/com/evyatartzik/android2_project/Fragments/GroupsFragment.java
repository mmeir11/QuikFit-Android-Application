package com.evyatartzik.android2_project.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.GroupChatActivity;
import com.evyatartzik.android2_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment {

    private View groupFragmentView;
    private ListView mListView;
    private FloatingActionButton mFloatingBtn;
    private ArrayAdapter<String> mArrayAdapter;
    private ArrayList<String> mListGroup = new ArrayList<>();

    private DatabaseReference mReference;

    final Calendar myCalendar = Calendar.getInstance();


    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupFragmentView =  inflater.inflate(R.layout.fragment_groups, container, false);
        mReference = FirebaseDatabase.getInstance().getReference("database/groups");
        initFields();
        retrieveAndDisplayGroups();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String currentGroupName =  parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(getContext(), GroupChatActivity.class);
                intent.putExtra("groupName", currentGroupName);
                startActivity(intent);
            }
        });


        mFloatingBtn = groupFragmentView.findViewById(R.id.fb_createGroup);
        mFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestNewGroup();
            }
        });



        return groupFragmentView;

    }



    private void initFields() {
        mListView = groupFragmentView.findViewById(R.id.list_view);
        mArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, mListGroup);
        mListView.setAdapter(mArrayAdapter);
    }

    private void retrieveAndDisplayGroups() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator iterator = dataSnapshot.getChildren().iterator();
                while (iterator.hasNext()){
                    set.add(((DataSnapshot)iterator.next()).getKey());
                }

                mListGroup.clear();
                mListGroup.addAll(set);
                mArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void RequestNewGroup() {

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.AlertDialog);
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


//                Activity activity = new Activity(title,location, activityType, dateStr, "",null );
                Activity activity = new Activity("כדורגל בשקמה", "Rishon Lezion","football", "30.5.19",30, "לא לאחר!!", null);

                if(title.isEmpty() || county.isEmpty() || city.isEmpty() || activityType.isEmpty() /*|| date.isEmpty()*/ || time.isEmpty()){
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


    }

    private void CreateNewGroup(final Activity activity) {
        mReference.child(activity.getTitle()).setValue(activity)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Group "+activity.getTitle() +" Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }






}

