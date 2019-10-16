package com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments.Fragment_Shipment_Charger_Information;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments.Fragment_Shipment_Container_Type;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments.Fragment_Shipment_Delivery_Information;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments.Fragment_Shipment_Load_Description;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments.Fragment_Shipment_Payment;
import com.appzone.shelcom.adapters.Shipment_Pager_Adapter;
import com.appzone.shelcom.language.Language_Helper;
import com.appzone.shelcom.models.OrderIdModel;
import com.appzone.shelcom.models.ShipmentUploadModel;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.tags.Tags;
import com.xw.repo.BubbleSeekBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentActivity extends AppCompatActivity {
    private LinearLayout ll_back,ll_next,ll_previous;
    private ImageView arrow,arrow_next,arrow_previous;
    private ViewPager pager;
    private BubbleSeekBar seekBar;
    private Shipment_Pager_Adapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private String current_language;
    private List<Fragment> fragments;
    private ShipmentUploadModel  shipmentUploadModel;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment);

        initView();
    }

    private void initView() {
        shipmentUploadModel = new ShipmentUploadModel();
        fragments = new ArrayList<>();
        Paper.init(this);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

        arrow = findViewById(R.id.arrow);
        arrow_next = findViewById(R.id.arrow_next);
        arrow_previous = findViewById(R.id.arrow_previous);
        seekBar = findViewById(R.id.seekBar);


        if (current_language.equals("ar"))
        {
            arrow.setRotation(180.0f);
            arrow_next.setRotation(180.0f);
            arrow_previous.setRotation(180.0f);

        }else
            {
                seekBar.setRotation(180.0f);
            }


        ll_back = findViewById(R.id.ll_back);
        ll_next = findViewById(R.id.ll_next);
        ll_previous = findViewById(R.id.ll_previous);

        pager = findViewById(R.id.pager);

        fragments.add(Fragment_Shipment_Container_Type.newInstance());
        fragments.add(Fragment_Shipment_Charger_Information.newInstance());
        fragments.add(Fragment_Shipment_Delivery_Information.newInstance());
        fragments.add(Fragment_Shipment_Load_Description.newInstance());
        fragments.add(Fragment_Shipment_Payment.newInstance());

        adapter = new Shipment_Pager_Adapter(getSupportFragmentManager());
        adapter.setFragments(fragments);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
        pager.beginFakeDrag();
        ll_previous.setVisibility(View.GONE);


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                int progress = position*25;
                seekBar.setProgress(progress);

                if (position==0)
                {
                    ll_previous.setVisibility(View.GONE);
                    ll_next.setVisibility(View.VISIBLE);
                }else if (position == pager.getAdapter().getCount()-1)
                {
                    ll_previous.setVisibility(View.VISIBLE);
                    ll_next.setVisibility(View.GONE);
                }else
                    {
                        ll_previous.setVisibility(View.VISIBLE);
                        ll_next.setVisibility(View.VISIBLE);
                    }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ll_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = pager.getCurrentItem()*25;
                seekBar.setProgress(progress);

                switch (pager.getCurrentItem())
                {
                    case 0:
                        Fragment_Shipment_Container_Type fragment_shipment_container_type = (Fragment_Shipment_Container_Type) adapter.getItem(0);
                        if (fragment_shipment_container_type.isDataOk())
                        {
                            pager.setCurrentItem(pager.getCurrentItem()+1);

                        }
                        break;

                    case 1:
                        Fragment_Shipment_Charger_Information fragment_shipment_charger_information = (Fragment_Shipment_Charger_Information) adapter.getItem(1);
                        if (fragment_shipment_charger_information.isDataOk())
                        {
                            pager.setCurrentItem(pager.getCurrentItem()+1);

                        }
                        break;

                    case 2:
                        Fragment_Shipment_Delivery_Information fragment_shipment_delivery_information = (Fragment_Shipment_Delivery_Information) adapter.getItem(2);
                        if (fragment_shipment_delivery_information.isDataOk())
                        {
                            pager.setCurrentItem(pager.getCurrentItem()+1);

                        }
                        break;

                    case 3:
                        Fragment_Shipment_Load_Description fragment_shipment_load_description = (Fragment_Shipment_Load_Description) adapter.getItem(3);
                        if (fragment_shipment_load_description.isDataOk())
                        {
                            pager.setCurrentItem(pager.getCurrentItem()+1);

                        }
                        break;

                }


            }
        });

        ll_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int progress = pager.getCurrentItem()*25;
                seekBar.setProgress(progress);
                pager.setCurrentItem(pager.getCurrentItem()-1);
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void saveContainerData(int truck_id, String shipment_type, int truck_amount, String truck_size_id)
    {
        shipmentUploadModel.setTruck_id(truck_id);
        shipmentUploadModel.setShipment_type(shipment_type);
        shipmentUploadModel.setTruck_amount(truck_amount);
        shipmentUploadModel.setTruck_size(truck_size_id);



    }

    public void SaveFromShipmentData(String code, String m_phone, String m_responsible_name, String address,String city_id, double lat, double lng, long date) {
        shipmentUploadModel.setFrom_company_phone_code(code.replace("+","00"));
        shipmentUploadModel.setFrom_company_phone(m_phone);
       // shipmentUploadModel.setFrom_company_name(m_company_name);
       // shipmentUploadModel.setFrom_company_email(email);
        shipmentUploadModel.setFrom_responsible_name(m_responsible_name);
       // shipmentUploadModel.setShipment_number(m_shipment_number);
        shipmentUploadModel.setFrom_address(address);
        shipmentUploadModel.setFrom_city_id(city_id);
        shipmentUploadModel.setFrom_lat(lat);
        shipmentUploadModel.setFrom_lng(lng);
        shipmentUploadModel.setFrom_date(date);

    }

    public void SaveFromShipmentDeliveryData(String code, String m_phone, String m_responsible_name, String address, String city_id, double lat, double lng, long date) {

        shipmentUploadModel.setTo_company_phone_code(code.replace("+","00"));
        shipmentUploadModel.setTo_company_phone(m_phone);
        //shipmentUploadModel.setTo_company_name(m_company_name);
        //shipmentUploadModel.setTo_company_email(email);
        shipmentUploadModel.setTo_responsible_name(m_responsible_name);
        shipmentUploadModel.setTo_address(address);
        shipmentUploadModel.setTo_city_id(city_id);
        shipmentUploadModel.setTo_lat(lat);
        shipmentUploadModel.setTo_lng(lng);
        shipmentUploadModel.setTo_date(date);
    }

    public void saveLoadDescriptionData(String m_description, Uri imgUri1, Uri imgUri2) {

        shipmentUploadModel.setLoad_description(m_description);
        //shipmentUploadModel.setLoad_value(m_value);
        //shipmentUploadModel.setLoad_weight(m_weight);
        shipmentUploadModel.setUri_1(String.valueOf(imgUri1));
        shipmentUploadModel.setUri_2(String.valueOf(imgUri2));

    }


    public void uploadOrder(int payment_method) {

        if (userModel!=null)
        {
            final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
            dialog.show();
            String user_id;
            user_id = String.valueOf(userModel.getUser().getId());

            /*if (userModel.getUser().getCompany_information()==null)
            {
                user_id = String.valueOf(userModel.getUser().getId());
            }else
            {
                user_id = String.valueOf(userModel.getUser().getCompany_information().getId());

            }*/

            RequestBody order_type_part = Common.getRequestBodyText("3");

            RequestBody user_id_part = Common.getRequestBodyText(user_id);
            RequestBody truck_id_part = Common.getRequestBodyText(String.valueOf(shipmentUploadModel.getTruck_id()));
            RequestBody truck_size_id_part = Common.getRequestBodyText(shipmentUploadModel.getTruck_size());
            RequestBody truck_amount_part = Common.getRequestBodyText(String.valueOf(shipmentUploadModel.getTruck_amount()));

            RequestBody description_part = Common.getRequestBodyText(shipmentUploadModel.getLoad_description());
            RequestBody shipment_type_part = Common.getRequestBodyText(shipmentUploadModel.getShipment_type());


            RequestBody phone_code_from_part = Common.getRequestBodyText(shipmentUploadModel.getFrom_company_phone_code());
            RequestBody phone_from_part = Common.getRequestBodyText(shipmentUploadModel.getFrom_company_phone());
           // RequestBody company_name_from_part = Common.getRequestBodyText(shipmentUploadModel.getFrom_company_name());
            //RequestBody company_email_from_part = Common.getRequestBodyText(shipmentUploadModel.getFrom_company_email());
            RequestBody responsible_name_from_part = Common.getRequestBodyText(shipmentUploadModel.getFrom_responsible_name());
            RequestBody city_id_from_part = Common.getRequestBodyText(shipmentUploadModel.getFrom_city_id());
            RequestBody address_from_part = Common.getRequestBodyText(shipmentUploadModel.getFrom_address());
            RequestBody lat_from_part = Common.getRequestBodyText(String.valueOf(shipmentUploadModel.getFrom_lat()));
            RequestBody lng_from_part = Common.getRequestBodyText(String.valueOf(shipmentUploadModel.getFrom_lng()));
            RequestBody date_from_part = Common.getRequestBodyText(String.valueOf((shipmentUploadModel.getFrom_date()/1000)));
         //   RequestBody shipment_number_part = Common.getRequestBodyText(shipmentUploadModel.getShipment_number());


            RequestBody phone_code_to_part = Common.getRequestBodyText(shipmentUploadModel.getTo_company_phone_code());
            RequestBody phone_to_part = Common.getRequestBodyText(shipmentUploadModel.getTo_company_phone());
           // RequestBody company_name_to_part = Common.getRequestBodyText(shipmentUploadModel.getTo_company_name());
            //RequestBody company_email_to_part = Common.getRequestBodyText(shipmentUploadModel.getTo_company_email());
            RequestBody responsible_name_to_part = Common.getRequestBodyText(shipmentUploadModel.getTo_responsible_name());
            RequestBody city_id_to_part = Common.getRequestBodyText(shipmentUploadModel.getTo_city_id());
            RequestBody address_to_part = Common.getRequestBodyText(shipmentUploadModel.getTo_address());
            RequestBody lat_to_part = Common.getRequestBodyText(String.valueOf(shipmentUploadModel.getTo_lat()));
            RequestBody lng_to_part = Common.getRequestBodyText(String.valueOf(shipmentUploadModel.getTo_lng()));
            RequestBody date_to_part = Common.getRequestBodyText(String.valueOf((shipmentUploadModel.getTo_date()/1000)));

           // RequestBody value_to_part = Common.getRequestBodyText(shipmentUploadModel.getLoad_value());
           // RequestBody weight_to_part = Common.getRequestBodyText(shipmentUploadModel.getLoad_weight());
            RequestBody payment_to_part = Common.getRequestBodyText(String.valueOf(payment_method));



            MultipartBody.Part image1_part = Common.getMultiPart(this,Uri.parse(shipmentUploadModel.getUri_1()),"image1");
            MultipartBody.Part image2_part = Common.getMultiPart(this,Uri.parse(shipmentUploadModel.getUri_2()),"image2");



            Api.getService(Tags.base_url)
                    .sendShippingOrder(order_type_part,user_id_part,description_part,truck_id_part,shipment_type_part,truck_amount_part,truck_size_id_part,phone_code_from_part,phone_from_part,responsible_name_from_part,city_id_from_part,address_from_part,lat_from_part,lng_from_part,date_from_part,phone_code_to_part,phone_to_part,responsible_name_to_part,city_id_to_part,address_to_part,lat_to_part,lng_to_part,payment_to_part,date_to_part,image1_part,image2_part)
                    .enqueue(new Callback<OrderIdModel>() {
                        @Override
                        public void onResponse(Call<OrderIdModel> call, Response<OrderIdModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null&&response.body().getOrder_details()!=null)
                            {
                                CreateAlertDialog(response.body().getOrder_details().getId()+"");
                            }else
                            {
                                Toast.makeText(ShipmentActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                                try {
                                    Log.e("Error_code", response.code() + "" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<OrderIdModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                Toast.makeText(ShipmentActivity.this, R.string.something, Toast.LENGTH_SHORT).show();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {

                            }
                        }
                    });
        }else
            {
                Common.CreateSignAlertDialog(this,getString(R.string.si_su));
            }



    }

    private void CreateAlertDialog(String order_id)
    {


        final AlertDialog dialog = new android.app.AlertDialog.Builder(this)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(this).inflate(R.layout.dialog_order_id,null);
        Button doneBtn = view.findViewById(R.id.doneBtn);

        TextView tv_msg = view.findViewById(R.id.tv_msg);
        tv_msg.setText(getString(R.string.order_sent_successfully_order_number_is)+" "+order_id);


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations= R.style.dialog_congratulation_animation;
        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(view);
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment :fragments)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment :fragments)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }



}
