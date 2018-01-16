package com.nyelam.android.detail;

import android.content.Intent;
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
import com.nyelam.android.booking.BookingServiceActivity;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.BannerViewPagerAdapter;
import com.nyelam.android.http.NYDoDiveDetailServiceRequest;
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

public class DetailServiceActivity extends AppCompatActivity implements
        DetailServiceFragment.OnFragmentInteractionListener,
        DetailServiceDiveSpotsFragment.OnFragmentInteractionListener,
        DetailServiceDiveCenterFragment.OnFragmentInteractionListener,
        DetailServiceReviewFragment.OnFragmentInteractionListener{

    private List<DetailServiceActivity.Frags> tags = Arrays.asList(new DetailServiceActivity.Frags(0,"home"), new DetailServiceActivity.Frags(1,"timeline"), new DetailServiceActivity.Frags(2,"interest"), new DetailServiceActivity.Frags(3,"tags"), new DetailServiceActivity.Frags(4,"more"));
    //private List<DetailServiceActivity.Frags> fragses = new ArrayList<>();

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
    protected DiveService diveService, newDiveService;
    private TextView nameTextView, ratingTextView, bookingTextView;
    //private View viewTabManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);
        initView();
        initToolbar();
        initExtra();
        initTab();
        initRequest();
        initControl();
    }

    private void initControl() {
        bookingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailServiceActivity.this, BookingServiceActivity.class);
                intent.putExtra(NYHelper.SERVICE, diveService.toString());
                startActivity(intent);
            }
        });
    }

    private void initExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            /*if(!extras.getString(NYHelper.ID_SERVICE).equals(null)){
                serviceId = extras.getString(NYHelper.ID_SERVICE);
            }*/

            if(!extras.getString(NYHelper.SERVICE).equals(null)){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(extras.getString(NYHelper.SERVICE));
                    diveService = new DiveService();
                    diveService.parse(obj);
                    if (diveService != null && NYHelper.isStringNotEmpty(diveService.getName())) nameTextView.setText(diveService.getName());
                    if (diveService != null && diveService.getRating() > 0){
                        ratingTextView.setText("*"+String.valueOf(diveService.getRating()));
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
        if (diveService != null && !TextUtils.isEmpty(diveService.getId())){
            NYDoDiveDetailServiceRequest req = new NYDoDiveDetailServiceRequest(DetailServiceActivity.this, diveService.getId());
            spcMgr.execute(req, onGetDetailServiceRequest());
        }
    }

    private RequestListener<DiveService> onGetDetailServiceRequest() {
        return new RequestListener<DiveService>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }*/
                NYHelper.handleAPIException(DetailServiceActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveService results) {

                if (diveService == null) diveService = new DiveService();
                newDiveService = results;

                fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());
                if (fragment != null && fragment instanceof DetailServiceFragment){

                    ((DetailServiceFragment) fragment).setContent(newDiveService);

                } else if (fragment != null && fragment instanceof DetailServiceDiveSpotsFragment){

                    ((DetailServiceDiveSpotsFragment) fragment).setDiveSpot(newDiveService);

                } else if (fragment != null && fragment instanceof DetailServiceDiveCenterFragment){

                    ((DetailServiceDiveCenterFragment) fragment).setDiveCenter(newDiveService);

                } else if (fragment != null && fragment instanceof DetailServiceReviewFragment){
                    //Toast.makeText(DetailServiceActivity.this, fragment.getClass().getName(), Toast.LENGTH_SHORT).show();
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
    }

    private void initBanner() {
        BannerList bannerList = new BannerList();
        List<Banner> banners = new ArrayList<>();
        if (diveService != null && diveService.getFeaturedImage() != null && !TextUtils.isEmpty(diveService.getFeaturedImage()))banners.add(new Banner("1", diveService.getFeaturedImage(), "captio", null));
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
        private static final int FRAGMENT_COUNT = 4;
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
                DetailServiceFragment fragment = DetailServiceFragment.newInstance();
                return fragment;
            } else if (position == 1) {
                DetailServiceDiveSpotsFragment fragment = DetailServiceDiveSpotsFragment.newInstance().newInstance();
                return fragment;
            }  else if (position == 2) {
                DetailServiceDiveCenterFragment fragment = DetailServiceDiveCenterFragment.newInstance().newInstance();
                return fragment;
            } else {
                DetailServiceReviewFragment fragment = DetailServiceReviewFragment.newInstance().newInstance();
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

        viewPager.setOffscreenPageLimit(4);
        //viewPager.setAdapter(new NYFragmentPagerAdapter(getSupportFragmentManager()));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
                //KTLog.e("CEK 1");
                //Toast.makeText(MainActivity.this, "test 1", Toast.LENGTH_SHORT).show();
                fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());
                if (fragment != null && fragment instanceof DetailServiceFragment){

                    ((DetailServiceFragment) fragment).setContent(newDiveService);

                } else if (fragment != null && fragment instanceof DetailServiceDiveSpotsFragment){

                    ((DetailServiceDiveSpotsFragment) fragment).setDiveSpot(newDiveService);

                } else if (fragment != null && fragment instanceof DetailServiceDiveCenterFragment){

                    ((DetailServiceDiveCenterFragment) fragment).setDiveCenter(newDiveService);

                } else if (fragment != null && fragment instanceof DetailServiceFragment){
                    //Toast.makeText(DetailServiceActivity.this, fragment.getClass().getName(), Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        spcMgr.start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

}
