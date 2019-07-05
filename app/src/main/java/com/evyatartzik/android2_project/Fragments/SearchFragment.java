package com.evyatartzik.android2_project.Fragments;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.evyatartzik.android2_project.Adapters.ActivityRvAdapter;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener, ActivityRvAdapter.ObjectListener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    static Location current_location;
    private ImageView loctionButton;
    private View rootView;
    private ArrayList<UserPreferences> PreferencesList;
    private ArrayList<User> databaseUsers;

    private ArrayList<User> usersMatchedBySearch;
    private  ArrayList<Activity> databaseActivities;
    private ArrayList<Activity> SearchActivityList;
    private ActivityRvAdapter activityRvAdapter;
    private RecyclerView SearchRv;




    boolean isAdvancedSearchOpen = false;
    private EditText freeTextTv;
    ChipGroup chipGroup;
    LinearLayout advancedLayout;
    LottieAnimationView searchBg;
    Button buttonSearch;
    private ArrayList<Activity> activitiesByUsers;
    private boolean userSelectedLocationSearch = false;
    private ArrayList<Activity> nearByActivities;
    private RecyclerView.LayoutManager layoutManager;


    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_fragment, container, false);
        SearchActivityList = new ArrayList<>();
        activityRvAdapter = new ActivityRvAdapter(getActivity(), SearchActivityList);
        SearchRv = rootView.findViewById(R.id.search_results_rv);
        SearchRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        SearchRv.setAdapter(activityRvAdapter);
        buttonSearch = rootView.findViewById(R.id.search_btn);
        activityRvAdapter.setListener(this);


        loctionButton = rootView.findViewById(R.id.location_btn);

        advancedLayout = rootView.findViewById(R.id.advanced_layout);
        searchBg = rootView.findViewById(R.id.search_bg);
        final TextView rangeTv = rootView.findViewById(R.id.range_tv);

        freeTextTv = rootView.findViewById(R.id.free_text_tv);

        chipGroup = rootView.findViewById(R.id.chips_container);

        freeTextTv.setOnEditorActionListener(this);





        getAllActivitysTypeList_And_Add_choices();

        getAllUsers();
        getAllActivities();

        ImageView advancedSearchBtn = rootView.findViewById(R.id.advanced_search_btn);
        advancedSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdvancedSearchOpen = !isAdvancedSearchOpen;
                advancedLayout.setVisibility(isAdvancedSearchOpen ? View.VISIBLE : View.GONE);
            }
        });

        loctionButton.setOnClickListener(this);
        buttonSearch.setOnClickListener(this);

        freeTextTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userSelectedLocationSearch = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return rootView;

    }



    private void buildLocationRequest() {

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10.0f);
    }


    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                current_location = locationResult.getLastLocation();

            }
        };

    }


    public void checkLocationAndUpdateText(){

    // check permissions
    try {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            buildLocationRequest();
                            buildLocationCallBack();

                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);

                            }
                            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
                            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    freeTextTv.setText(getLocationNameByLocation(location));

                                }
                            });

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        Toast.makeText(getActivity(), "Request Denied", Toast.LENGTH_SHORT).show();
                    }
                }).check();

    }

    catch(Exception ex){

        Log.d("check_prm","lala");
    }
}


    String getLocationNameByLocation(Location location) {
        Geocoder geocoder = new Geocoder(getActivity());
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String country = addressList.get(0).getCountryName();
            String city = addressList.get(0).getLocality();
            return city + ", " + country;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    private void Search(){

        Toast.makeText(getActivity(), "Searching...", Toast.LENGTH_SHORT).show();
        isAdvancedSearchOpen = false;
        advancedLayout.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(freeTextTv.getWindowToken(), 0);

        ArrayList<String> activities = new ArrayList<>();
        cleanLists();


        for (int i = 0; i < chipGroup.getChildCount(); ++i) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                activities.add((String) chip.getText());
            }//Get all user's selected activities

            }
        if(activities.size()!=0)/*If user select search by chips*/
        {
            usersMatchedBySearch = getMatchUsers(activities); //return all users with same preferences
            activitiesByUsers = getActivitiesByChips(activities);//return all activities with same title

        }
        /*If user select location method search*/
        if(userSelectedLocationSearch) //return all users with same location
        {//userSelectedLocationSearch=true if user press location
            String location = freeTextTv.getText().toString();
            nearByActivities = getActivitiesByLocation(location);
        }
        else if (freeTextTv.getText().toString().isEmpty())
        {
            Toast.makeText(getActivity(), R.string.failure_task, Toast.LENGTH_SHORT).show();

        }
        if(getListSize()!=0)
        {
            updateRecyclerView();
        }
    }

    private int getListSize() {
        return usersMatchedBySearch.size()+activitiesByUsers.size()+nearByActivities.size();
    }

    private void cleanLists() {
        usersMatchedBySearch = new ArrayList<>();
        activitiesByUsers =  new ArrayList<>();
        nearByActivities = new ArrayList<>();
    }

    private ArrayList<Activity> getActivitiesByLocation(String location) {
        ArrayList<Activity> allDatabaseActivitiesByChips = new ArrayList<>();
         for(Activity activity1: databaseActivities)
            {
                if(activity1.getTitle().equals(location))
                {
                    allDatabaseActivitiesByChips.add(activity1);
                }
            }
        return allDatabaseActivitiesByChips;
    }

    private ArrayList<User> getMatchUsers(ArrayList<String> activities) {
        ArrayList<User> usersBySameActivities = new ArrayList<>();

        for (String activity:activities) {
            for (User user: databaseUsers) {
                for (UserPreferences userPreferences: user.getUserPreferences())
                {
                    if(userPreferences.getName().equals(activity) || userPreferences.getName().equals(activity.toLowerCase()))
                    {
                        //Match !
                        usersBySameActivities.add(user);
                        break;
                    }
                }
            }
    }

    return usersBySameActivities;
    // Add here search logic




    }

    private ArrayList<Activity> getActivitiesByChips(ArrayList<String> activities) {
        ArrayList<Activity> allDatabaseActivitiesByChips = new ArrayList<>();
        for (String activity: activities) {
            for(Activity activity1: databaseActivities)
            {
                if(activity1.getType().toLowerCase().equals(activity.toLowerCase()))
                {
                    allDatabaseActivitiesByChips.add(activity1);
                }
            }
        }
                return allDatabaseActivitiesByChips;
    }

    private void updateRecyclerView() {
        searchBg.setVisibility(View.GONE);
        layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setOrientation(0);
        SearchRv.setLayoutManager(layoutManager);
        activityRvAdapter = new ActivityRvAdapter(getActivity(),activitiesByUsers) ;
        SearchRv.setAdapter(activityRvAdapter);
        activityRvAdapter.notifyDataSetChanged();

    }

    public void getAllActivitysTypeList_And_Add_choices(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("database");
        DatabaseReference preferencesRef = ref.child("preferences");

        PreferencesList = new ArrayList<>();



            preferencesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        UserPreferences preferencesRef = postSnapshot.getValue(UserPreferences.class);
                        PreferencesList.add(preferencesRef);
                    }
                    for(UserPreferences user_Preference : PreferencesList){
                        Chip chip = new Chip(getActivity(), null , R.style.Widget_MaterialComponents_Chip_Filter);
                        chip.setText(user_Preference.getName());
                        chip.setClickable(true);
                        chip.setCheckable(true);
                        chip.setChipBackgroundColorResource(R.color.chip);
                        chipGroup.addView(chip);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
    }


    public void getAllUsers(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("database");
        DatabaseReference activitysRef = ref.child("users");

        databaseUsers = new ArrayList<>();



        activitysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    databaseUsers.add(user);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void getAllActivities(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("database");
        DatabaseReference activitysRef = ref.child("activity");

        databaseActivities = new ArrayList<>();



        activitysRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Activity activity = postSnapshot.getValue(Activity.class);
                    databaseActivities.add(activity);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.location_btn:
                checkLocationAndUpdateText();
                userSelectedLocationSearch = true;
                break;
            case R.id.search_btn:
                Search();
                break;
                default:
                    Toast.makeText(getActivity(), R.string.failure_task, Toast.LENGTH_SHORT).show();

        }

    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        userSelectedLocationSearch = false;
        if (i == EditorInfo.IME_ACTION_SEARCH) {
            Search();
            return true;
        }
        return false;
    }

    /* open display activity fragment */
    @Override
    public void onObjectClicked(int pos, View view) {

    }
}