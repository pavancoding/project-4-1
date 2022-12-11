package com.example.reminderandtodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telecom.PhoneAccount;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.concurrent.TimeUnit;

import taimoor.sultani.sweetalert2.Sweetalert;

public class Phone_number extends AppCompatActivity {
    ImageView back;
    Button Generate_otp;
    TextInputLayout phone_number;
    CountryCodePicker codePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_number);
        back = findViewById(R.id.imageView2);
        phone_number = findViewById(R.id.Phone_number);
        codePicker = findViewById(R.id.country_code);
        Generate_otp = findViewById(R.id.button8);
        Sweetalert pDialog = new Sweetalert(Phone_number.this, Sweetalert.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Please Wait");
        pDialog.setCancelable(false);
        Generate_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = codePicker.getSelectedCountryCodeWithPlus() + phone_number.getEditText().getText().toString();
                if (PhoneNumberUtils.isGlobalPhoneNumber(codePicker.getSelectedCountryCodeWithPlus() + phone_number.getEditText().getText().toString()) == false && phone.length()>0) {
                    FancyToast.makeText(Phone_number.this, "InValid Number", FancyToast.LENGTH_SHORT, FancyToast.ERROR, false).show();
                } else {

                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    pDialog.show();
                    mDatabase.child("Phone_number").orderByValue().equalTo(phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue()!=null) {
                                Log.d("data", phone);
                                Intent i = new Intent(Phone_number.this, Otp_verification.class);
                                i.putExtra("phone", phone);
                                pDialog.cancel();
                                startActivity(i);
                                finish();
                            }
                            else{
                                pDialog.cancel();
                                Toast.makeText(Phone_number.this, "No Account exists with the Number", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                                        pDialog.cancel();
                        }
                    });

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Phone_number.this, Login_screen.class);
                startActivity(i);
                finish();
            }
        });
    }
}