package com.nyelam.android.divecenter;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveGuide;
import com.nyelam.android.data.DiveGuideList;
import com.nyelam.android.data.Location;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.BannerViewPagerAdapter;
import com.nyelam.android.http.NYDiveGuideDetailRequest;
import com.nyelam.android.http.NYDoDiveCenterDetailRequest;
import com.nyelam.android.view.NYBannerViewPager;
import com.nyelam.android.view.NYCustomViewPager;
import com.nyelam.android.view.NYHomepageDetailTabItemView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class DiveGuideActivity extends AppCompatActivity implements
    DiveGuideBioFragment.OnFragmentInteractionListener,
    DiveGuideAboutFragment.OnFragmentInteractionListener {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private NYBannerViewPager bannerViewPager;
    private CircleIndicator circleIndicator;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private NYFragmentPagerAdapter fragmentAdapter;
    private Fragment fragment;
    private ImageView menuItemImageView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LinearLayout tabManager;
    private NYCustomViewPager viewPager;
    private boolean mProtectFromCheckedChange = false;
    private boolean mProtectFromPagerChange = false;
    private DiveGuide diveGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_guide);
        diveGuide = new DiveGuide();
        initToolbar();
        initView();
        initTab();
        loadDummyDiveGuideDetail();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabManager = (LinearLayout) findViewById(R.id.tab_manager);
        viewPager = (NYCustomViewPager) findViewById(R.id.view_pager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //viewTabManager = (View) findViewById(R.id.view_tab_manager);
        menuItemImageView = (ImageView) findViewById(R.id.menu_item_imageView);
        bannerViewPager = (NYBannerViewPager) findViewById(R.id.promotion_view_pager);
        circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);

        fragmentAdapter = new NYFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);
    }


    private void initTab() {

        setSupportActionBar(toolbar);

        viewPager.setOffscreenPageLimit(4);
        //viewPager.setAdapter(new NYFragmentPagerAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                //KTLog.e("CEK 1");
                //Toast.makeText(MainActivity.this, "test 1", Toast.LENGTH_SHORT).show();
                fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());
                if (fragment != null && fragment instanceof DiveGuideBioFragment){

                    // TODO: buat metode
                    //((DiveGuideBioFragment) fragment).setContent();

                } else if (fragment != null && fragment instanceof DiveGuideAboutFragment){

                    // TODO: buat metode
                    ///((DiveGuideAboutFragment) fragment).setDiveSpot();

                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (mProtectFromPagerChange) {
                    return;
                }

                NYHomepageDetailTabItemView view = (NYHomepageDetailTabItemView) tabManager.getChildAt(position);
                onCheckedChanged(view, true);
                fragment = ((NYFragmentPagerAdapter) viewPager.getAdapter()).getFragment(position);
                if (position == 0 && fragment != null) {
                    fragment.onResume();
                }
            }
        });

        for (int i = 0; i < tabManager.getChildCount(); i++) {
            View child = tabManager.getChildAt(i);
            if (child instanceof NYHomepageDetailTabItemView) {
                ((NYHomepageDetailTabItemView) child).setDiveGuideActivity(this);
            }
        }

    }

    public int onCheckedChanged(NYHomepageDetailTabItemView view, boolean checked) {
        if (mProtectFromCheckedChange) {
            return -1;
        }

        mProtectFromCheckedChange = true;

        int checkedTabItemPosition = -1;

        for (int i = 0; i < tabManager.getChildCount(); i++) {
            View child = tabManager.getChildAt(i);
            if (child instanceof NYHomepageDetailTabItemView) {
                if (child == view) {
                    setCheckedStateForTab(child.getId(), checked);
                    checkedTabItemPosition = ((NYHomepageDetailTabItemView) child).getTabItemPosition();
                } else {
                    setCheckedStateForTab(child.getId(), false);
                }
            }
        }

        mProtectFromCheckedChange = false;
        return checkedTabItemPosition;
    }

    private void setCheckedStateForTab(int id, boolean checked) {
        View checkedView = tabManager.findViewById(id);
        if (checkedView != null && checkedView instanceof Checkable) {
            ((Checkable) checkedView).setChecked(checked);
        }
    }

    public void movePagerToTabItemPosition(int tabItemPosition) {

        mProtectFromPagerChange = true;
        if (tabItemPosition > -1) {
            viewPager.setCurrentItem(tabItemPosition, true);
        }
        mProtectFromPagerChange = false;
    }

    public class NYFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final int FRAGMENT_COUNT = 2;
        private Map<Integer, String> fragmentTags;
        private FragmentManager fragmentManager;

        public NYFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragmentManager = fm;
            this.fragmentTags = new HashMap<>();
        }

        @Override
        public int getCount() {
            return FRAGMENT_COUNT;
        }

        public Fragment getFragment(int position) {

            String tag = fragmentTags.get(position);
            if (tag == null) {
                return null;
            }

            Bundle b = new Bundle();
            fragmentManager.putFragment(b, tag, fragmentManager.findFragmentByTag(tag));
            return fragmentManager.findFragmentByTag(tag);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                DiveGuideBioFragment fragment = DiveGuideBioFragment.newInstance();
                return fragment;
            } else {
                DiveGuideAboutFragment fragment = DiveGuideAboutFragment.newInstance();
                return fragment;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);

            if (obj instanceof Fragment) {
                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                fragmentTags.put(position, tag);
            }

            return obj;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    private void initRequest() {
        if (diveGuide != null && !TextUtils.isEmpty(diveGuide.getId())){
            NYDiveGuideDetailRequest req = new NYDiveGuideDetailRequest(this.getClass(), DiveGuideActivity.this, diveGuide.getId());
            spcMgr.execute(req, onGetDetailDiveGuideRequest());
        }
    }

    private RequestListener<DiveGuide> onGetDetailDiveGuideRequest() {
        return new RequestListener<DiveGuide>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
//                if (progressBar != null) {
//                    mainProgressBar.setVisibility(View.GONE);
//                }
                NYHelper.handleAPIException(DiveGuideActivity.this, spiceException, null);
                //noResultTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(DiveGuide results) {
//                if (mainProgressBar != null) {
//                    mainProgressBar.setVisibility(View.GONE);
//                }
                diveGuide = results;

            }
        };
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spcMgr.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    private void loadDummyDiveGuideDetail(){
        JSONObject obj = null;
        try {
            obj = new JSONObject(NYHelper.getJSONFromResource(this, "dive_guide_detail.json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        DiveGuide diveGuide = new DiveGuide();
        diveGuide.parse(obj);

        this.diveGuide = diveGuide;

        if (diveGuide != null){
            NYLog.e("CEK DIVE GUIDE : "+diveGuide.toString());
        } else {
            NYLog.e("CEK DIVE GUIDE : null");
        }

        fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());
        if (fragment != null && fragment instanceof DiveGuideBioFragment){

            // TODO: buat metode
            ((DiveGuideBioFragment) fragment).setContent(this.diveGuide);

        } else if (fragment != null && fragment instanceof DiveGuideAboutFragment){

            // TODO: buat metode
            ((DiveGuideAboutFragment) fragment).setContent(this.diveGuide);

        }
    }

}
