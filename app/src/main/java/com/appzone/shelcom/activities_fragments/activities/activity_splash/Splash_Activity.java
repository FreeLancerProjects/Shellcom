package com.appzone.shelcom.activities_fragments.activities.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.activities_fragments.activities.sign_in_sign_up_activity.activity.Login_Activity;
import com.appzone.shelcom.language.Language_Helper;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.tags.Tags;


public class Splash_Activity extends AppCompatActivity {
   private FrameLayout fl;
   private Preferences preferences;
   private String session;
   private Animation animation;


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        preferences=Preferences.getInstance();
        session=preferences.getSession(this);
        fl=findViewById(R.id.fl);

        animation= AnimationUtils.loadAnimation(getBaseContext(),R.anim.lanuch);

        fl.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(session.equals(Tags.session_login)){
                    Intent intent = new Intent(Splash_Activity.this, Home_Activity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(Splash_Activity.this, Login_Activity.class);
                    startActivity(intent);
                }
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
