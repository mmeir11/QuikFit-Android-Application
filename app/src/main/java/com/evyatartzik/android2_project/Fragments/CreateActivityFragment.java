package com.evyatartzik.android2_project.Fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.evyatartzik.android2_project.Interfaces.SignupListener;
import com.evyatartzik.android2_project.Models.Activity;
import com.evyatartzik.android2_project.Models.ProfileImageUpload;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.MenuActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateActivityFragment extends Fragment implements SignupListener, View.OnClickListener {


    private EditText NameET;
    private EditText LocationET;
    private Button SaveButton, CancelButton, timePickerBtn ,datePickerBtn;
    private LottieAnimationView DonelottieAnimationView;
    private CircleImageView profile_Image;
    private ChipGroup chipGroup;
    private ArrayList<UserPreferences> userFavoriteList;
    private ArrayList<UserPreferences> userPreferencesList;
    private View rootView;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference usersRef, preferencesRef,uploadRef;
    private DatabaseReference current_user_ref,dataBaseActivity;
    private StorageReference mStorageRef;
    private Uri imageUri;
    private Uri filePath , uploadPhotoUri;
    final int CAMARA_PERRMISON_REQ = 2;
    final int CAMERA_REQUEST=1;
    private String uploadName;
    private final int PICK_IMAGE_REQUEST = 3;
    private File profilePhoto;
    private File file;
    private String user_id;
    private ImageView LocationImage;
    private static final int LOCATION_PERMISSION_REQUEST = 5;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    static Location current_location;
    final Calendar myCalendar = Calendar.getInstance();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.create_activity_fragment, container, false);


        NameET = rootView.findViewById(R.id.input_name);
        SaveButton = rootView.findViewById(R.id.save_activitiy_btn);
        DonelottieAnimationView = rootView.findViewById(R.id.done_animation);
        chipGroup = rootView.findViewById(R.id.user_preferences);
        LocationImage = rootView.findViewById(R.id.location_btn);
        LocationET = rootView.findViewById(R.id.input_location);
        CancelButton = rootView.findViewById(R.id.cancel_activitiy_btn);
        timePickerBtn = rootView.findViewById(R.id.timePickerBtn);
        datePickerBtn = rootView.findViewById(R.id.datePickerBtn);


        timePickerBtn.setOnClickListener(this);
        datePickerBtn.setOnClickListener(this);
        SaveButton.setOnClickListener(this);
        LocationImage.setOnClickListener(this);
        CancelButton.setOnClickListener(this);

        chipGroup.setSingleSelection(true);

        mStorageRef  = FirebaseStorage.getInstance().getReference("uploads");
        uploadRef =  FirebaseDatabase.getInstance().getReference("uploads");


        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        usersRef = ref.child("users");
        preferencesRef = ref.child("preferences");
        dataBaseActivity = ref.child("activity");

        /*user_preferences*/
        userPreferencesList = new ArrayList<>();
        userFavoriteList = new ArrayList<>();
        auth = FirebaseAuth.getInstance();

        getAllActivitysTypeList_And_Add_choices();


        return rootView;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMARA_PERRMISON_REQ)
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST && resultCode==getActivity().RESULT_OK)
        {

            Bitmap bitmap1 = (Bitmap) BitmapFactory.decodeFile(file.getAbsolutePath());
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap bitmapRotate = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
            profile_Image.setImageBitmap(bitmapRotate);
            uploadPhotoUri = imageUri;
        }

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==getActivity().RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                profile_Image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void finish_signup() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.location_photo:
                file = new File(Environment.getExternalStorageDirectory(),"profile_img.jpg");
                imageUri = FileProvider.getUriForFile(
                        getActivity(),
                        getActivity().getPackageName()+".provider", //(use your app signature + ".provider" )
                        file);

                if(Build.VERSION.SDK_INT>=23) {
                    int wasCamPermission = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
                    int wasWritePermission = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                    if(wasCamPermission != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMARA_PERRMISON_REQ);
                    }

                    else
                    {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                    }

                }

                break;


            case R.id.save_activitiy_btn:
                String type = null;
                final String name = NameET.getText().toString().trim();
                final String location = LocationET.getText().toString().trim();

                for (int i = 0; i < chipGroup.getChildCount(); ++i) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    if (chip.isChecked()) {
                        userFavoriteList.add(userPreferencesList.get(i));
                        type = userPreferencesList.get(i).getName();
                    }
                }


                if (TextUtils.isEmpty(location)) {
                    LocationET.setError(getText(R.string.mandatory_field));
                    return;
                }
                else if(userFavoriteList.size()==0 || userFavoriteList==null)
                {
                    Toast.makeText(getActivity(), R.string.selcet_pref, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    RequestNewGroup(name,location,type);}
                break;

            case R.id.location_btn:
                checkLoctionAndUpdateText();
                break;

            case R.id.cancel_activitiy_btn:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.datePickerBtn:

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


                break;
            case R.id.timePickerBtn:
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
                break;



        }
    }



    private void afterSucessAuth()
    {
        startActivity(new Intent(getActivity(), MenuActivity.class));
        //finish();
    }

    public void getAllActivitysTypeList_And_Add_choices(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("database");
        DatabaseReference preferencesRef = ref.child("preferences");


        preferencesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    UserPreferences preferencesRef = postSnapshot.getValue(UserPreferences.class);
                    userPreferencesList.add(preferencesRef);
                }
                for(UserPreferences user_Preference : userPreferencesList){
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
                                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        LocationET.setText(getLocationNameByLocation(location));

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


    private void RequestNewGroup(String name,String location,String type) {

                Activity activity = new Activity(name, location ,type, datePickerBtn.getText().toString(), timePickerBtn.getText().toString(), 30,"לא לאחר!!", null);
                CreateNewChat(activity);
                dataBaseActivity.child(activity.getTitle()).setValue(activity);
                getActivity().getSupportFragmentManager().popBackStack();

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


    private void CreateNewChat(final Activity activity) {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("database/chats");

        mReference.child(activity.getTitle()).setValue(activity.getTitle())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Chat "+activity.getTitle() +" Created Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
