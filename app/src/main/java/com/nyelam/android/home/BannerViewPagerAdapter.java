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
    private ArrayList<String> imageList;
    private int count = 0;
    private boolean isGallery = false;
    private boolean isOnlick = false;

    public BannerViewPagerAdapter(FragmentManager fm) {
        super(fm);
        bannerList = new BannerList();
    }

    public BannerViewPagerAdapter(FragmentManager fm, boolean isOnlick) {
        super(fm);
        this.bannerList = new BannerList();
        this.isOnlick = isOnlick;
    }

    @Override
    public Fragment getItem(int position) {
        Banner retBanner = bannerList.getList().get(position);
//        return BannerFragment.newInstance(position, retBanner);
        //return new BannerFragment(position, retBanner, isGallery);
        return new BannerFragment(position, retBanner, isGallery, imageList, isOnlick);
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

        imageList = new ArrayList<>();
        if (bannerList != null && bannerList.getList() != null){
            for (Banner banner : bannerList.getList()){
                imageList.add(banner.getImageUrl());
            }
        }
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

    public boolean isGallery() {
        return isGallery;
    }

    public void setGallery(boolean gallery) {
        isGallery = gallery;
    }
}