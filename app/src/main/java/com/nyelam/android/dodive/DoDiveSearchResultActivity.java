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
import com.nyelam.android.data.SearchResultList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveSearchServiceRequest;
import com.nyelam.android.http.NYDoDiveSearchTypeRequest;
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
    private EditText searchEditText;
    private TextView labelTextView;
    private TextView noResultTextView;
    private String diverId, diver, certificate, date, type;
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
    }

    private void initRequest() {
        progressBar.setVisibility(View.VISIBLE);
        if (type.equals("1")){
            NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(DoDiveSearchResultActivity.this,
                    getResources().getString(R.string.api_path_dodive_search_by_spot),
                    String.valueOf(page), diverId, type, certificate, diver, date);
            spcMgr.execute(req, onSearchServiceRequest());
        } else {
            NYDoDiveSearchServiceRequest req = new NYDoDiveSearchServiceRequest(DoDiveSearchResultActivity.this,
                    getResources().getString(R.string.api_path_dodive_search_by_dive_center),
                    String.valueOf(page), diverId, type, certificate, diver, date);
            spcMgr.execute(req, onSearchServiceRequest());
        }

    }

    private void initExtra() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            diverId = extras.getString(NYHelper.ID_DIVER);
            diver = extras.getString(NYHelper.DIVER);
            certificate = extras.getString(NYHelper.CERTIFICATE);
            date = extras.getString(NYHelper.DATE);
            type = extras.getString(NYHelper.TYPE);
        }
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        serviceAdapter = new DoDiveSearchServiceAdapter(this);
        recyclerView.setAdapter(serviceAdapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchEditText = (EditText) findViewById(R.id.search_editText);
        labelTextView = (TextView) findViewById(R.id.label_textView);
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
                /*doDiveSearchAdapter.clear();
                doDiveSearchAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);*/
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveServiceList results) {
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
                noResultTextView.setVisibility(View.GONE);
                serviceAdapter.clear();
                serviceAdapter.addResults(results.getList());
                serviceAdapter.notifyDataSetChanged();


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
