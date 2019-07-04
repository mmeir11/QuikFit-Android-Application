package com.evyatartzik.android2_project.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.evyatartzik.android2_project.Interfaces.SignupListener;
import com.evyatartzik.android2_project.Models.ProfileImageUpload;
import com.evyatartzik.android2_project.Models.User;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.UI.MenuActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterFragment extends Fragment implements SignupListener, View.OnClickListener {

    private EditText PasswordET;
    private android.widget.EditText RePasswordET;
    private EditText EmailET;
    private EditText NameET;
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


        profile_Image.setOnClickListener(this);
        RegisterButton.setOnClickListener(this);


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
            case R.id.btn_signup:
                final String email = EmailET.getText().toString().trim();
                final String password = PasswordET.getText().toString().trim();
                final String rePassword = RePasswordET.getText().toString().trim();
                final String name = NameET.getText().toString().trim();

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

                                    } else {
                                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                        Toast.makeText(getActivity(), R.string.sucess_register, Toast.LENGTH_SHORT).show();
                                        DonelottieAnimationView.setVisibility(View.VISIBLE);
                                        DonelottieAnimationView.playAnimation();
                                        //usersRef.child(UUID.randomUUID().toString()).setValue(user);
                                        user_id = firebaseUser.getUid();
                                        User user = new User(user_id,name,email,userFavoriteList,0,0,"profile.image","about");
                                        usersRef.child(firebaseUser.getUid()).setValue(user);
                                        uploadProfilePhoto(email);
                                        afterSucessAuth();
                                    }
                                }
                            });
                }
                    break;



        }
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
                    String uploadID = uploadRef.push().getKey();
                    uploadRef.child(uploadID).setValue(profileImageUpload);
                    usersRef.child(user_id).child("profile_pic_path").setValue(uploadName);
                } else {
                    Toast.makeText(getActivity(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
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
                    chip.setChipBackgroundColorResource(R.color.chip);
                    chipGroup.addView(chip);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
}
