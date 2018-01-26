package com.nyelam.android.dodive;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveCenterList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSearchDiveCenterRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class DoDiveSearchResultActivity extends BasicActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoDiveSearchDiveCenterAdapter diveCenterAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView titleTextView, labelTextView, noResultTextView;
    protected String keyword, diverId, diver, certificate, date, type;
    //protected String diveSpotId;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_dive_search_result);
        initView();
        initExtra();
        initAdapter();
        initRequest();
        //initToolbar();
        //initControl();
        initToolbar(true);
    }

    private void initRequest() {
        progressBar.setVisibility(View.VISIBLE);

        String url = "";
        if (type.equals("1")){
            url = getResources().getString(R.string.api_path_dodive_search_dive_center_by_spot);
        } else if (type.equals("2")){
            url = getResources().getString(R.string.api_path_dodive_search_dive_center_by_category);
        } else if (type.equals("5")){
            url = getResources().getString(R.string.api_path_dodive_search_dive_center_by_province);
        } else {
            url = getResources().getString(R.string.api_path_dodive_search_dive_center_by_city);
        }

        NYDoDiveSearchDiveCenterRequest req = new NYDoDiveSearchDiveCenterRequest(DoDiveSearchResultActivity.this,
                url, String.valueOf(page), diverId, type, certificate, diver, date);
        spcMgr.execute(req, onSearchServiceRequest());
    }

    private void initExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(!extras.getString(NYHelper.KEYWORD).equals(null)){
                keyword = extras.getString(NYHelper.KEYWORD);
                titleTextView.setText(keyword);
            }
            if(!extras.getString(NYHelper.ID_DIVER).equals(null)) diverId = extras.getString(NYHelper.ID_DIVER);
            if(!extras.getString(NYHelper.DIVER).equals(null)) diver = extras.getString(NYHelper.DIVER);
            if(!extras.getString(NYHelper.CERTIFICATE).equals(null)) certificate = extras.getString(NYHelper.CERTIFICATE);
            if(!extras.getString(NYHelper.SCHEDULE).equals(null)) date = extras.getString(NYHelper.SCHEDULE);
            if(!extras.getString(NYHelper.TYPE).equals(null)){
                type = extras.getString(NYHelper.TYPE);
            }

            /*if(!extras.get(NYHelper.DIVE_SPOT_ID).equals(null)){
                diveSpotId = extras.getString(NYHelper.DIVE_SPOT_ID);
            }*/

        }
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(spacingInPixels));

        diveCenterAdapter = new DoDiveSearchDiveCenterAdapter(this, diver, date, certificate);
        recyclerView.setAdapter(diveCenterAdapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        labelTextView = (TextView) findViewById(R.id.label_textView);
        titleTextView = (TextView) findViewById(R.id.title_textView);
        noResultTextView = (TextView) findViewById(R.id.no_result_textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private RequestListener<DiveCenterList> onSearchServiceRequest() {
        return new RequestListener<DiveCenterList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                diveCenterAdapter.clear();
                diveCenterAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveCenterList results) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                if (results != null){
                    noResultTextView.setVisibility(View.GONE);
                    diveCenterAdapter.clear();
                    diveCenterAdapter.addResults(results.getList());
                    diveCenterAdapter.notifyDataSetChanged();
                } else {
                    diveCenterAdapter.clear();
                    diveCenterAdapter.notifyDataSetChanged();
                    noResultTextView.setVisibility(View.VISIBLE);
                }

            }
        };
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
