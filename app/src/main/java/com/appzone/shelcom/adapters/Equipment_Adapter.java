package com.appzone.shelcom.adapters;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Equipment;
import com.appzone.shelcom.models.Equipment_Model;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Equipment_Adapter extends RecyclerView.Adapter<Equipment_Adapter.Eyas_Holder> {
    List<Equipment_Model> list;
    Context context;
    Home_Activity activity;
    Preferences preferences;
    String current_lang;
    String data;
    Fragment_Equipment fragment_equipment;

    public Equipment_Adapter(List<Equipment_Model> list, Context context, Fragment_Equipment fragment_equipment) {
        this.list = list;
        this.context = context;
        activity = (Home_Activity) context;
        preferences = Preferences.getInstance();
        this.fragment_equipment = fragment_equipment;
        Paper.init(context);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public Eyas_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.equipment_layout_row, viewGroup, false);
        Eyas_Holder eas = new Eyas_Holder(v);
        return eas;
    }

    @Override
    public void onBindViewHolder(@NonNull final Eyas_Holder viewHolder, final int i) {
        Equipment_Model model = list.get(i);
        if (current_lang.equals("ar")) {
            data = model.getAr_title();
        } else {
            data = model.getEn_title();
        }
        viewHolder.name.setText(data);
        Picasso.with(activity).load(Uri.parse(Tags.equipment_url + model.getEquipment_image())).fit().into(viewHolder.trankimage);

        Log.e("url",Tags.equipment_url + model.getEquipment_image());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Equipment_Model model = list.get(viewHolder.getAdapterPosition());
                fragment_equipment.setItemData(model);


            }

        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Eyas_Holder extends RecyclerView.ViewHolder {
        TextView name;
        RoundedImageView trankimage;

        public Eyas_Holder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textview_title);
            trankimage = itemView.findViewById(R.id.rounded_equipment);
        }


    }
}
