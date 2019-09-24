package com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.models.WaterOrderDetailsModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.tags.Tags;
import com.google.android.material.appbar.AppBarLayout;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Order_Details_Water_Delivery extends Fragment {
    private static final String TAG = "ORDER_ID";
    private static final String TAG2 = "PRICE";

    private ImageView image_back,image_map_arrow;
    private LinearLayout ll_back,ll,ll_order_state;
    private CircleImageView image;
    private TextView tv_client_name,tv_order_num,tv_size,tv_address,tv_city,tv_delivery_time,tv_cost;
    private ProgressBar progBar;
    private CoordinatorLayout cord_layout;
    private FrameLayout fl_view_location;
    private AppBarLayout app_bar;
    private Button btn_done;
    private String current_language;
    private UserModel userModel;
    private Preferences preferences;
    private Home_Activity activity;
    private String price;
    ///////////////////////////
    private ImageView image1,image5;
    private TextView tv1, tv5, tv_order_id;
    private View view1;


    private WaterOrderDetailsModel waterOrderDetailsModel;

    public static Fragment_Order_Details_Water_Delivery newInstance(int order_id, String price)
    {
        Bundle bundle = new Bundle();
        bundle.putInt(TAG,order_id);
        bundle.putString(TAG2,price);
        Fragment_Order_Details_Water_Delivery fragment_order_details_water_delivery = new Fragment_Order_Details_Water_Delivery();
        fragment_order_details_water_delivery.setArguments(bundle);
        return fragment_order_details_water_delivery;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_details_water_delivery,container,false);
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
        tv_client_name = view.findViewById(R.id.tv_client_name);
        tv_order_num = view.findViewById(R.id.tv_order_num);
        tv_size = view.findViewById(R.id.tv_size);
        tv_address = view.findViewById(R.id.tv_address);
        tv_city = view.findViewById(R.id.tv_city);
        tv_delivery_time = view.findViewById(R.id.tv_delivery_time);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        cord_layout = view.findViewById(R.id.cord_layout);
        fl_view_location = view.findViewById(R.id.fl_view_location);
        tv_cost = view.findViewById(R.id.tv_cost);
        app_bar = view.findViewById(R.id.app_bar);
        btn_done = view.findViewById(R.id.btn_done);
        ll_order_state = view.findViewById(R.id.ll_order_state);
        image1 = view.findViewById(R.id.image1);
        image5 = view.findViewById(R.id.image5);
        tv1 = view.findViewById(R.id.tv1);
        tv5 = view.findViewById(R.id.tv5);
        tv_order_id = view.findViewById(R.id.tv_order_id);
        view1 = view.findViewById(R.id.view1);

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
            price = bundle.getString(TAG2);

            getOrderData(order_id);
        }

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FinishOrder();
            }
        });

    }

    private void FinishOrder() {
        final ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService(Tags.base_url)
                .companyFinishOrder(waterOrderDetailsModel.getOrder_details().getOrder_id())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {
                            Toast.makeText(activity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                            activity.Back();
                            activity.refreshFragmentOrder();
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
                .getWaterOrderDetails(order_id,Tags.WATER_ORDER)
                .enqueue(new Callback<WaterOrderDetailsModel>() {
                    @Override
                    public void onResponse(Call<WaterOrderDetailsModel> call, Response<WaterOrderDetailsModel> response) {
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
                    public void onFailure(Call<WaterOrderDetailsModel> call, Throwable t) {
                        Log.e("error",t.getMessage());
                    }
                });
        fl_view_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.DisplayFragmentMapLocation_Details(Double.parseDouble(waterOrderDetailsModel.getOrder_details().getLatitude()),Double.parseDouble(waterOrderDetailsModel.getOrder_details().getLongitude()),waterOrderDetailsModel.getOrder_details().getAddress());
            }
        });
    }

    private void updateUI(WaterOrderDetailsModel waterOrderDetailsModel) {
        this.waterOrderDetailsModel = waterOrderDetailsModel;
        if (userModel.getUser().getCompany_information()==null)
        {
            Picasso.with(activity).load(Uri.parse(Tags.base_url+waterOrderDetailsModel.getOrder().getTo_user_image())).placeholder(R.drawable.logo_512).fit().into(image);
            tv_client_name.setText(waterOrderDetailsModel.getOrder().getTo_user_name());
            ll.setVisibility(View.GONE);

        }else
        {
            Picasso.with(activity).load(Uri.parse(Tags.base_url+waterOrderDetailsModel.getOrder().getFrom_user_image())).placeholder(R.drawable.logo_512).fit().into(image);
            tv_client_name.setText(waterOrderDetailsModel.getOrder().getFrom_user_name());
            ll.setVisibility(View.VISIBLE);

        }
        cord_layout.setVisibility(View.VISIBLE);
        tv_order_num.setText(String.format("%s %s","#",waterOrderDetailsModel.getOrder_details().getOrder_id()));
        tv_address.setText(waterOrderDetailsModel.getOrder_details().getAddress());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.ENGLISH);
        String date = dateFormat.format(new Date(Long.parseLong(waterOrderDetailsModel.getOrder_details().getArrival_time())*1000));
        tv_delivery_time.setText(date);
        tv_cost.setText(String.format("%s %s",price,getString(R.string.sar)));

        if (current_language.equals("ar")||current_language.equals("ur"))
        {
            tv_city.setText(waterOrderDetailsModel.getOrder_details().getAr_city_title());
            tv_size.setText(waterOrderDetailsModel.getOrder_details().getAr_size_title());
        }else
            {
                tv_city.setText(waterOrderDetailsModel.getOrder_details().getEn_city_title());
                tv_size.setText(waterOrderDetailsModel.getOrder_details().getEn_size_title());

            }

        updateStepView(Integer.parseInt(waterOrderDetailsModel.getOrder().getOrder_status()));

        tv_order_id.setText(String.format("%s %s","#",waterOrderDetailsModel.getOrder_details().getOrder_id()));

        if (userModel.getUser().getCompany_information()==null)
        {
            ll_order_state.setVisibility(View.VISIBLE);
        }else
        {
            ll_order_state.setVisibility(View.GONE);

        }

        if (waterOrderDetailsModel.getOrder().getOrder_status().equals("2"))
        {
            btn_done.setVisibility(View.GONE);
        }
        Log.e("order_state",waterOrderDetailsModel.getOrder().getOrder_status());


    }

    public void updateStepView(int completePosition) {
        switch (completePosition) {

            case 1:
                image1.setBackgroundResource(R.drawable.step_green_circle);
                image1.setImageResource(R.drawable.step_green_true);
                view1.setBackgroundColor(ContextCompat.getColor(activity, R.color.done));
                tv1.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

                break;
            case 2:
                image1.setBackgroundResource(R.drawable.step_green_circle);
                image1.setImageResource(R.drawable.step_green_true);
                view1.setBackgroundColor(ContextCompat.getColor(activity, R.color.done));
                tv1.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));

                image5.setBackgroundResource(R.drawable.step_green_circle);
                image5.setImageResource(R.drawable.step_green_heart);
                tv5.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimary));


        }
    }

}
