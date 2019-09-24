package com.appzone.shelcom.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_orders.Fragment_Current_Order;
import com.appzone.shelcom.models.OrderDataModel;
import com.appzone.shelcom.tags.Tags;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class OrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<OrderDataModel.OrderModel> orderModelList;
    private Context context;
    private Fragment fragment;
    private String user_type;

    public OrdersAdapter(List<OrderDataModel.OrderModel> orderModelList, Context context, Fragment fragment,String user_type) {

        this.orderModelList = orderModelList;
        this.context = context;
        this.fragment = fragment;
        this.user_type = user_type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.current_order_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            final MyHolder myHolder = (MyHolder) holder;
            OrderDataModel.OrderModel orderModel = orderModelList.get(myHolder.getAdapterPosition());
            myHolder.BindData(orderModel);

            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragment instanceof Fragment_Current_Order)
                    {
                        OrderDataModel.OrderModel orderModel = orderModelList.get(myHolder.getAdapterPosition());
                        Fragment_Current_Order  fragment_current_order = (Fragment_Current_Order) fragment;
                        fragment_current_order.setItemData(orderModel,holder.getAdapterPosition());

                    }
                }
            });

        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.progBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private ImageView image_state;
        private TextView tv_name, tv_order_num,tv_order_type;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            image_state = itemView.findViewById(R.id.image_state);

            tv_order_num = itemView.findViewById(R.id.tv_order_num);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_order_type = itemView.findViewById(R.id.tv_order_type);


        }

        public void BindData(OrderDataModel.OrderModel orderModel) {
            tv_order_num.setText("#" + orderModel.getId());

            if (user_type.equals(Tags.TYPE_USER))
            {
                tv_name.setText(orderModel.getTo_name());

            }else
                {
                    tv_name.setText(orderModel.getFrom_name());

                }

            if (orderModel.getOrder_type().equals(Tags.WATER_ORDER))
            {
                tv_order_type.setText(context.getString(R.string.wd));
            }else if (orderModel.getOrder_type().equals(Tags.RENTAL_ORDER))
            {
                tv_order_type.setText(context.getString(R.string.re));
            }
            else if (orderModel.getOrder_type().equals(Tags.SHIPPING_ORDER))
            {
                tv_order_type.setText(context.getString(R.string.st));
            }
            else if (orderModel.getOrder_type().equals(Tags.CONTAINERS_ORDER))
            {
                tv_order_type.setText(context.getString(R.string.con));
            }
            else if (orderModel.getOrder_type().equals(Tags.CLEARANCE_ORDER))
            {
                tv_order_type.setText(context.getString(R.string.cc));
            }

            else if (orderModel.getOrder_type().equals(Tags.ENGINEERING_ORDER))
            {
                tv_order_type.setText(context.getString(R.string.ec));
            }

            if (fragment instanceof Fragment_Current_Order)
            {
                image_state.setImageResource(R.drawable.ic_clock);
                image_state.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary));
            }else
                {
                    image_state.setImageResource(R.drawable.ic_done);
                    image_state.setColorFilter(ContextCompat.getColor(context,R.color.done));

                }




        }
    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);
            progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemViewType(int position) {
        OrderDataModel.OrderModel orderModel = orderModelList.get(position);
        if (orderModel == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }



    }
}
