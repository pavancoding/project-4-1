package com.example.reminderandtodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Settings extends Fragment {
    FirebaseAuth firebaseAuth;
    GoogleSignInClient googleSignInClient;
    Button b;
@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view=inflater.inflate(R.layout.fragment_settings, container, false);
    firebaseAuth=FirebaseAuth.getInstance();
    b=view.findViewById(R.id.button7);
    Intent i=getActivity().getIntent();
    String type_login=i.getStringExtra("type_login");
    // Initialize firebase user
    FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

    // Check condition
    /*if(firebaseUser!=null)
    {
        Log.d("user data",firebaseUser.getDisplayName());
        Log.d("user data",firebaseUser.getEmail());
    }*/
    googleSignInClient= GoogleSignIn.getClient(getActivity().getApplicationContext(),GoogleSignInOptions.DEFAULT_SIGN_IN);
    b.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(firebaseUser.getEmail()!=null) {
                        googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // Check condition
                                if (task.isSuccessful()) {
                                    firebaseAuth.signOut();
                                    Toast.makeText(getActivity().getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getActivity(), Login_screen.class);
                                    getActivity().startActivity(i);
                                    getActivity().finish();
                                }
                            }
                        });
                    }
                    else {
                        firebaseAuth.signOut();
                        Toast.makeText(getActivity().getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getActivity(), Login_screen.class);
                        getActivity().startActivity(i);
                        getActivity().finish();
                    }
            }});
    return view;
}
}