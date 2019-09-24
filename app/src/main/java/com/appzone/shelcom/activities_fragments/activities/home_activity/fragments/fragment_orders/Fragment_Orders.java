package com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.adapters.Pager_Adapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Orders extends Fragment {

    private TabLayout tab;
    private Home_Activity activity;
    private ViewPager pager;
    private Pager_Adapter pager_adapter;
    private List<Fragment> fragments;
    private List<String> titles;
    private String current_language;



    public static Fragment_Orders newInstance() {
        return new Fragment_Orders();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        initView(view);

        return view;


    }

    private void initView(View view) {
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
        activity = (Home_Activity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());




        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);


        fragments.add(Fragment_Current_Order.newInstance());
        fragments.add(Fragment_Previous_Order.newInstance());

        titles.add(getString(R.string.current));
        titles.add(getString(R.string.previou));

        pager_adapter = new Pager_Adapter(getChildFragmentManager());
        pager_adapter.setFragments(fragments);
        pager_adapter.setTitles(titles);
        pager.setAdapter(pager_adapter);


    }

    public void refreshFragment()
    {
        Fragment_Current_Order fragment_current_order = (Fragment_Current_Order) pager_adapter.getItem(0);
        Fragment_Previous_Order fragment_previous_order = (Fragment_Previous_Order) pager_adapter.getItem(1);

        fragment_current_order.getOrders();
        fragment_previous_order.getOrders();
    }
}