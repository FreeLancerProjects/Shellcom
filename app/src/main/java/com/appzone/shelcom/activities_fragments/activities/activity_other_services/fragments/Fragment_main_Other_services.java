package com.appzone.shelcom.activities_fragments.activities.activity_other_services.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.activity_other_services.OtherActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_main_Other_services extends Fragment {
    private ConstraintLayout cl_customer, cl_engneer, cl_container;

    private LinearLayout ll_back;
    private ImageView arrow;
    private String current_language;
    private OtherActivity activity;
    public static Fragment_main_Other_services newInstance() {
        return new Fragment_main_Other_services();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_services, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        activity= (OtherActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        ll_back=view.findViewById(R.id.ll_back);
        arrow = view.findViewById(R.id.arrow);
        if (current_language.equals("ar")||current_language.equals("ur"))
        {
            arrow.setRotation(180.0f);
        }


        cl_container=view.findViewById(R.id.co1);
        cl_customer=view.findViewById(R.id.co2);
        cl_engneer=view.findViewById(R.id.co3);
        cl_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.displayfragmentcontainer();
            }
        });
        cl_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.displayfragmentcustomerclearance();
            }
        });
        cl_engneer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.displayfragmentengneering();
            }
        });

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.Back();
            }
        });
    }
}
