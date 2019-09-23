package com.creativeshare.shell_com.activities_fragments.activities.activity_shipment_transportation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.shell_com.R;
import com.creativeshare.shell_com.activities_fragments.activities.activity_shipment_transportation.activity.ShipmentActivity;

public class Fragment_Shipment_Payment extends Fragment {

    private LinearLayout ll1,ll2,ll3;
    private RadioButton rb1,rb2,rb3;
    private int payment_method=0;
    private ShipmentActivity activity;
    private Button btn_send;


    public static Fragment_Shipment_Payment newInstance()
    {
        return new Fragment_Shipment_Payment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shipment_payment,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity = (ShipmentActivity) getActivity();
        ll1 = view.findViewById(R.id.ll1);
        ll2 = view.findViewById(R.id.ll2);
        ll3 = view.findViewById(R.id.ll3);

        rb1 = view.findViewById(R.id.rb1);
        rb2 = view.findViewById(R.id.rb2);
        rb3 = view.findViewById(R.id.rb3);

        btn_send = view.findViewById(R.id.btn_send);


        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setChecked(true);
                rb2.setChecked(false);
                rb3.setChecked(false);
                payment_method = 1;
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setChecked(false);
                rb2.setChecked(true);
                rb3.setChecked(false);
                payment_method = 2;
            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(true);
                payment_method = 3;
            }
        });

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setChecked(true);
                rb2.setChecked(false);
                rb3.setChecked(false);
                payment_method = 1;
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setChecked(false);
                rb2.setChecked(true);
                rb3.setChecked(false);
                payment_method = 2;
            }
        });

        rb3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rb1.setChecked(false);
                rb2.setChecked(false);
                rb3.setChecked(true);
                payment_method = 3;
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });

    }

    public void checkData(){

        if (payment_method == 0)
        {
            Toast.makeText(activity, getString(R.string.ch_pay_mthd), Toast.LENGTH_SHORT).show();
        }else
            {
                activity.uploadOrder(payment_method);
            }

    }
}
