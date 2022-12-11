package com.example.reminderandtodo;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.location.LocationServices;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class location_service extends Service {
    Location gps_loc;
    Location network_loc;
    Location final_loc;
    double longitude;
    double latitude;
    MediaPlayer player;
    AssetFileDescriptor afd;
    long count=0;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String BACKGROUND_LOCATION = Manifest.permission.ACCESS_BACKGROUND_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    Location lastlocation=null;
    boolean playing=false;
    LocationManager mLocationManager;
    NotificationCompat.Builder builder;
    void playsong(String playtone){
        if(player!=null && player.isPlaying()) {
            player.stop();
            player.release();
            player=new MediaPlayer();
        }
        player=new MediaPlayer();
        try {
            afd = getAssets().openFd("alarm.mp3");

        player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playing=true;
        player.setLooping(true);
        player.start();
    }
    NotificationManagerCompat notificationManager ; Thread location;
    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here

            lastlocation=location;


        }
    };
    boolean bool=true;
    void getLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);



    }
    @Override
    public void onCreate() {
        super.onCreate();


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
      bool=false;
      mLocationManager.removeUpdates(mLocationListener);
        if(player!=null && player.isPlaying()) {
            player.stop();
            player.release();
            player = new MediaPlayer();
        }
        playing=false;
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int i= super.onStartCommand(intent, flags, startId);
      location= new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (bool) {
                            Log.d("Service", "Service is running...");
                            if(lastlocation!=null) {
                                Location endPoint=new Location("locationA");
                                endPoint.setLatitude(16.316201310796266);
                                endPoint.setLongitude(80.43721575289965);

                                double distance=lastlocation.distanceTo(endPoint);
                                count+=1;
                                String loc=lastlocation.getLatitude()+" "+lastlocation.getLongitude()+" "+count+" "+distance;
                                if(distance<100 && playing==false) {
                                    playsong("alarm.mp3");
                                }
                                Log.d("location", lastlocation.getLatitude() + "" + lastlocation.getLongitude());
                                createNotification(loc);
                                startForeground(1, builder.build());
                                appendLog(loc);
                            }
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions((Activity) getApplicationContext(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                   0);
        }
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 0, (LocationListener) mLocationListener);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,
                    0, mLocationListener);

       location.start();
        createNotificationChannel();
        createNotification("demo");
        startForeground(1, builder.build());

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        builder= new NotificationCompat.Builder(this, "101");
        builder.setColor(getApplicationContext().getColor(android.R.color.system_neutral1_0));
        Intent brod=new Intent(getApplicationContext(),stop_notify.class);
        PendingIntent i=PendingIntent.getBroadcast(getApplicationContext(), 0, brod, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        Spannable spannable = new SpannableString("Stop");

        spannable.setSpan(new ForegroundColorSpan(Color.WHITE), 0, "Stop".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        builder.addAction(R.mipmap.ic_launcher2,spannable,i);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name ="channel1";
            String description ="channel for location";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("101", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("101");
        }
    }
    private void createNotification(String data){
            PendingIntent i;
                builder
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Reminder And Todo")
                .setContentText(data).setOngoing(true).setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            }
    private boolean getLocationPermission() {
        Log.d("TAG", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this,
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
      }
        return false;
    }
    public void appendLog(String text)
    {
        File logFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/ReminderAndTodo/Reminders/log.txt");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

