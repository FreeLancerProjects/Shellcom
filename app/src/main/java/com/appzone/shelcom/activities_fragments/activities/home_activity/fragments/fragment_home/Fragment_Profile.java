package com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.tags.Tags;
import com.google.android.material.appbar.AppBarLayout;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Profile extends Fragment {
    private ImageView image,arrow_company,arrow_logout;
    private TextView tv_name,tv_email,tv_phone,tv_address,tv_city;
    private SimpleRatingBar rateBar;
    private ConstraintLayout cons_logout;
    private LinearLayout ll_company_data,ll_register_company;
    private Home_Activity activity;
    private Preferences preferences;
    private UserModel userModel;
    private String current_language;
    private AppBarLayout app_bar;
    public static Fragment_Profile newInstance() {

        return new Fragment_Profile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (Home_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        arrow_company = view.findViewById(R.id.arrow_company);
        arrow_logout = view.findViewById(R.id.arrow_logout);

        if (current_language.equals("ar")||current_language.equals("ur"))
        {
            arrow_company.setRotation(180.0f);
            arrow_logout.setRotation(180.0f);

        }

        app_bar = view.findViewById(R.id.app_bar);

        image = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_email = view.findViewById(R.id.tv_email);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_address = view.findViewById(R.id.tv_address);
        tv_city = view.findViewById(R.id.tv_city);
        rateBar = view.findViewById(R.id.rateBar);
        cons_logout = view.findViewById(R.id.cons_logout);
        ll_company_data = view.findViewById(R.id.ll_company_data);

        ll_register_company = view.findViewById(R.id.ll_register_company);



        ll_register_company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userModel.getUser().getCompany_information()==null)
                {
                    activity.DisplayFragmentUpgradeToCompany();

                }else
                    {
                        CreateAlertDialog();
                    }
            }
        });


        app_bar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int total_range = appBarLayout.getTotalScrollRange();
                if ((total_range+i)<60)
                {
                    tv_name.setVisibility(View.GONE);
                    rateBar.setVisibility(View.GONE);
                    image.setVisibility(View.GONE);
                }else
                    {
                        tv_name.setVisibility(View.VISIBLE);
                        image.setVisibility(View.VISIBLE);
                        if (userModel.getUser().getCompany_information()!=null)
                        {
                            rateBar.setVisibility(View.VISIBLE);

                        }

                    }
            }
        });

        cons_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Logout();
            }
        });

        updateUi(userModel);


    }

    private void CreateAlertDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();

        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_sign,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(getString(R.string.already_company));
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        dialog.setView(view);
        dialog.show();
    }

    private void updateUi(UserModel userModel) {

        if (userModel!=null&&userModel.getUser()!=null)
        {
            // is user
            if (userModel.getUser().getCompany_information() == null)
            {
                rateBar.setVisibility(View.GONE);
                ll_company_data.setVisibility(View.GONE);
                Picasso.with(activity).load(Uri.parse(Tags.IMAGE_USERS_URL+userModel.getUser().getLogo())).placeholder(R.drawable.logo_512).fit().into(image);
                tv_name.setText(userModel.getUser().getUsername());
                tv_email.setText(userModel.getUser().getEmail());
                tv_phone.setText(String.format("%s %s",userModel.getUser().getPhone_code().replaceFirst("00","+"),userModel.getUser().getPhone()));
            }else
                {
                    // is company

                    ll_company_data.setVisibility(View.VISIBLE);
                    rateBar.setVisibility(View.VISIBLE);
                    Picasso.with(activity).load(Uri.parse(Tags.IMAGE_COMPANY_URL+userModel.getUser().getCompany_information().getCompany_logo())).placeholder(R.drawable.logo_512).fit().into(image);
                    tv_name.setText(userModel.getUser().getCompany_information().getTitle());
                    tv_email.setText(userModel.getUser().getEmail());
                    tv_phone.setText(String.format("%s %s",userModel.getUser().getPhone_code().replaceFirst("00","+"),userModel.getUser().getPhone()));
                    tv_city.setText(userModel.getUser().getCompany_information().getCity());
                    tv_address.setText(userModel.getUser().getCompany_information().getAddress());

                }
        }
    }

    public void UpdateUserData(UserModel userModel) {
        this.userModel = userModel;
        updateUi(userModel);
    }
}
