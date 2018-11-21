package com.nyelam.android.doshoporderhistory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.nyelam.android.doshoporder.DoShopCheckoutFragment;
import com.nyelam.android.helper.NYHelper;

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
                Bundle bundle1 = new Bundle();
                bundle1.putString(NYHelper.STATUS, "1");
                DoShopOrderHistoryFragment fragment1 = new DoShopOrderHistoryFragment();
                return fragment1;
            case 1:
                Bundle bundle2 = new Bundle();
                bundle2.putString(NYHelper.STATUS, "2");
                DoShopOrderHistoryFragment fragment2 = new DoShopOrderHistoryFragment();
                return fragment2;
            case 2:
                Bundle bundle3 = new Bundle();
                bundle3.putString(NYHelper.STATUS, "3");
                DoShopOrderHistoryFragment fragment3 = new DoShopOrderHistoryFragment();
                return fragment3;
            case 3:
                Bundle bundle4 = new Bundle();
                bundle4.putString(NYHelper.STATUS, "4");
                DoShopOrderHistoryFragment fragment4 = new DoShopOrderHistoryFragment();
                return fragment4;
            case 4:
                Bundle bundle5 = new Bundle();
                bundle5.putString(NYHelper.STATUS, "5");
                DoShopOrderHistoryFragment fragment5 = new DoShopOrderHistoryFragment();
                return fragment5;
            case 5:
                Bundle bundle6 = new Bundle();
                bundle6.putString(NYHelper.STATUS, "6");
                DoShopOrderHistoryFragment fragment6 = new DoShopOrderHistoryFragment();
                return fragment6;
            case 6:
                Bundle bundle7 = new Bundle();
                bundle7.putString(NYHelper.STATUS, "7");
                DoShopOrderHistoryFragment fragment7 = new DoShopOrderHistoryFragment();
                return fragment7;
            case 7:
                Bundle bundle8 = new Bundle();
                bundle8.putString(NYHelper.STATUS, "8");
                DoShopOrderHistoryFragment fragment8 = new DoShopOrderHistoryFragment();
                return fragment8;
            default:
                Bundle bundle = new Bundle();
                bundle.putString(NYHelper.STATUS, String.valueOf(position+1));
                DoShopOrderHistoryFragment lastFragment = new DoShopOrderHistoryFragment();
                lastFragment.setArguments(bundle);
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