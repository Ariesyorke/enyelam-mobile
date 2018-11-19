package com.nyelam.android.doshoporderhistory;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Aprilian Nur Wakhid Daini on 11/19/2018.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    final int PAGE_COUNT = 8;
    private String tabTitles[] = new String[]{"Pending", "Waiting for Payment", "Payment Accepted", "Payment Declined", "Order Cancelled", "Order Processed", "Order Sent", "Order Closed"};
    private Context context;

    public TabsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DoShopOrderHistoryFragment oneFragment = new DoShopOrderHistoryFragment();
                return oneFragment;
            case 1:
                DoShopOrderHistoryFragment twoFragment = new DoShopOrderHistoryFragment();
                return twoFragment;
            default:
                DoShopOrderHistoryFragment lastFragment = new DoShopOrderHistoryFragment();
                return lastFragment;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tabTitles[position];
    }
}