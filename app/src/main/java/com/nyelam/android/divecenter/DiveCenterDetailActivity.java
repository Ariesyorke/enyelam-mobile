package com.nyelam.android.divecenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.BannerViewPagerAdapter;
import com.nyelam.android.http.NYDiveCenterDetailRequest;
import com.nyelam.android.view.NYBannerViewPager;
import com.nyelam.android.view.NYCustomViewPager;
import com.nyelam.android.view.NYHomepageDetailTabItemView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class DiveCenterDetailActivity extends AppCompatActivity implements
        DiveCenterDetailFragment.OnFragmentInteractionListener,
        DiveCenterMapFragment.OnFragmentInteractionListener,
        DiveCenterListServiceFragment.OnFragmentInteractionListener {

    private List<Frags> tags = Arrays.asList(new Frags(0,"home"), new Frags(1,"timeline"), new Frags(2,"interest"), new Frags(3,"tags"));

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
    private String serviceId;
    //protected DiveService diveService, newDiveService;
    protected DiveCenter diveCenter;
    protected String diver;
    protected String certificate;
    protected String schedule;
    private TextView nameTextView, ratingTextView, bookingTextView;
    private ProgressDialog progressDialog;
    private boolean triggerBook;
    private String diveSpotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_center_detail);
        initView();
        initToolbar();
        initExtra();
        initTab();
        initRequest();
        initControl();
    }

    /*public DiveService getDiveService() {
        return newDiveService;
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NYHelper.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                triggerBook = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initControl() {

    }


    private void initExtra() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            if(intent.hasExtra(NYHelper.DIVER) &&!extras.get(NYHelper.DIVER).equals(null)){
                diver = extras.getString(NYHelper.DIVER);
            }

            if(intent.hasExtra(NYHelper.SCHEDULE) && !extras.getString(NYHelper.SCHEDULE).equals(null)){
                schedule = extras.getString(NYHelper.SCHEDULE);
            }

            if(intent.hasExtra(NYHelper.CERTIFICATE) && !extras.getString(NYHelper.CERTIFICATE).equals(null)){
                certificate = extras.getString(NYHelper.CERTIFICATE);
            }

            if(intent.hasExtra(NYHelper.DIVE_SPOT_ID) && !extras.get(NYHelper.DIVE_SPOT_ID).equals(null)){
                diveSpotId = extras.getString(NYHelper.DIVE_SPOT_ID);
            }

            if(intent.hasExtra(NYHelper.DIVE_CENTER) && !extras.getString(NYHelper.DIVE_CENTER).equals(null)){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(extras.getString(NYHelper.DIVE_CENTER));
                    diveCenter = new DiveCenter();
                    diveCenter.parse(obj);
                    if (diveCenter != null && NYHelper.isStringNotEmpty(diveCenter.getName())) nameTextView.setText(diveCenter.getName());
                    if (diveCenter != null && diveCenter.getRating() > 0){
                        ratingTextView.setText("*"+String.valueOf(diveCenter.getRating()));
                    } else {
                        ratingTextView.setText("-");
                    }
                    initBanner();
                    //Toast.makeText(this, diveService.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private void initRequest() {
        if (diveCenter == null)diveCenter = new DiveCenter();
        if (diveCenter != null && !TextUtils.isEmpty(diveCenter.getId())){
            NYDiveCenterDetailRequest req = new NYDiveCenterDetailRequest(this.getClass(), DiveCenterDetailActivity.this, diveCenter.getId());
            spcMgr.execute(req, onGetDetailDiveCenterRequest());
        }
    }

    private RequestListener<DiveCenter> onGetDetailDiveCenterRequest() {
        return new RequestListener<DiveCenter>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }*/
                NYHelper.handleAPIException(DiveCenterDetailActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveCenter results) {
                if (diveCenter == null) diveCenter = new DiveCenter();
                diveCenter = results;

                if (diveCenter != null){

                    if (NYHelper.isStringNotEmpty(diveCenter.getName())) nameTextView.setText(diveCenter.getName());
                    if (diveCenter.getRating() > 0) ratingTextView.setText(String.valueOf("*"+diveCenter.getRating()));

                    fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());
                    if (fragment != null && fragment instanceof DiveCenterListServiceFragment){

                        ((DiveCenterListServiceFragment) fragment).setContent(diveCenter);

                    } else if (fragment != null && fragment instanceof DiveCenterDetailFragment){

                        ((DiveCenterDetailFragment) fragment).setContent(diveCenter);

                    } else if (fragment != null && fragment instanceof DiveCenterMapFragment){

                        ((DiveCenterMapFragment) fragment).setContent(diveCenter);

                    }

                }

            }
        };
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
        nameTextView = (TextView) findViewById(R.id.name_textView);
        ratingTextView = (TextView) findViewById(R.id.rating_textView);
        bookingTextView = (TextView) findViewById(R.id.booking_textView);

        fragmentAdapter = new NYFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmentAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));
    }

    private void initBanner() {
        BannerList bannerList = new BannerList();
        List<Banner> banners = new ArrayList<>();
        if (diveCenter != null && diveCenter.getFeaturedImage() != null && !TextUtils.isEmpty(diveCenter.getFeaturedImage()))banners.add(new Banner("1", diveCenter.getFeaturedImage(), "captio", null));
        bannerList.setList(banners);
        //input data data
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getSupportFragmentManager());
        bannerViewPager.setAdapter(bannerViewPagerAdapter);
        circleIndicator.setViewPager(bannerViewPager);
        bannerViewPagerAdapter.setBannerList(bannerList);
        bannerViewPagerAdapter.notifyDataSetChanged();
        bannerViewPager.setOffscreenPageLimit(bannerList.getList().size());
        circleIndicator.setViewPager(bannerViewPager);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public class NYFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final int FRAGMENT_COUNT = 3;
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
                DiveCenterListServiceFragment fragment = DiveCenterListServiceFragment.newInstance();
                return fragment;
            } else if (position == 1) {
                DiveCenterDetailFragment fragment = DiveCenterDetailFragment.newInstance();
                return fragment;
            } else {
                DiveCenterMapFragment fragment = DiveCenterMapFragment.newInstance();
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


    public void movePagerToTabItemPosition(int tabItemPosition) {

        mProtectFromPagerChange = true;
        if (tabItemPosition > -1) {
            viewPager.setCurrentItem(tabItemPosition, true);
        }
        mProtectFromPagerChange = false;
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

    public class Frags {

        private int position;
        private String tag;

        public Frags(int pos, String tag) {
            this.position = pos;
            this.tag = tag;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public void closeDrawer(){
            //close navigation drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }


    private void initTab() {
        //tabMenu = getResources().getStringArray(R.array.menu_tab);
        //viewTabManager.setAlpha(0.5f);
        //NavDrawer
        setSupportActionBar(toolbar);

        viewPager.setOffscreenPageLimit(3);
        //viewPager.setAdapter(new NYFragmentPagerAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                //KTLog.e("CEK 1");
                //Toast.makeText(MainActivity.this, "test 1", Toast.LENGTH_SHORT).show();
                fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());
                if (fragment != null && fragment instanceof DiveCenterListServiceFragment){

                    ((DiveCenterListServiceFragment) fragment).setContent(diveCenter);

                } else if (fragment != null && fragment instanceof DiveCenterDetailFragment){

                    ((DiveCenterDetailFragment) fragment).setContent(diveCenter);

                } else if (fragment != null && fragment instanceof DiveCenterMapFragment){

                    ((DiveCenterMapFragment) fragment).setContent(diveCenter);

                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                //Toast.makeText(DetailServiceActivity.this, "1", Toast.LENGTH_SHORT).show();

                //KTLog.e("CEK 2");
                //Always
                //allFragments.contains(KTFragmentPagerAdapter.class.);
                /*if (stack.contains(position)) {
                    stack.pop(position);
                }*/
                //Toast.makeText(MainActivity.this, "test 2 "+String.valueOf(stack.size()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                //KTLog.e("CEK 3");

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
                ((NYHomepageDetailTabItemView) child).setDetailServiceActivity(this);
            }
        }

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
        spcMgr.start(getApplicationContext());

        setCheckedStateForTab(0, true);
        movePagerToTabItemPosition(0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

}
