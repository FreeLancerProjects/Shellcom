package com.creativeshare.shell_com.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.activity_other_services.OtherActivity;
import com.creativeshare.shell_com.activities_fragments.activities.activity_shipment_transportation.activity.ShipmentActivity;
import com.creativeshare.shell_com.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.shell_com.adapters.Slider_Adapter;
import com.creativeshare.shell_com.models.SliderDataModel;
import com.creativeshare.shell_com.models.UserModel;
import com.creativeshare.shell_com.preferences.Preferences;
import com.creativeshare.shell_com.remote.Api;
import com.creativeshare.shell_com.tags.Tags;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_main extends Fragment {
   // private LinearLayout ll_water_delivery,ll_shipment,ll_rental,ll_other_services;
    private ViewPager pager;
    private TabLayout tab;
    private Home_Activity activity;
    private TextView tv_no_ads;
    private Slider_Adapter slider_adapter;
    private ProgressBar progBar;
    private TimerTask timerTask;
    private Timer timer;
    private Preferences preferences;
    private UserModel userModel;
    private CardView card_furniture,card_container;
    public static Fragment_main newInstance() {
        return new Fragment_main();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        pager = view.findViewById(R.id.pager);
        tab = view.findViewById(R.id.tab);
        progBar = view.findViewById(R.id.progBar);
        tv_no_ads = view.findViewById(R.id.tv_no_ads);
        tab.setupWithViewPager(pager);
card_furniture=view.findViewById(R.id.card_furniture);
card_container=view.findViewById(R.id.card_container);
       /* ll_water_delivery = view.findViewById(R.id.ll_water_delivery);
        ll_shipment = view.findViewById(R.id.ll_shipment);
        ll_rental = view.findViewById(R.id.ll_rental);
        ll_other_services = view.findViewById(R.id.ll_other_services);*/

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


    /*    ll_water_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel!=null)
                {
                    activity.DisplayFragmentWaterDeliveryReserve();

                }else
                    {
                        CreateAlertDialog(activity,1);
                    }
            }
        });*/

    card_furniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel!=null)
                {
                    Intent intent = new Intent(activity, ShipmentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else
                    {
                        CreateAlertDialog(activity,2);
                    }

            }
        });

        card_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel!=null)
                {
                    activity.displayfragmentcontainer();

                }else
                    {
                        CreateAlertDialog(activity,3);
                    }
            }
        });
/*
        ll_other_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userModel!=null)
                {
                    Intent intent = new Intent(activity, OtherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else
                {
                    CreateAlertDialog(activity,4);
                }



            }
        });

*/

        getSlider();
    }

    private void getSlider()
    {
        Api.getService(Tags.base_url2)
                .getSliders()
                .enqueue(new Callback<SliderDataModel>() {
                    @Override
                    public void onResponse(Call<SliderDataModel> call, Response<SliderDataModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            updateUI(response.body());
                        }else
                            {
                                if (response.code() == 404)
                                {
                                    tv_no_ads.setVisibility(View.VISIBLE);
                                }
                                try {
                                    Log.e("error_code",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    }

                    @Override
                    public void onFailure(Call<SliderDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }
    private void updateUI(SliderDataModel sliderDataModel)
    {

        if (sliderDataModel.getData().size()>1)
        {
            slider_adapter = new Slider_Adapter(activity,sliderDataModel.getData());
            pager.setAdapter(slider_adapter);
            timerTask = new MyTimerTask();
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask,6000,6000);

            for (int i = 0 ; i<sliderDataModel.getData().size()-1;i++)
            {
                View view = ((ViewGroup)tab.getChildAt(0)).getChildAt(i);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
                params.setMargins(5,0,5,0);
                tab.requestLayout();

            }

        }else
            {
                slider_adapter = new Slider_Adapter(activity,sliderDataModel.getData());
                pager.setAdapter(slider_adapter);
            }
    }
    private void CreateAlertDialog(Context context, final int type)
    {
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_sign,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(getString(R.string.si_su));
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (type ==1)
                {
                    activity.DisplayFragmentWaterDeliveryReserve();

                }else if (type ==2)
                {
                    Intent intent = new Intent(activity, ShipmentActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else if (type ==3)
                {
                    activity.DisplayFragmentEquipments();

                }else if (type ==4)
                {
                    Intent intent = new Intent(activity, OtherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }
    private class MyTimerTask extends TimerTask
    {
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pager.getCurrentItem()<pager.getAdapter().getCount()-1)
                    {
                        pager.setCurrentItem(pager.getCurrentItem()+1);
                    }else
                        {
                            pager.setCurrentItem(0);
                        }
                }
            });
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer!=null)
        {
            timer.purge();
            timer.cancel();
        }

        if (timerTask!=null)
        {
            timerTask.cancel();
        }


    }
}
