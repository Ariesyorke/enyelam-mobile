package com.nyelam.android.doshoporderhistory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.bookinghistory.BookingHistoryDetailActivity;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopOrder;
import com.nyelam.android.data.DoShopOrderList;
import com.nyelam.android.data.Order;
import com.nyelam.android.data.Summary;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopOrderDetailRequest;
import com.nyelam.android.http.NYDoShopOrderListRequest;
import com.nyelam.android.inbox.NewMessageActivity;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

public class DoShopOrderDetailActivity extends BasicActivity {

    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopOrder order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_order_detail);
        ButterKnife.bind(this);
        initToolbar();
        initExtra();
        getOrderDetail(order.getOrderId());
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.ORDER)) {
                try {
                    JSONObject obj = new JSONObject(intent.getStringExtra(NYHelper.ORDER));
                    order = new DoShopOrder();
                    order.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getOrderDetail(String orderId){
        NYDoShopOrderDetailRequest req = null;
        try {
            req = new NYDoShopOrderDetailRequest(this, orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onOrderDetailRequest());
    }

    private RequestListener<DoShopOrder> onOrderDetailRequest() {
        return new RequestListener<DoShopOrder>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                //swipeRefreshLayout.setRefreshing(false);
                //llRelatedItem.setVisibility(View.GONE);
            }

            @Override
            public void onRequestSuccess(DoShopOrder order) {
                if (order != null)initOrderView(order);
            }
        };
    }

    private void initOrderView(DoShopOrder order) {

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
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(this);
    }

}
