package com.nyelam.android.divecenter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.Coordinate;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.Location;
import com.nyelam.android.divespot.DiveSpotDetailActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.home.BannerViewPagerAdapter;
import com.nyelam.android.http.NYDoDiveCenterDetailRequest;
import com.nyelam.android.http.NYDoDiveSearchServiceRequest;
import com.nyelam.android.view.NYBannerViewPager;
import com.nyelam.android.view.NYCustomViewPager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

public class DiveCenterDetailActivity extends AppCompatActivity implements
        DiveCenterDetailFragment.OnFragmentInteractionListener,
        DiveCenterMapFragment.OnFragmentInteractionListener,
        DiveCenterListServiceFragment.OnFragmentInteractionListener,
        OnMapReadyCallback {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private NYBannerViewPager bannerViewPager;
    private CircleIndicator circleIndicator;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private Toolbar toolbar;
    private NYCustomViewPager viewPager;
    private String serviceId;
    private int page = 1;
    private List<DiveService> serviceList;

    protected DiveCenter diveCenter;
    protected String diver;
    protected String certificate;
    protected String schedule;
    private ProgressDialog progressDialog;
    private boolean triggerBook;
    private String diveSpotId;
    protected String type;
    protected String diverId = "";
    private boolean ecoTrip = false;

    private DoDiveSearchServiceAdapter serviceAdapter;
    private RecyclerView recyclerView;
    private RatingBar ratingBar;
    private TextView titleTextView, nameTextView, ratingTextView, visitedTextView, phoneNumberTextView, locationTextView, descriptionTextView, noResultTextView;

    private GoogleMap map;
    private LinearLayout mainLinearLayout;
    private ProgressBar mainProgressBar, serviceProgressBar;
    private TextView noLocationTextView, openMapTextView;
    private SupportMapFragment mapFragment;
    private LinearLayout mapLinearLayout, phoneNumberLinearLayout;

    public boolean isEcoTrip() {
        return ecoTrip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dive_center_detail);
        initView();
        initMap();
        initToolbar();
        initExtra();
        initAdapter();
        initRequest();
        initControl();
    }

    private void initControl() {

        // TODO: call  dan map - deskripsi dan visited dan gambar belum

        phoneNumberLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (diveCenter != null && diveCenter.getContact() != null && NYHelper.isStringNotEmpty(diveCenter.getContact().getPhoneNumber())){

                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + diveCenter.getContact().getPhoneNumber()));
                    if (ActivityCompat.checkSelfPermission(DiveCenterDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(intent);

                }


            }
        });



        openMapTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (diveCenter != null && NYHelper.isStringNotEmpty(diveCenter.getName()) && diveCenter.getContact() != null && diveCenter.getContact().getLocation() != null &&
                        diveCenter.getContact().getLocation().getCoordinate() != null){
                    Coordinate coordinate = diveCenter.getContact().getLocation().getCoordinate();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<"+coordinate.getLat()+">,<"+coordinate.getLon()+">?q=<"+coordinate.getLat()+">,<"+coordinate.getLon()+">("+diveCenter.getName()+")"));
                    startActivity(intent);

                }
            }
        });

    }

    private void initMap() {
        mapFragment.getMapAsync(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
        params.height = (int)(width*0.7);
        mapFragment.getView().setLayoutParams(params);
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        serviceList = new ArrayList<>();

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,spacingInPixels,0,spacingInPixels));
        serviceAdapter = new DoDiveSearchServiceAdapter(this, diver, schedule, certificate, diveCenter);
        recyclerView.setAdapter(serviceAdapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NYHelper.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                triggerBook = true;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initExtra() {

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.IS_ECO_TRIP)) {
                ecoTrip = true;
            }
            if(intent.hasExtra(NYHelper.DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVER))){
                diver = extras.getString(NYHelper.DIVER);
            }

            if(intent.hasExtra(NYHelper.ID_DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.ID_DIVER))){
                diverId = extras.getString(NYHelper.ID_DIVER);
            }

            if(intent.hasExtra(NYHelper.TYPE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.TYPE))){
                type = extras.getString(NYHelper.TYPE);
            }

            if(intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SCHEDULE))){
                schedule = extras.getString(NYHelper.SCHEDULE);
            }

            if(intent.hasExtra(NYHelper.CERTIFICATE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.CERTIFICATE))){
                certificate = extras.getString(NYHelper.CERTIFICATE);
            }

            if(intent.hasExtra(NYHelper.DIVE_SPOT_ID) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVE_SPOT_ID))){
                diveSpotId = extras.getString(NYHelper.DIVE_SPOT_ID);
            }

            // TODO: title (tanggal + jumlah)
            if (NYHelper.isStringNotEmpty(diver) && NYHelper.isStringNotEmpty(schedule)){
                if (Integer.valueOf(diver) > 1){
                    titleTextView.setText(NYHelper.setMillisToDate(Long.valueOf(schedule))+", "+diver+" pax(s)");
                } else {
                    titleTextView.setText(NYHelper.setMillisToDate(Long.valueOf(schedule))+", "+diver+" pax");
                }
                int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
                toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
            }

            if(intent.hasExtra(NYHelper.DIVE_CENTER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVE_CENTER))){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(extras.getString(NYHelper.DIVE_CENTER));
                    diveCenter = new DiveCenter();
                    diveCenter.parse(obj);
                    if (NYHelper.isStringNotEmpty(diveCenter.getName())) nameTextView.setText(diveCenter.getName());
                    if(!TextUtils.isEmpty(diveCenter.getDescription())) {
                        descriptionTextView.setText(diveCenter.getDescription());
                    } else {
                        descriptionTextView.setText("-");
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
            NYDoDiveCenterDetailRequest req = new NYDoDiveCenterDetailRequest(this.getClass(), DiveCenterDetailActivity.this, diveCenter.getId());
            spcMgr.execute(req, onGetDetailDiveCenterRequest());
        }
    }

    private RequestListener<DiveCenter> onGetDetailDiveCenterRequest() {
        return new RequestListener<DiveCenter>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (mainProgressBar != null) {
                    mainProgressBar.setVisibility(View.GONE);
                }
                NYHelper.handleAPIException(DiveCenterDetailActivity.this, spiceException, null);
                //noResultTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(DiveCenter results) {
                if (mainProgressBar != null) {
                    mainProgressBar.setVisibility(View.GONE);
                }

                if (diveCenter == null) diveCenter = new DiveCenter();
                diveCenter = results;

                if (diveCenter != null){

                    mainLinearLayout.setVisibility(View.VISIBLE);

                    getServiceList();

                    if (NYHelper.isStringNotEmpty(diveCenter.getName()))nameTextView.setText(diveCenter.getName());
                    ratingBar.setRating(diveCenter.getRating());
                    ratingTextView.setText(String.valueOf(diveCenter.getRating()));
                    //visitedTextView.setText(diveCenter.getName());

                    if (diveCenter.getContact() != null){
                        Contact contact = diveCenter.getContact();
                        if (NYHelper.isStringNotEmpty(contact.getPhoneNumber())){
                            phoneNumberTextView.setText(contact.getPhoneNumber());
                        } else{
                            phoneNumberTextView.setText("-");
                        }

                        if (contact.getLocation()!=null){
                            Location location = contact.getLocation();
                            String locationText = "";
                            if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locationText=locationText+location.getCity().getName();
                            if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locationText=locationText+", "+location.getProvince().getName();
                            if (location.getCountry() != null && NYHelper.isStringNotEmpty(location.getCountry())) locationText=locationText+", "+location.getCountry();
                            locationTextView.setText(locationText);
                        } else {
                            locationTextView.setText("-");
                        }
                    }


                }

            }
        };
    }



    protected void getServiceList() {

        if (diveCenter != null && !TextUtils.isEmpty(diveCenter.getId())
                && !TextUtils.isEmpty(diver)
                && !TextUtils.isEmpty(certificate)){

            String apiPath = getString(R.string.api_path_dodive_service_list);
            if (NYHelper.isStringNotEmpty(type) && type.equals("1")){
                apiPath = getString(R.string.api_path_dodive_service_list_by_divespot);
            } else if (NYHelper.isStringNotEmpty(type) && type.equals("2")){
                apiPath = getString(R.string.api_path_dodive_service_list_by_category);
            }

            if(isEcoTrip()) {
                NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(this, apiPath, String.valueOf(page), diveCenter.getId(), certificate, diver, schedule, type, diverId, "1");
                spcMgr.execute(req, onGetServiceByDiveCenterRequest());
            } else {
                NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(this, apiPath, String.valueOf(page), diveCenter.getId(), certificate, diver, schedule, type, diverId);
                spcMgr.execute(req, onGetServiceByDiveCenterRequest());
            }

        }
    }

    private RequestListener<DiveServiceList> onGetServiceByDiveCenterRequest() {
        return new RequestListener<DiveServiceList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (serviceProgressBar != null)serviceProgressBar.setVisibility(View.GONE);
                //NYHelper.handleAPIException(.this, spiceException, null);
                noResultTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(DiveServiceList results) {

                if (serviceProgressBar != null)serviceProgressBar.setVisibility(View.GONE);

                if (serviceList == null) serviceList = new ArrayList<>();
                if (results != null && results.getList().size() > 0){
                    serviceList = results.getList();
                    serviceAdapter.addResults(serviceList);
                    serviceAdapter.notifyDataSetChanged();
                    noResultTextView.setVisibility(View.GONE);
                } else {
                    noResultTextView.setVisibility(View.VISIBLE);
                }

            }
        };
    }



    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (NYCustomViewPager) findViewById(R.id.view_pager);
        bannerViewPager = (NYBannerViewPager) findViewById(R.id.promotion_view_pager);
        circleIndicator = (CircleIndicator) findViewById(R.id.circle_indicator);
        mainProgressBar = (ProgressBar) findViewById(R.id.main_progress_bar);
        serviceProgressBar = (ProgressBar) findViewById(R.id.service_progress_bar);
        mainLinearLayout = (LinearLayout) findViewById(R.id.main_linearLayout);

        titleTextView = (TextView) findViewById(R.id.title_textView);
        nameTextView = (TextView) findViewById(R.id.name_textView);
        ratingTextView = (TextView) findViewById(R.id.rating_textView);
        visitedTextView = (TextView) findViewById(R.id.visited_textView);
        phoneNumberTextView = (TextView) findViewById(R.id.phone_number_textView);
        locationTextView = (TextView) findViewById(R.id.location_textView);
        descriptionTextView = (TextView) findViewById(R.id.description_textView);
        openMapTextView  = (TextView) findViewById(R.id.open_map_textView);
        noResultTextView  = (TextView) findViewById(R.id.no_result_textView);

        recyclerView  = (RecyclerView) findViewById(R.id.recyclerView);
        ratingBar  = (RatingBar) findViewById(R.id.ratingBar);
        phoneNumberLinearLayout  = (LinearLayout) findViewById(R.id.phone_number_linearLayout);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.loading));


        noLocationTextView = (TextView) findViewById(R.id.no_location_textView);
        openMapTextView = (TextView) findViewById(R.id.open_map_textView);
        mapLinearLayout  = (LinearLayout) findViewById(R.id.map_linearLayout);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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

    @Override
    protected void onStart() {
        super.onStart();
        spcMgr.start(getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        // Enable MyLocation Button in the Map
        if (ActivityCompat.checkSelfPermission(DiveCenterDetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setZoomGesturesEnabled(false);

        // Already two locations
        map.clear();
        // Adding new item to the ArrayList
        if (diveCenter != null){

            LatLng pos2 = new LatLng(0, 0);

            if (diveCenter.getContact() != null && diveCenter.getContact().getLocation() != null && diveCenter.getContact().getLocation().getCoordinate() != null){
                double lat = diveCenter.getContact().getLocation().getCoordinate().getLat();
                double lon = diveCenter.getContact().getLocation().getCoordinate().getLon();
                pos2 = new LatLng(lat,lon);
            }
            //center camera map
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos2, 12.0f));

            // Creating MarkerOptions
            BitmapDescriptor iconPin = BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_map);

            String name = "";
            String address = "";

            if (NYHelper.isStringNotEmpty(diveCenter.getName())) name = diveCenter.getName();
            if (diveCenter.getContact() != null && diveCenter.getContact().getLocation() != null){
                Location loc = diveCenter.getContact().getLocation();
                if (loc.getCity() != null && NYHelper.isStringNotEmpty(loc.getCity().getName())) address += loc.getCity().getName();
                if (loc.getProvince() != null && NYHelper.isStringNotEmpty(loc.getCity().getName())) address += ", "+loc.getProvince().getName();
                if (NYHelper.isStringNotEmpty(loc.getCountry())) address += ", "+loc.getCountry();
            }

            MarkerOptions optionsClient = new MarkerOptions().position(pos2)
                    .title(name)
                    .snippet(address)
                    .icon(iconPin);



            // Add new marker to the Google Map Android API V2
            Marker marker = map.addMarker(optionsClient);
            marker.showInfoWindow();

            //noLocationTextView.setVisibility(View.GONE);
            //progressBar.setVisibility(View.GONE);
            mapLinearLayout.setVisibility(View.VISIBLE);
        } else {
            //progressBar.setVisibility(View.GONE);
            //noLocationTextView.setVisibility(View.VISIBLE);
            mapLinearLayout.setVisibility(View.VISIBLE);
        }
    }
}
