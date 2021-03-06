package com.projectfootball.projectfootball;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    EditText MobileNumber,OTPEditview;
    Button SubmitButton,OTPButton;
    TextView Textview,Otp;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    final Context context = this;

    boolean mVerificationInProgress = false, SignUp=true;
    String mVerificationId;
    PhoneAuthProvider.ForceResendingToken mResendToken;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        SignUp = getIntent().getExtras().getBoolean("SignUp");
//        Toast.makeText(OtpActivity.this,"SignUp : "+ SignUp,Toast.LENGTH_LONG).show();

        MobileNumber = (EditText) findViewById(R.id.mobileNumber);
        SubmitButton = (Button) findViewById(R.id.submit_Button);
        OTPEditview = (EditText) findViewById(R.id.otp_editText);
        OTPButton = (Button) findViewById(R.id.otp_button);
        Textview = (TextView) findViewById(R.id.textView);
        Otp = (TextView) findViewById(R.id.otp);

        mAuth = FirebaseAuth.getInstance();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//                Toast.makeText(OtpActivity.this,"verification done"+ phoneAuthCredential,Toast.LENGTH_LONG).show();
                Toast.makeText(OtpActivity.this,"Inside onVerificationCompleted ",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, MainActivity.class);
//                intent.putExtra("mVerificationId",mVerificationId);
                startActivity(intent);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OtpActivity.this,"verification fail",Toast.LENGTH_LONG).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    Toast.makeText(OtpActivity.this,"invalid mob no",Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Toast.makeText(OtpActivity.this,"Too many requests, please try later" ,Toast.LENGTH_LONG).show();
                    // [END_EXCLUDE]
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //Log.d(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(OtpActivity.this,"Verification code sent to mobile",Toast.LENGTH_LONG).show();
                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                MobileNumber.setVisibility(View.GONE);
                SubmitButton.setVisibility(View.GONE);
                Textview.setVisibility(View.GONE);
                OTPButton.setVisibility(View.VISIBLE);
                OTPEditview.setVisibility(View.VISIBLE);
                Otp.setVisibility(View.VISIBLE);
                // ...
            }
        };



        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91"+MobileNumber.getText().toString(),        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        OtpActivity.this,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks
            }
        });

        OTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, OTPEditview.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if (SignUp==true){
            Toast.makeText(OtpActivity.this,"SignUp : "+ SignUp,Toast.LENGTH_LONG).show();

        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("LinkOTP", "linkWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
//                            updateUI(user);
                        } else {
                            Log.w("LinkOTPException", "FirebaseException", task.getException());
                            if (task.getException() instanceof FirebaseException) {
                                //mVerificationField.setError("Invalid code.");
                                Toast.makeText(OtpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                Log.w("LinkOTP", "linkWithCredential:failure", task.getException());
                                Toast.makeText(OtpActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            }
                        }

                        // ...
                    }
                });

        }
        else if (SignUp==false){
            Toast.makeText(OtpActivity.this,"SignUp : "+ SignUp,Toast.LENGTH_LONG).show();

            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithCredential:success");
                                Toast.makeText(OtpActivity.this,"Verification done",Toast.LENGTH_LONG).show();
                                FirebaseUser user = task.getResult().getUser();
                                // ...
                            } else {
                                // Sign in failed, display a message and update the UI
                                //Log.w(TAG, "signInWithCredential:failure", task.getException());
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                    Toast.makeText(OtpActivity.this,"Verification failed code invalid",Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }

    }
}
