package com.evyatartzik.android2_project.UI;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.evyatartzik.android2_project.Adapters.UserPreferencesAdapter;
import com.evyatartzik.android2_project.Models.ProfileImageUpload;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.Models.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginRegister extends AppCompatActivity implements View.OnClickListener  {

    File profilePhoto;
    final int CAMERA_REQUEST=1;
    private Button signupButton, loginButton;
    Button buttonForgotPassword;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference usersRef, preferencesRef,uploadRef;
    private DatabaseReference current_user_ref;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private Uri imageUri;
    final int CAMARA_PERRMISON_REQ = 1;
    final int WRITE_PERRMISON_REQ = 2;
    private final int PICK_IMAGE_REQUEST = 3;
    private CircleImageView profile_Image;
    private File file;
    private Uri filePath , uploadPhotoUri;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserPreferences> userPreferencesList;
    private UserPreferencesAdapter userPreferencesAdapter;
    ItemTouchHelper.SimpleCallback callback;
    private ArrayList<UserPreferences> userFavoriteList;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser!=null)
        {
            afterSucessAuth();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_login_register);
        profile_Image = findViewById(R.id.location_photo);
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        usersRef = ref.child("users");
        preferencesRef = ref.child("preferences");

        /*user_preferences*/
        userPreferencesList = new ArrayList<>();
        userFavoriteList = new ArrayList<>();
        initUserRefList();

        userPreferencesAdapter = new UserPreferencesAdapter(this.userPreferencesList);

        userPreferencesAdapter.setListener(new UserPreferencesAdapter.MyUserPrefListener() {
            @Override
            public void onCardClicked(int position, View view) {

            }

            @Override
            public void onCheckSelected(int position, UserPreferences userPreference) {
                Toast.makeText(LoginRegister.this, userPreference.getName(), Toast.LENGTH_SHORT).show();
                userFavoriteList.add(userPreference);
            }
        });



        /*Firebase storage*/
        mStorageRef  = FirebaseStorage.getInstance().getReference("uploads");
        uploadRef =  FirebaseDatabase.getInstance().getReference("uploads");




        signupButton = findViewById(R.id.link_signup);
        loginButton = findViewById(R.id.btn_login);
        buttonForgotPassword = findViewById(R.id.password_forgot);
        buttonForgotPassword.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();

        /*Firebase Storage*/
        mStorageRef = FirebaseStorage.getInstance().getReference();

        /*Forgot password*/


        /*Sign up to Firebase*/
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginRegister.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_register, null);
                final EditText PasswordET = view.findViewById(R.id.input_password);
                final EditText RePasswordET = view.findViewById(R.id.repeat_password);
                final EditText EmailET = view.findViewById(R.id.input_email);
                final EditText NameET = view.findViewById(R.id.input_name);
                final Button RegisterButton = view.findViewById(R.id.btn_signup);
                final LottieAnimationView DonelottieAnimationView = view.findViewById(R.id.done_animation);
                //final CircleImageView profile_Image = view.findViewById(R.id.location_photo);
                initUserPref(view);
                profile_Image = view.findViewById(R.id.location_photo);
                profile_Image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        file = new File(Environment.getExternalStorageDirectory(),"profile_img.jpg");
                        imageUri = FileProvider.getUriForFile(
                                LoginRegister.this,
                                getPackageName()+".provider", //(use your app signature + ".provider" )
                                file);

                        if(Build.VERSION.SDK_INT>=23) {
                            int wasCamPermission = checkSelfPermission(Manifest.permission.CAMERA);
                            int wasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

                            if(wasCamPermission != PackageManager.PERMISSION_GRANTED){
                                requestPermissions(new String[]{Manifest.permission.CAMERA},CAMARA_PERRMISON_REQ);


                            }
                            else if(wasWritePermission != PackageManager.PERMISSION_GRANTED){
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_PERRMISON_REQ);
                            }

                            else {
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                            }
                        }



                    }
                });
                RegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email = EmailET.getText().toString().trim();
                        final String password = PasswordET.getText().toString().trim();
                        final String rePassword = RePasswordET.getText().toString().trim();
                        final String name = NameET.getText().toString().trim();
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
                            Toast.makeText(LoginRegister.this, getText(R.string.mismatch_password), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else if(userFavoriteList.size()==0 || userFavoriteList==null)
                        {
                            Toast.makeText(LoginRegister.this, R.string.selcet_pref, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(LoginRegister.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(LoginRegister.this, R.string.failure_task, Toast.LENGTH_SHORT).show();

                                            } else {
                                                Toast.makeText(LoginRegister.this, R.string.sucess_register, Toast.LENGTH_SHORT).show();
                                                DonelottieAnimationView.setVisibility(View.VISIBLE);
                                                DonelottieAnimationView.playAnimation();
                                                User user = new User(name,email,password,userFavoriteList);
                                                usersRef.child(UUID.randomUUID().toString()).setValue(user);
                                                uploadProfilePhoto();
                                                afterSucessAuth();
                                            }
                                        }
                                    });
                        }
                    }
                });
                mBuilder.setView(view);
                AlertDialog alertDialog = mBuilder.create();
                alertDialog.show();

            }
        });
        /*Login to firebase*/
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editTextEmail = findViewById(R.id.input_email);
                final EditText editTextPassword = findViewById(R.id.input_password);

                final String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();

                if (email.isEmpty()) {
                    editTextEmail.setError(getText(R.string.mandatory_field));
                    return;
                }

                if (password.isEmpty()) {
                    editTextPassword.setError(getText(R.string.mandatory_field));
                    return;
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginRegister.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        editTextPassword.setError(getText(R.string.password_min));
                                    } else {
                                        editTextEmail.setError(getText(R.string.failure_task));
                                    }
                                } else {
                                    Toast.makeText(LoginRegister.this,R.string.sucess_Login, Toast.LENGTH_SHORT).show();
                                    //usersRef.setValue(new User(name,email,password));
                                    afterSucessAuth();
                                }
                            }
                        });
            }
        });

    }

    private void initUserPref(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.user_preferences);
        layoutManager = new LinearLayoutManager(LoginRegister.this);
        ((LinearLayoutManager) layoutManager).setOrientation(0);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new UserPreferencesAdapter(userPreferencesList);
        recyclerView.setAdapter(mAdapter);
        callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                if(direction==ItemTouchHelper.RIGHT)
                    Toast.makeText(LoginRegister.this, "Right", Toast.LENGTH_SHORT).show();
                else if(direction==ItemTouchHelper.LEFT)
                    Toast.makeText(LoginRegister.this, "Left", Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(userPreferencesAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST && resultCode==RESULT_OK)
        {
            Bitmap bitmap1 = (Bitmap) BitmapFactory.decodeFile(file.getAbsolutePath());
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            Bitmap bitmapRotate = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
            profile_Image.setImageBitmap(bitmapRotate);
            uploadPhotoUri = data.getData();
        }

        if(requestCode==PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data != null && data.getData() != null){
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                profile_Image.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }


        }
    }
    private void afterSucessAuth()
    {
        startActivity(new Intent(LoginRegister.this, MenuActivity.class));
        finish();
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.password_forgot:
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginRegister.this);
                AlertDialog alertDialog;
                final View view = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
                final EditText editTextMail = view.findViewById(R.id.mail_forgot_password);
                final Button sendResetMail = view.findViewById(R.id.btn_send_mail);
                final LottieAnimationView lottieAnimationViewEmail = view.findViewById(R.id.lottie_email);
                lottieAnimationViewEmail.playAnimation();

                sendResetMail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = editTextMail.getText().toString().trim();
                        if(!email.isEmpty())
                        {
                            auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Snackbar.make(view, R.string.mail_sent,
                                                Snackbar.LENGTH_LONG)
                                                .show();
                                        lottieAnimationViewEmail.setAnimation("mail.json");
                                        lottieAnimationViewEmail.setVisibility(View.VISIBLE);
                                        lottieAnimationViewEmail.playAnimation();

                                    } else {
                                        Snackbar.make(view, R.string.failure_task, Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            editTextMail.setError(getText(R.string.mandatory_field));
                        }
                    }
                });
                mBuilder.setView(view);
                alertDialog = mBuilder.create();
                alertDialog.show();
                break;



        }




        }
           public void initUserRefList()
            {
                preferencesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            UserPreferences userPreference =postSnapshot.getValue(UserPreferences.class);
                            userPreferencesList.add(userPreference);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }

            public void uploadProfilePhoto()
            {
                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+".jpg");
                fileReference.putFile(uploadPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(LoginRegister.this, "SucessTask", Toast.LENGTH_SHORT).show();
                        String uploadName = mStorageRef.child("uploads").getDownloadUrl().toString();
                        ProfileImageUpload profileImageUpload = new ProfileImageUpload("test",uploadName);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginRegister.this, R.string.failure_task, Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
            }
    }


