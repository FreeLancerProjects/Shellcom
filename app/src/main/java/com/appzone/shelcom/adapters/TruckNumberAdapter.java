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

import java.util.List;

public class TruckNumberAdapter extends RecyclerView.Adapter<TruckNumberAdapter.MyHolder> {

    private List<Integer> integerList;
    private Context context;
    private Fragment_Shipment_Container_Type fragment;


    public TruckNumberAdapter(List<Integer> integerList, Context context, Fragment_Shipment_Container_Type fragment) {
        this.integerList = integerList;
        this.context = context;
        this.fragment = fragment;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        int number = integerList.get(position);
        holder.BindData(number);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = integerList.get(holder.getAdapterPosition());

                fragment.setItemOfTruckNumber(number,holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return integerList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv_title;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_title);


        }


        private void BindData(int number)
        {
            tv_title.setText(String.valueOf(number));

        }
    }
}
