package com.nyelam.android.doshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.data.Variation;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopProductDetailRequest;
import com.nyelam.android.http.NYDoShopProductListRequest;
import com.nyelam.android.http.result.NYPaginationResult;
import com.nyelam.android.view.NYDialogAddToCart;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoShopDetailItemActivity extends BasicActivity implements NYDialogAddToCart.Listener {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopRecommendedAdapter recommendedAdapter;

    private String productId;
    private DoShopProduct product;

    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.ll_main_container) LinearLayout llMainContainer;
    @BindView(R.id.iv_item_image) ImageView ivItemImage;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.tv_review_point) TextView tvReviewPoint;
    @BindView(R.id.tv_write_review) TextView tvWriteReview;
    @BindView(R.id.tv_price) TextView tvPrice;
    @BindView(R.id.ll_price_container) LinearLayout llPriceContainer;
    @BindView(R.id.tv_price_strikethrough) TextView tvPriceStrikethrough;
    @BindView(R.id.tv_you_save ) TextView tvYouSave ;
    @BindView(R.id.tv_coinns) TextView tvCoinns;
    @BindView(R.id.spinner_item_size) Spinner spinnerItemSize;
    @BindView(R.id.et_quantity) EditText etQuantity;
    @BindView(R.id.tv_status) TextView tvStatus;
    @BindView(R.id.tv_add_to_basket) TextView tvAddToBasket;
    @BindView(R.id.tv_sent_date_one) TextView tvSentDateOne;
    @BindView(R.id.tv_sent_date_two) TextView tvSentDateTwo;
    @BindView(R.id.tv_product_description_title) TextView tvProductDescriptionTitle;
    @BindView(R.id.tv_product_description) TextView tvProductDescription;
    @BindView(R.id.ll_related_item) LinearLayout llRelatedItem;
    @BindView(R.id.rv_related_item) RecyclerView rvRelatedItem;

    @OnClick(R.id.tv_add_to_basket)void addToBasket(){
        NYDialogAddToCart dialog = new NYDialogAddToCart();
        dialog.showAddToCartDialog(this, product);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_detail_item);
        ButterKnife.bind(this);
        context = this;
        initExtra();
        if (product != null){
            progressBar.setVisibility(View.VISIBLE);
            llMainContainer.setVisibility(View.GONE);
            initProductDetail(product.getId());
        } else {

            Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
            dialogItemNotAvailable();
        }
    }

    private void initProductDetail(String id){

        Toast.makeText(context, "request detail", Toast.LENGTH_SHORT).show();

        NYDoShopProductDetailRequest req = null;
        try {
            req = new NYDoShopProductDetailRequest(context, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onProductDetailRequest());
    }

    private RequestListener<DoShopProduct> onProductDetailRequest() {
        return new RequestListener<DoShopProduct>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                //NYHelper.handleAPIException(context, spiceException, null);
                //swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                llMainContainer.setVisibility(View.GONE);
                dialogItemNotAvailable();
            }

            @Override
            public void onRequestSuccess(DoShopProduct nProduct) {
                //progressBar.setVisibility(View.GONE);
                //swipeRefreshLayout.setRefreshing(false);
                product = nProduct;
                setProductView(product);
                progressBar.setVisibility(View.GONE);
                llMainContainer.setVisibility(View.VISIBLE);
                if (product != null && product.getCategories() != null && product.getCategories().size() > 0 && NYHelper.isStringNotEmpty(product.getCategories().get(0).getId()))
                    initRelatedItem(product.getCategories().get(0).getId());


                List<Variation> sizes = new ArrayList<>();
                sizes.add(new Variation("1", "L"));
                sizes.add(new Variation("2", "XL"));
                final SizeSpinAdapter sizeSpinAdapter = new SizeSpinAdapter(DoShopDetailItemActivity.this, sizes);
                spinnerItemSize.setAdapter(sizeSpinAdapter);
            }
        };
    }

    private void initRelatedItem(String categoryId){
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
                llRelatedItem.setVisibility(View.GONE);
                NYLog.e("cek related error");
            }

            @Override
            public void onRequestSuccess(NYPaginationResult<DoShopProductList> listNYPaginationResult) {
                //progressBar.setVisibility(View.GONE);
                //swipeRefreshLayout.setRefreshing(false);
                NYLog.e("cek related success");
                if (listNYPaginationResult != null && listNYPaginationResult.item != null && listNYPaginationResult.item.getList() != null &&
                        listNYPaginationResult.item.getList().size() > 0){
                    NYLog.e("cek related data : "+listNYPaginationResult.item.getList().toString());

                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    //GridLayoutManager layoutManager = new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false);
                    rvRelatedItem.setLayoutManager(layoutManager);

                    recommendedAdapter = new DoShopRecommendedAdapter(context, listNYPaginationResult.item.getList(), true);
                    rvRelatedItem.setAdapter(recommendedAdapter);
                    recommendedAdapter.notifyDataSetChanged();
                    llRelatedItem.setVisibility(View.VISIBLE);
                } else {
                    llRelatedItem.setVisibility(View.GONE);
                }

            }
        };
    }

    private void initExtra() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if(intent.hasExtra(NYHelper.PRODUCT)) {
                try {
                    JSONObject obj = new JSONObject(intent.getStringExtra(NYHelper.PRODUCT));
                    product = new DoShopProduct();
                    product.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    public void setProductView(DoShopProduct product) {
        if (product != null){

            if (NYHelper.isStringNotEmpty(product.getStatus())){
                tvStatus.setText(product.getStatus());
                if (product.getStatus().equals("out stock"))tvStatus.setTextColor(getResources().getColor(R.color.red));
            }
            if (NYHelper.isStringNotEmpty(product.getProductName())){
                tvName.setText(product.getProductName());
                tvProductDescriptionTitle.setText("Product description "+product.getProductName());
            }
            if (NYHelper.isStringNotEmpty(product.getDescription()))tvProductDescription.setText(product.getDescription());
            if (product.getSpecialPrice() < product.getNormalPrice()){
                tvPrice.setText(NYHelper.priceFormatter(product.getSpecialPrice()));
                tvPriceStrikethrough.setText(NYHelper.priceFormatter(product.getNormalPrice()));

                double value = product.getNormalPrice()-product.getSpecialPrice();
                double percent = value/product.getNormalPrice();
                tvYouSave.setText("You Save : "+String.format("%.0f", percent));

                llPriceContainer.setVisibility(View.VISIBLE);
            } else {
                tvPrice.setText(NYHelper.priceFormatter(product.getNormalPrice()));
                llPriceContainer.setVisibility(View.GONE);
            }


            if (product.getVariations() != null && product.getVariations().getSizes() != null){
                final SizeSpinAdapter sizeSpinAdapter = new SizeSpinAdapter(this, product.getVariations().getSizes());
                spinnerItemSize.setAdapter(sizeSpinAdapter);
                spinnerItemSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        // Here you get the current item (a User object) that is selected by its position
                        Variation variation = sizeSpinAdapter.getItem(position);
                        if (NYHelper.isStringNotEmpty(variation.getName()))Toast.makeText(context, variation.getName(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {  }
                });
            }



            if (NYHelper.isStringNotEmpty(product.getFeaturedImage())){
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
                ImageLoader.getInstance().loadImage(product.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        ivItemImage.setImageResource(R.drawable.example_pic);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        ivItemImage.setImageResource(R.drawable.example_pic);
                    }
                });

                ImageLoader.getInstance().displayImage(product.getFeaturedImage(), ivItemImage, NYHelper.getOption());
            }

        }
    }

    private void dialogItemNotAvailable(){
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
    public void onShopAgainListener() {
        finish();
    }

    @Override
    public void onPayNowListener(DoShopProduct product) {
        Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DoShopCheckoutActivity.class);
        startActivity(intent);
    }

}
