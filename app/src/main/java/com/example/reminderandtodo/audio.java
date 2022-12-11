package com.example.reminderandtodo;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class audio extends AppCompatActivity {
    RadioGroup ringtones;
    MediaPlayer player;
    ImageView done,close;
    String ringtone="alarm";
    AssetFileDescriptor afd;
    String playtone="alarm.mp3";
    @Override
    public void onBackPressed() {
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        player=new MediaPlayer();
        done=findViewById(R.id.done);
        close=findViewById(R.id.close);
        ringtones=findViewById(R.id.ringtones);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(player.isPlaying()) {
                    player.stop();
                    player.release();
                    player=new MediaPlayer();
                }
                Intent intent = new Intent();
                intent.putExtra("ringtone", ringtone);
                intent.putExtra("playtone", playtone);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying()) {
                    player.stop();
                    player.release();
                    player=new MediaPlayer();
                }
                finish();
            }
        });
        ringtones.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioButton1:
                        if(player.isPlaying()) {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }

                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("alarm.mp3");
                            ringtone="alarm";
                            playtone="alarm.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.setLooping(true);
                            player.start();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton2:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("alert.mp3");
                            ringtone="alert";
                            playtone="alert.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.setLooping(true);

                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.radioButton3:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        AssetFileDescriptor afd;
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("Army trumph.mp3");
                            ringtone="Army Trumph";
                            playtone="Army trumph.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.setLooping(true);

                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton4:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("beep.mp3");
                            ringtone="Beep";
                            playtone="beep.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.setLooping(true);

                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton5:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("Before Sunrise Alarm.mp3");
                            ringtone="Before Sunrise";
                            playtone="Before Sunrise Alarm.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.setLooping(true);

                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton6:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try { player.release();
                            player=new MediaPlayer();

                            afd = getAssets().openFd("birds and flute.mp3");
                            ringtone="Birds and Flute";
                            playtone="birds and flute.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton7:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("birds and piano.mp3");
                            ringtone="Birds and Piano";
                            playtone="birds and piano.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case R.id.radioButton8:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("mixed alarm.mp3");
                            ringtone="Mixed alarm";
                            playtone="mixed alarm.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton9:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("scary.mp3");
                            ringtone="Scary";
                            playtone="scary.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton10:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("smooth piano.mp3");
                            ringtone="Smooth Piano";
                            playtone="smooth piano.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton11:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("water flow,birds.mp3");
                            ringtone="Water flow";
                            playtone="water flow,birds.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton12:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("Android Alarm Tone.mp3");
                            ringtone="Androd Alarm";
                            playtone="Android Alarm Tone.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton13:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("Realme Alarm Tone.mp3");
                            ringtone="Realme Alarm";
                            playtone="Realme Alarm Tone.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton14:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("Rise Realme ! tone.mp3");
                            ringtone="Rise tone";
                            playtone="Rise Realme ! tone.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton15:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("Rise Up.mp3");
                            ringtone="Rise up";
                            playtone="Rise Up.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.radioButton16:
                        if(player.isPlaying())
                        {
                            player.stop();
                            player.release();
                            player=new MediaPlayer();
                        }
                        try {
                            player.release();
                            player=new MediaPlayer();
                            afd = getAssets().openFd("Wake Up.mp3");
                            ringtone="Wake up";
                            playtone="Wake Up.mp3";
                            player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                            player.prepare();
                            player.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }
}