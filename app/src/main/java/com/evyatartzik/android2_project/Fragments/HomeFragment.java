package com.evyatartzik.android2_project.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

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
    private DatabaseReference mReferenceActivity;

    /**Arraylist of activity**/
    ArrayList<User> userArrayList;
    ArrayList<Activity> activitiesArrayList;
    final Calendar myCalendar = Calendar.getInstance();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_fragment, container, false);

        mReferenceActivity = FirebaseDatabase.getInstance().getReference("database/activity");

        activitiesArrayList = new ArrayList<>();
//        Activity activity = new Activity("כדורגל בשקמה", "Rishon Lezion","football", "30.5.19", "20:30", 30,"לא לאחר!!", null);
//        activitiesArrayList.add(activity);

        activityRvAdapter = new ActivityRvAdapter(getActivity(), activitiesArrayList);
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

        retrieveAndDisplayActivitys();

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
                floatingActionButton.hide();
                CreateActivityFragment createActivityFragment = new CreateActivityFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.home_fragment, createActivityFragment , "CREATE_ACTIVITY_FRAGMENT"). // give your fragment container id in first parameter
                        addToBackStack("CREATE_ACTIVITY_FRAGMENT").commit();
                break;
            default:
                Toast.makeText(getActivity(), R.string.failure_task, Toast.LENGTH_SHORT).show();
                break;


        }
    }


    private void retrieveAndDisplayActivitys()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("database");
        DatabaseReference activitysRef = ref.child("activity");

//        ActivitysList = new ArrayList<>();



        activitysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                activitiesArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

//                    GenericTypeIndicator<ArrayList<String>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<String>>() {};
//                    ArrayList<String> usersIDList = dataSnapshot.getValue(genericTypeIndicator );

                    Activity activitysRef = postSnapshot.getValue(Activity.class);
                    activitiesArrayList.add(activitysRef);
                }

                activityRvAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }


    @Override
    public void onActivityObjectClicked(int pos, View view) {


        Activity activity = activitiesArrayList.get(pos);

        floatingActionButton.hide();
        ActivityFragment activityFragment = new ActivityFragment();

        Bundle bundle=new Bundle();


        bundle.putSerializable("activity", activity);
        //set Fragment Arguments
        activityFragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.home_fragment, activityFragment , "test"). // give your fragment container id in first parameter
                addToBackStack("test").commit();
    }

    public void ShowFloatingButton(){floatingActionButton.show();}

}