package com.nyelam.android.doshoporderhistory;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    public DoShopOrderHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (getArguments() != null && getArguments().get(NYHelper.STATUS) != null){
            status = getArguments().getString(NYHelper.STATUS);
            Toast.makeText(getActivity(), "STATUS : "+status, Toast.LENGTH_SHORT).show();
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

        getOrderHistoryList(null);
    }

    private void getOrderHistoryList(String status){
        progressBar.setVisibility(View.VISIBLE);
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

    private RequestListener<DoShopOrderList> onOrderHistoryRequest() {
        return new RequestListener<DoShopOrderList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                //swipeRefreshLayout.setRefreshing(false);
                //llRelatedItem.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(DoShopOrderList orderList) {
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.GONE);
                //swipeRefreshLayout.setRefreshing(false);
                if (orderList!=null)adapter.setData(orderList.getList());
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
