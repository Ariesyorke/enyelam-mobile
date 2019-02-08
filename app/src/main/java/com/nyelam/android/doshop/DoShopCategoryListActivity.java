package com.nyelam.android.doshop;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopCategoryList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.divecenter.DoDiveSearchServiceAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoShopMasterCategoryRequest;
import com.nyelam.android.view.GridSpacingItemDecoration;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoShopCategoryListActivity extends BasicActivity {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopCategoryAdapter adapter;

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refresh_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_category_list);
        ButterKnife.bind(this);
        context = this;
        initView();
        initToolbar();
        initControl();
        loadCategories(false);
    }

    private void initControl() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                loadCategories(true);
            }
        });
    }

    private void loadCategories(boolean isRefresh) {
        swipeRefreshLayout.setRefreshing(true);
        NYDoShopMasterCategoryRequest req = new NYDoShopMasterCategoryRequest(context);
        spcMgr.execute(req, onCategoryRequest());
    }

    private RequestListener<DoShopCategoryList> onCategoryRequest() {
        return new RequestListener<DoShopCategoryList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                swipeRefreshLayout.setRefreshing(false);
                if (adapter != null)adapter.clear();
                tvNotFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(DoShopCategoryList categoryList) {
                swipeRefreshLayout.setRefreshing(false);
                if (categoryList != null && categoryList.getList() != null && !categoryList.getList().isEmpty()) {
                    adapter = new DoShopCategoryAdapter(context, categoryList.getList());
                    recyclerView.setAdapter(adapter);
                    tvNotFound.setVisibility(View.GONE);
                } else {
                    tvNotFound.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    private void initView() {
        int spacing_left = 10;
        int spacing_top=10;
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing_left, spacing_top));
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
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
    protected void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(this);
    }
}
