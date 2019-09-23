package com.creativeshare.shell_com.activities_fragments.activities.sign_in_sign_up_activity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.shell_com.activities_fragments.activities.sign_in_sign_up_activity.fragments.Fragment_Language;
import com.creativeshare.shell_com.activities_fragments.activities.sign_in_sign_up_activity.fragments.Fragment_Login;
import com.creativeshare.shell_com.activities_fragments.activities.sign_in_sign_up_activity.fragments.Fragment_Signup;
import com.creativeshare.shell_com.language.Language_Helper;
import com.creativeshare.shell_com.preferences.Preferences;
import com.creativeshare.shell_com.tags.Tags;

import io.paperdb.Paper;


public class Login_Activity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment_Login fragmentLogin;
    private Fragment_Signup fragmentSignup;
    private Fragment_Language fragment_language;

    private int fragment_counter = 0;
    private Preferences preferences;


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = Preferences.getInstance();
        Paper.init(this);

        if (savedInstanceState == null) {
            fragmentManager = this.getSupportFragmentManager();

            if (preferences.isLanguageSelected(this))
            {
                if (preferences.getSession(this).equals(Tags.session_login))
                {
                    NavigateToHomeActivity();
                }else
                    {
                        DisplayFragmentLogin();

                    }

            }else
                {
                   DisplayFragmentLanguage();
                }

        }

        getDataFromIntent();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("sign_up"))
        {
            boolean isSign_in = intent.getBooleanExtra("sign_up",true);
            if (!isSign_in)
            {
                DisplayFragmentSignUp();

            }
        }
    }


    private void DisplayFragmentLanguage()
    {

        fragment_language = Fragment_Language.newInstance();

        if (fragment_language.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_language).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();
        }
    }

    private void DisplayFragmentLogin()
    {

        fragment_counter += 1;
        if (fragmentLogin == null) {
            fragmentLogin = Fragment_Login.newInstance();
        }
        if (fragmentLogin.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentLogin).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentLogin, "fragmentLogin").addToBackStack("fragmentLogin").commit();
        }
    }

    public void DisplayFragmentSignUp()
    {

        fragment_counter += 1;

        if (fragmentSignup == null) {
            fragmentSignup = Fragment_Signup.newInstance();
        }
        if (fragmentSignup.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentSignup).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragmentSignup, "fragmentSignup").addToBackStack("fragmentSignup").commit();
        }
    }

    public void NavigateToHomeActivity()
    {
        Intent intent = new Intent(Login_Activity.this, Home_Activity.class);
        startActivity(intent);
        finish();
    }



    public void Back() {
        if (fragment_counter == 1) {
            finish();
        } else {
            fragment_counter -= 1;
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }


    public void RefreshActivity(String selected_language) {
        Log.e("lang",selected_language);
        Paper.book().write("lang",selected_language);
        preferences.setIsLanguageSelected(this);
        Language_Helper.setNewLocale(this,selected_language);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
