package com.creativeshare.shell_com.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.shell_com.activities_fragments.activities.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.shell_com.models.TermsModel;
import com.creativeshare.shell_com.remote.Api;
import com.creativeshare.shell_com.tags.Tags;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Terms_Condition extends Fragment {
    private static final String TAG="type";
    private TextView tv_title;
    private ImageView arrow;
    private TextView tv_content;
    private ProgressBar progBar;
    private String current_language;
    private AppCompatActivity activity;


    public static Fragment_Terms_Condition newInstance(int type)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG,type);
        Fragment_Terms_Condition fragment_terms_condition = new Fragment_Terms_Condition();
        fragment_terms_condition.setArguments(bundle);
        return fragment_terms_condition;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_conditions,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        activity = (AppCompatActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = view.findViewById(R.id.arrow);

        if (current_language.equals("ar"))
        {
            arrow.setRotation(180.0f);
        }
        tv_title = view.findViewById(R.id.tv_title);

        tv_content = view.findViewById(R.id.tv_content);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ActivityCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity instanceof Home_Activity)
                {
                    Home_Activity home_activity = (Home_Activity) activity;
                    home_activity.Back();
                }else if (activity instanceof Login_Activity)
                {
                    Login_Activity login_activity = (Login_Activity) activity;
                    login_activity.Back();
                }
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            int type = bundle.getInt(TAG);
            if (type == 1)
            {
                tv_title.setText(getString(R.string.terms_of_service));
                getTermsCondition();

            }else if (type == 2)
            {
                tv_title.setText(getString(R.string.about_tour));

                getAboutUs();
            }
        }
    }

    private void getTermsCondition()
    {

        Api.getService(Tags.base_url)
                .getTerms()
                .enqueue(new Callback<TermsModel>() {
                    @Override
                    public void onResponse(Call<TermsModel> call, Response<TermsModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            if (current_language.equals("ar"))
                            {
                                tv_content.setText(response.body().getAr_content());
                            }else
                                {
                                    tv_content.setText(response.body().getEn_content());

                                }
                        }else
                        {


                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TermsModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void getAboutUs()
    {

        Api.getService(Tags.base_url)
                .getAboutUs()
                .enqueue(new Callback<TermsModel>() {
                    @Override
                    public void onResponse(Call<TermsModel> call, Response<TermsModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            if (current_language.equals("ar")||current_language.equals("ur"))
                            {
                                tv_content.setText(response.body().getAr_content());
                            }else
                            {
                                tv_content.setText(response.body().getEn_content());

                            }
                        }else
                        {


                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TermsModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }



}
