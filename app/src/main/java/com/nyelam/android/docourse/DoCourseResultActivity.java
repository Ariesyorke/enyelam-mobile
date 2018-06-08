package com.nyelam.android.docourse;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.CategoryList;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.LicenseType;
import com.nyelam.android.data.Organization;
import com.nyelam.android.data.Price;
import com.nyelam.android.data.StateFacility;
import com.nyelam.android.data.StateFacilityList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.dodive.DoDiveSearchDiveServiceAdapter;
import com.nyelam.android.dodive.DoDiveSearchResultActivity;
import com.nyelam.android.dodive.FilterListServiceActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoCourseSearchResultRequest;
import com.nyelam.android.http.NYDoDiveSearchServiceResultRequest;
import com.nyelam.android.http.NYGetMinMaxPriceRequest;
import com.nyelam.android.http.result.NYPaginationResult;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DoCourseResultActivity extends BasicActivity implements NYCustomDialog.OnDialogFragmentClickListener {

    private int mRequestCode = 100;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoCourseSearchAdapter adapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView titleTextView, labelTextView, noResultTextView;
    private Organization organization;
    private LicenseType licenseType;
    private String isOpenWater = null;
    protected String keyword, diverId, diver, certificate, date, type;
    private ImageView searchImageView;
    private int page = 1;
    private boolean ecotrip = false;
    private int sortingType = 2;
    private double minPrice=-1, maxPrice=-1;
    private double minPriceDefault, maxPriceDefault;
    private List<String> totalDives;
    private CategoryList categoryList;
    private StateFacilityList stateFacilityList;
    private SwipeRefreshLayout swipeLayout;
    private TextView filterTextView;
    private LinearLayout filterLinearLayout;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_course_result);
        //Toast.makeText(this, "DoCourse 1", Toast.LENGTH_SHORT).show();
        initView();
        initExtra();
        initAdapter();
        //initRequest();
        //Toast.makeText(this, "DoCourse 2", Toast.LENGTH_SHORT).show();
        // TODO: hapus ini
        //requestPriceRange(false);
        initRequest(false);

        //initToolbar();
        initControl();
        initToolbar(true);
    }

    private void initControl() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {

                // TODO: load more ada bug !

                initRequest(false);
                /*if (loading) {
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;

                            Log.v("...", " Reached Last Item");
                            loadMoreVideos(searchVideos);
                        }

                    }
                }*/
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //endlessScroll.resetValue();
                //checkNewEvents();
                page=1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                // TODO: hapus ini
                //requestPriceRange(true);
                initRequest(true);
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoCourseResultActivity.this, DoDiveActivity.class);
                startActivity(intent);
            }
        });

        /*filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoDiveSearchResultActivity.this, FilterListServiceActivity.class);
                *//*intent.putExtra(NYHelper.ACTIVITY, this.getClass().getName());
                intent.putExtra(NYHelper.KEYWORD, keyword);
                intent.putExtra(NYHelper.ID_DIVER, diverId);
                intent.putExtra(NYHelper.DIVER, diver);
                intent.putExtra(NYHelper.CERTIFICATE, certificate);
                intent.putExtra(NYHelper.SCHEDULE, date);
                intent.putExtra(NYHelper.TYPE, type);
                intent.putExtra(NYHelper.IS_ECO_TRIP, ecotrip);
                intent.putStringArrayListExtra(NYHelper.CATEGORIES, categories);*//*
                //startActivity(intent);
                // TODO: kirim parameter ke filter
                intent.putExtra(NYHelper.CATEGORIES, categoryList.toString());
                intent.putExtra(NYHelper.FACILITIES, stateFacilityList.toString());
                intent.putExtra(NYHelper.TOTAL_DIVES, totalDives.toString());
                intent.putExtra(NYHelper.SORT_BY, sortingType);
                intent.putExtra(NYHelper.MIN_PRICE, minPrice);
                intent.putExtra(NYHelper.MIN_PRICE_DEAFULT, minPriceDefault);
                intent.putExtra(NYHelper.MAX_PRICE, maxPrice);
                intent.putExtra(NYHelper.MAX_PRICE_DEFAULT, maxPriceDefault);
                startActivityForResult(intent, mRequestCode);
            }
        });*/

        filterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoCourseResultActivity.this, FilterListServiceActivity.class);
                // TODO: kirim parameter ke filter
                // TODO: kirim parameter ke filter
                if(categoryList != null && categoryList.getList() != null && !categoryList.getList().isEmpty()) {
                    intent.putExtra(NYHelper.CATEGORIES, categoryList.getList().toString());
                }
                if(stateFacilityList != null && stateFacilityList.getList() != null && !stateFacilityList.getList().isEmpty()) {
                    intent.putExtra(NYHelper.FACILITIES, stateFacilityList.getList().toString());
                }
                intent.putExtra(NYHelper.TOTAL_DIVES, totalDives.toString());
                intent.putExtra(NYHelper.SORT_BY, sortingType);
                intent.putExtra(NYHelper.MIN_PRICE, minPrice);
                intent.putExtra(NYHelper.MIN_PRICE_DEAFULT, minPriceDefault);
                intent.putExtra(NYHelper.MAX_PRICE, maxPrice);
                intent.putExtra(NYHelper.MAX_PRICE_DEFAULT, maxPriceDefault);
                startActivityForResult(intent, mRequestCode);
            }
        });
    }

    private void initRequest(boolean isRefresh) {
        noResultTextView.setVisibility(View.GONE);
        //progressBar.setVisibility(View.VISIBLE);

        String apiPath = getString(R.string.api_path_docourse_search_service_by_province);

        if (NYHelper.isStringNotEmpty(type) && type.equals("1")){
            apiPath = getString(R.string.api_path_docourse_search_service_by_divespot);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("2")){
            apiPath = getString(R.string.api_path_docourse_search_service_by_category);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("3")){
            apiPath = getString(R.string.api_path_docourse_search_service_by_divecenter);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("5")){
            apiPath = getString(R.string.api_path_docourse_search_service_by_province);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("6")){
            apiPath = getString(R.string.api_path_docourse_search_service_by_city);
        }

        /*if(isEcoTrip()) {
            NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(this, apiPath, String.valueOf(page), diveCenter.getId(), certificate, diver, schedule, type, diverId, "1");
            spcMgr.execute(req, onGetServiceByDiveCenterRequest());
        } else {
            NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(this, apiPath, String.valueOf(page), diveCenter.getId(), certificate, diver, schedule, type, diverId);
            spcMgr.execute(req, onGetServiceByDiveCenterRequest());
        }*/

        List<Category> lsCategory = categoryList.getList();
        List<StateFacility> lsFacilities = stateFacilityList.getList();

        String organizationId = null;
        String licenseTypeId = null;

        if (organization != null && NYHelper.isStringNotEmpty(organization.getId())) organizationId = organization.getId();
        if (licenseType != null && NYHelper.isStringNotEmpty(licenseType.getId())) licenseTypeId = licenseType.getId();

        // TODO: tunggu URL dari Adam
        NYDoCourseSearchResultRequest req = new NYDoCourseSearchResultRequest(this, apiPath, String.valueOf(page), diverId, type, diver, certificate, date, String.valueOf(sortingType), lsCategory, lsFacilities, minPrice, maxPrice, organizationId, licenseTypeId, null, isOpenWater);
        spcMgr.execute(req, onSearchServiceRequest(isRefresh));

        // TODO: load data dummy, to test and waitting for API request
        //loadJSONAsset();
    }

    private void initExtra() {
        totalDives = new ArrayList<String>();
        categoryList = new CategoryList();
        stateFacilityList = new StateFacilityList();
        stateFacilityList = new StateFacilityList();
        organization = new Organization();
        licenseType = new LicenseType();

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.KEYWORD) && !extras.getString(NYHelper.KEYWORD).equals(null)){
                keyword = extras.getString(NYHelper.KEYWORD);
                //titleTextView.setText(keyword);
            }
            if(intent.hasExtra(NYHelper.ID_DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.ID_DIVER))) diverId = extras.getString(NYHelper.ID_DIVER);
            if(intent.hasExtra(NYHelper.DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVER))) diver = extras.getString(NYHelper.DIVER);
            if(intent.hasExtra(NYHelper.CERTIFICATE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.CERTIFICATE))) certificate = extras.getString(NYHelper.CERTIFICATE);
            if(intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SCHEDULE))) date = extras.getString(NYHelper.SCHEDULE);
            if(intent.hasExtra(NYHelper.TYPE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.TYPE))){
                type = extras.getString(NYHelper.TYPE);
            }

            if(intent.hasExtra(NYHelper.ORGANIZATION)){
                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.ORGANIZATION));
                    organization.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if(intent.hasExtra(NYHelper.LICENSE_TYPE)){
                try {
                    JSONObject obj = new JSONObject(extras.getString(NYHelper.LICENSE_TYPE));
                    licenseType.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            titleTextView.setText(NYHelper.setMillisToDate(Long.valueOf(date))+", "+diver+" pax (s)");
        }

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(spacingInPixels));

        adapter = new DoCourseSearchAdapter(this, diver, date, certificate, type, diverId);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        labelTextView = (TextView) findViewById(R.id.label_textView);
        titleTextView = (TextView) findViewById(R.id.title_textView);
        noResultTextView = (TextView) findViewById(R.id.no_result_textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        searchImageView = (ImageView) findViewById(R.id.search_imageView);
        //filterImageView = (ImageView) findViewById(R.id.filter_imageView);
        filterTextView = (TextView) findViewById(R.id.filter_textView);
        filterLinearLayout = (LinearLayout) findViewById(R.id.filter_linearLayout);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
    }

    private RequestListener<NYPaginationResult<DiveServiceList>> onSearchServiceRequest(boolean isRefresh) {
        return new RequestListener<NYPaginationResult<DiveServiceList>>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                /*adapter.clear();
                adapter.notifyDataSetChanged();*/
                if (adapter.getItemCount() <= 0)noResultTextView.setVisibility(View.VISIBLE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);

                // TODO: perbaiki ini menjadi pagination
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                if(swipeLayout != null) {
                    swipeLayout.setRefreshing(false);
                }
                if(recyclerView != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if (spiceException != null) {
                    NYHelper.handleAPIException(DoCourseResultActivity.this, spiceException, null);
                } else {
                    NYHelper.handleErrorMessage(DoCourseResultActivity.this, DoCourseResultActivity.this.getResources().getString(R.string.warn_no_connection));
                }
            }

            @Override
            public void onRequestSuccess(NYPaginationResult<DiveServiceList> result) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                /*if (results != null){
                    diveServiceAdapter.addResults(results);
                    diveServiceAdapter.notifyDataSetChanged();
                }

                if (diveServiceAdapter.getItemCount() > 0){
                    noResultTextView.setVisibility(View.GONE);
                    filterRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    noResultTextView.setVisibility(View.VISIBLE);
                    //filterRelativeLayout.setVisibility(View.GONE);
                }*/

                if (adapter.getItemCount() <= 0)noResultTextView.setVisibility(View.VISIBLE);

                //adapter.clear();
                if(progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                if(swipeLayout != null) {
                    swipeLayout.setRefreshing(false);
                }
                if(recyclerView != null) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                if(result != null && result.item != null && result.item.getList() != null && !result.item.getList().isEmpty()) {
                    if (adapter != null) {
                        progressBar.setVisibility(View.GONE);
                        adapter.addServices(result.item.getList(), true);
                        adapter.setSortType(sortingType);
                        adapter.sortData();
                        page++;
                    }
                }
                adapter.notifyDataSetChanged();

                /*if(adapter.getItemCount() > 0) {
                    filterRelativeLayout.setVisibility(View.VISIBLE);
                } else {
                    filterRelativeLayout.setVisibility(View.GONE);
                }*/

                //Toast.makeText(DoDiveSearchResultActivity.this, String.valueOf(adapter.getItemCount()), Toast.LENGTH_SHORT).show();

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void loadJSONAsset(){

        try {

            //Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            //JSONObject obj = new JSONObject(loadJSONFromAsset(this));
            JSONArray array = new JSONArray(loadJSONFromAsset(this));

            DiveServiceList results = new DiveServiceList();
            results.parse(array);

            if (results != null){
                noResultTextView.setVisibility(View.GONE);
                adapter.clear();
                adapter.addResults(results.getList());
                adapter.notifyDataSetChanged();
            } else {
                adapter.clear();
                adapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
            }

            //Toast.makeText(this, "end", Toast.LENGTH_SHORT).show();


        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*String myJson=inputStreamToString(getResources().openRawResource(R.raw.my_json));
        JSONObject jsonobject = new JSONObject(myJson);
        JSONArray jarray = (JSONArray) jsonobject.getJSONArray("formules");
        for (int i = 0; i < jarray.length(); i++){

        }*/
    }


    public String loadJSONFromAsset( Context context ) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("list_service_result.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // update the contact list
            page = 1;

            Bundle b = data.getExtras();

            if (data.hasExtra(NYHelper.SORT_BY))sortingType = b.getInt(NYHelper.SORT_BY);
            if (data.hasExtra(NYHelper.MIN_PRICE))minPrice = b.getDouble(NYHelper.MIN_PRICE);
            if (data.hasExtra(NYHelper.MAX_PRICE))maxPrice = b.getDouble(NYHelper.MAX_PRICE);

            NYLog.e("onresult sortBy : "+sortingType);
            NYLog.e("onresult minPrice : "+minPrice);
            NYLog.e("onresult maxPrice : "+maxPrice);

            totalDives = new ArrayList<>();
            if (data.hasExtra(NYHelper.TOTAL_DIVES)){
                try {
                    JSONArray arrayTotalDives = new JSONArray(b.getString(NYHelper.TOTAL_DIVES));
                    for (int i=0; i<arrayTotalDives.length(); i++) {
                        totalDives.add(arrayTotalDives.getString(i));
                    }
                    NYLog.e("onresult totalDives : "+totalDives.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (totalDives != null && totalDives.size() > 0) NYLog.e("cek total dives RESULT: "+totalDives.toString());

            NYLog.e("onresult categories : "+b.getString(NYHelper.CATEGORIES));
            NYLog.e("onresult facilities : "+b.getString(NYHelper.FACILITIES));

            if (data.hasExtra(NYHelper.CATEGORIES)){
                try {
                    JSONArray arrayCat = new JSONArray(b.getString(NYHelper.CATEGORIES));
                    categoryList = new CategoryList();
                    categoryList.parse(arrayCat);
                    NYLog.e("onresult categories final : "+categoryList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (data.hasExtra(NYHelper.FACILITIES)){
                try {
                    JSONArray arrayFac = new JSONArray(b.getString(NYHelper.FACILITIES));
                    stateFacilityList = new StateFacilityList();
                    stateFacilityList.parse(arrayFac);
                    NYLog.e("onresult facilities final : "+stateFacilityList.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            page=1;
            adapter.clear();
            adapter.notifyDataSetChanged();
            //initRequest();
            // TODO: hapus ini
            //requestPriceRange(true);
            initRequest(true);
        }
    }


    public void requestPriceRange(boolean isRefresh){

        //Toast.makeText(this, "DoCourse 3 price range", Toast.LENGTH_SHORT).show();


        if (!isRefresh)progressBar.setVisibility(View.VISIBLE);
        NYGetMinMaxPriceRequest req = null;
        try {
            //1 = do dive, 2 = do trip
            //req = new NYGetMinMaxPriceRequest(this, "1");
            // TODO: ganti type pice range 
            req = new NYGetMinMaxPriceRequest(this, "1", type, diverId, categoryList.getList(), diver, certificate, date, String.valueOf(sortingType), false);

        } catch (Exception e) {
            //Toast.makeText(this, "DoCourse 3 error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        spcMgr.execute(req, onGetMinMaxPriceRequest(isRefresh));
    }


    private RequestListener<Price> onGetMinMaxPriceRequest(final boolean isRefresh) {
        return new RequestListener<Price>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                filterLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onRequestSuccess(Price results) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                filterLinearLayout.setVisibility(View.VISIBLE);

                if (results != null){
                    minPriceDefault = results.getLowestPrice();
                    maxPriceDefault = results.getHighestPrice();
                    progressBar.setVisibility(View.VISIBLE);
                    initRequest(isRefresh);
                }
            }
        };
    }



    @Override
    public void onChooseListener(Object position) {
        sortingType = (Integer) position;
        //Toast.makeText(this, String.valueOf(sortingType), Toast.LENGTH_SHORT).show();
        //initRequest();
    }

    @Override
    public void onAcceptAgreementListener() {

    }

    @Override
    public void onCancelUpdate() {

    }

    @Override
    public void doUpdateVersion(String link) {

    }



}
