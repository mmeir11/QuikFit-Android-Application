package com.evyatartzik.android2_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class LoginRegister extends AppCompatActivity {

    File profilePhoto;
    final int CAMERA_REQUEST=1;
    private Button signupButton, loginButton;
    Button buttonForgotPassword;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    DatabaseReference usersRef;
    private StorageReference mStorageRef;
    ProgressDialog progressDialog;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        setContentView(R.layout.activity_login_register);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");
        usersRef = ref.child("users");


        signupButton = findViewById(R.id.link_signup);
        loginButton = findViewById(R.id.btn_login);
        buttonForgotPassword = findViewById(R.id.password_forgot);

        auth = FirebaseAuth.getInstance();

        /*Firebase Storage*/
        mStorageRef = FirebaseStorage.getInstance().getReference();

        /*Forgot password*/
        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        /*Sign up to Firebase*/
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginRegister.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_register, null);
                final EditText PasswordET = view.findViewById(R.id.input_password);
                final EditText EmailET = view.findViewById(R.id.input_email);
                final EditText NameET = view.findViewById(R.id.input_name);
                final Button RegisterButton = view.findViewById(R.id.btn_signup);
                final LottieAnimationView DonelottieAnimationView = view.findViewById(R.id.done_animation);
                final Button buttonCamera = view.findViewById(R.id.camera_btn);
                buttonCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                        }
                    }
                });
                RegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email = EmailET.getText().toString().trim();
                        final String password = PasswordET.getText().toString().trim();
                        final String name = NameET.getText().toString().trim();

                        if (TextUtils.isEmpty(email)) {
                            EmailET.setError(getText(R.string.mandatory_field));
                        } else if (TextUtils.isEmpty(password)) {
                            PasswordET.setError(getText(R.string.mandatory_field));
                        } else if (password.length() < 6) {
                            PasswordET.setError(getText(R.string.password_min));
                        } else {
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
                                                User user = new User(name,email,password);
                                                ref.child(name).setValue(user);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST && resultCode==RESULT_OK)
        {

            //UPLOAD IMAGE TO FIREBASE STORAGE...

        }
    }
    private void afterSucessAuth()
    {
        startActivity(new Intent(LoginRegister.this, MenuActivity.class));
        finish();
    }

}

