package com.nyelam.android.doshop;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DoShopBanner;
import com.nyelam.android.data.DoShopBannerList;
import com.nyelam.android.home.BannerFragment;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class DoShopBannerViewPagerAdapter extends FragmentStatePagerAdapter {

    private DoShopBannerList bannerList;
    private int count = 0;
    private boolean isGallery = false;

    public DoShopBannerViewPagerAdapter(FragmentManager fm) {
        super(fm);
        bannerList = new DoShopBannerList();
    }

    @Override
    public Fragment getItem(int position) {
        DoShopBanner retBanner = bannerList.getList().get(position);
//        return BannerFragment.newInstance(position, retBanner);
        return new DoShopBannerFragment(position, retBanner);
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void setBannerList(DoShopBannerList bannerList) {
        this.bannerList = new DoShopBannerList();
        this.bannerList = bannerList;
        this.count = bannerList.getList().size();
        this.notifyDataSetChanged();
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getCount() {
        return count;
    }


    public void clear() {
        bannerList = new DoShopBannerList();
    }

    public boolean isGallery() {
        return isGallery;
    }

    public void setGallery(boolean gallery) {
        isGallery = gallery;
    }
}