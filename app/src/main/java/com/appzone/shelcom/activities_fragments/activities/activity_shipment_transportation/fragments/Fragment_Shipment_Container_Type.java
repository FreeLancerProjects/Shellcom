package com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.activity.ShipmentActivity;
import com.appzone.shelcom.adapters.LoadSizeAdapter;
import com.appzone.shelcom.adapters.LoadTypeAdapter;
import com.appzone.shelcom.adapters.ShipmentContainersAdapterParent;
import com.appzone.shelcom.adapters.TruckNumberAdapter;
import com.appzone.shelcom.models.ContainersModel;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Shipment_Container_Type extends Fragment {

    private RecyclerView recView;
    private LinearLayoutManager manager;
    private TextView tv_type, tv_truck_number, tv_truck_size;
    private ShipmentContainersAdapterParent adapter;
    private ProgressBar progBar;
    private List<ContainersModel> containerModelList;
    private ShipmentActivity activity;
    private int container_id = -1, selected_pos = -1, parent_pos = -1, load_pos = -1, truck_number = 0;
    private String  truck_size_id = "", truck_type_id = "";
    private List<ContainersModel.Sizes> sizesList;
    private List<Integer> truck_number_list;
    private AlertDialog dialog_type, dialog_size, dialog_number;
    private String current_language;


    public static Fragment_Shipment_Container_Type newInstance() {
        return new Fragment_Shipment_Container_Type();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipment_container_type, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (ShipmentActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());


        containerModelList = new ArrayList<>();
        sizesList = new ArrayList<>();
        truck_number_list = new ArrayList<>();
        addNumbers();
        tv_type = view.findViewById(R.id.tv_type);
        tv_truck_number = view.findViewById(R.id.tv_truck_number);
        tv_truck_size = view.findViewById(R.id.tv_truck_size);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ActivityCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);

        adapter = new ShipmentContainersAdapterParent(containerModelList, activity, this);
        recView.setAdapter(adapter);
        recView.setNestedScrollingEnabled(true);


        tv_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parent_pos != -1 && selected_pos != -1) {
                    CreateLoadTypeAlertDialog(containerModelList.get(parent_pos).getTrans().get(selected_pos).getLoads());
                }
            }
        });

        tv_truck_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parent_pos != -1 && selected_pos != -1 && load_pos != -1) {
                    CreateLoadSizeAlertDialog(containerModelList.get(parent_pos).getTrans().get(selected_pos).getLoads().get(load_pos).getSizes());
                }
            }
        });
        tv_truck_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateTruckNumberAlertDialog();
            }
        });

        getContainers();
    }

    private void addNumbers() {
        for (int i = 1; i <= 9; i++) {
            truck_number_list.add(i);
        }
    }

    private void getContainers() {

        Api.getService(Tags.base_url)
                .getContainers()
                .enqueue(new Callback<List<ContainersModel>>() {
                    @Override
                    public void onResponse(Call<List<ContainersModel>> call, Response<List<ContainersModel>> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().size() > 0) {
                                containerModelList.addAll(response.body());


                                adapter.notifyDataSetChanged();
                            } else {
                                recView.setVisibility(View.GONE);
                            }

                        } else {


                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ContainersModel>> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    public boolean isDataOk() {

        if (!TextUtils.isEmpty(truck_type_id) &&
                !TextUtils.isEmpty(truck_size_id) &&
                container_id != -1 && truck_number != 0


        ) {
            tv_type.setError(null);
            tv_truck_number.setError(null);
            tv_truck_size.setError(null);

            activity.saveContainerData(container_id, truck_type_id, truck_number, truck_size_id);
            return true;
        } else {


            if (TextUtils.isEmpty(truck_type_id)) {
                tv_type.setError(getString(R.string.field_req));
            } else {
                tv_type.setError(null);

            }

            if (TextUtils.isEmpty(truck_size_id)) {
                tv_truck_size.setError(getString(R.string.field_req));
            } else {
                tv_truck_size.setError(null);

            }

            if (container_id == -1) {
                Toast.makeText(activity, getString(R.string.ch_truck), Toast.LENGTH_SHORT).show();
            }
            if (truck_number == 0) {
                tv_truck_number.setText(getString(R.string.field_req));
            }else
                {
                    tv_truck_number.setError(null);

                }
            return false;
        }
    }

    private void CreateLoadTypeAlertDialog(List<ContainersModel.Loads> loadsList) {


        dialog_type = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_load_type, null);

        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        RecyclerView recView = view.findViewById(R.id.recView);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        TextView tv_not = view.findViewById(R.id.tv_not);
        TextView tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.type_of_load3));


        if (loadsList.size() > 0) {
            LoadTypeAdapter adapter = new LoadTypeAdapter(loadsList, activity, this);
            recView.setAdapter(adapter);
            tv_not.setVisibility(View.GONE);

        } else {
            tv_not.setVisibility(View.VISIBLE);
            tv_not.setText(getString(R.string.no_load_to_display));
        }


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_type.dismiss();
            }
        });

        dialog_type.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog_type.setCanceledOnTouchOutside(false);
        dialog_type.setView(view);
        dialog_type.show();
    }

    private void CreateLoadSizeAlertDialog(List<ContainersModel.Sizes> sizesList) {


        dialog_size = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_load_type, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        RecyclerView recView = view.findViewById(R.id.recView);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        TextView tv_not = view.findViewById(R.id.tv_not);
        tv_title.setText(getString(R.string.size_of_truck));

        if (sizesList.size() > 0) {
            LoadSizeAdapter adapter = new LoadSizeAdapter(sizesList, activity, this);
            recView.setAdapter(adapter);
            tv_not.setVisibility(View.GONE);

        } else {
            tv_not.setVisibility(View.VISIBLE);
            tv_not.setText(getString(R.string.no_sizes));
        }


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_size.dismiss();
            }
        });

        dialog_size.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog_size.setCanceledOnTouchOutside(false);
        dialog_size.setView(view);
        dialog_size.show();
    }

    private void CreateTruckNumberAlertDialog() {


        dialog_number = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .create();


        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_load_type, null);
        TextView tv_title = view.findViewById(R.id.tv_title);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        RecyclerView recView = view.findViewById(R.id.recView);
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        tv_title.setText(getString(R.string.number_of_truck));

        TruckNumberAdapter adapter = new TruckNumberAdapter(truck_number_list, activity, this);
        recView.setAdapter(adapter);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_number.dismiss();
            }
        });

        dialog_number.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog_number.setCanceledOnTouchOutside(false);
        dialog_number.setView(view);
        dialog_number.show();
    }


    public void setItemData(ContainersModel.Trans trans, int adapterPosition, int parent_pos) {
        container_id = Integer.parseInt(trans.getId());
        selected_pos = adapterPosition;
        this.parent_pos = parent_pos;
        CreateLoadTypeAlertDialog(trans.getLoads());
        sizesList.clear();


    }

    public void setItemOfLoadType(ContainersModel.Loads loads, int adapterPosition) {
        this.load_pos = adapterPosition;
        truck_type_id = loads.getId();

        if (current_language.equals("ar"))
        {
            tv_type.setText(loads.getAr_title_load());
        }else
            {
                tv_type.setText(loads.getEn_title_load());

            }

        if (dialog_type != null) {
            dialog_type.dismiss();
        }

        CreateLoadSizeAlertDialog(loads.getSizes());
    }

    public void setItemOfLoadSize(ContainersModel.Sizes sizes) {
        truck_size_id = sizes.getId();
        if (current_language.equals("ar"))
        {
            tv_truck_size.setText(sizes.getAr_title_size());
        }else
        {
            tv_truck_size.setText(sizes.getEn_title_size());

        }
        CreateTruckNumberAlertDialog();
        if (dialog_size != null) {
            dialog_size.dismiss();
        }
    }

    public void setItemOfTruckNumber(int number, int adapterPosition) {
        this.truck_number = number;
        Log.e("nu",number+"_");
        tv_truck_number.setText(String.valueOf(number));

        if (dialog_number != null) {
            dialog_number.dismiss();
        }

    }
}
