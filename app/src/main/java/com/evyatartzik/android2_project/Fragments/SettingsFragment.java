package com.evyatartzik.android2_project.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.evyatartzik.android2_project.Adapters.GlobalApplication;
import com.evyatartzik.android2_project.Interfaces.FragmentToActivity;
import com.evyatartzik.android2_project.Models.ProfileImageUpload;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.LoginRegister;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment implements View.OnClickListener {


    private final int PICK_IMAGE_REQUEST = 3;
    private final int CAMARA_PERRMISON_REQ = 2;
    private final int CAMERA_REQUEST=1;

    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    DatabaseReference databaseUsers;
    ArrayList<User> users;
    User post = null;
    private View rootView;
    Context context;
    private User user;
    private TextView userName;
    private EditText password,rePassword;
    private Button buttonSignOut ,saveProfile;
    private File file;
    private Uri imageUri;
    private CircleImageView profile_Image;
    private Uri uploadPhotoUri ,filePath;
    private StorageReference mStorageRef;
    private String uploadName;
    private DatabaseReference uploadRef;
    private DatabaseReference usersRef;
    private String user_id;
    private FloatingActionButton camera_fab;
    private Switch notificationSwitch;
    private ArrayList<UserPreferences> userPreferences;
    private ArrayList<UserPreferences> PreferencesList;
    private ChipGroup chipGroup;
    private FragmentToActivity callback;

    SharedPreferences prefs;
    Boolean spNotification;

    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = GlobalApplication.getAppContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        spNotification = false;

        rootView = inflater.inflate(R.layout.settings_fragment, container, false);
        user = (User) getArguments().getSerializable("user");
        userPreferences = user.getUserPreferences();
        initFirebase();
        initLayoutByID();

        // check user activists
        getAllActivitysTypeList_And_Add_choices();




        userName.setText(user.getName());
        if(!user.getProfile_pic_path().equals("profile.image"))
        {
            Picasso.get().load(user.getProfile_pic_path()).rotate(90).into(profile_Image);

        }



        buttonSignOut.setOnClickListener(this);
        saveProfile.setOnClickListener(this);
        camera_fab.setOnClickListener(this);
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                    if(b)
                    {
                        Toast.makeText(context, getString(R.string.enable_notification), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(context, "Notification disabled", Toast.LENGTH_SHORT).show();

                    }
                    prefs.edit().putBoolean("notification", b).commit();

                }
            });


        return rootView;
    }

    private void initLayoutByID() {

        notificationSwitch = (Switch) rootView.findViewById(R.id.notification_sw);
        userName = rootView.findViewById(R.id.username_ET);
        password = rootView.findViewById(R.id.password_ET);
        rePassword = rootView.findViewById(R.id.re_password_ET);
        buttonSignOut = rootView.findViewById(R.id.signout_btn);
        saveProfile = rootView.findViewById(R.id.save_btn);
        profile_Image = rootView.findViewById(R.id.imageview_account_profile);
        camera_fab = rootView.findViewById(R.id.profile_pic_fab);
        chipGroup = rootView.findViewById(R.id.user_preferences);
        notificationSwitch.setChecked(prefs.getBoolean("notification", false));


    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseUsers =ref.child("users").child(currentUser.getUid());
        mStorageRef  = FirebaseStorage.getInstance().getReference("uploads");
        usersRef = ref.child("users");
        uploadRef =  FirebaseDatabase.getInstance().getReference("uploads");
        user_id = currentUser.getUid();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.signout_btn:
                mAuth.signOut();
                startActivity(new Intent(getActivity(), LoginRegister.class));
                getActivity().finish();
                break;
            case R.id.save_btn:
                enableDisableNotification();//check switch state and update accordingly
                uploadProfilePhoto(currentUser.getEmail());
                saveUploadData();
                break;
            case R.id.profile_pic_fab:
                //get Photo and upload to database
                takePhoto();

                break;
                default:
                    Toast.makeText(context, R.string.failure_task, Toast.LENGTH_SHORT).show();
        }


    }

    //All notification logic
    private void enableDisableNotification() {
        
        if(notificationSwitch.isChecked())
        {
            Toast.makeText(context, "ON", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Off", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUploadData() {
        final ArrayList<UserPreferences> updatedUserPref;
        updatedUserPref = new ArrayList<>();
        final String username_input = userName.getText().toString();
        final String password_input = password.getText().toString();
        final String rePassword_input = rePassword.getText().toString();

        for (int i = 0; i < chipGroup.getChildCount(); ++i) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                updatedUserPref.add(PreferencesList.get(i));
            }
        }

        User userUpdated = user;
        userUpdated.setUserPreferences(updatedUserPref);
        databaseUsers.setValue(userUpdated);

        // updatedUserPref contains the user new preferences

        if(checkInputs(password_input,rePassword_input))
        {
            if(!(password.getText().toString()==""&& rePassword.getText().toString()==""))
            {
                currentUser.updatePassword(password_input).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            signOut();
                        }
                        else
                        {
                            Toast.makeText(context, getString(R.string.failure_task), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        }
/*        if(uploadPhotoUri ==null && imageUri==null) {
            Toast.makeText(context, "Please upload photo", Toast.LENGTH_SHORT).show();
        }*/
/*
        else
        {
            Toast.makeText(context, getString(R.string.all_fields), Toast.LENGTH_SHORT).show();
        }*/

    }

    private void signOut() {
        mAuth.signOut();
        startActivity(new Intent(getActivity(), LoginRegister.class));

    }

    private boolean checkInputs(String password_input, String rePassword_input) {
                boolean isSucess  = true;
                if(password_input.trim().isEmpty()
                        || rePassword_input.trim().isEmpty())
                {
                    isSucess = false;
                }
                else if(password_input.equals(rePassword_input))
                {
                    isSucess = true;
                }
                return isSucess;

        }

    public void uploadProfilePhoto(final String email)
    {
        if(uploadPhotoUri==null)
        {
            return;
        }
        final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+".jpg");
        fileReference.putFile(uploadPhotoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    String uploadID = currentUser.getUid();
                    uploadRef.child(uploadID).setValue(profileImageUpload);
                    usersRef.child(user_id).child("profile_pic_path").setValue(uploadName);
                    callback.finish_task(3,uploadName,"");
                } else {
                    Toast.makeText(getActivity(),getString(R.string.failure_task), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void takePhoto()
    {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==getActivity().RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                profile_Image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

                    for (UserPreferences userpef : userPreferences){
                        chip.setText(user_Preference.getName());
                        chip.setClickable(true);
                        chip.setCheckable(true);
                        chip.setChipBackgroundColorResource(R.color.chip);
                        chip.setTextColor(getResources().getColor(R.color.white));

                        if(userpef.getName().equals(user_Preference.getName())){
                            chip.setChecked(true);
                            chip.setCheckedIconVisible(true);
                        }
                    }


                    chipGroup.addView(chip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    public void setOnUserChangeListener(FragmentToActivity callback) {
        this.callback = callback;
    }
}
