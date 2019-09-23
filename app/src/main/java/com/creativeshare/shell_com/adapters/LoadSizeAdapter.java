package com.creativeshare.shell_com.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.activity_shipment_transportation.fragments.Fragment_Shipment_Container_Type;
import com.creativeshare.shell_com.models.ContainersModel;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class LoadSizeAdapter extends RecyclerView.Adapter<LoadSizeAdapter.MyHolder> {

    private List<ContainersModel.Sizes> sizesList;
    private Context context;
    private String current_language;
    private Fragment_Shipment_Container_Type fragment;


    public LoadSizeAdapter(List<ContainersModel.Sizes> sizesList, Context context, Fragment_Shipment_Container_Type fragment) {
        this.sizesList = sizesList;
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

        ContainersModel.Sizes sizes = sizesList.get(position);
        holder.BindData(sizes);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainersModel.Sizes sizes = sizesList.get(holder.getAdapterPosition());

                fragment.setItemOfLoadSize(sizes);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sizesList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);


        }


        public void BindData(ContainersModel.Sizes sizes)
        {
            if (current_language.equals("ar")||current_language.equals("ur"))
            {
                tv_title.setText(sizes.getAr_title_size());
            }else
                {
                    tv_title.setText(sizes.getEn_title_size());

                }
        }
    }
}
