package com.nyelam.android.doshop;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.data.DoShopAddressList;
import com.nyelam.android.divecenter.DoDiveSearchServiceAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoShopAddressListRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoShopChooseAddressActivity extends AppCompatActivity {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopAddressAdapter adapter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @OnClick(R.id.iv_add_address) void addAddress(){
        Intent intent = new Intent(this, DoShopAddAddressActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_choose_address);
        ButterKnife.bind(this);
        context = getApplicationContext();

//        List<DoShopAddress> addresses = new ArrayList<>();
//        addresses.add(new DoShopAddress("Aprilian", "Jalan Pajajaran", "018181881"));
//        addresses.add(new DoShopAddress("Aprilian Nur", "Jalan Pajajaran", "018181881"));

        adapter = new DoShopAddressAdapter(this);

        //ADAPTER SERVICE LIST
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,spacingInPixels,0,spacingInPixels));
        recyclerView.setAdapter(adapter);

        loadAddress(false);
    }

    private void loadAddress(boolean isRefresh){
        if (!isRefresh)progressBar.setVisibility(View.VISIBLE);
        NYDoShopAddressListRequest req = null;
        try {
            req = new NYDoShopAddressListRequest(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onLoadAdressRequest());
    }

    private RequestListener<DoShopAddressList> onLoadAdressRequest() {
        return new RequestListener<DoShopAddressList>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                NYHelper.handleAPIException(context, spiceException, null);
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);

                if (spiceException != null) {
                    NYHelper.handleAPIException(DoShopChooseAddressActivity.this, spiceException, null);
                } else {
                    NYHelper.handleErrorMessage(DoShopChooseAddressActivity.this, getResources().getString(R.string.warn_no_connection));
                }
            }

            @Override
            public void onRequestSuccess(DoShopAddressList addressList) {
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.GONE);
                if (addressList != null && addressList.getList() != null){
                    adapter.setAddresses(addressList.getList());
                    adapter.notifyDataSetChanged();

                    //Toast.makeText(context, "address ada : "+String.valueOf(addressList.getList().size()), Toast.LENGTH_SHORT).show();
//                    DoShopCnategoryStorage storage = new DoShopCategoryStorage(DoShopActivity.this);
//                    storage.setCategoryList(categoryList);
//
//                    menuCategoryAdapter = new DoShopMenuCategoryAdapter( DoShopActivity.this, categoryList.getList());
//                    menuCategoryAdapter.notifyDataSetChanged();
//                    listViewMenu.setAdapter(menuCategoryAdapter);
                }

            }
        };
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
        loadAddress(true);
    }

}
