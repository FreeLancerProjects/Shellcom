package com.appzone.shelcom.activities_fragments.activities.activity_other_services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.activity_other_services.fragments.Fragment_Container;
import com.appzone.shelcom.activities_fragments.activities.activity_other_services.fragments.Fragment_Customer_Clearance;
import com.appzone.shelcom.activities_fragments.activities.activity_other_services.fragments.Fragment_Engineering_Consultances;
import com.appzone.shelcom.activities_fragments.activities.activity_other_services.fragments.Fragment_main_Other_services;
import com.appzone.shelcom.language.Language_Helper;
import com.appzone.shelcom.preferences.Preferences;

import java.util.List;

public class OtherActivity extends AppCompatActivity {
    private int fragment_count = 1;
    private FragmentManager fragmentManager;
    private Fragment_main_Other_services fragment_main_other_services;
    private Fragment_Engineering_Consultances fragment_engineering_consultances;
    private Fragment_Container fragment_container;
    private Fragment_Customer_Clearance fragment_customer_clearance;



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        if (savedInstanceState == null) {
            fragmentManager = this.getSupportFragmentManager();
            displayFragmentOtherServices();
        }

    }

    private void displayFragmentOtherServices() {

        fragment_main_other_services = Fragment_main_Other_services.newInstance();
        if (fragment_main_other_services.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_main_other_services).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_main_other_services, "fragment_main_other_services").addToBackStack("fragment_main_other_services").commit();
        }
    }



    public void displayfragmentengneering() {
        fragment_count+=1;
        fragment_engineering_consultances = Fragment_Engineering_Consultances.newInstance();
        if (fragment_engineering_consultances.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_engineering_consultances).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_engineering_consultances, "fragment_engineering_consultances").addToBackStack("fragment_engineering_consultances").commit();
        }
    }

    public void displayfragmentcontainer() {
        fragment_count+=1;
        fragment_container = Fragment_Container.newInstance();
        if (fragment_container.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_container).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_container, "fragment_container").addToBackStack("fragment_container").commit();
        }
    }

    public void displayfragmentcustomerclearance() {
        fragment_count+=1;
        fragment_customer_clearance = Fragment_Customer_Clearance.newInstance();
        if (fragment_customer_clearance.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_customer_clearance).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_customer_clearance, "fragment_customer_clearance").addToBackStack("fragment_customer_clearance").commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment:fragmentList)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back() {
        Log.e("frcou",fragment_count+"_");
        if (fragment_count>1)
        {
            fragment_count-=1;
            super.onBackPressed();
        }else
            {
                finish();
            }
    }
}
