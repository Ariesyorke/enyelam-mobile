package com.nyelam.android.dodive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.DiveSpotList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSearchDiveSpotRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class DoDiveSearchResultDiveSpotActivity extends BasicActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoDiveSearchDiveSpotAdapter diveSpotAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView titleTextView, labelTextView, noResultTextView;
    protected String keyword, diverId, diver, certificate, date, type;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_dive_search_result_dive_spot);
        initView();
        initExtra();
        initAdapter();
        initRequest();
        initToolbar(true);
    }

    private void initRequest() {
        progressBar.setVisibility(View.VISIBLE);

        String url = "";
        if (type.equals("5")) {
            url = getResources().getString(R.string.api_path_dodive_search_dive_spots_by_province);
        } else {
            url = getResources().getString(R.string.api_path_dodive_search_dive_spots_by_city);
        }

        NYDoDiveSearchDiveSpotRequest req = new NYDoDiveSearchDiveSpotRequest(DoDiveSearchResultDiveSpotActivity.this,
                url, String.valueOf(page), diverId, type);
        spcMgr.execute(req, onSearchSpotRequest());
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (intent.hasExtra(NYHelper.KEYWORD) && !extras.getString(NYHelper.KEYWORD).equals(null)) {
                keyword = extras.getString(NYHelper.KEYWORD);
                titleTextView.setText(keyword);
            }
            if (intent.hasExtra(NYHelper.ID_DIVER) && !extras.getString(NYHelper.ID_DIVER).equals(null)){
                diverId = extras.getString(NYHelper.ID_DIVER);
            }
            if (intent.hasExtra(NYHelper.DIVER) && !extras.getString(NYHelper.DIVER).equals(null)){
                diver = extras.getString(NYHelper.DIVER);
            }
            if (intent.hasExtra(NYHelper.CERTIFICATE) && !extras.getString(NYHelper.CERTIFICATE).equals(null)){
                certificate = extras.getString(NYHelper.CERTIFICATE);
            }
            if (intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SCHEDULE))){
                date = extras.getString(NYHelper.SCHEDULE);
            }
            if (intent.hasExtra(NYHelper.TYPE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.TYPE))) {
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

        diveSpotAdapter = new DoDiveSearchDiveSpotAdapter(this, diver, date, certificate, type);
        recyclerView.setAdapter(diveSpotAdapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        labelTextView = (TextView) findViewById(R.id.label_textView);
        titleTextView = (TextView) findViewById(R.id.title_textView);
        noResultTextView = (TextView) findViewById(R.id.no_result_textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private RequestListener<DiveSpotList> onSearchSpotRequest() {
        return new RequestListener<DiveSpotList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                diveSpotAdapter.clear();
                diveSpotAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveSpotList results) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }

                if (results != null) {
                    noResultTextView.setVisibility(View.GONE);
                    diveSpotAdapter.clear();
                    diveSpotAdapter.addResults(results.getList());
                    diveSpotAdapter.notifyDataSetChanged();
                } else {
                    diveSpotAdapter.clear();
                    diveSpotAdapter.notifyDataSetChanged();
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