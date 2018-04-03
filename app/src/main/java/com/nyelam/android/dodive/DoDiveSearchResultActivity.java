package com.nyelam.android.dodive;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveCenterList;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSearchDiveCenterRequest;
import com.nyelam.android.http.NYDoDiveSearchServiceRequest;
import com.nyelam.android.http.NYDoDiveSearchServiceResultRequest;
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

public class DoDiveSearchResultActivity extends BasicActivity implements NYCustomDialog.OnDialogFragmentClickListener {

    private int mRequestCode = 100;

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoDiveSearchDiveServiceAdapter diveServiceAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView titleTextView, labelTextView, noResultTextView;
    protected String keyword, diverId, diver, certificate, date, type;
    private FloatingActionButton sortFloatingButton;
    //protected String diveSpotId;
    private ImageView filterImageView;
    private ImageView searchImageView;
    private int page = 1;
    private int sortingType = 1;
    private ArrayList<String> categories;
    private boolean ecotrip = false;

    public boolean isEcotrip() {
        return ecotrip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_dive_search_result);
        initView();
        initExtra();
        initAdapter();
        initRequest();
        //initToolbar();
        initControl();
        initToolbar(true);
    }

    private void initControl() {
        sortFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NYCustomDialog().showSortingDialog(DoDiveSearchResultActivity.this, sortingType);
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoDiveSearchResultActivity.this, DoDiveActivity.class);
                startActivity(intent);
            }
        });

        filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoDiveSearchResultActivity.this, FilterListDiveCenterActivity.class);
                intent.putExtra(NYHelper.KEYWORD, keyword);
                intent.putExtra(NYHelper.ID_DIVER, diverId);
                intent.putExtra(NYHelper.DIVER, diver);
                intent.putExtra(NYHelper.CERTIFICATE, certificate);
                intent.putExtra(NYHelper.SCHEDULE, date);
                intent.putExtra(NYHelper.TYPE, type);
                intent.putExtra(NYHelper.IS_ECO_TRIP, ecotrip);
                intent.putStringArrayListExtra(NYHelper.CATEGORIES, categories);
                //startActivity(intent);
                startActivityForResult(intent, mRequestCode);
            }
        });
    }

    private void initRequest() {
        progressBar.setVisibility(View.VISIBLE);

        String apiPath = getString(R.string.api_path_dodive_service_list);

        if (NYHelper.isStringNotEmpty(type) && type.equals("1")){
            apiPath = getString(R.string.api_path_dodive_search_service_by_divespot);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("2")){
            apiPath = getString(R.string.api_path_dodive_search_service_by_category);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("3")){
            apiPath = getString(R.string.api_path_dodive_search_service_by_divecenter);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("5")){
            apiPath = getString(R.string.api_path_dodive_search_service_by_province);
        } else if (NYHelper.isStringNotEmpty(type) && type.equals("6")){
            apiPath = getString(R.string.api_path_dodive_search_service_by_city);
        }

        /*if(isEcoTrip()) {
            NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(this, apiPath, String.valueOf(page), diveCenter.getId(), certificate, diver, schedule, type, diverId, "1");
            spcMgr.execute(req, onGetServiceByDiveCenterRequest());
        } else {
            NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(this, apiPath, String.valueOf(page), diveCenter.getId(), certificate, diver, schedule, type, diverId);
            spcMgr.execute(req, onGetServiceByDiveCenterRequest());
        }*/

        // TODO: tunggu URL dari Adam
        NYDoDiveSearchServiceResultRequest req = null;

        if(getIntent().hasExtra(NYHelper.IS_ECO_TRIP)) {
            int ecoTrip = getIntent().getIntExtra(NYHelper.IS_ECO_TRIP, 1);
            ecotrip = true;
            req = new NYDoDiveSearchServiceResultRequest(this, apiPath, String.valueOf(page), diverId, type, diver, certificate, date, String.valueOf(sortingType), categories, null, null, String.valueOf(ecoTrip));
        } else {
            req = new NYDoDiveSearchServiceResultRequest(this, apiPath, String.valueOf(page), diverId, type, diver, certificate, date, String.valueOf(sortingType), categories, null, null, String.valueOf(0));
        }

        spcMgr.execute(req, onSearchServiceRequest());

        // TODO: load data dummy, to test and waitting for API request
        //loadJSONAsset();

    }

    private void initExtra() {
        categories = new ArrayList<>();

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

            if(intent.hasExtra(NYHelper.CATEGORIES) && !extras.get(NYHelper.CATEGORIES).equals(null)){
                categories = extras.getStringArrayList(NYHelper.CATEGORIES);
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

        diveServiceAdapter = new DoDiveSearchDiveServiceAdapter(this, diver, date, certificate, type, diverId);
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
        filterImageView = (ImageView) findViewById(R.id.filter_imageView);
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
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveServiceList results) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

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


}
