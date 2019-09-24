package com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.adapters.Equipment_Adapter;
import com.appzone.shelcom.models.Equipment_Model;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.remote.Api;
import com.appzone.shelcom.tags.Tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
///// Ahmed saad
public class Fragment_Equipment extends Fragment {
    private Home_Activity activity;
    private Preferences preferences;
    private String curent_language;
    private ImageView arrow;
    private LinearLayout ll_back;

    private RecyclerView equipment;
    private ProgressBar progBar;
    private Equipment_Adapter equipment_adapter;
    private ArrayList<Equipment_Model> equipment_models;

    public static Fragment_Equipment newInstance() {
        return new Fragment_Equipment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_equipment, container, false);
        intitview(view);
        getequipment();
        return view;
    }

    private void intitview(View view) {
        equipment_models = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        preferences = Preferences.getInstance();
        Paper.init(activity);
        curent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = view.findViewById(R.id.arrow);
        ll_back = view.findViewById(R.id.ll_back);

        equipment = view.findViewById(R.id.recv_equipment);
        progBar = view.findViewById(R.id.progBarAds);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        if (curent_language.equals("ar")) {
            arrow.setRotation(180.0f);
        }

        equipment.setItemViewCacheSize(25);
        equipment.setDrawingCacheEnabled(true);
        equipment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        equipment_adapter = new Equipment_Adapter(equipment_models, activity,this);
        equipment.setAdapter(equipment_adapter);
        equipment.setLayoutManager(new GridLayoutManager(activity, 1));

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.Back();
            }
        });

    }

    private void getequipment() {
        Api.getService(Tags.base_url).get_equipments().enqueue(new Callback<List<Equipment_Model>>() {
            @Override
            public void onResponse(Call<List<Equipment_Model>> call, Response<List<Equipment_Model>> response) {
                if (response.isSuccessful()) {
                    progBar.setVisibility(View.GONE);
                    if (response.body().size() > 0) {
                        equipment_models.addAll(response.body());
                        equipment_adapter.notifyDataSetChanged();

                    } else {
                        Log.e("message", "no data found");
                    }
                } else {
                    Log.e("error code", response.code() + "" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Equipment_Model>> call, Throwable t) {
                progBar.setVisibility(View.GONE);

                Log.e("error message", t.getMessage());

            }
        });
    }


    public void setItemData(Equipment_Model model) {
        activity.DisplayFragmentEquipment(model.getId());
    }
}
