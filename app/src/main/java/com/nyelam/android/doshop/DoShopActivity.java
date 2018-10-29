package com.nyelam.android.doshop;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.nyelam.android.R;
import com.nyelam.android.StarterActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Cart;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.Update;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYCategoryRequest;
import com.nyelam.android.http.NYUpdateVersionRequest;
import com.nyelam.android.http.NYVoucherCartRequest;
import com.nyelam.android.view.NYCustomDialog;
import com.nyelam.android.view.NYUnswipableViewPager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class DoShopActivity extends AppCompatActivity {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private DoShopAdapter doShopAdapter;
    private ArrayList<Object> objects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop);

        context = this;

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.progressBar);

        initCategory();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshs();
            }
        });

    }

    private void initCategory(){
        NYCategoryRequest req = new NYCategoryRequest(context);
        spcMgr.execute(req, onCategoryRequest());
    }

    private RequestListener<DoShopList> onCategoryRequest() {
        return new RequestListener<DoShopList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(context, spiceException, null);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onRequestSuccess(DoShopList doShopList) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                objects.add(doShopList.getCategories());
                objects.add(doShopList.getRecommended());

                final GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

                    @Override
                    public int getSpanSize(int position) {
                        switch (doShopAdapter.getItemViewType(position)){
                            case DoShopAdapter.VIEW_TYPE_CATEGORY:
                                return 2 ;
                            case DoShopAdapter.VIEW_TYPE_RECOMMENDED:
                                return 2;
                            default:
                                return 2;
                        }
                    }
                });

                recyclerView.setLayoutManager(layoutManager);

                doShopAdapter = new DoShopAdapter(context, objects);
                doShopAdapter.addModules(doShopList);
                recyclerView.setAdapter(doShopAdapter);
                recyclerView.setVisibility(View.VISIBLE);
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
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

    public void onRefreshs(){
        swipeRefreshLayout.setRefreshing(true);
        objects = new ArrayList<>();
        initCategory();
    }
}
