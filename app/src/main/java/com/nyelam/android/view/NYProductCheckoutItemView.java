package com.nyelam.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopMerchant;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.doshoporder.DoShopCheckoutFragment;
import com.nyelam.android.helper.NYHelper;

public class NYProductCheckoutItemView extends FrameLayout {
    TextView tvMerchantName;
    TextView tvMerchantAddress;
    LinearLayout llContainer;
    TextView tvChooseCourier;
    LinearLayout llContainerDelivery;
    TextView tvServiceName;
    TextView tvServiceCost;
    TextView tvChangeCourier;
    TextView tvSubTotal;

    public NYProductCheckoutItemView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public NYProductCheckoutItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NYProductCheckoutItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttrs) {
        View.inflate(context, R.layout.view_item_checkout_parent, this);
        tvMerchantName = (TextView) findViewById(R.id.tv_merchant_name);
        tvMerchantAddress = (TextView) findViewById(R.id.tv_merchant_address);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        tvChooseCourier = (TextView) findViewById(R.id.tv_choose_courier);
        llContainerDelivery = (LinearLayout) findViewById(R.id.ll_container_delivery);
        tvServiceName  = (TextView) findViewById(R.id.tv_service_name);
        tvServiceCost  = (TextView) findViewById(R.id.tv_service_cost);
        tvChangeCourier = (TextView) findViewById(R.id.tv_change_courier);
        tvSubTotal = (TextView) findViewById(R.id.tv_sub_total);
    }

    public void initMerchant(Context context, final DoShopMerchant merchant, final ProductCheckoutItemListener listener) {
        if (merchant != null) {
            if (NYHelper.isStringNotEmpty(merchant.getName()))
                tvMerchantName.setText(merchant.getName());
            if (NYHelper.isStringNotEmpty(merchant.getAddress()))
                tvMerchantAddress.setText(merchant.getAddress());

            //holder.tvSubTotal.setText(NYHelper.priceFormatter(merchant.));

            llContainer.removeAllViews();
            for (DoShopProduct product : merchant.getDoShopProducts()) {

                LayoutInflater inflaterAddons = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View productView = inflaterAddons.inflate(R.layout.view_item_checkout_child, null);

                final ImageView ivItemImage = (ImageView) productView.findViewById(R.id.iv_item_image);
                TextView tvItemName = (TextView) productView.findViewById(R.id.tv_item_name);
                TextView tvItemQty = (TextView) productView.findViewById(R.id.tv_item_qty);
                TextView tvItemPrice = (TextView) productView.findViewById(R.id.tv_item_price);
                TextView tvItemPriceStrike = (TextView) productView.findViewById(R.id.tv_item_price_strikethrough);

                if (product != null) {
                    tvItemQty.setText(String.valueOf(product.getQty()));
                    if (NYHelper.isStringNotEmpty(product.getProductName()))
                        tvItemName.setText(product.getProductName());
                    if (product.getSpecialPrice() < product.getNormalPrice()) {
                        tvItemPriceStrike.setText(NYHelper.priceFormatter(product.getNormalPrice()));
                        tvItemPrice.setText(NYHelper.priceFormatter(product.getSpecialPrice()));
                        tvItemPriceStrike.setVisibility(View.VISIBLE);
                        tvItemPrice.setText(NYHelper.priceFormatter(product.getSpecialPrice()));
                    } else {
                        tvItemPrice.setText(NYHelper.priceFormatter(product.getNormalPrice()));
                        tvItemPriceStrike.setVisibility(View.GONE);
                        tvItemPrice.setText(NYHelper.priceFormatter(product.getNormalPrice()));
                    }

                    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
                    if (NYHelper.isStringNotEmpty(product.getFeaturedImage())) {

                        ImageLoader.getInstance().loadImage(product.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                ivItemImage.setImageResource(R.mipmap.ic_launcher);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                ivItemImage.setImageBitmap(loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                ivItemImage.setImageResource(R.mipmap.ic_launcher);
                            }
                        });

                        ImageLoader.getInstance().displayImage(product.getFeaturedImage(), ivItemImage, NYHelper.getOption());

                    } else {
                        ivItemImage.setImageResource(R.mipmap.ic_launcher);
                    }

                    llContainer.addView(productView);
                }
            }

            if (merchant.getDeliveryService() != null) {
                String name = "";
                if (NYHelper.isStringNotEmpty(merchant.getDeliveryService().getName()))
                    name += merchant.getDeliveryService().getName();
                if (merchant.getDeliveryService().getTypes() != null && merchant.getDeliveryService().getTypes().size() > 0 && NYHelper.isStringNotEmpty(merchant.getDeliveryService().getTypes().get(0).getName()))
                    name += " {" + merchant.getDeliveryService().getTypes().get(0).getName() + ")";
                if (NYHelper.isStringNotEmpty(name)) tvServiceName.setText(name);
                tvServiceCost.setText(NYHelper.priceFormatter(merchant.getDeliveryService().getPrice()));
                tvChooseCourier.setVisibility(View.GONE);
                llContainerDelivery.setVisibility(View.VISIBLE);
                tvChangeCourier.setVisibility(View.VISIBLE);
            } else {
                tvChangeCourier.setVisibility(View.GONE);
                llContainerDelivery.setVisibility(View.GONE);
                tvChooseCourier.setVisibility(View.VISIBLE);
            }

            tvChooseCourier.setOnClickListener(listener);

            tvChangeCourier.setOnClickListener(listener);
        }
    }
}

