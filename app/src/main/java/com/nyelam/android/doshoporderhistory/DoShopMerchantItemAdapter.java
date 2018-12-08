package com.nyelam.android.doshoporderhistory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;


public class DoShopMerchantItemAdapter extends RecyclerView.Adapter<DoShopMerchantItemAdapter.MyViewHolder> {

    private List<DoShopMerchant> data = new ArrayList<>();
    private Activity context;

    public DoShopMerchantItemAdapter(Activity context) {
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_checkout_parent, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final DoShopMerchant merchant = data.get(position);

        if (merchant != null){
            if (NYHelper.isStringNotEmpty(merchant.getName())) holder.tvMerchantName.setText(merchant.getName());
            if (NYHelper.isStringNotEmpty(merchant.getAddress())) holder.tvMerchantAddress.setText(merchant.getAddress());

            holder.llContainer.removeAllViews();
            for (DoShopProduct product : merchant.getDoShopProducts()){

                LayoutInflater inflaterAddons = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View productView = inflaterAddons.inflate(R.layout.view_item_checkout_child, null);

                final ImageView ivItemImage = (ImageView) productView.findViewById(R.id.iv_item_image);
                TextView tvItemName = (TextView) productView.findViewById(R.id.tv_item_name);
                TextView tvItemQty = (TextView) productView.findViewById(R.id.tv_item_qty);
                TextView tvItemPrice = (TextView) productView.findViewById(R.id.tv_item_price);
                TextView tvItemPriceStrike = (TextView) productView.findViewById(R.id.tv_item_price_strikethrough);

                if (product != null) {
                    tvItemQty.setText(String.valueOf(product.getQty()));
                    if (NYHelper.isStringNotEmpty(product.getProductName())) tvItemName.setText(product.getProductName());
                    if (product.getSpecialPrice() < product.getNormalPrice()){
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

                    holder.llContainer.addView(productView);
                }
            }

            if (merchant.getDeliveryService() != null){
                String name = "";
                if (NYHelper.isStringNotEmpty(merchant.getDeliveryService().getName()))name+=merchant.getDeliveryService().getName();
                if (merchant.getDeliveryService().getTypes() != null && merchant.getDeliveryService().getTypes().size() > 0 && NYHelper.isStringNotEmpty(merchant.getDeliveryService().getTypes().get(0).getName()))
                    name+=" {"+merchant.getDeliveryService().getTypes().get(0).getName()+")";
                if (NYHelper.isStringNotEmpty(name))holder.tvServiceName.setText(name);
                holder.tvServiceCost.setText(NYHelper.priceFormatter(merchant.getDeliveryService().getPrice()));
                holder.tvChooseCourier.setVisibility(View.GONE);
                holder.llContainerDelivery.setVisibility(View.VISIBLE);
                holder.tvChangeCourier.setVisibility(View.VISIBLE);
            } else {
                holder.tvChangeCourier.setVisibility(View.GONE);
                holder.llContainerDelivery.setVisibility(View.GONE);
                holder.tvChooseCourier.setVisibility(View.VISIBLE);
            }


            holder.tvChooseCourier.setVisibility(View.GONE);
            holder.tvChangeCourier.setVisibility(View.GONE);
        }
    }

    public List<DoShopMerchant> getData() {
        if (data == null) data = new ArrayList<>();
        return data;
    }

    public void setData(List<DoShopMerchant> data) {
        if (data != null)this.data = data;
    }

    public void addData(List<DoShopMerchant> data) {
        if (this.data == null) this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    @Override
    public int getItemCount() {
        if (data == null) data = new ArrayList<>();
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMerchantName;
        TextView tvMerchantAddress;
        LinearLayout llContainer;
        TextView tvChooseCourier;
        LinearLayout llContainerDelivery;
        TextView tvServiceName;
        TextView tvServiceCost;
        TextView tvChangeCourier;
        TextView tvSubTotal;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMerchantName = (TextView) itemView.findViewById(R.id.tv_merchant_name);
            tvMerchantAddress = (TextView) itemView.findViewById(R.id.tv_merchant_address);
            llContainer = (LinearLayout) itemView.findViewById(R.id.ll_container);
            tvChooseCourier = (TextView) itemView.findViewById(R.id.tv_choose_courier);
            llContainerDelivery = (LinearLayout) itemView.findViewById(R.id.ll_container_delivery);
            tvServiceName  = (TextView) itemView.findViewById(R.id.tv_service_name);
            tvServiceCost  = (TextView) itemView.findViewById(R.id.tv_service_cost);
            tvChangeCourier = (TextView) itemView.findViewById(R.id.tv_change_courier);
            tvSubTotal = (TextView) itemView.findViewById(R.id.tv_sub_total);
        }
    }
}
