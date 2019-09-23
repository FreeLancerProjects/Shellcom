package com.creativeshare.shell_com.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.shell_com.models.UserModel;
import com.creativeshare.shell_com.preferences.Preferences;
import com.creativeshare.shell_com.remote.Api;
import com.creativeshare.shell_com.share.Common;
import com.creativeshare.shell_com.tags.Tags;
import com.zcw.togglebutton.ToggleButton;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_More extends Fragment {
    private Home_Activity activity;
    private LinearLayout ll_allow_receive_order;
    private ConstraintLayout cons_edit_profile,cons_language,cons_terms,cons_rate,cons_about,cons_contact;
    private ImageView arrow1,arrow2,arrow3,arrow4,arrow5,arrow6;
    private String current_language;
    private String [] language_array;
    private ToggleButton toggle_btn;
    private UserModel userModel;
    private Preferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more,container,false);
        initView(view);
        return view;
    }

    public static Fragment_More newInstance()
    {
        return new Fragment_More();
    }
    private void initView(View view) {
        activity = (Home_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang",Locale.getDefault().getLanguage());

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);
        arrow6 = view.findViewById(R.id.arrow6);


        language_array = new String[]{"English","Urdu","العربية"};

        if (current_language.equals("ar")||current_language.equals("ur"))
        {

            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);
            arrow6.setRotation(180.0f);

        }

        ll_allow_receive_order = view.findViewById(R.id.ll_allow_receive_order);

        cons_edit_profile = view.findViewById(R.id.cons_edit_profile);
        cons_language = view.findViewById(R.id.cons_language);
        cons_terms = view.findViewById(R.id.cons_terms);
        cons_rate = view.findViewById(R.id.cons_rate);
        cons_about = view.findViewById(R.id.cons_about);
        cons_contact = view.findViewById(R.id.cons_contact);

        toggle_btn = view.findViewById(R.id.toggle_btn);

        if (userModel.getUser().getCompany_information()!=null)
        {
            if (userModel.getUser().getCompany_information().getIs_avaliable()==0)
            {
                toggle_btn.setToggleOff();
            }else
                {
                    toggle_btn.setToggleOn();
                }
        }
        cons_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + activity.getPackageName())));
                } catch (android.content.ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + activity.getPackageName())));
                }
            }
        });

        cons_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentTerms_AboutUs(1);
            }

        });

        cons_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentContactUS();
            }
        });

        cons_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentTerms_AboutUs(2);

            }
        });


        cons_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateLanguageDialog();
            }
        });

        cons_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity.DisplayFragmentEditProfile();
            }
        });


        toggle_btn.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on)
                {
                    updateState(1);
                }else
                {
                    updateState(0);

                }
            }
        });





        if (userModel!=null&&userModel.getUser().getCompany_information()!=null)
        {
            ll_allow_receive_order.setVisibility(View.VISIBLE);
        }else
        {
            ll_allow_receive_order.setVisibility(View.GONE);


        }

    }

    private void updateState(final int state)
    {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.show();

        Api.getService(Tags.base_url)
                .changeAvailability(state,userModel.getUser().getId())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {

                            Log.e("sss",state+"_");
                            userModel.getUser().getCompany_information().setIs_avaliable(state);
                            activity.updateUserData(userModel);

                            if (state ==0)
                            {
                                toggle_btn.setToggleOff();


                            }else
                            {
                                toggle_btn.setToggleOn();

                            }
                        }else
                        {

                            if (state ==0)
                            {
                                toggle_btn.setToggleOff();


                            }else
                            {
                                toggle_btn.setToggleOn();

                            }
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            if (state ==0)
                            {
                                toggle_btn.setToggleOff();


                            }else
                            {
                                toggle_btn.setToggleOn();

                            }
                            dialog.dismiss();
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }




    private void CreateLanguageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view  = LayoutInflater.from(activity).inflate(R.layout.dialog_language,null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(language_array.length-1);
        numberPicker.setDisplayedValues(language_array);
        numberPicker.setWrapSelectorWheel(false);
        if (current_language.equals("ar"))
        {
            numberPicker.setValue(2);

        }else if (current_language.equals("en"))
        {
            numberPicker.setValue(0);

        }else
            {
                numberPicker.setValue(1);
            }
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int pos = numberPicker.getValue();
                if (pos == 0)
                {
                    activity.RefreshActivity("en");
                }else if (pos ==1)
                {
                    activity.RefreshActivity("ur");

                }
                else
                {
                    activity.RefreshActivity("ar");

                }

            }
        });




        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }


}
