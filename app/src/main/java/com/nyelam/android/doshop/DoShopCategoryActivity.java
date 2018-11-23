package com.nyelam.android.doshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.docourse.DoCourseActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopProductListRequest;
import com.nyelam.android.http.result.NYPaginationResult;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoShopCategoryActivity extends BasicActivity {

    private boolean isRefresh = false;
    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopRecommendedAdapter adapter;
    private boolean loadmore=true;
    private LinearLayoutManager layoutManager;

    private int page = 1;
    private String categoryId;
    private DoShopCategory category;
    private String sortBy = "1";
    private String minPrice = "0";
    private String maxPrice = "10000000";

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @BindView(R.id.rv_item_list)
    RecyclerView rvItemList;

    @BindView(R.id.refresh_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.ll_filter) void filter(){
        Intent intent = new Intent(this, DoShopFilterActivity.class);
        intent.putExtra(NYHelper.SORT_BY, sortBy);
        intent.putExtra(NYHelper.MIN_PRICE, Double.valueOf(minPrice));
        intent.putExtra(NYHelper.MAX_PRICE, Double.valueOf(maxPrice));
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.ll_sort_by) void sortBy(){
        Intent intent = new Intent(this, DoShopFilterActivity.class);
        intent.putExtra(NYHelper.SORT_BY, sortBy);
        intent.putExtra(NYHelper.MIN_PRICE, Double.valueOf(minPrice));
        intent.putExtra(NYHelper.MAX_PRICE, Double.valueOf(maxPrice));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_category);
        ButterKnife.bind(this);
        context = this;
        initToolbar();
        initExtra();

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        rvItemList.setLayoutManager(layoutManager);
        adapter = new DoShopRecommendedAdapter(context);
        rvItemList.setAdapter(adapter);

        if (category != null && NYHelper.isStringNotEmpty(category.getId())){
            initListItem(category.getId(), null);
        } else {
            //dialogCategoryNotAvailable();
            initListItem(null, null);
        }

        initControl();
    }

    private void initControl() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                adapter.clear();
                adapter.notifyDataSetChanged();
                initListItem(categoryId, s.toString());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadmore = true;
                swipeRefreshLayout.setRefreshing(true);
                adapter.clear();
                adapter.notifyDataSetChanged();
                initListItem(categoryId, etSearch.getText().toString());
            }
        });

        layoutManager = ((LinearLayoutManager)rvItemList.getLayoutManager());
        rvItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == recyclerView.getChildCount()) {
                    if (loadmore) {
                        loadmore = false;
                        initListItem(categoryId, etSearch.getText().toString());
                    }
                }
            }
        });
    }

    private void initListItem(String categoryId, String keyword){
        progressBar.setVisibility(View.VISIBLE);
        NYDoShopProductListRequest req = new NYDoShopProductListRequest(context, String.valueOf(page), keyword, categoryId, minPrice,  maxPrice, sortBy);
        spcMgr.execute(req, onRealtedItemRequest());
    }

    private RequestListener<NYPaginationResult<DoShopProductList>> onRealtedItemRequest() {
        return new RequestListener<NYPaginationResult<DoShopProductList>>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                loadmore = false;
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                NYLog.e("cek related error");
                if (adapter.getItemCount() == 0) tvNotFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(NYPaginationResult<DoShopProductList> listNYPaginationResult) {
                loadmore = true;
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                NYLog.e("cek related success");
                if (listNYPaginationResult != null && listNYPaginationResult.item != null && listNYPaginationResult.item.getList() != null &&
                        listNYPaginationResult.item.getList().size() > 0){
                    NYLog.e("cek related data : "+listNYPaginationResult.item.getList().toString());

                    if (isRefresh)
                        adapter.setData(listNYPaginationResult.item.getList());
                    else
                        adapter.addData(listNYPaginationResult.item.getList());
                    adapter.notifyDataSetChanged();

                    page++;
                }

                if (adapter.getItemCount() == 0) tvNotFound.setVisibility(View.VISIBLE);

            }
        };
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.CATEGORY)) {
                try {
                    JSONObject obj = new JSONObject(intent.getStringExtra(NYHelper.CATEGORY));
                    category = new DoShopCategory();
                    category.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void dialogCategoryNotAvailable(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.logout));
        builder.setMessage(getString(R.string.warn_item_not_available));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        //toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Toast.makeText(context, "tes 1", Toast.LENGTH_SHORT).show();

        if (resultCode == Activity.RESULT_OK && data != null) {
            //Toast.makeText(context, "tes 2", Toast.LENGTH_SHORT).show();
            Bundle b = data.getExtras();
            if (data.hasExtra(NYHelper.SORT_BY)) sortBy = b.getString(NYHelper.SORT_BY);
            if (data.hasExtra(NYHelper.MIN_PRICE)) minPrice =   String.valueOf(b.getDouble(NYHelper.MIN_PRICE, 0));
            if (data.hasExtra(NYHelper.MAX_PRICE)) maxPrice = String.valueOf(b.getDouble(NYHelper.MAX_PRICE, 1000000));


            adapter.clear();
            adapter.notifyDataSetChanged();

            if (category != null && NYHelper.isStringNotEmpty(category.getId())){
                initListItem(category.getId(), null);
            } else {
                //dialogCategoryNotAvailable();
                initListItem(null, null);
            }
        }

    }
}
