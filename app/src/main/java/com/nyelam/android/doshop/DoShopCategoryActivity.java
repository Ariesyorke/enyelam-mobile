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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.BrandList;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.data.Filter;
import com.nyelam.android.data.Price;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.doshoporder.DoShopCheckoutActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopProductFilter;
import com.nyelam.android.http.NYDoShopProductListRequest;
import com.nyelam.android.http.result.NYPaginationResult;
import com.nyelam.android.storage.CartStorage;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.view.CircularTextView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private String recommendation;
    private int page = 1;
    private String categoryId;
    private DoShopCategory category;
    private String sortBy = "0";
    private String minPrice = "0";
    private String maxPrice = "10000000";

    private String minPriceDefault = "0";
    private String maxPriceDefault = "10000000";
    private BrandList brandList;
    private List<String> selectedBrands;
    private TextWatcher keywordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s.toString()) && s.toString().length() > 2) {
                page = 1;
                initProductFilter(null, s.toString());
            }
        }
    };

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @BindView(R.id.rv_item_list)
    RecyclerView rvItemList;

    @BindView(R.id.refresh_swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.filter_linearLayout)
    LinearLayout llFilter;

    @OnClick(R.id.ll_filter) void filter(){
        Intent intent = new Intent(this, DoShopFilterActivity.class);
        intent.putExtra(NYHelper.SORT_BY, sortBy);
        intent.putExtra(NYHelper.MIN_PRICE, Double.valueOf(minPrice));
        intent.putExtra(NYHelper.MIN_PRICE_DEAFULT, Double.valueOf(minPriceDefault));
        intent.putExtra(NYHelper.MAX_PRICE, Double.valueOf(maxPrice));
        intent.putExtra(NYHelper.MAX_PRICE_DEFAULT, Double.valueOf(maxPriceDefault));
        if(selectedBrands != null && !selectedBrands.isEmpty()) {
            String[] brands = new String[selectedBrands.size()];
            brands = selectedBrands.toArray(brands);
            intent.putExtra(NYHelper.SELECTED_BRANDS, brands);
        }
        if(brandList != null) {
            intent.putExtra(NYHelper.FILTER_BRANDS, brandList.toString());
        }
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.ll_sort_by) void sortBy(){
        Intent intent = new Intent(this, DoShopFilterActivity.class);
        intent.putExtra(NYHelper.SORT_BY, sortBy);
        intent.putExtra(NYHelper.MIN_PRICE, Double.valueOf(minPrice));
        intent.putExtra(NYHelper.MIN_PRICE_DEAFULT, Double.valueOf(minPriceDefault));
        intent.putExtra(NYHelper.MAX_PRICE, Double.valueOf(maxPrice));
        intent.putExtra(NYHelper.MAX_PRICE_DEFAULT, Double.valueOf(maxPriceDefault));
        if(selectedBrands != null && !selectedBrands.isEmpty()) {
            intent.putExtra(NYHelper.SELECTED_BRANDS, selectedBrands.toArray());
        }
        if(brandList != null) {
            intent.putExtra(NYHelper.FILTER_BRANDS, brandList.toString());
        }
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.filter_linearLayout) void intentFilter(){
        Intent intent = new Intent(this, DoShopFilterActivity.class);
        intent.putExtra(NYHelper.SORT_BY, sortBy);
        intent.putExtra(NYHelper.MIN_PRICE, Double.valueOf(minPrice));
        intent.putExtra(NYHelper.MIN_PRICE_DEAFULT, Double.valueOf(minPriceDefault));
        intent.putExtra(NYHelper.MAX_PRICE, Double.valueOf(maxPrice));
        intent.putExtra(NYHelper.MAX_PRICE_DEFAULT, Double.valueOf(maxPriceDefault));
        if(selectedBrands != null && !selectedBrands.isEmpty()) {
            intent.putExtra(NYHelper.SELECTED_BRANDS, selectedBrands.toArray());
        }
        if(brandList != null) {
            intent.putExtra(NYHelper.FILTER_BRANDS, brandList.toString());
        }
        startActivityForResult(intent, 1);
    }


    @BindView(R.id.tv_cart_count)
    CircularTextView tvCartCount;

    @BindView(R.id.iv_cart)
    ImageView ivCart;

    @OnClick(R.id.iv_cart) void intentToCart(){
        LoginStorage storage = new LoginStorage(this);
        if (storage.isUserLogin()){
            startActivity(new Intent(context, DoShopCheckoutActivity.class));
        } else {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivityForResult(intent, NYHelper.LOGIN_REQ);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_category);
        ButterKnife.bind(this);
        context = this;
        initToolbar();
        initExtra();
        initList();
        initProductFilter(categoryId, null);
        initControl();
    }

    private void initList() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        rvItemList.setLayoutManager(layoutManager);
        adapter = new DoShopRecommendedAdapter(context);
        rvItemList.setAdapter(adapter);

    }

    private void initControl() {
        etSearch.removeTextChangedListener(keywordTextWatcher);
        etSearch.addTextChangedListener(keywordTextWatcher);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Toast.makeText(context, "refresh : "+category.getId(), Toast.LENGTH_SHORT).show();
                page = 1;
                loadmore = true;
                swipeRefreshLayout.setRefreshing(true);
                adapter.clear();
                adapter.notifyDataSetChanged();

                if (category != null && NYHelper.isStringNotEmpty(category.getId())){
                    initListItem(category.getId(), etSearch.getText().toString());
                } else {
                    initListItem(null, etSearch.getText().toString());
                }
            }
        });

        layoutManager = ((LinearLayoutManager)rvItemList.getLayoutManager());
        rvItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition >= recyclerView.getAdapter().getItemCount() - 1) {
                    if (loadmore) {
                        loadmore = false;
                        initListItem(categoryId, etSearch.getText().toString());
                    }
                }
            }
        });
    }

    private void initListItem(String categoryId, String keyword){
        if(TextUtils.isEmpty(categoryId) && TextUtils.isEmpty(keyword)) return;
        if(page == 1) swipeRefreshLayout.setRefreshing(true);
        NYDoShopProductListRequest req = new NYDoShopProductListRequest(context, String.valueOf(page), keyword, categoryId, minPrice,  maxPrice, sortBy, selectedBrands, null, recommendation);
        spcMgr.execute(req, onRealtedItemRequest());
    }

    private RequestListener<NYPaginationResult<DoShopProductList>> onRealtedItemRequest() {
        return new RequestListener<NYPaginationResult<DoShopProductList>>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                loadmore = false;
                swipeRefreshLayout.setRefreshing(false);
                if (adapter.getItemCount() == 0) tvNotFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(NYPaginationResult<DoShopProductList> listNYPaginationResult) {
                loadmore = true;
                swipeRefreshLayout.setRefreshing(false);
                if (listNYPaginationResult != null && listNYPaginationResult.item != null && listNYPaginationResult.item.getList() != null &&
                        listNYPaginationResult.item.getList().size() > 0) {
                    adapter.addData(listNYPaginationResult.item.getList());

                    adapter.notifyDataSetChanged();
                    page++;
                }

                if (adapter.getItemCount() == 0){
                    llFilter.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.VISIBLE);
                } else {
                    llFilter.setVisibility(View.VISIBLE);
                    tvNotFound.setVisibility(View.GONE);
                }

            }
        };
    }


    private void initProductFilter(String categoryId, String keyword) {
        if(TextUtils.isEmpty(categoryId) && TextUtils.isEmpty(keyword)) return;
        swipeRefreshLayout.setRefreshing(true);
        llFilter.setVisibility(View.GONE);
        List<String> categories = null;
        if(!TextUtils.isEmpty(categoryId)) {
            categories = new ArrayList<>();
            categories.add(categoryId);
        }
        NYDoShopProductFilter req = new NYDoShopProductFilter(context, categories, keyword, null, null);
        spcMgr.execute(req, onGetProductFilterRequest(categoryId));
    }

    private RequestListener<Filter> onGetProductFilterRequest(final String categoryId) {
        return new RequestListener<Filter>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                loadmore = false;
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onRequestSuccess(Filter filter) {
                loadmore = false;
                adapter.clear();
                adapter.notifyDataSetChanged();

                if(filter.getBrandList() != null) {
                    brandList = filter.getBrandList();
                }

                if (filter.getPrice() != null) {
                    Price price = filter.getPrice();
                    minPrice = String.valueOf(price.getLowestPrice());
                    minPriceDefault = String.valueOf(price.getLowestPrice());
                    maxPrice = String.valueOf(price.getHighestPrice());
                    maxPriceDefault = String.valueOf(price.getHighestPrice());
                    initListItem(categoryId, etSearch.getText().toString());
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    tvNotFound.setVisibility(View.VISIBLE);
                    llFilter.setVisibility(View.GONE);
               }

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
                    if (category != null)categoryId = category.getId();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if(intent.hasExtra(NYHelper.RECOMMENDED)) {
                recommendation = intent.getStringExtra(NYHelper.RECOMMENDED);
            }
        }
        if (category == null && (recommendation == null || TextUtils.isEmpty(recommendation))) {
            etSearch.setVisibility(View.VISIBLE);
            etSearch.setClickable(true);
        } else {
            etSearch.setVisibility(View.INVISIBLE);
        }
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

        // TODO: check cart di cache
        LoginStorage loginStorage = new LoginStorage(this);
        if (loginStorage.isUserLogin()) {
            CartStorage storage = new CartStorage(context);
            if (storage.getSize() > 0) {
                tvCartCount.setText(String.valueOf(storage.getSize()));
                tvCartCount.setVisibility(View.VISIBLE);
                int margin = (int) getResources().getDimension(R.dimen.padding);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivCart.getLayoutParams();
                params.setMargins(0, 0, margin, 0);
                ivCart.setLayoutParams(params);
            } else {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivCart.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                ivCart.setLayoutParams(params);
                tvCartCount.setVisibility(View.GONE);
            }
        } else {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivCart.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            ivCart.setLayoutParams(params);
            tvCartCount.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Bundle b = data.getExtras();
            if (data.hasExtra(NYHelper.SORT_BY)) sortBy = b.getString(NYHelper.SORT_BY);
            if (data.hasExtra(NYHelper.MIN_PRICE)) minPrice =   String.valueOf(b.getDouble(NYHelper.MIN_PRICE, 0));
            if (data.hasExtra(NYHelper.MAX_PRICE)) maxPrice = String.valueOf(b.getDouble(NYHelper.MAX_PRICE, 1000000));
            if (data.hasExtra(NYHelper.SELECTED_BRANDS)) {
                String[] brands = data.getStringArrayExtra(NYHelper.SELECTED_BRANDS);
                selectedBrands = Arrays.asList(brands);
            } else {
                selectedBrands = null;
            }
            page = 1;
            adapter.clear();
            adapter.notifyDataSetChanged();
        }

    }
}
