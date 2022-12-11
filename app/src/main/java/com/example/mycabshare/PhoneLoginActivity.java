package com.example.mycabshare;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.chaos.view.PinView;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText phoneEditText;
    private TextView otptext;
    private PinView firstPinView;
    private ConstraintLayout phonelayout;
    private String selected_country_code="+91";
    private ProgressBar progressBar;

    ///////firebase phone auth////////
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResentToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private FirebaseAuth mAuth;
    ///////firebase phone auth////////



    private static final int CREDENTIAL_PICKER_REQUEST =120 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        ccp= findViewById(R.id.ccp);
        phoneEditText=findViewById(R.id.editTextTextPersonName);
        firstPinView=(PinView) findViewById(R.id.firstPinView);
        phonelayout=(ConstraintLayout) findViewById(R.id.phonelayout);
        otptext = (TextView) findViewById(R.id.otptext);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();


        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                selected_country_code=ccp.getSelectedCountryCodeWithPlus();
            }
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start , int before, int after) {

                if(s.toString().length()== 10)
                {
                    Toast.makeText(PhoneLoginActivity.this,"Valid Phone Number",Toast.LENGTH_SHORT).show();
                    phonelayout.setVisibility(View.GONE);
                    firstPinView.setVisibility(View.VISIBLE);
                    otptext.setVisibility(View.VISIBLE);
                    sendotp();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        firstPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start , int before, int count) {

                if(s.toString().length()== 6)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,firstPinView.getText().toString().trim());
                    signInWithAuthCredentials(credential);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
      /*  ///////////auto phone selector////////
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Credentials.getClient(PhoneLoginActivity.this).getHintPickerIntent(hintRequest);
        try
        {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0,new Bundle());
        }
        catch (IntentSender.SendIntentException e)
        {
            e.printStackTrace();
        }*/

        /////////////////otp calbacks//////////////////////////////

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();
                if(code != null)
                {
                    firstPinView.setText(code);

                    signInWithAuthCredentials(phoneAuthCredential);
                }

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(PhoneLoginActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                phonelayout.setVisibility(View.VISIBLE);
                firstPinView.setVisibility(View.GONE);

            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token){
                super.onCodeSent(verificationId, token);

                mVerificationId=verificationId;
                mResentToken=token;
                Toast.makeText(PhoneLoginActivity.this,"OTP sent to given number",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                phonelayout.setVisibility(View.GONE);
                firstPinView.setVisibility(View.VISIBLE);

            }
        };

        /////////////////otp calbacks//////////////////////////////

    }


    private void sendotp() {

        progressBar.setVisibility(View.VISIBLE);
        String phonenumber = selected_country_code+phoneEditText.getText().toString();

        PhoneAuthOptions options=
                PhoneAuthOptions.newBuilder(mAuth)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setPhoneNumber(phonenumber)
                        .setActivity(PhoneLoginActivity.this)
                        .setCallbacks(callbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK)
        {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);

            phoneEditText.setText(Credential.getId().substring);


        }
        else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE)
        {
            // *** No phone numbers available ***
            Toast.makeText(PhoneLoginActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
        }
    }*/

    private void signInWithAuthCredentials(PhoneAuthCredential credentials) {

        mAuth.signInWithCredential(credentials)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            final String[] token = {null};
                            FirebaseMessaging.getInstance().getToken()
                                    .addOnCompleteListener(new OnCompleteListener<String>() {
                                        @Override
                                        public void onComplete(@NonNull Task<String> task) {
                                            if (!task.isSuccessful()) {
                                                return;
                                            }

                                            // Get new FCM registration token
                                            token[0] = task.getResult();

                                        }
                                    });
                            String number= phoneEditText.getText().toString().trim();
                            String Ridertoken = token.toString();

                            User user = new User(number,Ridertoken);

                            FirebaseDatabase.getInstance().getReference("Riders")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user);

                            Toast.makeText(PhoneLoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(PhoneLoginActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(PhoneLoginActivity.this, "Log in Failed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PhoneLoginActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
    }
}