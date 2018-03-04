package com.nyelam.android.dodive;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchResultList;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveSearchTypeRequest;
import com.nyelam.android.storage.KeywordHistoryStorage;
import com.nyelam.android.storage.ModulHomepageStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DoDiveSearchActivity extends AppCompatActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoDiveSearchAdapter doDiveSearchAdapter;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private TextView labelTextView;
    private TextView noResultTextView;
    private String date, diver;
    private boolean certificate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_dive_search);
        initGetExtras();
        initView();
        initControl();
        initAdapter();
        initFirstData();
    }

    private void initFirstData() {
        loadHistoryCache();
    }
    private void initGetExtras() {

        Intent intent = getIntent();
        Bundle extras = getIntent().getExtras();

        if (extras != null){

            if (intent.hasExtra(NYHelper.CERTIFICATE)){
                certificate = intent.getBooleanExtra(NYHelper.CERTIFICATE, false);
            }

            if (intent.hasExtra(NYHelper.SCHEDULE)){
                date = intent.getStringExtra(NYHelper.SCHEDULE);
            }

            if (intent.hasExtra(NYHelper.DIVER)){
                diver = intent.getStringExtra(NYHelper.DIVER);
            }

        }

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        doDiveSearchAdapter = new DoDiveSearchAdapter(this, date, diver, certificate);
        recyclerView.setAdapter(doDiveSearchAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    View view = DoDiveSearchActivity.this.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                }
            }
        });
    }

    private void initControl() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                spcMgr.cancel(NYSpiceService.class, "search_result");
                if (keyword != null && !TextUtils.isEmpty(keyword) && keyword.length() > 2){
                    noResultTextView.setVisibility(View.GONE);
                    labelTextView.setText(getResources().getString(R.string.search_results));
                    NYDoDiveSearchTypeRequest req = new NYDoDiveSearchTypeRequest(DoDiveSearchActivity.this, keyword);
                    spcMgr.execute(req, "search_result", DurationInMillis.NEVER, onSearchKeywordRequest());
//                    spcMgr.execute(req, onSearchKeywordRequest());
                } else {
                    loadHistoryCache();
                }
            }
        });
    }

    private void loadHistoryCache() {
        doDiveSearchAdapter.clear();
        KeywordHistoryStorage keywordHistoryStorage = new KeywordHistoryStorage(DoDiveSearchActivity.this);
        NYLog.e("ISI JSON " + keywordHistoryStorage.getSearchResults() );
        if (keywordHistoryStorage.getSearchResults() != null && keywordHistoryStorage.getSearchResults().size() > 0){
            noResultTextView.setVisibility(View.GONE);
            doDiveSearchAdapter.clear();

            List<SearchResult> searchList = new ArrayList<>();
            searchList = keywordHistoryStorage.getSearchResults();
            Collections.reverse(searchList);
            doDiveSearchAdapter.addResults(searchList);
            doDiveSearchAdapter.notifyDataSetChanged();

        } else {
            doDiveSearchAdapter.clear();
            doDiveSearchAdapter.notifyDataSetChanged();
            noResultTextView.setVisibility(View.GONE);
        }
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        searchEditText = (EditText) findViewById(R.id.search_editText);
        labelTextView = (TextView) findViewById(R.id.label_textView);
        noResultTextView = (TextView) findViewById(R.id.no_result_textView);
    }


    private RequestListener<SearchResultList> onSearchKeywordRequest() {
        return new RequestListener<SearchResultList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }*/
                doDiveSearchAdapter.clear();
                doDiveSearchAdapter.notifyDataSetChanged();
                noResultTextView.setVisibility(View.VISIBLE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(SearchResultList results) {
                /*if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*/

                if (results != null){
                    noResultTextView.setVisibility(View.GONE);
                    doDiveSearchAdapter.clear();
                    doDiveSearchAdapter.addResults(results.getList());
                    doDiveSearchAdapter.notifyDataSetChanged();
                } else {
                    doDiveSearchAdapter.clear();
                    doDiveSearchAdapter.notifyDataSetChanged();
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
