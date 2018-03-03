package com.nyelam.android.ecotrip;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        ecoTripViewPagerAdapter = new EcoTripViewPagerAdapter(getSupportFragmentManager());
        ecoTripViewPager.setAdapter(ecoTripViewPagerAdapter);
        circleIndicator.setViewPager(ecoTripViewPager);
        circleIndicator.setViewPager(ecoTripViewPager);
    }

    private void initControl() {

    }

    private void initView() {
        ecoTripViewPager = (ViewPager) findViewById(R.id.eco_trip_view_pager);
        circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
    }

}
