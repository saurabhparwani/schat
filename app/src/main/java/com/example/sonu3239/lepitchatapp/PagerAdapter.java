package com.example.sonu3239.lepitchatapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter { ;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        //Initializing tab count

    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Request tab1 = new Request();
                return tab1;
            case 1:
                Chat tab2 = new Chat();
                return tab2;
            case 2:
                Friend tab3 = new Friend();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Requests";
            case 1:
                return "Chat";
            case 2:
                return "Friend";
        }
        return  null;
    }
}
