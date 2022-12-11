package com.example.reminderandtodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import in.aabhasjindal.otptextview.OtpTextView;
import taimoor.sultani.sweetalert2.Sweetalert;

public class Otp_verification extends AppCompatActivity {
    OtpTextView tv;
    TextView Timer;
    ImageView back;
    String mVerificationId=null;
    FirebaseAuth mAuth;
    TextView displaynumber;
    Button otp_verfication;
    Sweetalert pDialog;
    TextView Resend_otp;
    boolean resend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_verification);
        tv=findViewById(R.id.otp_view);
        tv.requestFocus();
        Timer=findViewById(R.id.textView11);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Intent data=getIntent();
        String phone=data.getStringExtra("phone");
        Log.d("data",phone);
        Resend_otp=findViewById(R.id.textView10);
        resend=false;
        back=findViewById(R.id.imageView2);

        displaynumber=findViewById(R.id.textView7);
        otp_verfication=findViewById(R.id.button6);
        mAuth=FirebaseAuth.getInstance();
        displaynumber.setText("Please enter the 6 digits code sent to at \n"+phone);
       pDialog = new Sweetalert(this, Sweetalert.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);

        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(Otp_verification.this, Phone_number.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
        CountDownTimer timer= new CountDownTimer(59000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(millisUntilFinished/1000>10)
                Timer.setText("00 : "+millisUntilFinished/1000);
                else
                    Timer.setText("00 : 0"+millisUntilFinished/1000);
            }
            public void onFinish() {
                resend=true;
            }
        };

        sendVerificationCode(phone);
        timer.start();
        otp_verfication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCode(tv.getOTP());
            }
        });
        Resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(resend) {
                    sendVerificationCode(phone);
                    resend=false;
                    timer.start();
                    Toast.makeText(Otp_verification.this, "Otp Sent", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void sendVerificationCode(String mobile) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(mobile)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(Otp_verification.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    final String code = phoneAuthCredential.getSmsCode();

                    if (code != null && mVerificationId !=null) {
                        tv.setOTP(code);
                        pDialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                // yourMethod();
                            }
                        }, 2000);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(Otp_verification.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.i("data", "onVerificationFailed: " + e.getLocalizedMessage());
                }

                //when the code is generated then this method will receive the code.
                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                   super.onCodeSent(s, forceResendingToken);

                    //storing the verification id that is sent to the user
                    mVerificationId = s;
                }
            };

    private void verifyCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        pDialog.show();
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(Otp_verification.this,
                        new OnCompleteListener<AuthResult>() {

                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //verification successful we will start the profile activity
                                    Intent intent = new Intent(Otp_verification.this, Main_Screen.class);
                                    intent.putExtra("type_login", "phonenumber");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    pDialog.cancel();
                                    startActivity(intent);

                                } else {

                                    //verification unsuccessful.. display an error message

                                    String message = "Somthing is wrong, we will fix it soon...";

                                    if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                        message = "Invalid code entered...";
                                    }
                                    Toast.makeText(Otp_verification.this,message,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
    }
}