package com.nyelam.android.diveservice;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.booking.BookingServiceSummaryActivity;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.CartReturn;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.EquipmentRentAdded;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.divecenter.DiveCenterDetailFragment;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.BannerViewPagerAdapter;
import com.nyelam.android.http.NYCartExpiredException;
import com.nyelam.android.http.NYDoDiveDetailServiceRequest;
import com.nyelam.android.http.NYDoDiveServiceCartRequest;
import com.nyelam.android.http.NYDoTripDetailServiceRequest;
import com.nyelam.android.http.NYServiceOutOfStockException;
import com.nyelam.android.storage.LoginStorage;
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
        DetailServiceReviewFragment.OnFragmentInteractionListener,
        DiveSpotPickerFragment.OnFragmentInteractionListener,
        DiveCenterDetailFragment.OnFragmentInteractionListener{

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
    protected String diver;
    protected String schedule;
    protected String certificate = "0";
    private TextView titleTextView, bookingTextView;
    private ImageView shareImageView, backImageView;
    private DiveCenter diveCenter;
    private ProgressDialog progressDialog;
    //private View viewTabManager;
    private boolean triggerBook;

    private int availabilityStock = 0;
    private TextView diverTextView;
    private ImageView minusImageView, plusImageView;

    // TODO: diveSpotID and Type is not used in Cart 
    private String diveSpotId;
    protected boolean isDoTrip;
    protected boolean isDoCourse;

    protected List<EquipmentRentAdded> equipmentRentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_service);
        initView();
        //initToolbar();
        initExtra();
        initTab();
        initRequest();
        initControl();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NYHelper.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                triggerBook = true;
            }
        } else {
            //Toast.makeText(this, "hallo", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initControl() {
        bookingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginStorage storage = new LoginStorage(getApplicationContext());
                if (storage.isUserLogin() && diveService.getAvailabilityStock() >= Integer.valueOf(diver) ) {
                    doBook();
                } else if (!isDoCourse && storage.isUserLogin() && diveService.getAvailabilityStock() < Integer.valueOf(diver) ) {
                    Toast.makeText(DetailServiceActivity.this, "Sorry, Dive Service stock is not available", Toast.LENGTH_SHORT).show();
                }  else {
                    Intent intent = new Intent(DetailServiceActivity.this, AuthActivity.class);
                    startActivityForResult(intent, NYHelper.LOGIN_REQ);
                }
            }
        });

        minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int diverTemp = Integer.valueOf(diver) - 1;
                if (diverTemp >= 1){
                    diver = String.valueOf(diverTemp);
                    diverTextView.setText(diver);
                }
            }
        });

        plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int diverTemp = Integer.valueOf(diver) + 1;
                if (diverTemp <= availabilityStock){
                    diver = String.valueOf(diverTemp);
                    diverTextView.setText(diver);
                }
            }
        });

        shareImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "e-Nyelam");
                    String sAux = "\ne-Nyelam - Find the Best Offer for Your Diving in Indonesia\n\n";
                    sAux = sAux + "http://e-nyelam.com/download\n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void doBook() {

        NYLog.e("CEK BOOKING : DATA -> "+newDiveService.toString());

        NYLog.e("CEK BOOKING : INIT");

        progressDialog.show();
        NYDoDiveServiceCartRequest req = null;

        String equipment = null;
        if (equipmentRentList != null) equipment = equipmentRentList.toString();

        try {
            if (!isDoCourse){
                req = new NYDoDiveServiceCartRequest(DetailServiceActivity.this, diveService.getId(), diver, schedule, newDiveService.getDiveCenter().getId(), equipment);
            } else if (isDoCourse && newDiveService.getOrganization() != null && NYHelper.isStringNotEmpty(newDiveService.getOrganization().getId())
                    && newDiveService.getLicenseType() != null && NYHelper.isStringNotEmpty(newDiveService.getLicenseType().getId())) {

                NYLog.e("CEK BOOKING : INIT 2");

                req = new NYDoDiveServiceCartRequest(DetailServiceActivity.this, diveService.getId(), diver, schedule, newDiveService.getDiveCenter().getId(), newDiveService.getOrganization().getId(), newDiveService.getLicenseType().getId(), equipment);
            }
            spcMgr.execute(req, onCreateCartServiceRequest());
        } catch (Exception e) {

            NYLog.e("CEK BOOKING : ERROR");

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            e.printStackTrace();
        }
    }

    private void initExtra() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {

            if (intent.hasExtra(NYHelper.IS_DO_TRIP)){
                isDoTrip = extras.getBoolean(NYHelper.IS_DO_TRIP);
            }

            if (intent.hasExtra(NYHelper.IS_DO_COURSE)){
                isDoCourse = extras.getBoolean(NYHelper.IS_DO_COURSE);
            }

            if (intent.hasExtra(NYHelper.DIVE_CENTER)) {
                try {
                    JSONObject obj = new JSONObject(intent.getStringExtra(NYHelper.DIVE_CENTER));
                    diveCenter = new DiveCenter();
                    diveCenter.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(intent.hasExtra(NYHelper.DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVER))){
                diver = extras.getString(NYHelper.DIVER);
                diverTextView.setText(diver);
            }

            if(intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SCHEDULE))){
                schedule = extras.getString(NYHelper.SCHEDULE);
            }

            if(intent.hasExtra(NYHelper.CERTIFICATE)  && NYHelper.isStringNotEmpty(extras.getString(NYHelper.CERTIFICATE))){
                certificate = extras.getString(NYHelper.CERTIFICATE);
            }

            if(intent.hasExtra(NYHelper.DIVE_SPOT_ID) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVE_SPOT_ID))){
                diveSpotId = extras.getString(NYHelper.DIVE_SPOT_ID);
            }

            // TODO: title (tanggal + jumlah)
            if (NYHelper.isStringNotEmpty(diver) && NYHelper.isStringNotEmpty(schedule)){

                String dateString = "";

                if (isDoCourse){
                    dateString = NYHelper.setMillisToMonthAndYear(Long.valueOf(schedule));
                } else {
                    dateString = NYHelper.setMillisToDate(Long.valueOf(schedule));
                }

                if (Integer.valueOf(diver) > 1){
                    titleTextView.setText(dateString+", "+diver+" pax(s)");
                } else {
                    titleTextView.setText(dateString+", "+diver+" pax");
                }


//                int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
//                toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
            }

            if(intent.hasExtra(NYHelper.SERVICE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SERVICE))){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(extras.getString(NYHelper.SERVICE));
                    diveService = new DiveService();
                    diveService.parse(obj);
                    initBanner();
                    //Toast.makeText(this, diveService.toString(), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }







        }

        //Toast.makeText(this, diveService.getId()+" - "+diveCenter.getId(), Toast.LENGTH_SHORT).show();
    }

    private void initRequest() {
        if (diveService != null && !TextUtils.isEmpty(diveService.getId())){
            if (isDoCourse){
                NYDoDiveDetailServiceRequest req = new NYDoDiveDetailServiceRequest(DetailServiceActivity.this, getResources().getString(R.string.api_path_docourse_detail_service), diveService.getId(), diver, certificate, schedule);
                spcMgr.execute(req, onGetDetailServiceRequest());
            } else if (isDoTrip){
                NYDoTripDetailServiceRequest req = new NYDoTripDetailServiceRequest(DetailServiceActivity.this, diveService.getId(), diver, certificate, schedule);
                spcMgr.execute(req, onGetDetailServiceRequest());
            } else {
                NYDoDiveDetailServiceRequest req = new NYDoDiveDetailServiceRequest(DetailServiceActivity.this, diveService.getId(), diver, certificate, schedule);
                spcMgr.execute(req, onGetDetailServiceRequest());
            }
        }
    }

    private RequestListener<DiveService> onGetDetailServiceRequest() {
        return new RequestListener<DiveService>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                bookingTextView.setEnabled(false);
                bookingTextView.setBackgroundResource(R.drawable.ny_book_disable);

                if(spiceException != null) {
                    if (spiceException.getCause() instanceof NYServiceOutOfStockException) {
                        NYHelper.handlePopupMessage(DetailServiceActivity.this, "Sorry, Schedule not available or out of stock", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                    } else {
                        NYHelper.handleAPIException(DetailServiceActivity.this, spiceException, false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                    }
                }

            }

            @Override
            public void onRequestSuccess(DiveService results) {

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                if (results != null){

                    bookingTextView.setEnabled(true);
                    bookingTextView.setBackgroundResource(R.drawable.ny_book);

                    if (diveService == null) diveService = new DiveService();
                    newDiveService = results;
                    diveService = newDiveService;

                    availabilityStock = diveService.getAvailabilityStock();
                    //Toast.makeText(DetailServiceActivity.this, String.valueOf(availabilityStock), Toast.LENGTH_SHORT).show();

                    List<DiveSpot> diveSpots = newDiveService.getDiveSpots();

                    if(diveSpots != null && !diveSpots.isEmpty() && diveSpots.size() == 1) {
                        diveSpotId = newDiveService.getDiveSpots().get(0).getId();
                    }

                    fragment =  fragmentAdapter.getFragment(viewPager.getCurrentItem());
                    if (fragment != null && fragment instanceof DetailServiceFragment){

                        ((DetailServiceFragment) fragment).setContent();

                    } else if (fragment != null && fragment instanceof DetailServiceDiveSpotsFragment){

                        ((DetailServiceDiveSpotsFragment) fragment).setDiveSpot();

                    } else if (fragment != null && fragment instanceof DetailServiceDiveCenterFragment){

                        ((DetailServiceDiveCenterFragment) fragment).setDiveCenter();

                    } else if (fragment != null && fragment instanceof DetailServiceReviewFragment){
                        //Toast.makeText(DetailServiceActivity.this, fragment.getClass().getName(), Toast.LENGTH_SHORT).show();
                    }
                    initBanner();
                } else {
                    NYHelper.handlePopupMessage(DetailServiceActivity.this, "Sorry, Schedule not available or out of stock", false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                }
            }
        };
    }



    private RequestListener<CartReturn> onCreateCartServiceRequest() {
        return new RequestListener<CartReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                NYHelper.handleAPIException(DetailServiceActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(CartReturn cartReturn) {

                //NYLog.e("CEK CART RETURN : "+cartReturn.toString());

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                //Intent intent = new Intent(DetailServiceActivity.this, BookingServiceActivity.class);
                Intent intent = new Intent(DetailServiceActivity.this, BookingServiceSummaryActivity.class);
                //intent.putExtra(NYHelper.CART_TOKEN, cartReturn.getCartToken());
                intent.putExtra(NYHelper.CART_RETURN, cartReturn.toString());
                intent.putExtra(NYHelper.SERVICE, newDiveService.toString());
                intent.putExtra(NYHelper.DIVER, diver);
                intent.putExtra(NYHelper.SCHEDULE, schedule);
                intent.putExtra(NYHelper.CERTIFICATE, certificate);
                intent.putExtra(NYHelper.DIVE_CENTER, newDiveService.getDiveCenter().toString());
                intent.putExtra(NYHelper.IS_DO_COURSE, isDoCourse);
                if (equipmentRentList != null && !equipmentRentList.isEmpty()){
                    NYLog.e("EQUIPMENT SERVICE EXIST!");
                    intent.putExtra(NYHelper.EQUIPMENT_RENT, equipmentRentList.toString());
                } else {
                    NYLog.e("EQUIPMENT SERVICE NOT EXIST!");

                }
                startActivity(intent);
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
        titleTextView = (TextView) findViewById(R.id.title_textView);
        bookingTextView = (TextView) findViewById(R.id.booking_textView);
        shareImageView = (ImageView) findViewById(R.id.share_imageView);
        backImageView = (ImageView) findViewById(R.id.back_imageView);

        diverTextView = (TextView) findViewById(R.id.diver_textView);
        minusImageView = (ImageView) findViewById(R.id.minus_imageView);
        plusImageView = (ImageView) findViewById(R.id.plus_imageView);

        fragmentAdapter = new NYFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setPagingEnabled(false);
        viewPager.setAdapter(fragmentAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));

        bookingTextView.setEnabled(false);
        bookingTextView.setBackgroundResource(R.drawable.ny_book_disable);
    }

    private void initBanner() {
        BannerList bannerList = new BannerList();
        List<Banner> banners = new ArrayList<>();
        if (diveService != null && diveService.getFeaturedImage() != null && !TextUtils.isEmpty(diveService.getFeaturedImage()))banners.add(new Banner("1", diveService.getFeaturedImage(), "captio", null));
        bannerList.setList(banners);
        if(diveService != null && diveService.getImages() != null && !diveService.getImages().isEmpty()) {
            int i = 1;
            for(String image: diveService.getImages()) {
                banners.add(new Banner(String.valueOf(i), image, "gallery", null));
                i++;
            }
        }
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
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    @Override
    public void onDiveSpotChoosen(String diveSpotId) {
        this.diveSpotId = diveSpotId;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doBook();
            }
        },500);
    }

    public class NYFragmentPagerAdapter extends FragmentPagerAdapter {
        private static final int FRAGMENT_COUNT = 1;
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
            } else {
                DetailServiceReviewFragment fragment = DetailServiceReviewFragment.newInstance();
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

                    ((DetailServiceFragment) fragment).setContent();

                } else if (fragment != null && fragment instanceof DetailServiceDiveSpotsFragment){

                    ((DetailServiceDiveSpotsFragment) fragment).setDiveSpot();

                } else if (fragment != null && fragment instanceof DetailServiceDiveCenterFragment){

                    ((DetailServiceDiveCenterFragment) fragment).setDiveCenter();

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
        if(triggerBook) {
            triggerBook = false;
            doBook();
        }
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

    public void openDiveSpotPickerDialog() {
        List<DiveSpot> diveSpots = newDiveService.getDiveSpots();
        if (diveSpots == null || diveSpots.isEmpty()) {
            NYHelper.handleErrorMessage(this, "Dive Spot is empty");
            return;
        }
        DiveSpotPickerFragment fragment = DiveSpotPickerFragment.newInstance();
        fragment.show(getSupportFragmentManager().beginTransaction(), fragment.getTag());
    }

    protected DiveService getDiveService(){
        if (newDiveService != null){
            return newDiveService;
        } else {
            return diveService;
        }
    }

}
