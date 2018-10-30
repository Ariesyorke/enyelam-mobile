package com.nyelam.android.doshop;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.docourse.DoCourseActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopProductListRequest;
import com.nyelam.android.http.result.NYPaginationResult;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoShopCategoryActivity extends BasicActivity {

    private boolean isRefresh = false;
    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopRecommendedAdapter adapter;

    private String categoryId;
    private DoShopCategory category;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.tv_not_found)
    TextView tvNotFound;

    @BindView(R.id.rv_item_list)
    RecyclerView rvItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_category);
        ButterKnife.bind(this);
        context = this;
        initExtra();

        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);
        rvItemList.setLayoutManager(layoutManager);
        adapter = new DoShopRecommendedAdapter(context);
        rvItemList.setAdapter(adapter);

        if (category != null && NYHelper.isStringNotEmpty(category.getId())){
            progressBar.setVisibility(View.VISIBLE);
            initListItem(category.getId());
        } else {
            dialogCategoryNotAvailable();
        }
    }

    private void initListItem(String categoryId){
        NYLog.e("cek related 1");
        NYDoShopProductListRequest req = new NYDoShopProductListRequest(context, "1", null, categoryId, "40000",  "500000", "1");
        spcMgr.execute(req, onRealtedItemRequest());
    }

    private RequestListener<NYPaginationResult<DoShopProductList>> onRealtedItemRequest() {
        return new RequestListener<NYPaginationResult<DoShopProductList>>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                //swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                NYLog.e("cek related error");
                if (adapter.getItemCount() == 0) tvNotFound.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRequestSuccess(NYPaginationResult<DoShopProductList> listNYPaginationResult) {
                //progressBar.setVisibility(View.GONE);
                //swipeRefreshLayout.setRefreshing(false);
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
                }

                if (adapter.getItemCount() == 0) tvNotFound.setVisibility(View.VISIBLE);

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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
