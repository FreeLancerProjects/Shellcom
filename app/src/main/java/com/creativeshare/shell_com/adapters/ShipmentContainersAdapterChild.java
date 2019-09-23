package com.creativeshare.shell_com.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.activity_shipment_transportation.fragments.Fragment_Shipment_Container_Type;
import com.creativeshare.shell_com.models.ContainersModel;
import com.creativeshare.shell_com.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class ShipmentContainersAdapterChild extends RecyclerView.Adapter<ShipmentContainersAdapterChild.MyHolder> {

    private List<ContainersModel.Trans> containerModelList;
    private Context context;
    private String current_language;
    private Fragment_Shipment_Container_Type fragment;
    private SparseBooleanArray sparseBooleanArray;
    private ShipmentContainersAdapterParent shipmentContainersAdapterParent;
    private int parent_pos;


    public ShipmentContainersAdapterChild(List<ContainersModel.Trans> containerModelList, Context context, Fragment_Shipment_Container_Type fragment, ShipmentContainersAdapterParent shipmentContainersAdapterParent, int pos) {
        this.containerModelList = containerModelList;
        this.context = context;
        this.fragment = fragment;
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        sparseBooleanArray = new SparseBooleanArray();
        this.shipmentContainersAdapterParent = shipmentContainersAdapterParent;
        this.parent_pos = pos;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.container_type_child_row,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {


        if (sparseBooleanArray.get(position))
        {
            holder.fl.setBackgroundResource(R.drawable.selected_edt);
            holder.tv_name.setTextColor(ContextCompat.getColor(context,R.color.white));
        }else
            {
                holder.fl.setBackgroundResource(R.drawable.unselected_edt);

                holder.tv_name.setTextColor(ContextCompat.getColor(context,R.color.black));

            }
        ContainersModel.Trans  trans = containerModelList.get(position);
        holder.BindData(trans);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContainersModel.Trans  trans = containerModelList.get(position);
                fragment.setItemData(trans,holder.getAdapterPosition(),parent_pos);
                shipmentContainersAdapterParent.setSelectedPos(parent_pos,holder.getAdapterPosition());
            }
        });



    }

    @Override
    public int getItemCount() {
        return containerModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private FrameLayout fl;
        private TextView tv_name;
        private ImageView  image;


        public MyHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            image = itemView.findViewById(R.id.image);
            fl = itemView.findViewById(R.id.fl);


        }


        public void BindData(ContainersModel.Trans trans)
        {

            if (current_language.equals("ar")||current_language.equals("ur"))
            {
                tv_name.setText(trans.getAr_title());
            }else
                {
                    tv_name.setText(trans.getEn_title());
                }

            Picasso.with(context).load(Uri.parse(Tags.IMAGE_CONTAINER_URL+trans.getTrans_image())).fit().into(image);
        }
    }

    public void setSelectedPos(int pos)
    {

        sparseBooleanArray.clear();
        sparseBooleanArray.put(pos,true);
        notifyDataSetChanged();
    }


}
