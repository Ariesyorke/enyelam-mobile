package com.nyelam.android.dotrip;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.CategoryList;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.Facilities;
import com.nyelam.android.data.Price;
import com.nyelam.android.data.StateFacility;
import com.nyelam.android.data.StateFacilityList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.dodive.FilterListServiceActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoTripSearchServiceResultRequest;
import com.nyelam.android.http.NYGetMinMaxPriceRequest;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DoTripResultActivity extends BasicActivity implements NYCustomDialog.OnDialogFragmentClickListener {

    private int mRequestCode = 100;

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    protected String keyword, diverId, diver, certificate, date, type;
    private DoTripDiveServiceAdapter diveServiceAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView titleTextView, labelTextView, noResultTextView;
    private FloatingActionButton sortFloatingButton;
    //private ImageView filterImageView;
    private ImageView searchImageView;
    private int page = 1;

    private int sortingType = 2;
    private double minPrice=-1, minPriceDefault;
    private double maxPrice=-1, maxPriceDefault;
    //private ArrayList<String> categories;
    private List<String> totalDives;
    private CategoryList categoryList;
    private StateFacilityList stateFacilityList;
    private TextView filterTextView;
    private LinearLayout filterLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_trip_result);
        initView();
        initExtra();
        initAdapter();
        requestPriceRange();
        /*initRequest();*/
        //initToolbar();
        initControl();
        initToolbar(true);
    }

    private void initControl() {

        sortFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NYCustomDialog().showSortingDialog(DoTripResultActivity.this, sortingType);
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoTripResultActivity.this, DoTripActivity.class);
                startActivity(intent);
            }
        });

        /*filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoTripResultActivity.this, FilterListServiceActivity.class);
                // TODO: kirim parameter ke filter
                if (categoryList != null && categoryList.getList() != null && categoryList.getList().size() > 0)intent.putExtra(NYHelper.CATEGORIES, categoryList.getList().toString());
                if (stateFacilityList != null && stateFacilityList.getList() != null && stateFacilityList.getList().size() > 0)intent.putExtra(NYHelper.FACILITIES, stateFacilityList.getList().toString());
                if (totalDives != null && totalDives.size() > 0)intent.putExtra(NYHelper.TOTAL_DIVES, totalDives.toString());
                intent.putExtra(NYHelper.SORT_BY, sortingType);
                intent.putExtra(NYHelper.MIN_PRICE_DEAFULT, minPriceDefault);
                intent.putExtra(NYHelper.MIN_PRICE, minPrice);
                intent.putExtra(NYHelper.MAX_PRICE_DEFAULT, maxPriceDefault);
                intent.putExtra(NYHelper.MAX_PRICE, maxPrice);
                startActivityForResult(intent, mRequestCode);
            }
        });*/

        filterTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoTripResultActivity.this, FilterListServiceActivity.class);
                // TODO: kirim parameter ke filter
                if (categoryList != null && categoryList.getList() != null && categoryList.getList().size() > 0)intent.putExtra(NYHelper.CATEGORIES, categoryList.getList().toString());
                if (stateFacilityList != null && stateFacilityList.getList() != null && stateFacilityList.getList().size() > 0)intent.putExtra(NYHelper.FACILITIES, stateFacilityList.getList().toString());
                if (totalDives != null && totalDives.size() > 0)intent.putExtra(NYHelper.TOTAL_DIVES, totalDives.toString());
                intent.putExtra(NYHelper.SORT_BY, sortingType);
                intent.putExtra(NYHelper.MIN_PRICE_DEAFULT, minPriceDefault);
                intent.putExtra(NYHelper.MIN_PRICE, minPrice);
                intent.putExtra(NYHelper.MAX_PRICE_DEFAULT, maxPriceDefault);
                intent.putExtra(NYHelper.MAX_PRICE, maxPrice);
                startActivityForResult(intent, mRequestCode);
            }
        });
    }


    private void initRequest() {
        progressBar.setVisibility(View.VISIBLE);

        // TODO: buat service request dan ganti URL untuk DoTrip
        String apiPath = getString(R.string.api_path_dotrip_service_list);

        if (NYHelper.isStringNotEmpty(type) && type.equals("1")){
            apiPath = getString(R.string.api_path_dotrip_service_list_by_divespot);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("2")){
            apiPath = getString(R.string.api_path_dotrip_service_list_by_category);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("3")){
            apiPath = getString(R.string.api_path_dotrip_service_list_by_divecenter);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("5")){
            apiPath = getString(R.string.api_path_dotrip_service_list_by_province);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("6")){
            apiPath = getString(R.string.api_path_dotrip_service_list_by_city);
        }

        // TODO: tunggu URL dari Adam
        List<Category> lsCategory = categoryList.getList();
        List<StateFacility> lsFacilities = stateFacilityList.getList();
        NYDoTripSearchServiceResultRequest req = new NYDoTripSearchServiceResultRequest(this, apiPath, String.valueOf(page), diverId, type, diver, certificate, date, String.valueOf(sortingType), lsCategory, lsFacilities, totalDives, minPrice, maxPrice);
        spcMgr.execute(req, onSearchServiceRequest());

        // TODO: load data dummy, to test and waitting for API request
        //loadJSONAsset();
    }

    private void initExtra() {
        totalDives = new ArrayList<String>();
        categoryList = new CategoryList();
        stateFacilityList = new StateFacilityList();
        stateFacilityList = new StateFacilityList();

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.KEYWORD) && !extras.getString(NYHelper.KEYWORD).equals(null)){
                keyword = extras.getString(NYHelper.KEYWORD);
            }
            if(intent.hasExtra(NYHelper.ID_DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.ID_DIVER))) diverId = extras.getString(NYHelper.ID_DIVER);
            if(intent.hasExtra(NYHelper.DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVER))) diver = extras.getString(NYHelper.DIVER);
            if(intent.hasExtra(NYHelper.CERTIFICATE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.CERTIFICATE))) certificate = extras.getString(NYHelper.CERTIFICATE);
            if(intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SCHEDULE))) date = extras.getString(NYHelper.SCHEDULE);

            if(intent.hasExtra(NYHelper.MIN_PRICE)) minPrice = extras.getDouble(NYHelper.MIN_PRICE);
            if(intent.hasExtra(NYHelper.MAX_PRICE)) maxPrice = extras.getDouble(NYHelper.MAX_PRICE);

            if(intent.hasExtra(NYHelper.TYPE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.TYPE))){
                type = extras.getString(NYHelper.TYPE);
            }

            /*if(intent.hasExtra(NYHelper.CATEGORIES) && !extras.get(NYHelper.CATEGORIES).equals(null)){
                categories = extras.getStringArrayList(NYHelper.CATEGORIES);
            }*/

            if (NYHelper.isStringNotEmpty(date) && NYHelper.isStringNotEmpty(diver))titleTextView.setText(NYHelper.setMillisToDate(Long.valueOf(date))+", "+diver+" pax (s)");

        }

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(spacingInPixels));

        diveServiceAdapter = new DoTripDiveServiceAdapter(this, diver, date, certificate, type, diverId);
        recyclerView.setAdapter(diveServiceAdapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        labelTextView = (TextView) findViewById(R.id.label_textView);
        titleTextView = (TextView) findViewById(R.id.title_textView);
        noResultTextView = (TextView) findViewById(R.id.no_result_textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        sortFloatingButton = (FloatingActionButton) findViewById(R.id.sort_floatingButton);
        searchImageView = (ImageView) findViewById(R.id.search_imageView);
        //filterImageView = (ImageView) findViewById(R.id.filter_imageView);
        filterTextView = (TextView) findViewById(R.id.filter_textView);
        filterLinearLayout = (LinearLayout) findViewById(R.id.filter_linearLayout);
    }

    private RequestListener<DiveServiceList> onSearchServiceRequest() {
        return new RequestListener<DiveServiceList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                diveServiceAdapter.clear();
                diveServiceAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
                //filterRelativeLayout.setVisibility(View.GONE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveServiceList results) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                if (results != null){
                    diveServiceAdapter.addResults(results.getList());
                    diveServiceAdapter.notifyDataSetChanged();
                }

                if (diveServiceAdapter.getItemCount() > 0){
                    noResultTextView.setVisibility(View.GONE);
                    filterLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    noResultTextView.setVisibility(View.VISIBLE);
                    //filterRelativeLayout.setVisibility(View.GONE);
                }


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

    @Override
    public void onChooseListener(Object position) {
        sortingType = (Integer) position;
        //Toast.makeText(this, String.valueOf(sortingType), Toast.LENGTH_SHORT).show();
        initRequest();
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

    public void loadJSONAsset(){

        try {

            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();

            if (progressBar != null) {
                progressBar.setVisibility(View.GONE);
            }

            //JSONObject obj = new JSONObject(loadJSONFromAsset(this));
            JSONArray array = new JSONArray(loadJSONFromAsset(this));

            DiveServiceList results = new DiveServiceList();
            results.parse(array);

            if (results != null){
                noResultTextView.setVisibility(View.GONE);
                diveServiceAdapter.clear();
                diveServiceAdapter.addResults(results.getList());
                diveServiceAdapter.notifyDataSetChanged();
            } else {
                diveServiceAdapter.clear();
                diveServiceAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
            }

            Toast.makeText(this, "end", Toast.LENGTH_SHORT).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

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

            diveServiceAdapter.clear();
            diveServiceAdapter.notifyDataSetChanged();
            page=1;

            initRequest();

        }
    }


    public void requestPriceRange(){
        progressBar.setVisibility(View.VISIBLE);
        NYGetMinMaxPriceRequest req = null;
        try {
            //1 = do dive, 2 = do trip
            //req = new NYGetMinMaxPriceRequest(this, "2");
            req = new NYGetMinMaxPriceRequest(this, "2", type, diverId, categoryList.getList(), diver, certificate, date, String.valueOf(sortingType), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onGetMinMaxPriceRequest());
    }

    private RequestListener<Price> onGetMinMaxPriceRequest() {
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
                    initRequest();
                }
            }
        };
    }



}

