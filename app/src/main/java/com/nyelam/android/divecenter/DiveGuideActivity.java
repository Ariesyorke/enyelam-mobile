package com.nyelam.android.divecenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveGuide;
import com.nyelam.android.data.DiveGuideList;
import com.nyelam.android.data.Location;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.diveservice.DetailServiceActivity;
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

import de.hdodenhof.circleimageview.CircleImageView;
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
    private TextView diveGuideNameTextView;
    private TextView diveGuideLicenseTextView;
    private CircleImageView userImageView;
    private boolean isStillLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_guide);
        diveGuide = new DiveGuide();
        initView();
        initExtra();
        initToolbar();
        initTab();
        initRequest();
    }


    private void initExtra() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            if(intent.hasExtra(NYHelper.DIVE_GUIDE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVE_GUIDE))){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(extras.getString(NYHelper.DIVE_GUIDE));
                    diveGuide = new DiveGuide();
                    diveGuide.parse(obj);

                    if (diveGuide != null){
                        if (NYHelper.isStringNotEmpty(diveGuide.getFullName())) diveGuideNameTextView.setText(diveGuide.getFullName());

                        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
                        if (NYHelper.isStringNotEmpty(diveGuide.getPicture())) {

                            ImageLoader.getInstance().loadImage(diveGuide.getPicture(), NYHelper.getOption(), new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    userImageView.setImageResource(R.mipmap.ic_launcher);
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    userImageView.setImageBitmap(loadedImage);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {
                                    userImageView.setImageResource(R.mipmap.ic_launcher);
                                }
                            });

                            ImageLoader.getInstance().displayImage(diveGuide.getPicture(), userImageView, NYHelper.getOption());

                        } else {
                            userImageView.setImageResource(R.mipmap.ic_launcher);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
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
        diveGuideNameTextView = (TextView) findViewById(R.id.dive_guide_name_textView);
        diveGuideLicenseTextView = (TextView) findViewById(R.id.dive_guide_license_textView);
        userImageView = (CircleImageView) findViewById(R.id.user_imageView);

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
                    ((DiveGuideBioFragment) fragment).setContentNew();

                } else if (fragment != null && fragment instanceof DiveGuideAboutFragment){

                    // TODO: buat metode
                    ((DiveGuideAboutFragment) fragment).setContentNew();

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

        //loadDummyDiveGuideDetail();

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
        if (diveGuide != null && NYHelper.isStringNotEmpty(diveGuide.getId())){
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

                if (diveGuide.getCertificateDiver() != null && NYHelper.isStringNotEmpty(diveGuide.getCertificateDiver().getName()))
                    diveGuideLicenseTextView.setText(diveGuide.getCertificateDiver().getName());

                //fragmentAdapter.notifyDataSetChanged();

//                fragmentAdapter = new NYFragmentPagerAdapter(getSupportFragmentManager());
//                viewPager.setAdapter(fragmentAdapter);


                fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());

                if (fragment != null && fragment instanceof DiveGuideBioFragment){

                    // TODO: buat metode
                    ((DiveGuideBioFragment) fragment).setContentNew();

                } else if (fragment != null && fragment instanceof DiveGuideAboutFragment){

                    // TODO: buat metode
                    ((DiveGuideAboutFragment) fragment).setContentNew();

                }


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


    protected DiveGuide getDiveGuide(){
        return this.diveGuide;
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
            if (NYHelper.isStringNotEmpty(diveGuide.getFullName()))diveGuideNameTextView.setText(diveGuide.getFullName());
            if (diveGuide.getCertificateDiver() != null && NYHelper.isStringNotEmpty(diveGuide.getCertificateDiver().getName()))
                diveGuideLicenseTextView.setText(diveGuide.getCertificateDiver().getName());
            NYLog.e("CEK DIVE GUIDE : "+diveGuide.toString());
        } else {
            NYLog.e("CEK DIVE GUIDE : null");
        }

        isStillLoad = false;

        fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());

        if (fragment != null && fragment instanceof DiveGuideBioFragment){

            // TODO: buat metode
            ((DiveGuideBioFragment) fragment).setContentNew();

        } else if (fragment != null && fragment instanceof DiveGuideAboutFragment){

            // TODO: buat metode
            ((DiveGuideAboutFragment) fragment).setContentNew();

        }



//        fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());
//
//        Toast.makeText(this, "frag : "+String.valueOf(viewPager.getCurrentItem()), Toast.LENGTH_SHORT).show();
//
//        if (fragment != null && fragment instanceof DiveGuideBioFragment){
//
//            // TODO: buat metode
//            ((DiveGuideBioFragment) fragment).setContent(this.diveGuide);
//
//        } else if (fragment != null && fragment instanceof DiveGuideAboutFragment){
//
//            // TODO: buat metode
//            ((DiveGuideAboutFragment) fragment).setContent(this.diveGuide);
//
//        }


    }

}
