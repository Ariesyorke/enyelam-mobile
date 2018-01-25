package com.nyelam.android.dodive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchResultList;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSearchServiceRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class DoDiveSearchResultActivity extends BasicActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoDiveSearchServiceAdapter serviceAdapter;
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
            url = getResources().getString(R.string.api_path_dodive_search_by_spot);
        } else if (type.equals("2")){
            url = getResources().getString(R.string.api_path_dodive_search_by_spot);
        } else if (type.equals("3")){
            url = getResources().getString(R.string.api_path_dodive_search_by_dive_center);
        } else if (type.equals("4")){
            url = getResources().getString(R.string.api_path_dodive_search_by_country);
        } else if (type.equals("5")){
            url = getResources().getString(R.string.api_path_dodive_search_by_province);
        } else {
            url = getResources().getString(R.string.api_path_dodive_search_by_city);
        }

        NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(DoDiveSearchResultActivity.this,
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

        serviceAdapter = new DoDiveSearchServiceAdapter(this, diver, date);
        recyclerView.setAdapter(serviceAdapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        labelTextView = (TextView) findViewById(R.id.label_textView);
        titleTextView = (TextView) findViewById(R.id.title_textView);
        noResultTextView = (TextView) findViewById(R.id.no_result_textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    private RequestListener<DiveServiceList> onSearchServiceRequest() {
        return new RequestListener<DiveServiceList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                serviceAdapter.clear();
                serviceAdapter.notifyDataSetChanged();
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
                    serviceAdapter.clear();
                    serviceAdapter.addResults(results.getList());
                    serviceAdapter.notifyDataSetChanged();
                } else {
                    serviceAdapter.clear();
                    serviceAdapter.notifyDataSetChanged();
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
