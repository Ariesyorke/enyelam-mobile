package com.nyelam.android.doshoporderhistory;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoShopOrderHistoryActivity extends BasicActivity {

    private android.support.v4.view.ViewPager ViewPager;
    private TabsPagerAdapter adapter;

    @BindView(R.id.viewpagertab) SmartTabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_order_history);
        ButterKnife.bind(this);
        initToolbar();

        adapter = new TabsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
