package com.evyatartzik.android2_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginRegister extends AppCompatActivity {

    private Button signupButton, loginButton;
    Button buttonForgotPassword;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    DatabaseReference usersRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");
        usersRef = ref.child("users");


        signupButton = findViewById(R.id.link_signup);
        loginButton = findViewById(R.id.btn_login);
        buttonForgotPassword = findViewById(R.id.password_forgot);

        auth = FirebaseAuth.getInstance();

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
                                                ref.child(name).setValue(new User(name,email,password));
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
                                    //String name = findNameInDataBase();
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
        Intent intent = new Intent(LoginRegister.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

}

