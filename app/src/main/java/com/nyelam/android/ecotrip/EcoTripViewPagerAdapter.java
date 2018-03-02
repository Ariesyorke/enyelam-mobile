package com.nyelam.android.ecotrip;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.home.BannerFragment;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class EcoTripViewPagerAdapter extends FragmentPagerAdapter {

    private BannerList bannerList;
    private int count = 0;

    public EcoTripViewPagerAdapter(FragmentManager fm) {
        super(fm);
        bannerList = new BannerList();
    }

    @Override
    public Fragment getItem(int position) {
        Banner retBanner = null;
        if(bannerList != null && bannerList.getList() != null) retBanner = bannerList.getList().get(position);
        return BannerFragment.newInstance(position, retBanner);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }



    public void setBannerList(BannerList bannerList) {
        if (this.bannerList == null) this.bannerList = new BannerList();
        this.bannerList = bannerList;
        if (bannerList != null) this.count = bannerList.getList().size();
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }


    public void clear() {
        bannerList = new BannerList();
    }


}


