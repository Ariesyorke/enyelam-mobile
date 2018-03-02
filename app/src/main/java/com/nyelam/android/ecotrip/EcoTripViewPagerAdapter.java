package com.nyelam.android.ecotrip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nyelam.android.R;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class EcoTripViewPagerAdapter extends FragmentPagerAdapter {
    private int[] backgroundDrawables = {
            R.drawable.eco_trip_1_bg,
            R.drawable.eco_trip_2_bg,
            R.drawable.eco_trip_3_bg,
            R.drawable.eco_trip_4_bg
    };
    private int[] iconDrawables = {
            R.drawable.eco_trip_1,
            R.drawable.eco_trip_2,
            R.drawable.eco_trip_3,
            R.drawable.eco_trip_4

    };

    public EcoTripViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position != 4) {
            return EcoTripOnboardingFragment.newInstance(backgroundDrawables[position], iconDrawables[position]);
        } else {
            return EcoTripOnboardingGetStartedFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }





}


