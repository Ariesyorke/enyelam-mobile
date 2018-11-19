package com.nyelam.android.doshop;


import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopCart;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopCheckout;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoShopAddVoucherRequest;
import com.nyelam.android.http.NYDoShopListCartRequest;
import com.nyelam.android.http.NYDoShopRemoveProductCartRequest;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class DoShopCartFragment extends BasicFragment {

    private DoShopCartFragment thisFragment;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private CheckoutListener listener;
    private DoShopCartListAdapter adapter;
    private DoShopCartReturn cartReturn;

    @BindView(R.id.tv_total)
    TextView tvTotal;

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.ll_main_container)
    LinearLayout llMainContainer;

    @BindView(R.id.tv_sub_total)
    TextView tvSubTotal;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.tv_checkout) void checkOut(){
        listener.proceedToCheckOut();
    }

    @BindView(R.id.et_voucher_code)
    EditText etVoucherCode;


    @BindView(R.id.ll_voucher_container)
    LinearLayout llVoucherContainer;

    @BindView(R.id.tv_voucher_code)
    TextView tvVoucherCode;

    @BindView(R.id.tv_voucher_total)
    TextView tvVoucherTotal;

    @OnClick(R.id.tv_apply_voucher_code) void applyVoucher(){
        //listener.proceedToCheckOut();
        String voucher = etVoucherCode.getText().toString().trim();
        if (NYHelper.isStringNotEmpty(voucher) && cartReturn != null && NYHelper.isStringNotEmpty(cartReturn.getCartToken())){
            getApplyVoucher(cartReturn.getCartToken(), voucher);
        }
    }

    public DoShopCartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (CheckoutListener) getActivity();
        thisFragment = this;
        initAdapter();
    }

    private void initAdapter() {
        adapter = new DoShopCartListAdapter(getActivity(), thisFragment);

        //ADAPTER SERVICE LIST
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,spacingInPixels,0,spacingInPixels));
        recyclerView.setAdapter(adapter);

        getCartList(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCartList(true);
            }
        });

    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_do_shop_cart;
    }

    private void getCartList(boolean isRefresh){
        if (!isRefresh)progressBar.setVisibility(View.VISIBLE);
        NYDoShopListCartRequest req = null;
        try {
            req = new NYDoShopListCartRequest(getActivity());
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            tvNotFound.setVisibility(View.VISIBLE);
            e.printStackTrace();
        }
        spcMgr.execute(req, onCartListRequest());
    }

    private RequestListener<DoShopCartReturn> onCartListRequest() {
        return new RequestListener<DoShopCartReturn>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                llMainContainer.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);

            }

            @Override
            public void onRequestSuccess(DoShopCartReturn cartReturn) {
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                thisFragment.cartReturn = cartReturn;

                List<DoShopProduct> products = new ArrayList<DoShopProduct>();

                if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null){
                    for (DoShopMerchant merchant :cartReturn.getCart().getMerchants()){
                        if (merchant != null && merchant.getDoShopProducts() != null){
                            for (DoShopProduct product : merchant.getDoShopProducts()){
                                products.add(product);
                            }
                        }
                    }
                }

                adapter.setData(products);
                adapter.notifyDataSetChanged();

                initCartReturn(cartReturn);
                llMainContainer.setVisibility(View.VISIBLE);
            }
        };
    }

    protected void onRemoveItem(String productCartId){
        pDialog.show();
        NYDoShopRemoveProductCartRequest req = null;
        try {
            req = new NYDoShopRemoveProductCartRequest(getActivity(), productCartId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onRemoveItemRequest());
    }

    private RequestListener<DoShopCartReturn> onRemoveItemRequest() {
        return new RequestListener<DoShopCartReturn>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                pDialog.dismiss();
                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(DoShopCartReturn cartReturn) {
                pDialog.dismiss();

                List<DoShopProduct> products = new ArrayList<DoShopProduct>();

                if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null){
                    for (DoShopMerchant merchant :cartReturn.getCart().getMerchants()){
                        if (merchant != null && merchant.getDoShopProducts() != null){
                            for (DoShopProduct product : merchant.getDoShopProducts()){
                                products.add(product);
                            }
                        }
                    }
                }

                adapter.setData(products);
                adapter.notifyDataSetChanged();

                initCartReturn(cartReturn);
            }
        };
    }



    private void getApplyVoucher(String cartToken, String voucherCode){
        pDialog.show();
        NYDoShopAddVoucherRequest req = null;
        try {
            req = new NYDoShopAddVoucherRequest(getActivity(), cartToken, voucherCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onApplyVoucherRequest());
    }

    private RequestListener<DoShopCartReturn> onApplyVoucherRequest() {
        return new RequestListener<DoShopCartReturn>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                pDialog.dismiss();
                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(DoShopCartReturn cartReturn) {
                pDialog.dismiss();

                List<DoShopProduct> products = new ArrayList<DoShopProduct>();

                if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null){
                    for (DoShopMerchant merchant :cartReturn.getCart().getMerchants()){
                        if (merchant != null && merchant.getDoShopProducts() != null){
                            for (DoShopProduct product : merchant.getDoShopProducts()){
                                products.add(product);
                            }
                        }
                    }
                }

                adapter.setData(products);
                adapter.notifyDataSetChanged();


                initCartReturn(cartReturn);
            }
        };
    }



    private void initCartReturn(DoShopCartReturn cartReturn){
        if (cartReturn != null && cartReturn.getCart() != null){
            tvTotal.setText(NYHelper.priceFormatter(cartReturn.getCart().getTotal()));
            tvSubTotal.setText(NYHelper.priceFormatter(cartReturn.getCart().getSubTotal()));

            if (cartReturn.getCart().getVoucher() != null){
                if (NYHelper.isStringNotEmpty(cartReturn.getCart().getVoucher().getCode()))tvVoucherCode.setText("Voucher {"+cartReturn.getCart().getVoucher().getCode()+")");
                tvVoucherTotal.setText(" - "+NYHelper.priceFormatter(cartReturn.getCart().getVoucher().getValue()));
                llVoucherContainer.setVisibility(View.VISIBLE);
            } else {
                llVoucherContainer.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.setTitle("Cart");
        listener.stepView(0);
        if (!spcMgr.isStarted()) spcMgr.start(getActivity());
    }
}
