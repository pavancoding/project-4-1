package com.example.reminderandtodo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class Display_screen extends AppCompatActivity {
    SliderView sliderView;
    int[] images={R.drawable.timeandplace,R.drawable.driblee,R.drawable.reminder};
    String data[]={"Location Reminder\nReminds You When You Reach The Destination","Schedule The Task\nTo Remind You Before The Deadline","Get Live Notification Or Alarm Of Task And Reminders"};
    private CharSequence mText;
    private int mIndex;
    TextView tv=null;
    private long mDelay = 500;
    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            tv.setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        tv.setText("");
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
    Button login,register;
    FirebaseAuth firebaseAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_screen);
        tv=findViewById(R.id.textView);
        setCharacterDelay(60);
        animateText("Never Forget Task\nJust leave To Us\nWe Remind You...");
        sliderView=findViewById(R.id.slideView);
        SliderViewExample example=new SliderViewExample(images,data);
        sliderView.setSliderAdapter(example);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();
        login=findViewById(R.id.button);
        register=findViewById(R.id.button2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Display_screen.this,Signup.class);
                startActivity(i);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Display_screen.this,Login_screen.class);
                startActivity(i);
                finish();
            }
        });
        firebaseAuth= FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        // Check condition
        if(firebaseUser!=null)
        {
            startActivity(new Intent(Display_screen.this,Main_Screen.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}