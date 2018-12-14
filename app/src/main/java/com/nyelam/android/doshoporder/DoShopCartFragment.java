package com.nyelam.android.doshoporder;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopMerchantList;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoShopAddVoucherRequest;
import com.nyelam.android.http.NYDoShopChangeQuantityRequest;
import com.nyelam.android.http.NYDoShopListCartRequest;
import com.nyelam.android.http.NYDoShopRemoveProductCartRequest;
import com.nyelam.android.storage.CartStorage;
import com.nyelam.android.view.NYCartItemView;
import com.nyelam.android.view.NYCartItemViewListener;
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

//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;

    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.ll_cart_item_container)
    LinearLayout cartItemContainer;

    @OnClick(R.id.tv_checkout) void checkOut(){
        if (cartReturn != null)listener.proceedToCheckOut(cartReturn);
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
            // Check if no view has focus:
            NYHelper.hideKeyboard(getActivity());
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
        initView();
    }

    private void initCartItems(List<DoShopProduct> products) {
        cartItemContainer.removeAllViews();

        for(int i = 0; i < products.size(); i++) {
            NYCartItemView view = new NYCartItemView(getActivity());
            cartItemContainer.addView(view);
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            if(i == 0) {
                view.setPadding(0, 0, 0, 8);
            } else {
                view.setPadding(0, 8, 0, 8);
            }
            view.initData(products.get(i), getActivity(), new NYCartItemViewListener() {
                @Override
                public void onQuantityChange(String productCartId, String quantity) {
                    DoShopCartFragment.this.onQuantityChange(productCartId, quantity);
                }

                @Override
                public void onRemoveItem(String productCartId) {
                    DoShopCartFragment.this.onRemoveItem(productCartId);
                }
            });
        }
    }

    private void initView() {
//        adapter = new DoShopCartListAdapter(getActivity(), thisFragment);
//
//        //ADAPTER SERVICE LIST
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding);
//        LinearLayoutManager layoutManager
//                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,spacingInPixels,0,spacingInPixels));
//        recyclerView.setAdapter(adapter);

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
                pDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                llMainContainer.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(DoShopCartReturn cartReturn) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                thisFragment.cartReturn = cartReturn;

                if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null && cartReturn.getCart().getMerchants().size() > 0){
                    initCartReturn(cartReturn);
                    llMainContainer.setVisibility(View.VISIBLE);
                } else {
                    llMainContainer.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.VISIBLE);
                }
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
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                llMainContainer.setVisibility(View.GONE);
                tvNotFound.setVisibility(View.VISIBLE);
                if(!spiceException.getCause().getMessage().trim().toLowerCase().equals("unknown error")) NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(DoShopCartReturn cartReturn) {
                pDialog.dismiss();

                thisFragment.cartReturn = cartReturn;

                if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null && cartReturn.getCart().getMerchants().size() > 0){
                    initCartReturn(cartReturn);
                    llMainContainer.setVisibility(View.VISIBLE);

                    DoShopMerchantList merchantList = new DoShopMerchantList();
                    merchantList.setList(cartReturn.getCart().getMerchants());
                    CartStorage storage = new CartStorage(getActivity());
                    storage.setMerchants(merchantList);
                    storage.save();

                } else {
                    llMainContainer.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.VISIBLE);

                    CartStorage storage = new CartStorage(getActivity());
                    storage.setMerchants(null);
                    storage.save();
                }
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

                etVoucherCode.setText("");
                thisFragment.cartReturn = cartReturn;

                if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null && cartReturn.getCart().getMerchants().size() > 0){
                    initCartReturn(cartReturn);
                    llMainContainer.setVisibility(View.VISIBLE);
                } else {
                    llMainContainer.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.VISIBLE);
                }
            }
        };
    }



    private void initCartReturn(DoShopCartReturn cartReturn){

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
        initCartItems(products);
//        adapter.setData(products);
//        adapter.notifyDataSetChanged();


        if (cartReturn != null && cartReturn.getCart() != null){
            tvTotal.setText(NYHelper.priceFormatter(cartReturn.getCart().getTotal()));
            tvSubTotal.setText(NYHelper.priceFormatter(cartReturn.getCart().getSubTotal()));

            if (cartReturn.getCart().getVoucher() != null){
                if (NYHelper.isStringNotEmpty(cartReturn.getCart().getVoucher().getCode()))tvVoucherCode.setText("Voucher ("+cartReturn.getCart().getVoucher().getCode()+")");
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
        if (pDialog != null)pDialog.cancel();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.setTitle("Cart");
        listener.stepView(0);
        if (!spcMgr.isStarted()) spcMgr.start(getActivity());
    }

    public void onQuantityChange(String id, String item) {
        pDialog.show();
        NYDoShopChangeQuantityRequest req = null;
        try {
            req = new NYDoShopChangeQuantityRequest(getActivity(), id, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onQuantityChangeRequest());
    }

    private RequestListener<DoShopCartReturn> onQuantityChangeRequest() {
        return new RequestListener<DoShopCartReturn>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                pDialog.dismiss();
                NYHelper.handleAPIException(getActivity(), spiceException, null);
                initCartReturn(cartReturn);
            }

            @Override
            public void onRequestSuccess(DoShopCartReturn cartReturn) {
                pDialog.dismiss();

                etVoucherCode.setText("");
                thisFragment.cartReturn = cartReturn;

                initCartReturn(cartReturn);

                if (cartReturn != null && cartReturn.getCart() != null && cartReturn.getCart().getMerchants() != null && cartReturn.getCart().getMerchants().size() > 0){
                    DoShopMerchantList merchantList = new DoShopMerchantList();
                    merchantList.setList(cartReturn.getCart().getMerchants());
                    CartStorage storage = new CartStorage(getActivity());
                    storage.setMerchants(merchantList);
                    storage.save();
                } else {
                    CartStorage storage = new CartStorage(getActivity());
                    storage.setMerchants(null);
                    storage.save();
                }

            }
        };
    }

}
