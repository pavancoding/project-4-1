package com.example.reminderandtodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import taimoor.sultani.sweetalert2.Sweetalert;


public class Signup extends AppCompatActivity {
    ImageView back;
    TextInputLayout first_name,last_name,gmail,phone_number,password,re_type_password;
    CountryCodePicker countrycode;
    Button Sign_up;
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean isValidPassword(String password)
    {
        if (!((password.length() >= 8)
                && (password.length() <=16 ))) {
            return false;
        }
        if (password.contains(" ")) {
            return false;
        }
        if (true) {
            int count = 0;
            for (int i = 0; i <= 9; i++) {
                String str1 = Integer.toString(i);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }
        if (!(password.contains("@") || password.contains("#")
                || password.contains("!") || password.contains("~")
                || password.contains("$") || password.contains("%")
                || password.contains("^") || password.contains("&")
                || password.contains("*") || password.contains("(")
                || password.contains(")") || password.contains("-")
                || password.contains("+") || password.contains("/")
                || password.contains(":") || password.contains(".")
                || password.contains(", ") || password.contains("<")
                || password.contains(">") || password.contains("?")
                || password.contains("|"))) {
            return false;
        }

        if (true) {
            int count = 0;
            for (int i = 65; i <= 90; i++) {
                char c = (char)i;

                String str1 = Character.toString(c);
                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }

        if (true) {
            int count = 0;
            for (int i = 97; i <= 122; i++) {
                char c = (char)i;
                String str1 = Character.toString(c);

                if (password.contains(str1)) {
                    count = 1;
                }
            }
            if (count == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean verfication(){

        if(first_name.getEditText().getText().toString().length()==0 || last_name.getEditText().getText().toString().length()==0 || gmail.getEditText().getText().toString().length()==0 || phone_number.getEditText().getText().toString().length()==0 || password.getEditText().getText().toString().length()==0 || re_type_password.getEditText().getText().toString().length()==0  ) {
            FancyToast.makeText(this,"Fill All Fields",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            return false;
        }
       if(PhoneNumberUtils.isGlobalPhoneNumber(countrycode.getSelectedCountryCodeWithPlus()+phone_number.getEditText().getText().toString())==false && phone_number.getEditText().getText().toString().length()>0){
           FancyToast.makeText(this,"InValid Number",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
           return false;
       }
        if( isValidEmail(gmail.getEditText().getText().toString())==false){
            FancyToast.makeText(this,"InValid Gmail",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
            return false;
        }
        if(isValidPassword(password.getEditText().getText().toString())==false){
            password.setError("1 Uppercase,1 Lowercase,1 Special character,1 Number,Length >8");
            re_type_password.setError(null);
            return false;
        }
        if(password.getEditText().getText().toString().equals(re_type_password.getEditText().getText().toString())==false){
            password.setError(null);
            re_type_password.setError("Password not Matched");
            return false;
        }
        password.setError(null);
        re_type_password.setError(null);
        return true;
    }
    public void signup(){

    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_screen);
        back=findViewById(R.id.imageView2);
        first_name=findViewById(R.id.First_name);
        last_name=findViewById(R.id.Last_name);
        gmail=findViewById(R.id.Gmail);
        phone_number=findViewById(R.id.Phone_number);
        password=findViewById(R.id.Password);
        re_type_password=findViewById(R.id.Re_type_Password);
        countrycode=findViewById(R.id.country_code);
        Sign_up=findViewById(R.id.Sign_up);
        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean value=verfication();
                Sweetalert  pDialog = new Sweetalert(Signup.this, Sweetalert.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Please Wait");
                pDialog.setCancelable(false);

                DatabaseReference mDatabase;
                if(value==true) {
                    pDialog.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Gmails").orderByValue().equalTo(gmail.getEditText().getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue() == null) {
                                mDatabase.child("Phone_number").orderByValue().equalTo(countrycode.getSelectedCountryCodeWithPlus()+phone_number.getEditText().getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.getValue() == null) {
                                            FirebaseAuth mAuth;
                                            mAuth = FirebaseAuth.getInstance();
                                            Map<String,String> data=new HashMap<>();

                                            String email=gmail.getEditText().getText().toString(),pass=password.getEditText().getText().toString();

                                            data.put("First_name", first_name.getEditText().getText().toString());
                                            data.put("Last_name",last_name.getEditText().getText().toString());
                                            data.put("Gmail",email);
                                            data.put("Phone_number",countrycode.getSelectedCountryCodeWithPlus()+phone_number.getEditText().getText().toString());
                                            mAuth
                                                    .createUserWithEmailAndPassword(email, pass)
                                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                                        @Override
                                                        public void onComplete(@NonNull Task<AuthResult> task)
                                                        {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(),
                                                                                "Registration successful!",
                                                                                Toast.LENGTH_LONG)
                                                                        .show();
                                                                mDatabase.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(data);
                                                                mDatabase.child("Gmails").push().setValue(email);
                                                                mDatabase.child("Phone_number").push().setValue(countrycode.getSelectedCountryCodeWithPlus()+phone_number.getEditText().getText().toString());
                                                                Intent i=new Intent(Signup.this,Login_screen.class);
                                                                pDialog.cancel();
                                                                mAuth.signOut();
                                                                startActivity(i);
                                                                finish();
                                                            }
                                                            else {

                                                                // Registration failed
                                                                Toast.makeText(
                                                                                getApplicationContext(),
                                                                                "Registration failed!!"
                                                                                        + " Please try again later",
                                                                                Toast.LENGTH_LONG)
                                                                        .show();
                                                                pDialog.cancel();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            pDialog.cancel();
                                            Toast.makeText(Signup.this, "phone number exists", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        pDialog.cancel();
                                    }
                                });
                            } else {
                                pDialog.cancel();
                                Toast.makeText(Signup.this, "Gmail already exists", Toast.LENGTH_SHORT).show();
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
                Intent i=new Intent(Signup.this,Display_screen.class);
                startActivity(i);
                finish();
            }
        });

    }
}