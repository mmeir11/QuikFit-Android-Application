package com.evyatartzik.android2_project.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.evyatartzik.android2_project.Interfaces.SignupListener;
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
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterFragment extends Fragment implements SignupListener, View.OnClickListener {

    private EditText PasswordET;
    private ProgressBar progressBar;
    private android.widget.EditText RePasswordET;
    private EditText EmailET;
    private EditText NameET;
    private EditText LocationET;
    private Button RegisterButton;
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
    private DatabaseReference current_user_ref;
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.register_fragment, container, false);

        PasswordET = rootView.findViewById(R.id.input_password);
        RePasswordET = rootView.findViewById(R.id.repeat_password);
        EmailET = rootView.findViewById(R.id.input_email);
        NameET = rootView.findViewById(R.id.input_name);
        RegisterButton = rootView.findViewById(R.id.btn_signup);
        profile_Image = rootView.findViewById(R.id.location_photo);
        DonelottieAnimationView = rootView.findViewById(R.id.done_animation);
        profile_Image = rootView.findViewById(R.id.location_photo);
        chipGroup = rootView.findViewById(R.id.user_preferences);
        LocationImage = rootView.findViewById(R.id.location_btn);
        LocationET = rootView.findViewById(R.id.input_location);
        progressBar = rootView.findViewById(R.id.progress_bar);

        profile_Image.setOnClickListener(this);
        RegisterButton.setOnClickListener(this);
        LocationImage.setOnClickListener(this);


        mStorageRef  = FirebaseStorage.getInstance().getReference("uploads");
        uploadRef =  FirebaseDatabase.getInstance().getReference("uploads");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        usersRef = ref.child("users");
        preferencesRef = ref.child("preferences");

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


            Bitmap bitmap1 = BitmapFactory.decodeFile(file.getAbsolutePath());
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
            case R.id.btn_signup:
                final String email = EmailET.getText().toString().trim();
                final String password = PasswordET.getText().toString().trim();
                final String rePassword = RePasswordET.getText().toString().trim();
                final String name = NameET.getText().toString().trim();
                final String location = LocationET.getText().toString().trim();

                for (int i = 0; i < chipGroup.getChildCount(); ++i) {
                    Chip chip = (Chip) chipGroup.getChildAt(i);
                    if (chip.isChecked()) {
                        userFavoriteList.add(userPreferencesList.get(i));
                    }
                }


                if (TextUtils.isEmpty(email)) {
                    EmailET.setError(getText(R.string.mandatory_field));
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    PasswordET.setError(getText(R.string.mandatory_field));
                    return;
                } else if (password.length() < 6) {
                    PasswordET.setError(getText(R.string.password_min));
                    return;
                } else if(!password.equals(rePassword))
                {
                    Toast.makeText(getActivity(), getText(R.string.mismatch_password), Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(userFavoriteList.size()==0 || userFavoriteList==null)
                {
                    Toast.makeText(getActivity(), R.string.selcet_pref, Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getActivity(), R.string.failure_task, Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        //usersRef.child(UUID.randomUUID().toString()).setValue(user);
                                        user_id = firebaseUser.getUid();
                                        User user = new User(user_id,name,email,userFavoriteList,0,0,location,"profile.image","about");
                                        usersRef.child(firebaseUser.getUid()).setValue(user);
                                        uploadProfilePhoto(email);
                                        if(uploadPhotoUri == null){
                                            DonelottieAnimationView.setVisibility(View.VISIBLE);
                                            DonelottieAnimationView.playAnimation();
                                            afterSucessAuth();


                                        }

                                    }
                                }
                            });
                }
                    break;

            case R.id.location_btn:
                checkLoctionAndUpdateText();
                break;



        }
    }

    public void uploadProfilePhoto(final String email)
    {

        if(uploadPhotoUri==null)
        {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+".jpg");
        fileReference.putFile(uploadPhotoUri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0*taskSnapshot.getBytesTransferred())/(taskSnapshot.getTotalByteCount());
                progressBar.setProgress((int)progress);

            }
        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    uploadName  = task.getResult().toString();
                    ProfileImageUpload profileImageUpload = new ProfileImageUpload(email,uploadName);
                    String uploadID = uploadRef.push().getKey();
                    uploadRef.child(uploadID).setValue(profileImageUpload);
                    usersRef.child(user_id).child("profile_pic_path").setValue(uploadName);
                    Toast.makeText(getActivity(), R.string.sucess_register, Toast.LENGTH_SHORT).show();
                    DonelottieAnimationView.setVisibility(View.VISIBLE);
                    DonelottieAnimationView.playAnimation();
                    afterSucessAuth();
                } else {
                    Toast.makeText(getActivity(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


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
                    chip.setTextColor(getResources().getColor(R.color.white));
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
                            Toast.makeText(getActivity(), R.string.failure_task, Toast.LENGTH_SHORT).show();
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


    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }


    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
