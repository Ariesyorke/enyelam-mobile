package com.nyelam.android.ecotrip;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nyelam.android.R;

import me.relex.circleindicator.CircleIndicator;

public class EcoTripActivity extends AppCompatActivity {

    private ViewPager ecoTripViewPager;
    private EcoTripViewPagerAdapter ecoTripViewPagerAdapter;
    private CircleIndicator circleIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eco_trip);
        initView();
        initViewPager();
        initControl();
    }

    private void initViewPager() {
        ecoTripViewPagerAdapter = new EcoTripViewPagerAdapter(getSupportFragmentManager(), false);
        ecoTripViewPager.setAdapter(ecoTripViewPagerAdapter);
        circleIndicator.setViewPager(ecoTripViewPager);
        circleIndicator.setViewPager(ecoTripViewPager);
    }

    private void initControl() {
        ecoTripViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 4) {
                    circleIndicator.setVisibility(View.GONE);
                } else {
                    circleIndicator.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        ecoTripViewPager = (ViewPager) findViewById(R.id.eco_trip_view_pager);
        circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
    }

}
