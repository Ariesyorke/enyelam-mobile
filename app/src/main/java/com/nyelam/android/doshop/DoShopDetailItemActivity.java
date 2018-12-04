package com.nyelam.android.doshop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.etiennelawlor.imagegallery.library.activities.ImageGalleryActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.data.Variation;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.doshoporder.DoShopCheckoutActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoShopAddToCartRequest;
import com.nyelam.android.http.NYDoShopProductDetailRequest;
import com.nyelam.android.http.NYDoShopProductListRequest;
import com.nyelam.android.http.result.NYPaginationResult;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.view.NYDialogAddToCart;
import com.nyelam.android.view.NYSpinner;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.stfalcon.frescoimageviewer.ImageViewer;

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
    private ArrayAdapter qtyAdapterColor;
    private ArrayAdapter qtyAdapterSize;


    private String productId;
    private DoShopProduct product;
    //private List<Variation> chosenVariations;

    private Variation chosenVariationSize;
    private Variation chosenVariationColor;
    private int chosenQtySize;
    private int chosenQtyColor;

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
    @BindView(R.id.spinner_item_color)
    NYSpinner spinnerItemColor;
    @BindView(R.id.spinner_item_size)
    NYSpinner spinnerItemSize;
    @BindView(R.id.spinner_quantity_color) NYSpinner spinnerQuantityColor;
    @BindView(R.id.et_quantity_color) EditText etQuantityColor;
    @BindView(R.id.spinner_quantity_size) NYSpinner spinnerQuantitySize;
    @BindView(R.id.et_quantity_size) EditText etQuantitySize;
    @BindView(R.id.tv_status) TextView tvStatus;
    @BindView(R.id.tv_add_to_basket) TextView tvAddToBasket;
    @BindView(R.id.tv_sent_date_one) TextView tvSentDateOne;
    @BindView(R.id.tv_sent_date_two) TextView tvSentDateTwo;
    @BindView(R.id.tv_product_description_title) TextView tvProductDescriptionTitle;
    @BindView(R.id.tv_product_description) TextView tvProductDescription;
    @BindView(R.id.ll_related_item) LinearLayout llRelatedItem;
    @BindView(R.id.ll_container_color) LinearLayout llContainerColor;
    @BindView(R.id.ll_container_size) LinearLayout llContainerSize;
    @BindView(R.id.rv_related_item) RecyclerView rvRelatedItem;

    @OnClick(R.id.tv_add_to_basket)void addToBasket(){
//        NYDialogAddToCart dialog = new NYDialogAddToCart();
//        dialog.showAddToCartDialog(this, product);

        LoginStorage storage = new LoginStorage(this);
        if (storage.isUserLogin() && product != null && NYHelper.isStringNotEmpty(product.getId())){
            addToCart(product.getId(),chosenVariationSize, chosenVariationColor, String.valueOf(chosenQtySize));
        } else if (product != null && NYHelper.isStringNotEmpty(product.getId())){
            Intent intent = new Intent(this, AuthActivity.class);
            startActivityForResult(intent, NYHelper.LOGIN_REQ);
        }

    }

    @OnClick(R.id.iv_item_image) void showImage(){
        if (product != null && NYHelper.isStringNotEmpty(product.getFeaturedImage())){
            ArrayList<String> images = new ArrayList<>();
            images.add(product.getFeaturedImage());

            if (product.getImages() != null && product.getImages().size() > 0){
                for (String im : product.getImages()){
                    if (NYHelper.isStringNotEmpty(im))images.add(im);
                }

                Intent intent = new Intent(DoShopDetailItemActivity.this, DoShopDetailItemImagesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(DoShopDetailItemImagesActivity.KEY_IMAGES, images);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_detail_item);
        ButterKnife.bind(this);
        //chosenVariations = new ArrayList<>();
        context = this;
        initExtra();
        if (product != null){
            progressBar.setVisibility(View.VISIBLE);
            llMainContainer.setVisibility(View.GONE);
            initProductDetail(product.getId());
        } else {
            //Toast.makeText(context, "not found", Toast.LENGTH_SHORT).show();
            dialogItemNotAvailable();
        }
    }

    private void initProductDetail(String id){

        //Toast.makeText(context, "request detail", Toast.LENGTH_SHORT).show();

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

                if (product != null && product.getCategories() != null && product.getCategories().size() > 0 && NYHelper.isStringNotEmpty(product.getCategories().get(0).getId())){
                    initRelatedItem(product.getCategories().get(0).getId());
                }

                if (product != null && product.getVariations() != null && product.getVariations().getColors() != null && product.getVariations().getColors().size() > 0){
                    final SizeSpinAdapter colorSpinAdapter = new SizeSpinAdapter(DoShopDetailItemActivity.this, product.getVariations().getColors());
                    spinnerItemColor.setAdapter(colorSpinAdapter);
                    llContainerColor.setVisibility(View.VISIBLE);
                } else {
                    llContainerColor.setVisibility(View.GONE);
                }

                if (product != null && product.getVariations() != null && product.getVariations().getSizes() != null){
                    final SizeSpinAdapter sizeSpinAdapter = new SizeSpinAdapter(DoShopDetailItemActivity.this, product.getVariations().getSizes());
                    spinnerItemSize.setAdapter(sizeSpinAdapter);
                    llContainerSize.setVisibility(View.VISIBLE);
                } else {
                    llContainerSize.setVisibility(View.GONE);
                }

                if (product == null || product.getVariations() == null || ((product.getVariations().getSizes() == null || product.getVariations().getSizes().size() <= 0) && (product.getVariations().getColors() == null || product.getVariations().getColors().size() <= 0)) ){
                    // TODO: hide add to cart
                    tvAddToBasket.setVisibility(View.GONE);
                    dialogItemNotAvailable();
                }

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


    private void addToCart(String productId, Variation variationSize, Variation variationColor, String qty){
        //NYLog.e("cek related 1");
        pDialog.show();
        NYDoShopAddToCartRequest req = null;
        try {
            req = new NYDoShopAddToCartRequest(context, productId, variationSize, variationColor, qty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onAddToCartRequest());
    }

    private RequestListener<DoShopCartReturn> onAddToCartRequest() {
        return new RequestListener<DoShopCartReturn>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                pDialog.dismiss();
                NYHelper.handleAPIException(context, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DoShopCartReturn cart) {
                pDialog.dismiss();

                NYDialogAddToCart dialog = new NYDialogAddToCart();
                dialog.showAddToCartDialog(DoShopDetailItemActivity.this, product);
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


    public void setProductView(final DoShopProduct product) {
        if (product != null){

            if (NYHelper.isStringNotEmpty(product.getStatus())){
                tvStatus.setText(product.getStatus());
                if (product.getStatus().equals("out stock"))tvStatus.setTextColor(getResources().getColor(R.color.red));
            }
            if (NYHelper.isStringNotEmpty(product.getProductName())){
                tvName.setText(product.getProductName());
                tvProductDescriptionTitle.setText("Product description "+product.getProductName());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && NYHelper.isStringNotEmpty(product.getDescription())) {
                tvProductDescription.setText(Html.fromHtml(product.getDescription()));
            } else if (NYHelper.isStringNotEmpty(product.getDescription())){
                tvProductDescription.setText(Html.fromHtml(product.getDescription()));
            }


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

            if (product.getVariations() != null && product.getVariations().getColors() != null){

                final SizeSpinAdapter colorSpinAdapter = new SizeSpinAdapter(this, product.getVariations().getColors());
                spinnerItemColor.setAdapter(colorSpinAdapter);
                spinnerItemColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {

                        Variation variation = colorSpinAdapter.getItem(position);
                        chosenVariationColor = variation;

                        if (variation.getQty() > 0){
                            List<String> quantities = new ArrayList<>();
                            for (int i=1; i<=variation.getQty(); i++){
                                quantities.add(String.valueOf(i));
                            }

                            qtyAdapterColor = new ArrayAdapter(context, R.layout.spinner_quantity, quantities);
                            spinnerQuantityColor.setAdapter(qtyAdapterColor);
                            chosenQtyColor = 1;
                        } else {
                            chosenQtyColor = 0;
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {  }
                });

                spinnerQuantityColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        // Here you get the current item (a User object) that is selected by its position
                        if (qtyAdapterColor != null && qtyAdapterColor.getItem(position) != null)chosenQtyColor = Integer.valueOf((String) qtyAdapterColor.getItem(position));
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {  }
                });
            }




            if (product.getVariations() != null && product.getVariations().getSizes() != null) {
                final SizeSpinAdapter sizeSpinAdapter = new SizeSpinAdapter(this, product.getVariations().getSizes());
                spinnerItemSize.setAdapter(sizeSpinAdapter);
                spinnerItemSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        // Here you get the current item (a User object) that is selected by its position
                        //chosenVariations = new ArrayList<>();

                        Variation variation = sizeSpinAdapter.getItem(position);
                        chosenVariationSize = variation;

                        if (variation.getQty() > 0){
                            List<String> quantities = new ArrayList<>();
                            for (int i=1; i<=variation.getQty(); i++){
                                quantities.add(String.valueOf(i));
                            }

                            qtyAdapterSize = new ArrayAdapter(context, R.layout.spinner_quantity, quantities);
                            spinnerQuantitySize.setAdapter(qtyAdapterSize);
                            chosenQtySize = 1;
                        } else {
                            chosenQtySize = 0;
                        }

                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapter) {  }
                });

                spinnerQuantitySize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view,
                                               int position, long id) {
                        // Here you get the current item (a User object) that is selected by its position
                        if (qtyAdapterSize != null && qtyAdapterSize.getItem(position) != null)chosenQtySize = Integer.valueOf((String) qtyAdapterSize.getItem(position));
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
        builder.setTitle("Item Not Available");
        builder.setCancelable(false);
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
        //Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, DoShopCheckoutActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NYHelper.LOGIN_REQ) {
            if (resultCode == RESULT_OK) {
                //triggerBook = true;
                if (product != null && NYHelper.isStringNotEmpty(product.getId())){
                    addToCart(product.getId(),chosenVariationSize, chosenVariationColor, String.valueOf(chosenQtySize));
                }
            }
        } else {
            //Toast.makeText(this, "hallo", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
