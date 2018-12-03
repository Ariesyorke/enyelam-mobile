package com.nyelam.android.doshoporder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopAddressList;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoShopAddressListRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoShopChooseAddressActivity extends BasicActivity {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopAddressAdapter adapter;
    private String TAG = "billing";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @BindView(R.id.ll_container_yesno)
    LinearLayout llContainerYesno;

    @BindView(R.id.rbNo)
    RadioButton rbNo;

    @BindView(R.id.rbYes)
    RadioButton rbYes;

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

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
        rbNo.setChecked(true);
        initExtra();

        adapter = new DoShopAddressAdapter(this, TAG);
        //ADAPTER SERVICE LIST
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,spacingInPixels,0,spacingInPixels));
        recyclerView.setAdapter(adapter);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == 0){
                    adapter.setTAG(TAG);
                } else {
                    adapter.setTAG("billing and shipping");
                }
            }
        });

        loadAddress(false);
    }


    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.TAG) && NYHelper.isStringNotEmpty(extras.getString(NYHelper.TAG))){
                TAG = extras.getString(NYHelper.TAG);
            }
        }
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
                llContainerYesno.setVisibility(View.GONE);

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
                    llContainerYesno.setVisibility(View.VISIBLE);
                    adapter.setAddresses(addressList.getList());
                    adapter.notifyDataSetChanged();
                }

            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (pDialog != null) pDialog.cancel();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!spcMgr.isStarted()) spcMgr.start(this);
        loadAddress(true);
    }

}
