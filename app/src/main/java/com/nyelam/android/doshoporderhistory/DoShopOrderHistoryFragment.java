package com.nyelam.android.doshoporderhistory;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopOrder;
import com.nyelam.android.data.DoShopOrderList;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.doshop.DoShopRecommendedAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopListCartRequest;
import com.nyelam.android.http.NYDoShopOrderListRequest;
import com.nyelam.android.http.result.NYPaginationResult;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoShopOrderHistoryFragment extends BasicFragment {

    private String status = "1";
    private int page = 1;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopOrderHistoryAdapter adapter;
    private boolean loadmore=true;
    private LinearLayoutManager layoutManager;

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.refresh_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    public DoShopOrderHistoryFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public DoShopOrderHistoryFragment(String status) {
        // Required empty public constructor
        this.status = status;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null && getArguments().get(NYHelper.STATUS) != null){
            status = getArguments().getString(NYHelper.STATUS);
            //Toast.makeText(getActivity(), "STATUS : "+status, Toast.LENGTH_SHORT).show();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getOrderHistoryList(status);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DoShopOrderHistoryAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        getOrderHistoryList(status, false);
        initController();
    }

    private void initController() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                swipeRefreshLayout.setRefreshing(true);
                getOrderHistoryList(status, true);
            }
        });


        layoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == recyclerView.getChildCount()) {
                    if (loadmore) {
                        loadmore = false;
                        getOrderHistoryList(status, true);
                    }
                }
            }
        });
    }

    private void getOrderHistoryList(String status, boolean isRefresh){
        if (isRefresh){
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }

        tvNotFound.setVisibility(View.GONE);
        NYDoShopOrderListRequest req = null;
        try {
            req = new NYDoShopOrderListRequest(getActivity(), status, String.valueOf(page));
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            tvNotFound.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
        spcMgr.execute(req, onOrderHistoryRequest());
    }

    private RequestListener<NYPaginationResult<DoShopOrderList>> onOrderHistoryRequest() {
        return new RequestListener<NYPaginationResult<DoShopOrderList>>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                //swipeRefreshLayout.setRefreshing(false);
                //llRelatedItem.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);
                loadmore = false;
                if (adapter.getItemCount() <= 0){
                    tvNotFound.setVisibility(View.VISIBLE);
                } else {
                    tvNotFound.setVisibility(View.GONE);
                }
            }

            @Override
            public void onRequestSuccess(NYPaginationResult<DoShopOrderList> orderList) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.GONE);
                //swipeRefreshLayout.setRefreshing(false);
                if (orderList!=null && orderList.item != null && orderList.item.getList() != null && orderList.item.getList().size() > 0){
                    if (page == 1) adapter.setData(orderList.item.getList());
                    else adapter.addData(orderList.item.getList());
                    adapter.notifyDataSetChanged();
                    page++;
                    loadmore = true;
                }
            }
        };
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_do_shop_order_history;
    }


    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(getActivity());
    }


}
