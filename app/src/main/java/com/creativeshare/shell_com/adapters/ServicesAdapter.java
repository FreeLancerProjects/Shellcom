package com.creativeshare.shell_com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.home_activity.fragments.fragment_home.Fragment_Upgrade_To_Company;
import com.creativeshare.shell_com.models.ServicesModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.MyHolder> {

    private List<ServicesModel> servicesModelList;
    private Context context;
    private String current_language;
    private Fragment_Upgrade_To_Company fragment;


    public ServicesAdapter(List<ServicesModel> servicesModelList, Context context, Fragment_Upgrade_To_Company fragment) {
        this.servicesModelList = servicesModelList;
        this.context = context;
        this.fragment = fragment;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        ServicesModel  servicesModel = servicesModelList.get(position);
        holder.BindData(servicesModel);

        holder.image_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment.setItemForDelete(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return servicesModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;
        private ImageView  image_delete;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);
            image_delete = itemView.findViewById(R.id.image_delete);


        }


        public void BindData(ServicesModel servicesModel)
        {
            if (current_language.equals("ar")||current_language.equals("ur"))
            {
                tv_title.setText(servicesModel.getAr_title());
            }else
                {
                    tv_title.setText(servicesModel.getEn_title());

                }
        }
    }
}
