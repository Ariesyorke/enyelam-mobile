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
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchResultList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveSearchRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DoDiveSearchActivity extends AppCompatActivity {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoDiveSearchAdapter doDiveSearchAdapter;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private TextView labelTextView;
    private TextView noResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_dive_search);
        initView();
        initVontrol();
        initAdapter();
    }



    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        doDiveSearchAdapter = new DoDiveSearchAdapter(this);
        recyclerView.setAdapter(doDiveSearchAdapter);
    }

    private void initVontrol() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //if (qty.getText().toString().equals(s.toString())){return;}
                //.makeText(DoDiveSearchActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
                //String keyword = NYHelper.trim(s.toString());
                String keyword = s.toString().trim();
                if (keyword != null && !TextUtils.isEmpty(keyword)){
                    noResultTextView.setVisibility(View.GONE);
                    labelTextView.setText(getResources().getString(R.string.search_results));
                    NYDoDiveSearchRequest req = new NYDoDiveSearchRequest(DoDiveSearchActivity.this, keyword);
                    spcMgr.execute(req, onSearchKeywordRequest());
                } else {
                    doDiveSearchAdapter.clear();
                    doDiveSearchAdapter.notifyDataSetChanged();
                    labelTextView.setText(getResources().getString(R.string.recent_searches));
                    noResultTextView.setVisibility(View.VISIBLE);
                }
            }
        });
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
                noResultTextView.setVisibility(View.GONE);
                doDiveSearchAdapter.clear();
                doDiveSearchAdapter.addResults(results.getList());
                doDiveSearchAdapter.notifyDataSetChanged();


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
