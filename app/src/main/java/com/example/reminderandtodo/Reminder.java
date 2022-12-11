package com.example.reminderandtodo;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class Reminder extends Fragment {
    RecyclerView Reminders;
    FloatingActionButton btn;
    AlertDialog dialog;
    map_dalog dalog;
    private final int REQUEST_CHECK_SETTINGS=10001;
    private boolean gpsEnabled=false;
    GoogleApiClient mGoogleApiClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    RelativeLayout l;
    alarm_dialog dalog2;
    String current="";
    private boolean mLocationPermissionsGranted=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_reminder, container, false);
    if (Build.VERSION.SDK_INT >= 23) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        } else {
            //do something
        }
    }
    Reminders=view.findViewById(R.id.Reminders);
    btn=view.findViewById(R.id.floatingActionButton);
    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle("Select Type");
            builder.setCancelable(false);
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });
            View view= getLayoutInflater().inflate(R.layout.type_reminderrs, null);
            LinearLayout Reminder_alarm=view.findViewById(R.id.Reminder_alarm);
            LinearLayout Reminder_location=view.findViewById(R.id.Reminder_location);
            Reminder_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current="alarm_dialog";
                    dialog.cancel();
                    dalog2=new alarm_dialog(getActivity(),getActivity().getSupportFragmentManager(),getActivity().getContentResolver(),getActivity().getApplicationContext());
                    dalog2.showbottomsheet();
                }
            });
            Reminder_location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current="map_di" +
                            "alog";
                    dialog.cancel();
                    if(mLocationPermissionsGranted==false){
                        getLocationPermission();
                    }
                    if(gpsEnabled==false){
                        displayLocationSettingsRequest(getContext());
                    }
                    if(gpsEnabled==true) {
                        dalog = new map_dalog(getActivity(), getActivity().getSupportFragmentManager(), getActivity().getContentResolver(), getActivity().getApplicationContext());
                        dalog.showbottomsheet();
                    }
                }
            });
            builder.setView(view);
            dialog= builder.show();
        }
    });

    ArrayList<ReminderData> data=new ArrayList<ReminderData>() ;
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","6h 25min Left","alarm","Office work",new String[]{"Ring Once"}));
    data.add(new ReminderData(R.drawable.location_reminders,"demoid","25h 25min Left","alarm","Office work",new String[]{"M,T,W,TH,F,S,Su"}));
    data.add(new ReminderData(R.drawable.checklist_reminders,"demoid","10d 5hr Left","alarm","Office work",new String[]{"Daily"}));
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","10m 15days Left","alarm","Office work",new String[]{"Ring Once"}));
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","10y 16yrs Left","alarm","Office work",new String[]{"Ring Once"}));
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","6h 25min Left","alarm","Office work",new String[]{"Ring Once"}));
    data.add(new ReminderData(R.drawable.location_reminders,"demoid","25h 25min Left","alarm","Office work",new String[]{"M,T,W,TH,F,S,Su"}));
    data.add(new ReminderData(R.drawable.checklist_reminders,"demoid","10d 5hr Left","alarm","Office work",new String[]{"Daily"}));
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","10m 15days Left","alarm","Office work",new String[]{"Ring Once"}));
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","10y 16yrs Left","alarm","Office work",new String[]{"Ring Once"}));
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","6h 25min Left","alarm","Office work",new String[]{"Ring Once"}));
    data.add(new ReminderData(R.drawable.location_reminders,"demoid","25h 25min Left","alarm","Office work",new String[]{"M,T,W,TH,F,S,Su"}));
    data.add(new ReminderData(R.drawable.checklist_reminders,"demoid","10d 5hr Left","alarm","Office work",new String[]{"Daily"}));
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","10m 15days Left","alarm","Office work",new String[]{"Ring Once"}));
    data.add(new ReminderData(R.drawable.alarm_reminders,"demoid","10y 16yrs Left","alarm","Office work",new String[]{"Ring Once"}));
    ReminderAdapter adapter = new ReminderAdapter(data);
    Reminders.setHasFixedSize(true);
    Reminders.setNestedScrollingEnabled(false);
    Reminders.setLayoutManager((new LinearLayoutManager(getActivity())));
    Reminders.setAdapter(adapter);

    return view;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==2){
            if(resultCode==RESULT_OK){
                dalog.setmaps(data.getStringExtra("latitude")+","+data.getStringExtra("longitude")+","+data.getStringExtra("radius"));
            }
        }
        if(requestCode==100 && current.equals("map_dialog"))
            dalog.ActivityResult(requestCode, resultCode, data);
        else if(requestCode==100 && current.equals("alarm_dialog"))
            dalog2.ActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            if(resultCode == RESULT_OK){
                if(current.equals("alarm_dialog")){
                    dalog2.getRingtone(data.getStringExtra("ringtone"),data.getStringExtra("playtone"));
                }
                else{
                    dalog.getRingtone(data.getStringExtra("ringtone"));
                }
            }
        }
    }
    private void getLocationPermission() {
        Log.d("TAG", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionsGranted = true;
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            permissions,
                            LOCATION_PERMISSION_REQUEST_CODE);
                }

            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    private void displayLocationSettingsRequest(Context context) {
        mGoogleApiClient= new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("TAG", "All location settings are satisfied.");
                        gpsEnabled=true;
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("TAG", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("TAG", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("TAG", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }
}
