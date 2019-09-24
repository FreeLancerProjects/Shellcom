package com.appzone.shelcom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.activity_shipment_transportation.fragments.Fragment_Shipment_Container_Type;
import com.appzone.shelcom.models.ContainersModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class LoadTypeAdapter extends RecyclerView.Adapter<LoadTypeAdapter.MyHolder> {

    private List<ContainersModel.Loads> loadsList;
    private Context context;
    private String current_language;
    private Fragment_Shipment_Container_Type fragment;


    public LoadTypeAdapter(List<ContainersModel.Loads> loadsList, Context context, Fragment_Shipment_Container_Type fragment) {
        this.loadsList = loadsList;
        this.context = context;
        this.fragment = fragment;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        ContainersModel.Loads loads = loadsList.get(position);
        holder.BindData(loads);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainersModel.Loads loads = loadsList.get(holder.getAdapterPosition());

                fragment.setItemOfLoadType(loads,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return loadsList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);


        }


        public void BindData(ContainersModel.Loads loads)
        {
            if (current_language.equals("ar")||current_language.equals("ur"))
            {
                tv_title.setText(loads.getAr_title_load());
            }else
                {
                    tv_title.setText(loads.getEn_title_load());

                }
        }
    }
}
