package com.nyelam.android.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class BannerViewPagerAdapter extends FragmentStatePagerAdapter {

    private BannerList bannerList;
    private int count = 0;

    public BannerViewPagerAdapter(FragmentManager fm) {
        super(fm);
        bannerList = new BannerList();
    }

    @Override
    public Fragment getItem(int position) {
        Banner retBanner = bannerList.getList().get(position);
//        return BannerFragment.newInstance(position, retBanner);
        return new BannerFragment(position, retBanner);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setBannerList(BannerList bannerList) {
        this.bannerList = new BannerList();
        this.bannerList = bannerList;
        this.count = bannerList.getList().size();
        this.notifyDataSetChanged();
//        if (bannerList != null){
//            this.bannerList = bannerList;
//            this.count = bannerList.getList().size();
//        }
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