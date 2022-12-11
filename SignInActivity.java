package com.example.driverapp;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.driverapp.Model.TokenModel;
import com.example.driverapp.Utils.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.FirebaseInstallationsApi;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignInActivity extends AppCompatActivity {
    private TextView reggis;
    private EditText mEmail, mPass;
    private Button login;
    private Button forgot;
    FirebaseAuth mAuth;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;

    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        reggis = (TextView) findViewById(R.id.regtext);
        reggis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        mEmail = (EditText) findViewById(R.id.editTextTextPersonName);
        mPass = (EditText) findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();

        login = (Button) findViewById(R.id.butsign);
        forgot = (Button) findViewById(R.id.forgotpass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = inflater.inflate(R.layout.reset_pop, null);

                reset_alert.setTitle("Forgot Password?")
                        .setMessage("Enter your Email Address to get Password reset link.")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText email = view1.findViewById(R.id.resetid);
                                if (email.getText().toString().isEmpty()) {
                                    email.setError("Required Field");
                                    return;
                                }
                                mAuth.sendPasswordResetEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(SignInActivity.this, "Reset Email Sent", Toast.LENGTH_SHORT).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view1)
                        .create().show();
            }
        });

       // init();


    }

    /*private void init() {

        firebaseAuth = FirebaseAuth.getInstance();
        listener = myFirebaseAuth->{
            FirebaseUser user = myFirebaseAuth.getCurrentUser();
            if(user!=null) {
                //update Token
                FirebaseInstallations.getInstance().getToken(true).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<InstallationTokenResult>() {
                    @Override
                    public void onSuccess(InstallationTokenResult firebaseInstallations) {
                        Log.d("TOKEN", String.valueOf(firebaseInstallations.getToken()));
                        UserUtils.updateToken(SignInActivity.this,firebaseInstallations.getToken());
                    }
                });

            }
        };
    }*/


    private void loginUser() {
        mEmail = (EditText) findViewById(R.id.editTextTextPersonName);
        mPass = (EditText) findViewById(R.id.editTextTextPassword);
        String email;
        email = mEmail.getText().toString();
        String pass;
        pass = mPass.getText().toString();

        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            if (!pass.isEmpty()) {


                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(SignInActivity.this, "Login Successfully !!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignInActivity.this, MapsActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignInActivity.this, "Login Failed !!", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                mPass.setError("Empty Fields Are not Allowed");
            }
        } else if (email.isEmpty()) {
            mEmail.setError("Empty Fields Are not Allowed");
        } else {
            mEmail.setError("Pleas Enter Correct Email");
        }
    }
}