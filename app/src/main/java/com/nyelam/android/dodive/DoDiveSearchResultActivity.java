package com.nyelam.android.dodive;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveCenterList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSearchDiveCenterRequest;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class DoDiveSearchResultActivity extends BasicActivity implements NYCustomDialog.OnDialogFragmentClickListener {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoDiveSearchDiveCenterAdapter diveCenterAdapter;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView titleTextView, labelTextView, noResultTextView;
    protected String keyword, diverId, diver, certificate, date, type;
    private FloatingActionButton sortFloatingButton;
    //protected String diveSpotId;
    private ImageView searchImageView;
    private int page = 1;
    private int sortingType = 1;

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
                url, String.valueOf(page), diverId, type, certificate, diver, date, String.valueOf(sortingType));
        spcMgr.execute(req, onSearchServiceRequest());
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(!extras.getString(NYHelper.KEYWORD).equals(null)){
                keyword = extras.getString(NYHelper.KEYWORD);
                titleTextView.setText(keyword);
            }
            if(intent.hasExtra(NYHelper.ID_DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.ID_DIVER))) diverId = extras.getString(NYHelper.ID_DIVER);
            if(intent.hasExtra(NYHelper.DIVER) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.DIVER))) diver = extras.getString(NYHelper.DIVER);
            if(intent.hasExtra(NYHelper.CERTIFICATE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.CERTIFICATE))) certificate = extras.getString(NYHelper.CERTIFICATE);
            if(intent.hasExtra(NYHelper.SCHEDULE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.SCHEDULE))) date = extras.getString(NYHelper.SCHEDULE);
            if(intent.hasExtra(NYHelper.TYPE) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.TYPE))){
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

        diveCenterAdapter = new DoDiveSearchDiveCenterAdapter(this, diver, date, certificate, type, diverId);
        recyclerView.setAdapter(diveCenterAdapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        labelTextView = (TextView) findViewById(R.id.label_textView);
        titleTextView = (TextView) findViewById(R.id.title_textView);
        noResultTextView = (TextView) findViewById(R.id.no_result_textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        sortFloatingButton = (FloatingActionButton) findViewById(R.id.sort_floatingButton);
        searchImageView = (ImageView) findViewById(R.id.search_imageView);
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
    public void onChooseListener(int position) {
        sortingType = position;
    }

}
