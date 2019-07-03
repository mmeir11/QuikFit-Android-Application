package com.evyatartzik.android2_project.Fragments;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.materialrangebar.RangeBar;
import com.evyatartzik.android2_project.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class SearchFragment extends Fragment implements View.OnClickListener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    static Location current_location;
    private ImageView loctionButton;
    private AutoCompleteTextView userLocationTextbox;
    private View rootView;




    boolean isAdvancedSearchOpen = false;
    EditText freeTextTv, teacherTv;
    AutoCompleteTextView locationTv;
    int minPrice, maxPrice;
    ChipGroup topicsGroup;
    LinearLayout advancedLayout;
    ImageView searchBg;



    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_fragment, container, false);
        loctionButton = rootView.findViewById(R.id.location_btn);
        userLocationTextbox = rootView.findViewById(R.id.user_location);

        advancedLayout = rootView.findViewById(R.id.advanced_layout);
        searchBg = rootView.findViewById(R.id.search_bg);
        final TextView rangeTv = rootView.findViewById(R.id.range_tv);

        freeTextTv = rootView.findViewById(R.id.free_text_tv);
        teacherTv = rootView.findViewById(R.id.teacher_tv);
        RangeBar rangeBar = rootView.findViewById(R.id.range_bar);
        topicsGroup = rootView.findViewById(R.id.topics_container);

        minPrice = Integer.parseInt(rangeBar.getLeftPinValue());
        maxPrice = Integer.parseInt(rangeBar.getRightPinValue());


        ImageView advancedSearchBtn = rootView.findViewById(R.id.advanced_search_btn);
        advancedSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAdvancedSearchOpen = !isAdvancedSearchOpen;
                advancedLayout.setVisibility(isAdvancedSearchOpen ? View.VISIBLE : View.GONE);
            }
        });

        loctionButton.setOnClickListener(this);

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

    public void checkLoctionAndUpdateText(){

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

                            userLocationTextbox.setText(getLocationNameByLocation(current_location));

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

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.location_btn:
                checkLoctionAndUpdateText();

        }

    }
}