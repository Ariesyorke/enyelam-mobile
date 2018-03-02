package com.nyelam.android.ecotrip;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.dodive.DoDiveSearchActivity;
import com.nyelam.android.dodive.DoDiveSearchResultActivity;
import com.nyelam.android.dodive.TotalDiverSpinnerAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.BannerViewPagerAdapter;
import com.nyelam.android.view.NYBannerViewPager;
import com.nyelam.android.view.NYCustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
