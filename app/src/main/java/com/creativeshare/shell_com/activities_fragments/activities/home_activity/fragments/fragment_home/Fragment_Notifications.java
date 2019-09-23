package com.creativeshare.shell_com.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.creativeshare.shell_com.adapters.NotificationsAdapter;
import com.creativeshare.shell_com.models.NotificationDataModel;
import com.creativeshare.shell_com.models.UserModel;
import com.creativeshare.shell_com.preferences.Preferences;
import com.creativeshare.shell_com.remote.Api;
import com.creativeshare.shell_com.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Notifications extends Fragment {

    private ProgressBar progBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private Home_Activity activity;
    private LinearLayout ll_not;
    private UserModel userModel;
    private Preferences preferences;
    private NotificationsAdapter adapter;
    private List<NotificationDataModel.NotificationModel> notificationModelList;
    private boolean isLoading = false;
    private int current_page = 1;
    private boolean isFirstTime = true;
    private String current_language;
    private int selected_pos = -1;


    @Override
    public void onStart() {
        super.onStart();
        if (!isFirstTime && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        initView(view);
        return view;
    }


    public static Fragment_Notifications newInstance() {
        return new Fragment_Notifications();
    }

    private void initView(View view) {

        notificationModelList = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang",Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        ll_not = view.findViewById(R.id.ll_not);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);


        recView.setDrawingCacheEnabled(true);
        recView.setItemViewCacheSize(25);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);

        adapter = new NotificationsAdapter(notificationModelList,activity,this);
        recView.setAdapter(adapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int lastVisibleItem = ((LinearLayoutManager) manager).findLastCompletelyVisibleItemPosition();
                    int totalItems = manager.getItemCount();

                    if (lastVisibleItem >= (totalItems - 5) && !isLoading) {
                        isLoading = true;
                        notificationModelList.add(null);
                        adapter.notifyItemInserted(notificationModelList.size() - 1);
                        int next_page = current_page + 1;
                        loadMore(next_page);
                    }
                }
            }
        });
        getNotification();

    }

    private void getNotification() {

        if (userModel == null) {
            preferences = Preferences.getInstance();
            userModel = preferences.getUserData(activity);
        }

        String user_type;
        String company_id;
        String user_id;


        if (userModel.getUser().getCompany_information()==null)
        {
            user_type = Tags.TYPE_USER;
            user_id = String.valueOf(userModel.getUser().getId());
            company_id ="0";

        }else
            {
                user_type = Tags.TYPE_COMPANY;
                user_id = String.valueOf(userModel.getUser().getId());
                company_id =String.valueOf(userModel.getUser().getCompany_information().getId());
            }

        Log.e("us",user_type+"_"+user_id+"_"+company_id);

        Api.getService(Tags.base_url)
                .getNotifications(user_type,user_id,company_id,1)
                .enqueue(new Callback<NotificationDataModel>() {
            @Override
            public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    notificationModelList.clear();

                    if (response.body() != null && response.body().getData().size() > 0) {
                        ll_not.setVisibility(View.GONE);
                        notificationModelList.addAll(response.body().getData());
                        adapter.notifyDataSetChanged();
                        isFirstTime = false;
                    } else {
                        ll_not.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();


                    }
                } else {

                    Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                try {
                    progBar.setVisibility(View.GONE);
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });

    }
    private void loadMore(int page_index)
    {
        if (userModel == null) {
            preferences = Preferences.getInstance();
            userModel = preferences.getUserData(activity);
        }

        String user_type;
        String company_id;
        String user_id;
        if (userModel.getUser().getCompany_information()==null)
        {
            user_type = Tags.TYPE_USER;
            user_id = String.valueOf(userModel.getUser().getId());
            company_id ="0";
        }else
        {
            user_type = Tags.TYPE_COMPANY;
            user_id = String.valueOf(userModel.getUser().getId());
            company_id =String.valueOf(userModel.getUser().getCompany_information().getId());
        }

        Api.getService(Tags.base_url)
                .getNotifications(user_type,user_id,company_id,page_index)
                .enqueue(new Callback<NotificationDataModel>() {
                    @Override
                    public void onResponse(Call<NotificationDataModel> call, Response<NotificationDataModel> response) {
                        progBar.setVisibility(View.GONE);
                        isLoading = false;
                        notificationModelList.remove(notificationModelList.size()-1);
                        adapter.notifyItemRemoved(notificationModelList.size()-1);

                        if (response.isSuccessful()) {

                            if (response.body() != null && response.body().getData().size() > 0) {
                                notificationModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();
                                current_page = response.body().getMeta().getCurrent_page();
                            }
                        } else {

                            Toast.makeText(activity, R.string.failed, Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<NotificationDataModel> call, Throwable t) {
                        try {
                            notificationModelList.remove(notificationModelList.size()-1);
                            adapter.notifyItemRemoved(notificationModelList.size()-1);
                            isLoading = false;
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    public void setItemData(NotificationDataModel.NotificationModel notificationModel, int adapterPosition)
    {
        this.selected_pos =adapterPosition;
        if (notificationModel.getAction_type().equals("1")&&notificationModel.getOrder_type().equals(Tags.WATER_ORDER))
        {
            activity.DisplayFragmentCompanyAddWaterOffer(Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getId());
        }else if (notificationModel.getAction_type().equals("1")&&notificationModel.getOrder_type().equals(Tags.SHIPPING_ORDER))
        {
            activity.DisplayFragmentCompanyAddShipmentOffer(Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getId());
        }
        else if (notificationModel.getAction_type().equals("1")&&notificationModel.getOrder_type().equals(Tags.RENTAL_ORDER))
        {
            activity.DisplayFragmentCompanyAddRentalEquipmentOffer(Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getId());
        }
        else if (notificationModel.getAction_type().equals("1")&&notificationModel.getOrder_type().equals(Tags.CONTAINERS_ORDER))
        {
            activity.DisplayFragmentCompanyAddContainersOffer(Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getId());
        }
        else if (notificationModel.getAction_type().equals("1")&&notificationModel.getOrder_type().equals(Tags.CLEARANCE_ORDER))
        {
            activity.DisplayFragmentCompanyAddCustomsOffer(Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getId());
        }
        else if (notificationModel.getAction_type().equals("1")&&notificationModel.getOrder_type().equals(Tags.ENGINEERING_ORDER))
        {
            activity.DisplayFragmentCompanyAddEngineeringOffer(Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getId());
        }else if (notificationModel.getAction_type().equals("2")&&notificationModel.getOrder_type().equals(Tags.WATER_ORDER))
        {
            activity.DisplayFragmentClientWaterOffer(notificationModel.getId(),Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getOffer_id(),notificationModel.getOffer_price());
        }else if (notificationModel.getAction_type().equals("2")&&notificationModel.getOrder_type().equals(Tags.SHIPPING_ORDER))
        {
            activity.DisplayFragmentClientShipmentOffer(notificationModel.getId(),Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getOffer_id(),notificationModel.getOffer_price());
        }
        else if (notificationModel.getAction_type().equals("2")&&notificationModel.getOrder_type().equals(Tags.RENTAL_ORDER))
        {
            activity.DisplayFragmentClientRentalEquipmentOffer(notificationModel.getId(),Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getOffer_id(),notificationModel.getOffer_price());
        }
        else if (notificationModel.getAction_type().equals("2")&&notificationModel.getOrder_type().equals(Tags.CONTAINERS_ORDER))
        {
            activity.DisplayFragmentClientContainersOffer(notificationModel.getId(),Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getOffer_id(),notificationModel.getOffer_price());
        }
        else if (notificationModel.getAction_type().equals("2")&&notificationModel.getOrder_type().equals(Tags.CLEARANCE_ORDER))
        {
            activity.DisplayFragmentClientCustomsOffer(notificationModel.getId(),Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getOffer_id(),notificationModel.getOffer_price());
        }
        else if (notificationModel.getAction_type().equals("2")&&notificationModel.getOrder_type().equals(Tags.ENGINEERING_ORDER))
        {
            activity.DisplayFragmentClientEngineeringOffer(notificationModel.getId(),Integer.parseInt(notificationModel.getOrder_id()),notificationModel.getOffer_id(),notificationModel.getOffer_price());
        }
    }

    public void removeItem()
    {
        if (selected_pos!=-1)
        {
            notificationModelList.remove(selected_pos);
            adapter.notifyItemRemoved(selected_pos);
            selected_pos=-1;

            if (notificationModelList.size()==0)
            {
                ll_not.setVisibility(View.VISIBLE);
            }

        }
    }
}
