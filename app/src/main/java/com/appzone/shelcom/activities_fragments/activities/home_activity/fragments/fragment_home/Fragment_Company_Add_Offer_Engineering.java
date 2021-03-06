package com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.models.EngineeringOrderDetailsModel;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.tags.Tags;
import com.google.android.material.appbar.AppBarLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Company_Add_Offer_Engineering extends Fragment {
    private static final String TAG = "ORDER_ID";
    private static final String TAG2 = "NOTIFICATION_ID";

    private ImageView image_back,image_map_arrow;
    private LinearLayout ll_back,ll;
    private CircleImageView image;
    private RoundedImageView img_property;
    private TextView tv_client_name,tv_order_num,tv_property_type,tv_address,tv_area,tv_description;
    private ProgressBar progBar;
    private CoordinatorLayout cord_layout;
    private FrameLayout fl_view_location;
    private EditText edt_cost;
    private AppBarLayout app_bar;
    private Button btn_accept,btn_refused;
    private String current_language;
    private UserModel userModel;
    private Preferences preferences;
    private Home_Activity activity;
    private int notification_id;


    private EngineeringOrderDetailsModel engineeringOrderDetailsModel;

    public static Fragment_Company_Add_Offer_Engineering newInstance(int order_id, int notification_id)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG,order_id);
        bundle.putInt(TAG2,notification_id);
        Fragment_Company_Add_Offer_Engineering fragment_company_add_offer_water_delivery = new Fragment_Company_Add_Offer_Engineering();
        fragment_company_add_offer_water_delivery.setArguments(bundle);
        return fragment_company_add_offer_water_delivery;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company_add_offer_engineering,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        image_back = view.findViewById(R.id.image_back);
        image_map_arrow = view.findViewById(R.id.image_map_arrow);
        if (current_language.equals("ar")||current_language.equals("ur")){
            image_back.setRotation(180.0f);
            image_map_arrow.setRotation(180.0f);
        }
        ll_back = view.findViewById(R.id.ll_back);
        ll = view.findViewById(R.id.ll);
        image = view.findViewById(R.id.image);
        img_property = view.findViewById(R.id.img_property);
        tv_client_name = view.findViewById(R.id.tv_client_name);
        tv_order_num = view.findViewById(R.id.tv_order_num);
        tv_description = view.findViewById(R.id.tv_description);

        tv_property_type = view.findViewById(R.id.tv_property_type);
        tv_address = view.findViewById(R.id.tv_address);
        tv_area = view.findViewById(R.id.tv_area);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        cord_layout = view.findViewById(R.id.cord_layout);
        fl_view_location = view.findViewById(R.id.fl_view_location);
        edt_cost = view.findViewById(R.id.edt_cost);
        app_bar = view.findViewById(R.id.app_bar);
        btn_accept = view.findViewById(R.id.btn_accept);
        btn_refused = view.findViewById(R.id.btn_refused);

        app_bar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int total_range = appBarLayout.getTotalScrollRange();
                if ((total_range+i)>60)
                {
                    image.setVisibility(View.VISIBLE);
                    tv_client_name.setVisibility(View.VISIBLE);
                }else
                    {
                        image.setVisibility(View.GONE);
                        tv_client_name.setVisibility(View.GONE);
                    }
            }
        });

        Bundle bundle = getArguments();
        if (bundle!=null)
        {
            int order_id = bundle.getInt(TAG);
            notification_id = bundle.getInt(TAG2);

            getOrderData(order_id);
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cost = edt_cost.getText().toString().trim();
                if (!TextUtils.isEmpty(cost))
                {
                    edt_cost.setError(null);
                    Common.CloseKeyBoard(activity,edt_cost);
                    Accept(cost);
                }else
                {
                    edt_cost.setError(getString(R.string.field_req));
                }
            }
        });
        btn_refused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refuse();
            }
        });
    }

    private void Refuse() {

        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .companyRefuseOrder(notification_id)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            activity.updateNotificationData();
                        }else
                        {
                            try {
                                Log.e("code_error",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        try {
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Log.e("error",t.getMessage());
                        }catch (Exception e)
                        {

                        }


                    }
                });
    }
    private void Accept(String cost) {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .companySendOffer(userModel.getUser().getCompany_information().getId(),engineeringOrderDetailsModel.getOrder_details().getOrder_id(),String.valueOf(notification_id),cost)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            activity.updateNotificationData();
                        }else
                        {
                            try {
                                Log.e("code_error",response.code()+"_"+response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                        try {
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Log.e("error",t.getMessage());
                        }catch (Exception e)
                        {

                        }


                    }
                });

    }
    private void getOrderData(int order_id) {

        Api.getService(Tags.base_url)
                .getEngineeringOrderDetails(order_id,Tags.ENGINEERING_ORDER)
                .enqueue(new Callback<EngineeringOrderDetailsModel>() {
                    @Override
                    public void onResponse(Call<EngineeringOrderDetailsModel> call, Response<EngineeringOrderDetailsModel> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            updateUI(response.body());
                        }else
                            {
                                try {
                                    Log.e("code_error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onFailure(Call<EngineeringOrderDetailsModel> call, Throwable t) {
                        Log.e("error",t.getMessage());
                    }
                });
        fl_view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentMapLocation_Details(Double.parseDouble(engineeringOrderDetailsModel.getOrder_details().getLatitude()),Double.parseDouble(engineeringOrderDetailsModel.getOrder_details().getLongitude()),engineeringOrderDetailsModel.getOrder_details().getAddress());
            }
        });
    }

    private void updateUI(EngineeringOrderDetailsModel engineeringOrderDetailsModel) {
        this.engineeringOrderDetailsModel = engineeringOrderDetailsModel;
        cord_layout.setVisibility(View.VISIBLE);
        ll.setVisibility(View.VISIBLE);

        Picasso.with(activity).load(Uri.parse(Tags.base_url+engineeringOrderDetailsModel.getOrder().getFrom_user_image())).placeholder(R.drawable.logo_512).fit().into(image);
        Picasso.with(activity).load(Uri.parse(Tags.eng_url+engineeringOrderDetailsModel.getOrder_details().getPropertyImage())).fit().into(img_property);

        tv_client_name.setText(engineeringOrderDetailsModel.getOrder().getFrom_user_name());
        tv_order_num.setText(String.format("%s %s","#",engineeringOrderDetailsModel.getOrder_details().getOrder_id()));
        tv_address.setText(engineeringOrderDetailsModel.getOrder_details().getAddress());
        tv_area.setText(engineeringOrderDetailsModel.getOrder_details().getPropertyArea());
        tv_description.setText(engineeringOrderDetailsModel.getOrder().getDescription());
        if (current_language.equals("ar")||current_language.equals("ur"))
        {
            tv_property_type.setText(engineeringOrderDetailsModel.getOrder_details().getAr_type());
        }else
            {
                tv_property_type.setText(engineeringOrderDetailsModel.getOrder_details().getEn_type());

            }
    }
}
