package com.evyatartzik.android2_project;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginRegister extends AppCompatActivity {

    private Button signupButton, loginButton;
    TextView textViewForgotPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);


        textViewForgotPassword = findViewById(R.id.password_forgot);
        textViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Implemt new dialog that send reset mail
                /**
                 *         btnReset.setOnClickListener(new View.OnClickListener() {
                 *             @Override
                 *             public void onClick(View v) {
                 *
                 *                 String email = inputEmail.getText().toString().trim();
                 *
                 *                 if (TextUtils.isEmpty(email)) {
                 *                     Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                 *                     return;
                 *                 }
                 *
                 *                 progressBar.setVisibility(View.VISIBLE);
                 *                 auth.sendPasswordResetEmail(email)
                 *                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                 *                             @Override
                 *                             public void onComplete(@NonNull Task<Void> task) {
                 *                                 if (task.isSuccessful()) {
                 *                                     Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                 *                                 } else {
                 *                                     Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                 *                                 }
                 *
                 *                                 progressBar.setVisibility(View.GONE);
                 *                             }
                 *                         });
                 *             }
                 *         });
                 */
            }
        });


        auth = FirebaseAuth.getInstance();
        signupButton = findViewById(R.id.link_signup);
        loginButton = findViewById(R.id.btn_login);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginRegister.this);
                final View view = getLayoutInflater().inflate(R.layout.dialog_register, null);
                final EditText NameET = view.findViewById(R.id.input_name);
                final EditText PasswordET = view.findViewById(R.id.input_password);
                final EditText EmailET = view.findViewById(R.id.input_email);
                final Button RegisterButton = view.findViewById(R.id.btn_signup);
                final LottieAnimationView DonelottieAnimationView = view.findViewById(R.id.done_animation);


                RegisterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String email = EmailET.getText().toString().trim();
                        final String password = PasswordET.getText().toString().trim();

                        if (TextUtils.isEmpty(email)) {
                            Snackbar.make(view, "Enter Email", Snackbar.LENGTH_SHORT).show();
                        } else if (TextUtils.isEmpty(password)) {
                            Snackbar.make(view, "Enter Password", Snackbar.LENGTH_SHORT).show();
                        } else if (password.length() < 6) {
                            Snackbar.make(view, "Password too short, enter minimum 6 characters!", Snackbar.LENGTH_SHORT).show();
                        } else {
                            DonelottieAnimationView.setVisibility(View.VISIBLE);
                            DonelottieAnimationView.loop(false);

                            auth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(LoginRegister.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                            } else {
                                                finish();
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final EditText editTextEmail = findViewById(R.id.input_email);
                final EditText editTextPassword = findViewById(R.id.input_password);

                final String email = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();

                if (email.isEmpty()) {
                    editTextEmail.setError("Enter Mail");
                    return;
                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Enter Password");
                    return;
                }
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginRegister.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        editTextPassword.setError(task.getException().getMessage());
                                    } else {
                                        editTextEmail.setError(task.getException().getMessage());
                                    }
                                } else {
                                    Toast.makeText(LoginRegister.this, "Succesfully login", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
            }
        });


    }



}

