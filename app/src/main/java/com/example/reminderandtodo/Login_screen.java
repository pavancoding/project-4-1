package com.example.reminderandtodo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import taimoor.sultani.sweetalert2.Sweetalert;

public class Login_screen extends AppCompatActivity {
    TextInputLayout tl1, tl2;
    Button b, google;
    Button phone_number;
    SignInButton btSignIn;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    ImageView back;
    Sweetalert pDialog;
    TextView forgot_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tl1 = findViewById(R.id.Gmail);
        tl2 = findViewById(R.id.password);
        back = findViewById(R.id.imageView2);
        google = findViewById(R.id.button4);
        forgot_password=findViewById(R.id.textView4);
        EditText t1 = tl1.getEditText();
        pDialog = new Sweetalert(this, Sweetalert.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        phone_number=findViewById(R.id.button5);
        t1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tl1.setError(null);
                return false;
            }
        });
        tl2.getEditText().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                tl2.setError(null);
                return false;
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmail=tl1.getEditText().getText().toString();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                if(gmail.length()>0) {
                    auth.sendPasswordResetEmail(gmail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Login_screen.this, "Mail sent", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(Login_screen.this, "Fail to sent Mail", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(Login_screen.this, "Invalid Gmail", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b = findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               firebaseAuth=FirebaseAuth.getInstance();
               String email=tl1.getEditText().getText().toString();
               String password=tl2.getEditText().getText().toString();
                Sweetalert pDialog = new Sweetalert(Login_screen.this, Sweetalert.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Please Wait");
                pDialog.setCancelable(false);

               if(email.length()>0 && password.length()>0) {
                   pDialog.show();
                   firebaseAuth.signInWithEmailAndPassword(email, password)
                           .addOnCompleteListener(Login_screen.this, new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if (task.isSuccessful()) {
                                       Intent i=new Intent(Login_screen.this
                                               , Main_Screen.class)
                                               .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                       i.putExtra("type_login", "password");
                                       startActivity(i);

                                       displayToast("Login successful");
                                       pDialog.cancel();
                                       finish();
                                   } else {
                                       tl2.setError("Invalid Gmail/Password");
                                       pDialog.cancel();
                                   }
                               }
                           });
               }
               else{
                   Toast.makeText(Login_screen.this, "Fill Fields", Toast.LENGTH_SHORT).show();
               }
            }
        });
        phone_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  Intent i=new Intent(Login_screen.this,Phone_number.class);
                  startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login_screen.this, Display_screen.class);
                startActivity(i);
                finish();
            }
        });

        firebaseAuth= FirebaseAuth.getInstance();
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                ).requestIdToken("443907710448-uiau0f6dkki9ghlrjt24a391v1enbjao.apps.googleusercontent.com").requestEmail().build();
                googleSignInClient = GoogleSignIn.getClient(Login_screen.this
                        , googleSignInOptions);
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 100);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            Task<GoogleSignInAccount> signInAccountTask=GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            if (signInAccountTask.isSuccessful()) {


                try {
                    GoogleSignInAccount googleSignInAccount = signInAccountTask
                            .getResult(ApiException.class);

                    DatabaseReference mDatabase;
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(googleSignInAccount!=null) {
                        pDialog.show();
                        mDatabase.child("Gmails").orderByValue().equalTo(googleSignInAccount.getEmail()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.getValue() != null) {
                                    if (googleSignInAccount != null) {
                                        AuthCredential authCredential = GoogleAuthProvider
                                                .getCredential(googleSignInAccount.getIdToken()
                                                        , null);
                                        firebaseAuth.signInWithCredential(authCredential)
                                                .addOnCompleteListener(Login_screen.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            String s = "Google sign in successful";
                                                            displayToast(s);
                                                            Intent i=new Intent(Login_screen.this
                                                                    , Main_Screen.class)
                                                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            i.putExtra("type_login", "Gmail");
                                                            startActivity(i);
                                                            displayToast("Firebase authentication successful");
                                                            pDialog.cancel();
                                                            finish();
                                                        } else {
                                                            pDialog.cancel() ;
                                                            displayToast("Authentication Failed :" + task.getException()
                                                                    .getMessage());
                                                        }
                                                    }
                                                });

                                    }
                                } else {
                                    pDialog.cancel();
                                    googleSignInClient.signOut();
                                    Toast.makeText(Login_screen.this, "No Account exists with the gmail", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                pDialog.cancel();
                                Log.d("data", error.getMessage());
                            }
                        });
                    }
                } catch (ApiException e) {
                    pDialog.cancel();
                    e.printStackTrace();
                }
            }
        }
    }
    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}