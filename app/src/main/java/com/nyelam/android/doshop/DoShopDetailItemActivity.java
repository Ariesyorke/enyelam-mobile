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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopMerchantList;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.DoShopProductList;
import com.nyelam.android.data.Variation;
import com.nyelam.android.data.VariationsUtility;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.doshoporder.DoShopCheckoutActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.BannerViewPagerAdapter;
import com.nyelam.android.http.NYDoShopAddToCartRequest;
import com.nyelam.android.http.NYDoShopProductDetailRequest;
import com.nyelam.android.http.NYDoShopProductListRequest;
import com.nyelam.android.http.result.NYPaginationResult;
import com.nyelam.android.storage.CartStorage;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.view.NYBannerViewPager;
import com.nyelam.android.view.NYDialogAddToCart;
import com.nyelam.android.view.NYSpinner;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToDoubleBiFunction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

public class DoShopDetailItemActivity extends BasicActivity implements NYDialogAddToCart.Listener {

    private Context context;
    private SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DoShopRecommendedAdapter recommendedAdapter;

    private String productId;
    private DoShopProduct product;
    private Map<String, String> selectedMapVariations;

    private BannerViewPagerAdapter bannerViewPagerAdapter;
    @BindView(R.id.banner_view_pager) NYBannerViewPager bannerViewPager;
    @BindView(R.id.circle_indicator) CircleIndicator circleIndicator;

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

    @BindView(R.id.tv_status) TextView tvStatus;
    @BindView(R.id.tv_add_to_basket) TextView tvAddToBasket;
    @BindView(R.id.tv_sent_date_one) TextView tvSentDateOne;
    @BindView(R.id.tv_sent_date_two) TextView tvSentDateTwo;
    @BindView(R.id.tv_product_description_title) TextView tvProductDescriptionTitle;
    @BindView(R.id.tv_product_description) TextView tvProductDescription;
    @BindView(R.id.ll_related_item) LinearLayout llRelatedItem;
    @BindView(R.id.rv_related_item) RecyclerView rvRelatedItem;

    @BindView(R.id.tv_brand_name) TextView tvBrandName;
    @BindView(R.id.tv_merchant_name) TextView tvMerchantName;
    @BindView(R.id.civ_merchant) ImageView civMerchant;

    @BindView(R.id.ll_container_spinner) LinearLayout llContainerSpinner;
    @BindView(R.id.et_quantity) EditText etQuantity;

    @OnClick(R.id.tv_add_to_basket)void addToBasket(){
//        NYDialogAddToCart dialog = new NYDialogAddToCart();
//        dialog.showAddToCartDialog(this, product);

        LoginStorage storage = new LoginStorage(this);

        int quantity = 0;
        if (NYHelper.isStringNotEmpty(etQuantity.getText().toString()))quantity = Integer.valueOf(etQuantity.getText().toString());

        // TODO: check selected variations
        boolean isValid = true;
        String tagKey = null;
        for (final Map.Entry<String, String> entry : selectedMapVariations.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()){
                isValid = false;
                tagKey = entry.getKey();
                break;
            }
        }

        // TODO: selected param
        if (!isValid){
            Toast.makeText(context, "Please, select "+tagKey+" first to continue", Toast.LENGTH_SHORT).show();
        } else if (quantity <= 0){
            Toast.makeText(context, "Please, insert quantity item", Toast.LENGTH_SHORT).show();
        } else if (storage.isUserLogin() && product != null && NYHelper.isStringNotEmpty(product.getId())){
            addToCart(product.getId(),selectedMapVariations, String.valueOf(quantity));
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

    @OnClick(R.id.ll_see_all) void seeAll(){
        if (product != null && product.getCategories() != null && product.getCategories().size() > 0 && NYHelper.isStringNotEmpty(product.getCategories().get(0).getId())){
            Intent intent = new Intent(this, DoShopCategoryActivity.class);
            intent.putExtra(NYHelper.CATEGORY, product.getCategories().get(0).getId().toString());
            startActivity(intent);
        }
    }

    @OnClick(R.id.ll_merchant) void showMerchant(){
        if (product != null && product.getMerchant() != null && NYHelper.isStringNotEmpty(product.getMerchant().getId())){
            Intent intent = new Intent(this, DoShopMerchantActivity.class);
            intent.putExtra(NYHelper.MERCHANT, product.getMerchant().toString());
            startActivity(intent);
        }
    }

    @OnClick(R.id.tv_brand_name) void showBrandName(){
        if (product != null && product.getBrand() != null && NYHelper.isStringNotEmpty(product.getBrand().getId())){
            Intent intent = new Intent(this, DoShopBrandActivity.class);
            intent.putExtra(NYHelper.BRAND, product.getBrand().toString());
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_detail_item);
        ButterKnife.bind(this);
        initToolbar();
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

        //initTestVariations();
    }

    private void initTestVariations() {
        try {
            JSONObject obj = new JSONObject(NYHelper.getJSONFromResource(this, "variations.json"));
            Map<String, List<Variation>> vars = VariationsUtility.jsonToMap(obj);

            if (vars != null)NYLog.e("CEK VARS : "+vars.size());

            // TODO: init TEMP selected variations
            selectedMapVariations = new HashMap<>();

            llContainerSpinner.removeAllViews();

            if (vars.size() > 0){
                for (final Map.Entry<String, List<Variation>> entry : vars.entrySet()) {

                    // TODO: DEBUG DATA
                    System.out.println(entry);
                    NYLog.e("CEK VARS key : "+entry.getKey());
                    NYLog.e("CEK VARS value : "+entry.getValue());

                    // TODO: CHECK AND PROSES DATA
                    if (entry != null && NYHelper.isStringNotEmpty(entry.getKey()) && entry.getValue() != null && entry.getValue().size() > 0){

                        // TODO: tampung data + default selected
                        List<Variation> variations = new ArrayList<>();
                        variations.add(new Variation(null, "Select "+entry.getKey()));
                        variations.addAll(entry.getValue());

                        // TODO: init selected variations, sesuai jumlah data  dari API
                        selectedMapVariations.put(entry.getKey(), null);

                        // TODO: init view spinner
                        LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View spinnerView = inflaterAddons.inflate(R.layout.spinner_variation, null);
                        LinearLayout llContainer = (LinearLayout) spinnerView.findViewById(R.id.ll_container);
                        TextView tvTitle = (TextView) spinnerView.findViewById(R.id.tv_title);
                        NYSpinner spinner = (NYSpinner) spinnerView.findViewById(R.id.spinner);

                        if (NYHelper.isStringNotEmpty(entry.getKey())) tvTitle.setText(entry.getKey());

                        // TODO: init data spinner
                        final SizeSpinAdapter spinAdapter = new SizeSpinAdapter(this, variations);
                        spinner.setAdapter(spinAdapter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view,
                                                       int position, long id) {

                                selectedMapVariations.put(entry.getKey(), spinAdapter.getVariationId(position));
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapter) {  }
                        });


                        llContainerSpinner.addView(spinnerView);
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
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
                } else {
                    // TODO: jika tidak ada kategori, kategori ID = null
                    initRelatedItem(null);
                }

                if (product == null || product.getVariations() == null || product.getVariations().size() <= 0){
                    // TODO: hide add to cart
                    tvAddToBasket.setVisibility(View.GONE);
                    dialogItemNotAvailable();
                }

            }
        };
    }

    private void initRelatedItem(String categoryId){
        NYLog.e("cek related 1");
        NYDoShopProductListRequest req = new NYDoShopProductListRequest(context, "1", null, categoryId, "40000",  "500000", "1", null, null, "1");
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


    private void addToCart(String productId, Map<String, String> variations, String qty){
        //NYLog.e("cek related 1");
        pDialog.show();
        NYDoShopAddToCartRequest req = null;
        try {
            req = new NYDoShopAddToCartRequest(context, productId, variations, qty);
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

                if (cart != null && cart.getCart() != null && cart.getCart().getMerchants() != null && cart.getCart().getMerchants().size() > 0){
                    //Toast.makeText(context, "save to cache", Toast.LENGTH_SHORT).show();
                    DoShopMerchantList merchantList = new DoShopMerchantList();
                    merchantList.setList(cart.getCart().getMerchants());
                    CartStorage storage = new CartStorage(context);
                    storage.setMerchants(merchantList);
                    storage.save();
                }

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

            if (product.getBrand() != null && NYHelper.isStringNotEmpty(product.getBrand().getName()))tvBrandName.setText(product.getBrand().getName());

            if (product.getMerchant() != null){
                if (NYHelper.isStringNotEmpty(product.getMerchant().getName()))tvMerchantName.setText(product.getMerchant().getName());
                if (NYHelper.isStringNotEmpty(product.getMerchant().getLogo())){
                    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
                    ImageLoader.getInstance().loadImage(product.getMerchant().getLogo(), NYHelper.getOption(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            civMerchant.setImageResource(R.drawable.example_pic);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            civMerchant.setImageResource(R.drawable.example_pic);
                        }
                    });

                    ImageLoader.getInstance().displayImage(product.getMerchant().getLogo(), civMerchant, NYHelper.getOption());
                }

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


            etQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                        s.clear();
                    }
                }
            });


            // TODO: init image gallery
            BannerList bannerList = new BannerList();
            List<Banner> banners = new ArrayList<>();
            if (product != null && product.getFeaturedImage() != null && !TextUtils.isEmpty(product.getFeaturedImage()))banners.add(new Banner("1", product.getFeaturedImage(), "captio", null));
            bannerList.setList(banners);
            if(product != null && product.getImages() != null && !product.getImages().isEmpty()) {
                int i = 1;
                for(String image: product.getImages()) {
                    banners.add(new Banner(String.valueOf(i), image, "gallery", null));
                    i++;
                }
            }


            // TODO: init image gallery
            //bannerViewPager = new NYBannerViewPager(context);
            bannerViewPagerAdapter = new BannerViewPagerAdapter(getSupportFragmentManager(), true);
            bannerViewPagerAdapter.setGallery(true);
            bannerViewPager.setAdapter(bannerViewPagerAdapter);
            circleIndicator.setViewPager(bannerViewPager);
            bannerViewPagerAdapter.setBannerList(bannerList);
            bannerViewPagerAdapter.notifyDataSetChanged();
            bannerViewPager.setOffscreenPageLimit(bannerList.getList().size());
            circleIndicator.setViewPager(bannerViewPager);

//            bannerViewPager.setOnItemClickListener(new NYBannerViewPager.OnItemClickListener() {
//                @Override
//                public void onItemClick(int position) {
//
//
//                    if (product != null && NYHelper.isStringNotEmpty(product.getFeaturedImage())){
//                        ArrayList<String> images = new ArrayList<>();
//                        images.add(product.getFeaturedImage());
//
//                        if (product.getImages() != null && product.getImages().size() > 0){
//                            for (String im : product.getImages()){
//                                if (NYHelper.isStringNotEmpty(im))images.add(im);
//                            }
//
//                            Intent intent = new Intent(DoShopDetailItemActivity.this, DoShopDetailItemImagesActivity.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putStringArrayList(DoShopDetailItemImagesActivity.KEY_IMAGES, images);
//                            bundle.putInt(NYHelper.POSITION, position);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//                        }
//                    }
//
//                    Toast.makeText(context, String.valueOf(position), Toast.LENGTH_SHORT).show();
//                }
//            });


            // TODO: init VARIATIONS
            try {

                // TODO: init TEMP selected variations
                selectedMapVariations = new HashMap<>();

                llContainerSpinner.removeAllViews();

                if (product.getVariations() != null && product.getVariations().size() > 0){

                    // TODO: reverse order
                    Map<String, List<Variation>> sortedMap = new TreeMap<String, List<Variation>>();
                    sortedMap.putAll(product.getVariations());
                    product.setVariations(sortedMap);


                    for (final Map.Entry<String, List<Variation>> entry : product.getVariations().entrySet()) {

                        // TODO: CHECK AND PROSES DATA
                        if (entry != null && NYHelper.isStringNotEmpty(entry.getKey()) && entry.getValue() != null && entry.getValue().size() > 0){

                            // TODO: tampung data + default selected
                            List<Variation> variations = new ArrayList<>();
                            variations.add(new Variation(null, "Select "+entry.getKey()));
                            variations.addAll(entry.getValue());

                            // TODO: init selected variations, sesuai jumlah data  dari API
                            selectedMapVariations.put(entry.getKey(), null);

                            // TODO: init view spinner
                            LayoutInflater inflaterAddons = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View spinnerView = inflaterAddons.inflate(R.layout.spinner_variation, null);
                            LinearLayout llContainer = (LinearLayout) spinnerView.findViewById(R.id.ll_container);
                            TextView tvTitle = (TextView) spinnerView.findViewById(R.id.tv_title);
                            NYSpinner spinner = (NYSpinner) spinnerView.findViewById(R.id.spinner);

                            if (NYHelper.isStringNotEmpty(entry.getKey())) tvTitle.setText(entry.getKey());

                            // TODO: init data spinner
                            final SizeSpinAdapter spinAdapter = new SizeSpinAdapter(this, variations);
                            spinner.setAdapter(spinAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view,
                                                           int position, long id) {

                                    selectedMapVariations.put(entry.getKey(), spinAdapter.getVariationId(position));
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> adapter) {  }
                            });


                            llContainerSpinner.addView(spinnerView);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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

                LoginStorage storage = new LoginStorage(context);

                int quantity = 0;
                if (NYHelper.isStringNotEmpty(etQuantity.getText().toString()))quantity = Integer.valueOf(etQuantity.getText().toString());

                // TODO: check selected variations
                boolean isValid = true;
                String tagKey = null;
                for (final Map.Entry<String, String> entry : selectedMapVariations.entrySet()) {
                    if (entry.getValue() == null || entry.getValue().isEmpty()){
                        isValid = false;
                        tagKey = entry.getKey();
                        break;
                    }
                }

                // TODO: selected param
                if (!isValid){
                    Toast.makeText(context, "Please, select "+tagKey+" first to continue", Toast.LENGTH_SHORT).show();
                } else if (quantity <= 0){
                    Toast.makeText(context, "Please, insert quantity item", Toast.LENGTH_SHORT).show();
                } else if (storage.isUserLogin() && product != null && NYHelper.isStringNotEmpty(product.getId())){
                    addToCart(product.getId(),selectedMapVariations, String.valueOf(quantity));
                } else if (product != null && NYHelper.isStringNotEmpty(product.getId())){
                    Intent intent = new Intent(this, AuthActivity.class);
                    startActivityForResult(intent, NYHelper.LOGIN_REQ);
                }
            }
        } else {
            //Toast.makeText(this, "hallo", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Detail");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Detail");
        this.setTitle("Detail");
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

}
