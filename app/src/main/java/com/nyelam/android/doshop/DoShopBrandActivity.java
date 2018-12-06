package com.nyelam.android.doshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Brand;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.doshoporder.DoShopCheckoutActivity;
import com.nyelam.android.helper.NYHelper;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoShopBrandActivity extends BasicActivity {

    private boolean isRefresh = false;
    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopRecommendedAdapter adapter;
    private boolean loadmore=true;
    private LinearLayoutManager layoutManager;

    private int page = 1;
    private String brandId = null;
    private Brand brand;
    private String sortBy = "1";
    private String minPrice = "0";
    private String maxPrice = "10000000";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

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
        intent.putExtra(NYHelper.MAX_PRICE, Double.valueOf(maxPrice));
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.ll_sort_by) void sortBy(){
        Intent intent = new Intent(this, DoShopFilterActivity.class);
        intent.putExtra(NYHelper.SORT_BY, sortBy);
        intent.putExtra(NYHelper.MIN_PRICE, Double.valueOf(minPrice));
        intent.putExtra(NYHelper.MAX_PRICE, Double.valueOf(maxPrice));
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.filter_linearLayout) void intentFilter(){
        Intent intent = new Intent(this, DoShopFilterActivity.class);
        intent.putExtra(NYHelper.SORT_BY, sortBy);
        intent.putExtra(NYHelper.MIN_PRICE, Double.valueOf(minPrice));
        intent.putExtra(NYHelper.MAX_PRICE, Double.valueOf(maxPrice));
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
        setContentView(R.layout.activity_do_shop_brand);
        ButterKnife.bind(this);
        context = this;
        initExtra();
        initToolbar();

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        rvItemList.setLayoutManager(layoutManager);
        adapter = new DoShopRecommendedAdapter(context);
        rvItemList.setAdapter(adapter);

        if (brand != null && NYHelper.isStringNotEmpty(brand.getId())){
            initListItem(null, null);
        } else {
            //dialogCategoryNotAvailable();
            initListItem(null, null);
        }

        initControl();
    }

    private void initControl() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                adapter.clear();
                adapter.notifyDataSetChanged();
                initListItem(brandId, s.toString());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                loadmore = true;
                swipeRefreshLayout.setRefreshing(true);
                adapter.clear();
                adapter.notifyDataSetChanged();
                initListItem(brandId, etSearch.getText().toString());
            }
        });

        layoutManager = ((LinearLayoutManager)rvItemList.getLayoutManager());
        rvItemList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisiblePosition == recyclerView.getChildCount()) {
                    if (loadmore) {
                        loadmore = false;
                        initListItem(brandId, etSearch.getText().toString());
                    }
                }
            }
        });
    }

    private void initListItem(String categoryId, String keyword){
        progressBar.setVisibility(View.VISIBLE);
        NYDoShopProductListRequest req = new NYDoShopProductListRequest(context, String.valueOf(page), keyword, categoryId, minPrice,  maxPrice, sortBy);
        spcMgr.execute(req, onRealtedItemRequest());
    }

    private RequestListener<NYPaginationResult<DoShopProductList>> onRealtedItemRequest() {
        return new RequestListener<NYPaginationResult<DoShopProductList>>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                loadmore = false;
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                NYLog.e("cek related error");
                if (adapter.getItemCount() == 0) tvNotFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(NYPaginationResult<DoShopProductList> listNYPaginationResult) {
                loadmore = true;
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                NYLog.e("cek related success");
                if (listNYPaginationResult != null && listNYPaginationResult.item != null && listNYPaginationResult.item.getList() != null &&
                        listNYPaginationResult.item.getList().size() > 0){
                    NYLog.e("cek related data : "+listNYPaginationResult.item.getList().toString());

                    if (isRefresh)
                        adapter.setData(listNYPaginationResult.item.getList());
                    else
                        adapter.addData(listNYPaginationResult.item.getList());
                    adapter.notifyDataSetChanged();

                    page++;
                }

                if (adapter.getItemCount() == 0){
                    tvNotFound.setVisibility(View.VISIBLE);
                } else {
                    tvNotFound.setVisibility(View.GONE);
                    llFilter.setVisibility(View.VISIBLE);
                }

            }
        };
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.BRAND)) {
                try {
                    JSONObject obj = new JSONObject(intent.getStringExtra(NYHelper.BRAND));
                    brand = new Brand();
                    brand.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private void dialogCategoryNotAvailable(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.logout));
        builder.setMessage(getString(R.string.warn_item_not_available));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
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
        CartStorage storage = new CartStorage(context);
        if (storage.getSize() > 0){
            tvCartCount.setText(String.valueOf(storage.getSize()));
            tvCartCount.setVisibility(View.VISIBLE);
            int margin = (int)getResources().getDimension(R.dimen.padding);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivCart.getLayoutParams();
            params.setMargins(0, 0, margin, 0);
            ivCart.setLayoutParams(params);
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);

        Toast.makeText(context, "init name", Toast.LENGTH_SHORT).show();
        if (brand != null && NYHelper.isStringNotEmpty(brand.getName())){
            Toast.makeText(context, "name found", Toast.LENGTH_SHORT).show();
            tvTitle.setText(brand.getName());
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(brand.getName());
            this.setTitle(brand.getName());
        }
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

        //Toast.makeText(context, "tes 1", Toast.LENGTH_SHORT).show();

        if (resultCode == Activity.RESULT_OK && data != null) {
            //Toast.makeText(context, "tes 2", Toast.LENGTH_SHORT).show();
            Bundle b = data.getExtras();
            if (data.hasExtra(NYHelper.SORT_BY)) sortBy = b.getString(NYHelper.SORT_BY);
            if (data.hasExtra(NYHelper.MIN_PRICE)) minPrice =   String.valueOf(b.getDouble(NYHelper.MIN_PRICE, 0));
            if (data.hasExtra(NYHelper.MAX_PRICE)) maxPrice = String.valueOf(b.getDouble(NYHelper.MAX_PRICE, 1000000));


            adapter.clear();
            adapter.notifyDataSetChanged();

            if (brand != null && NYHelper.isStringNotEmpty(brand.getId())){
                initListItem(brand.getId(), null);
            } else {
                //dialogCategoryNotAvailable();
                initListItem(null, null);
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            //Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
            llFilter.setVisibility(View.GONE);
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            //Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
            llFilter.setVisibility(View.VISIBLE);
        }
    }
}
