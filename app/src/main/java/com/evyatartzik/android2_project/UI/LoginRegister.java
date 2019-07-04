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
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
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
import com.evyatartzik.android2_project.Fragments.RegisterFragment;
import com.evyatartzik.android2_project.Interfaces.SignupListener;
import com.evyatartzik.android2_project.Models.ProfileImageUpload;
import com.evyatartzik.android2_project.Models.UserPreferences;
import com.evyatartzik.android2_project.R;
import com.evyatartzik.android2_project.Models.User;
import com.google.android.gms.auth.api.signin.internal.Storage;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginRegister extends AppCompatActivity implements View.OnClickListener  {

    String uploadName;
    final int CAMERA_REQUEST=1;
    private Button signupButton, loginButton;
    Button buttonForgotPassword;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private DatabaseReference usersRef, preferencesRef,uploadRef;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private final int PICK_IMAGE_REQUEST = 3;
    private ArrayList<UserPreferences> userPreferencesList;
    private RegisterFragment SignupFragment;
    private SignupListener signupListener;



    private void setListener(SignupListener signupListener){
        this.signupListener = signupListener;
    }

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
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("database");
        usersRef = ref.child("users");
        preferencesRef = ref.child("preferences");

        /*user_preferences*/
        userPreferencesList = new ArrayList<>();

        initUserRefList();


        /*Firebase storage*/
        mStorageRef  = FirebaseStorage.getInstance().getReference("uploads");
        uploadRef =  FirebaseDatabase.getInstance().getReference("uploads");


        signupButton = findViewById(R.id.link_signup);
        loginButton = findViewById(R.id.btn_login);
        buttonForgotPassword = findViewById(R.id.password_forgot);
        buttonForgotPassword.setOnClickListener(this);
        auth = FirebaseAuth.getInstance();


        /*Forgot password*/

        SignupFragment = new RegisterFragment();
        signupButton.setOnClickListener(this);


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



    private void afterSucessAuth()
    {
        startActivity(new Intent(LoginRegister.this, MenuActivity.class));
        //finish();
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

            case R.id.link_signup:
                getSupportFragmentManager().beginTransaction().add(R.id.signup_fragment, SignupFragment,"signup_fragment").commit();



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

}


