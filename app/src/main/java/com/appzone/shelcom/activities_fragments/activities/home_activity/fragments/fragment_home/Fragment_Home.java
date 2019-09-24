package com.appzone.shelcom.activities_fragments.activities.home_activity.fragments.fragment_home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.appzone.shelcom.R;
import com.appzone.shelcom.activities_fragments.activities.home_activity.activity.Home_Activity;
import com.appzone.shelcom.models.UserModel;
import com.appzone.shelcom.preferences.Preferences;
import com.appzone.shelcom.share.Common;

import java.util.Locale;

import io.paperdb.Paper;


public class Fragment_Home extends Fragment {
    private AHBottomNavigation bottomNavigationView;
    private Home_Activity activity;
    private TextView tv_title;
    private String current_lang;
    private UserModel userModel;
    private Preferences preferences;

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);

        setUpBottomNav();

        return view;
    }

    private void initView(View view) {
        preferences = Preferences.getInstance();
        activity = (Home_Activity) getActivity();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        tv_title = view.findViewById(R.id.tv_title);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);


    }



    private void setUpBottomNav() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.home, R.drawable.ic_home, R.color.gray4);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.orders, R.drawable.ic_cart, R.color.gray4);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.not), R.drawable.ic_notification, R.color.gray4);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.accountِ, R.drawable.ic_user_profile, R.color.gray4);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.more, R.drawable.ic_more, R.color.gray);

        bottomNavigationView.addItem(item1);
        bottomNavigationView.addItem(item2);
        bottomNavigationView.addItem(item3);
        bottomNavigationView.addItem(item4);
        bottomNavigationView.addItem(item5);
        bottomNavigationView.setInactiveColor(ContextCompat.getColor(getActivity(),R.color.gray4));
        bottomNavigationView.setAccentColor(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
        bottomNavigationView.setDefaultBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
        bottomNavigationView.setForceTint(true);
        bottomNavigationView.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomNavigationView.setColored(false);
        bottomNavigationView.setTitleTextSizeInSp(16,13);
        bottomNavigationView.setCurrentItem(0);


        bottomNavigationView.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        activity.DisplayFragmentMain();
                        break;
                    case 1:
                        if (userModel!=null)
                        {
                            activity.DisplayFragmentOrders();

                        }else
                            {
                                Common.CreateUserNotSignInAlertDialog(activity);

                            }
                        break;
                    case 2:

                        if (userModel!=null)
                        {
                            activity.DisplayFragmentNotification();
                        }else
                        {
                            Common.CreateUserNotSignInAlertDialog(activity);

                        }
                        break;
                    case 3:
                        if (userModel!=null)
                        {
                            activity.DisplayFragmentProfile();

                        }else
                        {
                            Common.CreateUserNotSignInAlertDialog(activity);

                        }
                        break;

                    case 4:
                        if (userModel!=null)
                        {
                            activity.DisplayFragmentMore();

                        }else
                        {
                            Common.CreateUserNotSignInAlertDialog(activity);

                        }
                        break;


                }
                return false;
            }
        });
    }

    public void UpdateAHBottomNavigationPosition(int pos) {

        if (pos == 0) {
            tv_title.setText(getString(R.string.home));
        } else if (pos == 1) {
            tv_title.setText(getString(R.string.orders));

        } else if (pos == 2) {

            tv_title.setText(getString(R.string.not));

        } else if (pos == 3) {
            tv_title.setText(getString(R.string.accountِ));

        }
        else if (pos == 4) {
            tv_title.setText(getString(R.string.more));

        }
        bottomNavigationView.setCurrentItem(pos, false);
    }
}