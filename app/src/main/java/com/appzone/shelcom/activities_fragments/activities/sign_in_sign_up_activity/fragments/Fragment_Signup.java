package com.appzone.shelcom.activities_fragments.activities.sign_in_sign_up_activity.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.sign_in_sign_up_activity.activity.Login_Activity;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Signup extends Fragment implements OnCountryPickerListener {


    private ImageView image_back, image_phone_code;
    private EditText edt_name, edt_phone, edt_email, edt_password;
    private TextView tv_code;
    private Button btn_sign_up;
    private CountryPicker picker;
    private Login_Activity activity;
    private String current_language;
    private String code = "";
    private Preferences preferences;


    public static Fragment_Signup newInstance() {
        return new Fragment_Signup();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (Login_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        image_back = view.findViewById(R.id.image_back);
        image_phone_code = view.findViewById(R.id.image_phone_code);

        if (current_language.equals("ar")) {
            image_back.setRotation(180.0f);
            image_phone_code.setRotation(180.0f);
        }


        edt_name = view.findViewById(R.id.edt_name);
        edt_phone = view.findViewById(R.id.edt_phone);
        tv_code = view.findViewById(R.id.tv_code);
        edt_email = view.findViewById(R.id.edt_email);
        edt_password = view.findViewById(R.id.edt_password);
        btn_sign_up = view.findViewById(R.id.btn_sign_up);

        CreateCountryDialog();

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });


        image_phone_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.showDialog(activity);
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });


    }

    private void CreateCountryDialog() {
        CountryPicker.Builder builder = new CountryPicker.Builder()
                .canSearch(true)
                .with(activity)
                .listener(this);
        picker = builder.build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);


        if (picker.getCountryFromSIM() != null) {
            updateUi(picker.getCountryFromSIM());

        } else if (telephonyManager != null && picker.getCountryByISO(telephonyManager.getNetworkCountryIso()) != null) {
            updateUi(picker.getCountryByISO(telephonyManager.getNetworkCountryIso()));


        } else if (picker.getCountryByLocale(Locale.getDefault()) != null) {
            updateUi(picker.getCountryByLocale(Locale.getDefault()));

        } else {
            tv_code.setText("+966");
            code = "+966";
        }


    }

    @Override
    public void onSelectCountry(Country country) {
        updateUi(country);
    }

    private void updateUi(Country country) {

        tv_code.setText(country.getDialCode());
        code = country.getDialCode();


    }

    private void checkData() {

        String m_name = edt_name.getText().toString().trim();
        String m_phone = edt_phone.getText().toString().trim();
        String m_email = edt_email.getText().toString().trim();
        String m_password = edt_password.getText().toString().trim();

        if (!TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_phone) &&
                !TextUtils.isEmpty(m_email) &&
                Patterns.EMAIL_ADDRESS.matcher(m_email).matches() &&
                !TextUtils.isEmpty(m_password) &&
                !TextUtils.isEmpty(code)

        ) {
            Common.CloseKeyBoard(activity, edt_name);
            edt_name.setError(null);
            edt_phone.setError(null);
            edt_email.setError(null);
            edt_password.setError(null);

            sign_up(m_name, code, m_phone, m_email, m_password);
        } else {
            if (TextUtils.isEmpty(m_name)) {
                edt_name.setError(getString(R.string.field_req));
            } else {
                edt_name.setError(null);

            }


            if (TextUtils.isEmpty(m_phone)) {
                edt_phone.setError(getString(R.string.field_req));
            } else {
                edt_phone.setError(null);

            }


            if (TextUtils.isEmpty(m_email)) {
                edt_email.setError(getString(R.string.field_req));
            } else if (!Patterns.EMAIL_ADDRESS.matcher(m_email).matches()) {
                edt_email.setError(getString(R.string.inv_email));

            } else {
                edt_email.setError(null);

            }

            if (TextUtils.isEmpty(m_password)) {
                edt_password.setError(getString(R.string.field_req));
            } else {
                edt_password.setError(null);

            }


            if (TextUtils.isEmpty(code)) {
                tv_code.setError(getString(R.string.field_req));
            } else {
                tv_code.setError(null);

            }

        }

    }

    private void sign_up(String m_name, String code, String m_phone, String m_email, String m_password) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .sign_up(m_name, m_email, m_password, code.replace("+", "00"), m_phone, 1)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null) {

                            preferences = Preferences.getInstance();
                            preferences.create_update_userdata(activity,response.body());
                            activity.NavigateToHomeActivity();
                        } else if (response.code() == 422) {
                                Common.CreateSignAlertDialog(activity,getString(R.string.email_exists));
                        } else {

                            try {
                                Log.e("Error_code",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(activity,getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }


}
