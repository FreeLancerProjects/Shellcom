package com.appzone.shelcom.activities_fragments.activities.home_activity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Client_Offer_Containers;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Client_Offer_Customs;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Client_Offer_Engineering;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Client_Offer_Rental_Equipment;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Client_Offer_Shipping;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Client_Offer_Water_Delivery;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Company_Add_Offer_Containers;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Company_Add_Offer_Customs;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Company_Add_Offer_Engineering;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Company_Add_Offer_Rental_Equipment;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Company_Add_Offer_Shipping;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Company_Add_Offer_Water_Delivery;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Contact_Us;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Container;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Equipment;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Home;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Map;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Map_Location_Details;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_More;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Notifications;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Order_Details_Containers;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Order_Details_Customs;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Order_Details_Engineering;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Order_Details_Rental_Equipment;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Order_Details_Shipping;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Order_Details_Water_Delivery;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Profile;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Rental_Of_Equipment;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Terms_Condition;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Upgrade_To_Company;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Water_Delivery;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_main;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_orders.Fragment_Orders;
import com.appzone.shelcom.activities_fragments.activities.sign_in_sign_up_activity.activity.Login_Activity;
import com.appzone.shelcom.language.Language_Helper;
import com.appzone.shelcom.models.SelectedLocation;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.share.Common;
import com.appzone.shelcom.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.util.List;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Activity extends AppCompatActivity {
    private int fragment_count = 0;
    private FragmentManager fragmentManager;
    private Fragment_Home fragment_home;
    private Fragment_main fragment_main;
    private Fragment_Notifications fragment_notifications;
    private Fragment_Profile fragment_profile;
    private Fragment_More fragment_more;
    private Fragment_Upgrade_To_Company fragment_upgrade_to_company;
    private Fragment_Contact_Us fragment_contact_us;
    private Fragment_Map fragment_map;
    private Fragment_Terms_Condition fragment_terms_condition;
    private Fragment_Water_Delivery fragment_water_delivery;
    private Fragment_Orders fragment_orders;
    private Fragment_Equipment fragment_equipment;
    private Fragment_Rental_Of_Equipment fragment_rental_of_equipment;
    private Fragment_Company_Add_Offer_Water_Delivery fragment_company_add_offer_water_delivery;
    private Fragment_Map_Location_Details fragment_map_location_details;
    private Fragment_Company_Add_Offer_Shipping fragment_company_add_offer_shipping;
    private Fragment_Company_Add_Offer_Rental_Equipment fragment_company_add_offer_rental_equipment;
    private Fragment_Company_Add_Offer_Containers fragment_company_add_offer_containers;
    private Fragment_Company_Add_Offer_Customs fragment_company_add_offer_customs;
    private Fragment_Company_Add_Offer_Engineering fragment_company_add_offer_engineering;
    private Fragment_Client_Offer_Containers fragment_client_offer_containers;
    private Fragment_Client_Offer_Customs  fragment_client_offer_customs;
    private Fragment_Client_Offer_Engineering fragment_client_offer_engineering;
    private Fragment_Client_Offer_Rental_Equipment fragment_client_offer_rental_equipment;
    private Fragment_Client_Offer_Shipping fragment_client_offer_shipping;
    private Fragment_Client_Offer_Water_Delivery fragment_client_offer_water_delivery;
    private Fragment_Order_Details_Containers fragment_order_details_containers;
    private Fragment_Order_Details_Customs fragment_order_details_customs;
    private Fragment_Order_Details_Engineering fragment_order_details_engineering;
    private Fragment_Order_Details_Rental_Equipment fragment_order_details_rental_equipment;
    private Fragment_Order_Details_Shipping fragment_order_details_shipping;
    private Fragment_Order_Details_Water_Delivery fragment_order_details_water_delivery;

    private UserModel userModel;
    private Preferences preferences;
    ////////////////////////////sheet//////////
    private View root;
    private Button btn_water_delivery,btn_shipment,btn_rental;
    private Fragment_Container fragment_container;
    //private BottomSheetBehavior behavior;





    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        fragmentManager = this.getSupportFragmentManager();

        initView();
        if (savedInstanceState == null) {
            DisplayFragmentHome();
            DisplayFragmentMain();
        }

    }

    private void initView() {
        /*root = findViewById(R.id.root);

        behavior = BottomSheetBehavior.from(root);

        btn_water_delivery = findViewById(R.id.btn_water_delivery);
        btn_shipment = findViewById(R.id.btn_shipment);
        btn_rental = findViewById(R.id.btn_rental);

        btn_water_delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                DisplayFragmentWaterDeliveryOrder();
            }
        });

        btn_shipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                DisplayFragmentShipmentOrder();
            }
        });

        btn_rental.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                DisplayFragmentRentalOrder();
            }
        });
*/
        updateToken();

    }

    private void updateToken() {
        if (userModel!=null)
        {
            final int user_id;

            if (userModel.getUser().getCompany_information()==null)
            {
                user_id = userModel.getUser().getId();
            }else
                {
                    user_id = userModel.getUser().getCompany_information().getId();

                }

            FirebaseInstanceId.getInstance()
                    .getInstanceId()
                    .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                        @Override
                        public void onComplete(@NonNull Task<InstanceIdResult> task) {

                            if (task.isSuccessful())
                            {
                                String token = task.getResult().getToken();
                                Api.getService(Tags.base_url)
                                        .updateToken(user_id,token)
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                if (response.isSuccessful())
                                                {
                                                    Log.e("token","updated successfully");
                                                }else
                                                {


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
                                                    Log.e("Error",t.getMessage());
                                                }catch (Exception e){}
                                            }
                                        });
                            }
                        }
                    });


        }
    }
    public void updateUserData(UserModel userModel)
    {
        this.userModel = userModel;
        preferences.create_update_userdata(this,userModel);
        if (fragment_profile!=null&&fragment_profile.isAdded())
        {
            fragment_profile.UpdateUserData(userModel);

        }
    }

    public void DisplayFragmentHome()
    {


        fragment_count += 1;

        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();
        }

    }
    public void DisplayFragmentMain()
    {


        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }

        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_main == null) {
            fragment_main = Fragment_main.newInstance();
        }

        if (fragment_main.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_main).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(0);
        }


    }
    public void DisplayFragmentOrders()
    {

        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }

        if (fragment_orders == null) {
            fragment_orders = Fragment_Orders.newInstance();
        }

        if (fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_orders).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_orders, "fragment_orders").addToBackStack("fragment_orders").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(1);
        }






    }

    public void DisplayFragmentNotification()
    {



        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }

        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_notifications == null) {
            fragment_notifications = Fragment_Notifications.newInstance();
        }

        if (fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_notifications).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_notifications, "fragment_notifications").addToBackStack("fragment_notifications").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(2);
        }

    }
    public void DisplayFragmentProfile()
    {

        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }


        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }


        if (fragment_profile == null) {
            fragment_profile = Fragment_Profile.newInstance();
        }

        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(3);
        }

    }

    public void DisplayFragmentMore()
    {


        if (fragment_notifications != null && fragment_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_notifications).commit();
        }
        if (fragment_profile != null && fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_profile).commit();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }

        if (fragment_orders != null && fragment_orders.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_orders).commit();
        }
        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }

        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_main_child, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.UpdateAHBottomNavigationPosition(4);
        }

    }

    public void DisplayFragmentUpgradeToCompany()
    {

        fragment_count +=1;

        fragment_upgrade_to_company = Fragment_Upgrade_To_Company.newInstance();

        if (fragment_upgrade_to_company.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_upgrade_to_company).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_upgrade_to_company, "fragment_upgrade_to_company").addToBackStack("fragment_upgrade_to_company").commit();

        }


    }

    public void DisplayFragmentWaterDeliveryReserve()
    {
        if (userModel!=null)
        {
            fragment_count +=1;

            fragment_water_delivery = Fragment_Water_Delivery.newInstance();

            if (fragment_water_delivery.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_water_delivery).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_water_delivery, "fragment_water_delivery").addToBackStack("fragment_water_delivery").commit();

            }

        }else
            {
                Common.CreateUserNotSignInAlertDialog(this);
            }



    }

    public void DisplayFragmentContactUS()
    {

        fragment_count +=1;

        fragment_contact_us = Fragment_Contact_Us.newInstance();

        if (fragment_contact_us.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_contact_us).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_contact_us, "fragment_contact_us").addToBackStack("fragment_contact_us").commit();

        }


    }

    public void DisplayFragmentTerms_AboutUs(int type)
    {

        fragment_count +=1;

        fragment_terms_condition = Fragment_Terms_Condition.newInstance(type);

        if (fragment_terms_condition.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_terms_condition).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_terms_condition, "fragment_terms_condition").addToBackStack("fragment_terms_condition").commit();

        }


    }

    public void DisplayFragmentMap(String from)
    {

        fragment_count +=1;

        fragment_map = Fragment_Map.newInstance(from);

        if (fragment_map.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_map).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_map, "fragment_map").addToBackStack("fragment_map").commit();

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
    public void DisplayFragmentEquipments() {

        if (userModel!=null)
        {
            fragment_count += 1;

            fragment_equipment = Fragment_Equipment.newInstance();


            if (fragment_equipment.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_equipment).commit();
            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_equipment, "fragment_equipment").addToBackStack("fragment_equipment").commit();

            }
        }else
        {
            Common.CreateUserNotSignInAlertDialog(this);
        }


    }

    public void DisplayFragmentEquipment(int id) {
        fragment_count += 1;

        fragment_rental_of_equipment = Fragment_Rental_Of_Equipment.newInstance(id);


        if (fragment_rental_of_equipment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_rental_of_equipment).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_rental_of_equipment, "fragment_rental_of_equipment").addToBackStack("fragment_rental_of_equipment").commit();

        }
    }

    public void DisplayFragmentCompanyAddWaterOffer(int order_id, int notification_id)
    {
        fragment_count += 1;

        fragment_company_add_offer_water_delivery = Fragment_Company_Add_Offer_Water_Delivery.newInstance(order_id,notification_id);


        if (fragment_company_add_offer_water_delivery.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_company_add_offer_water_delivery).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_company_add_offer_water_delivery, "fragment_company_add_offer_water_delivery").addToBackStack("fragment_company_add_offer_water_delivery").commit();

        }
    }
    public void DisplayFragmentCompanyAddShipmentOffer(int order_id, int notification_id)
    {
        fragment_count += 1;

        fragment_company_add_offer_shipping = Fragment_Company_Add_Offer_Shipping.newInstance(order_id,notification_id);


        if (fragment_company_add_offer_shipping.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_company_add_offer_shipping).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_company_add_offer_shipping, "fragment_company_add_offer_shipping").addToBackStack("fragment_company_add_offer_shipping").commit();

        }
    }

    public void DisplayFragmentCompanyAddRentalEquipmentOffer(int order_id, int notification_id)
    {
        fragment_count += 1;

        fragment_company_add_offer_rental_equipment = Fragment_Company_Add_Offer_Rental_Equipment.newInstance(order_id,notification_id);


        if (fragment_company_add_offer_rental_equipment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_company_add_offer_rental_equipment).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_company_add_offer_rental_equipment, "fragment_company_add_offer_rental_equipment").addToBackStack("fragment_company_add_offer_rental_equipment").commit();

        }
    }

    public void DisplayFragmentCompanyAddContainersOffer(int order_id, int notification_id)
    {
        fragment_count += 1;

        fragment_company_add_offer_containers = Fragment_Company_Add_Offer_Containers.newInstance(order_id,notification_id);


        if (fragment_company_add_offer_containers.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_company_add_offer_containers).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_company_add_offer_containers, "fragment_company_add_offer_containers").addToBackStack("fragment_company_add_offer_containers").commit();

        }
    }

    public void DisplayFragmentCompanyAddCustomsOffer(int order_id, int notification_id)
    {
        fragment_count += 1;

        fragment_company_add_offer_customs = Fragment_Company_Add_Offer_Customs.newInstance(order_id,notification_id);


        if (fragment_company_add_offer_customs.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_company_add_offer_customs).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_company_add_offer_customs, "fragment_company_add_offer_customs").addToBackStack("fragment_company_add_offer_customs").commit();

        }
    }

    public void DisplayFragmentCompanyAddEngineeringOffer(int order_id, int notification_id)
    {
        fragment_count += 1;

        fragment_company_add_offer_engineering = Fragment_Company_Add_Offer_Engineering.newInstance(order_id,notification_id);


        if (fragment_company_add_offer_engineering.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_company_add_offer_engineering).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_company_add_offer_engineering, "fragment_company_add_offer_engineering").addToBackStack("fragment_company_add_offer_engineering").commit();

        }
    }


    public void DisplayFragmentClientWaterOffer(int notification_id, int order_id, String offer_id, String price)
    {
        fragment_count += 1;

        fragment_client_offer_water_delivery = Fragment_Client_Offer_Water_Delivery.newInstance(notification_id,order_id,offer_id,price);


        if (fragment_client_offer_water_delivery.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_client_offer_water_delivery).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_client_offer_water_delivery, "fragment_client_offer_water_delivery").addToBackStack("fragment_client_offer_water_delivery").commit();

        }
    }

    public void DisplayFragmentClientShipmentOffer(int notification_id, int order_id, String offer_id, String price)
    {
        fragment_count += 1;

        fragment_client_offer_shipping = Fragment_Client_Offer_Shipping.newInstance(notification_id,order_id,offer_id,price);


        if (fragment_client_offer_shipping.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_client_offer_shipping).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_client_offer_shipping, "fragment_client_offer_shipping").addToBackStack("fragment_client_offer_shipping").commit();

        }
    }

    public void DisplayFragmentClientRentalEquipmentOffer(int notification_id, int order_id, String offer_id, String price)
    {
        fragment_count += 1;

        fragment_client_offer_rental_equipment = Fragment_Client_Offer_Rental_Equipment.newInstance(notification_id,order_id,offer_id,price);


        if (fragment_client_offer_rental_equipment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_client_offer_rental_equipment).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_client_offer_rental_equipment, "fragment_client_offer_rental_equipment").addToBackStack("fragment_client_offer_rental_equipment").commit();

        }
    }

    public void DisplayFragmentClientContainersOffer(int notification_id, int order_id, String offer_id, String price)
    {
        fragment_count += 1;

        fragment_client_offer_containers = Fragment_Client_Offer_Containers.newInstance(notification_id,order_id,offer_id,price);


        if (fragment_client_offer_containers.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_client_offer_containers).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_client_offer_containers, "fragment_client_offer_containers").addToBackStack("fragment_client_offer_containers").commit();

        }
    }

    public void DisplayFragmentClientCustomsOffer(int notification_id, int order_id, String offer_id, String price)
    {
        fragment_count += 1;

        fragment_client_offer_customs = Fragment_Client_Offer_Customs.newInstance(notification_id,order_id,offer_id,price);


        if (fragment_client_offer_customs.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_client_offer_customs).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_client_offer_customs, "fragment_client_offer_customs").addToBackStack("fragment_client_offer_customs").commit();

        }
    }

    public void DisplayFragmentClientEngineeringOffer(int notification_id, int order_id, String offer_id, String price)
    {
        fragment_count += 1;

        fragment_client_offer_engineering = Fragment_Client_Offer_Engineering.newInstance(notification_id,order_id,offer_id,price);


        if (fragment_client_offer_engineering.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_client_offer_engineering).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_client_offer_engineering, "fragment_client_offer_engineering").addToBackStack("fragment_client_offer_engineering").commit();

        }
    }
    public void DisplayFragmentMapLocation_Details(double lat , double lng, String address)
    {
        fragment_count += 1;

        fragment_map_location_details = Fragment_Map_Location_Details.newInstance(lat,lng,address);


        if (fragment_map_location_details.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_map_location_details).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_map_location_details, "fragment_map_location_details").addToBackStack("fragment_map_location_details").commit();

        }
    }
    public void setLocationDataFromMapFragment(String from, final SelectedLocation selectedLocation)
    {
        if (from.equals("fragment_upgrade_to_company"))
        {
            if (fragment_upgrade_to_company!=null&&fragment_upgrade_to_company.isAdded())
            {
                new Handler()
                        .postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fragment_upgrade_to_company.setLocation(selectedLocation);
                            }
                        },1);
            }
        }

        fragment_count -=1;
        super.onBackPressed();
    }


    public void DisplayFragmentOrderDetailsContainers(int order_id, String price)
    {
        fragment_count += 1;

        fragment_order_details_containers = Fragment_Order_Details_Containers.newInstance(order_id,price);


        if (fragment_order_details_containers.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_order_details_containers).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_order_details_containers, "fragment_order_details_containers").addToBackStack("fragment_order_details_containers").commit();

        }
    }
    public void DisplayFragmentOrderDetailsCustoms(int order_id, String price)
    {
        fragment_count += 1;

        fragment_order_details_customs = Fragment_Order_Details_Customs.newInstance(order_id,price);


        if (fragment_order_details_customs.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_order_details_customs).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_order_details_customs, "fragment_order_details_customs").addToBackStack("fragment_order_details_customs").commit();

        }
    }
    public void DisplayFragmentOrderDetailsEngineering(int order_id, String price)
    {
        fragment_count += 1;

        fragment_order_details_engineering = Fragment_Order_Details_Engineering.newInstance(order_id,price);


        if (fragment_order_details_engineering.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_order_details_engineering).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_order_details_engineering, "fragment_order_details_engineering").addToBackStack("fragment_order_details_engineering").commit();

        }
    }
    public void DisplayFragmentOrderDetailsRntalEquipment(int order_id, String price)
    {
        fragment_count += 1;

        fragment_order_details_rental_equipment = Fragment_Order_Details_Rental_Equipment.newInstance(order_id,price);


        if (fragment_order_details_rental_equipment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_order_details_rental_equipment).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_order_details_rental_equipment, "fragment_order_details_rental_equipment").addToBackStack("fragment_order_details_rental_equipment").commit();

        }
    }
    public void DisplayFragmentOrderDetailsShipping(int order_id, String price)
    {
        fragment_count += 1;

        fragment_order_details_shipping = Fragment_Order_Details_Shipping.newInstance(order_id,price);


        if (fragment_order_details_shipping.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_order_details_shipping).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_order_details_shipping, "fragment_order_details_shipping").addToBackStack("fragment_order_details_shipping").commit();

        }
    }
    public void DisplayFragmentOrderDetailsWaterDelivery(int order_id, String price)
    {
        fragment_count += 1;

        fragment_order_details_water_delivery = Fragment_Order_Details_Water_Delivery.newInstance(order_id,price);


        if (fragment_order_details_water_delivery.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_order_details_water_delivery).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_order_details_water_delivery, "fragment_order_details_water_delivery").addToBackStack("fragment_order_details_water_delivery").commit();

        }
    }

    public void updateNotificationData()
    {
        if (fragment_notifications!=null&&fragment_notifications.isAdded())
        {
            Back();
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_notifications.removeItem();
                            refreshFragmentOrder();
                        }
                    },1000);
        }
    }

    public void refreshFragmentOrder()
    {
        if (fragment_orders!=null&&fragment_orders.isAdded())
        {
            new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_orders.refreshFragment();
                        }
                    },100);
        }
    }



    public void NavigateToSignInActivity(boolean isSignIn) {

        Intent intent = new Intent(this, Login_Activity.class);
        intent.putExtra("sign_up",isSignIn);
        startActivity(intent);
        finish();

    }

    public void RefreshActivity(String lang)
    {
        Paper.book().write("lang",lang);
        Language_Helper.setNewLocale(this,lang);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent =  getIntent();
                        finish();
                        startActivity(intent);
                    }
                },1050);



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments)
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments)
        {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back() {
        if (fragment_count > 1) {
            super.onBackPressed();
            fragment_count -= 1;
        } else {
            if (fragment_main!=null&&fragment_main.isAdded()&&!fragment_main.isVisible())
            {
                DisplayFragmentMain();
            }else
            {
                if (userModel!=null)
                {
                    finish();
                }else
                {
                    NavigateToSignInActivity(true);
                }
            }
        }

    }


    public void Logout() {
        this.userModel = null;
        preferences.create_update_userdata(this,null);
        preferences.create_update_session(this, Tags.session_logout);
        NavigateToSignInActivity(true);
    }


}