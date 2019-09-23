package com.creativeshare.shell_com.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class Shipment_Pager_Adapter extends FragmentStatePagerAdapter {

    private List<Fragment> fragments;

    public Shipment_Pager_Adapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
    }

    public void setFragments(List<Fragment> fragments)
    {
        this.fragments = fragments;
    }


    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }



}