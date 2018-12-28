package com.nyelam.android.doshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;


public class DoShopRecommendedAdapter extends RecyclerView.Adapter<DoShopRecommendedAdapter.MyViewHolder> {

    private boolean isRelatedItem = false;
    private List<DoShopProduct> data = new ArrayList<>();
    private Context context;

    public DoShopRecommendedAdapter(Context contexts) {
        this.context = contexts;
    }

    public DoShopRecommendedAdapter(Context contexts, List<DoShopProduct> data) {
        this.context = contexts;
        this.data = data;
    }

    public DoShopRecommendedAdapter(Context contexts, List<DoShopProduct> data, boolean isRelatedItem) {
        this.context = contexts;
        this.data = data;
        this.isRelatedItem = isRelatedItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recommended_do_shop_item, parent, false);
        if (isRelatedItem){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_do_shop_related_product, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //holder.image.setImageResource(data.get(position).getImage());
        //holder.title.setText(data.get(position).getHeader());
        //holder.description.setText(data.get(position).getSubHeader());

        final DoShopProduct product = data.get(position);

        if (product != null){
            if (NYHelper.isStringNotEmpty(product.getProductName())) holder.name.setText(product.getProductName());

            if (product.getSpecialPrice() < product.getNormalPrice()){
                holder.priceStrike.setText(NYHelper.priceFormatter(product.getNormalPrice()));
                holder.price.setText(NYHelper.priceFormatter(product.getSpecialPrice()));
                holder.priceStrike.setVisibility(View.VISIBLE);
            } else {
                holder.price.setText(NYHelper.priceFormatter(product.getNormalPrice()));
                holder.priceStrike.setVisibility(View.GONE);
            }

            //product.setFeaturedImage("https://www.zamzar.com/images/filetypes/jpg.png");

            if (NYHelper.isStringNotEmpty(product.getFeaturedImage())){


                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
                ImageLoader.getInstance().loadImage(product.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        holder.image.setImageResource(R.drawable.example_pic);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        holder.image.setImageResource(R.drawable.example_pic);
                    }
                });

                ImageLoader.getInstance().displayImage(product.getFeaturedImage(), holder.image, NYHelper.getOption());
            }
        }
        
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product != null && NYHelper.isStringNotEmpty(product.getId())){
                    Intent intent = new Intent(context, DoShopDetailItemActivity.class);
                    intent.putExtra(NYHelper.PRODUCT, product.toString());
                    context.startActivity(intent);    
                } else {
                    Toast.makeText(context, "Sorry, this item is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<DoShopProduct> getData() {
        if (data == null) data = new ArrayList<>();
        return data;
    }

    public void setData(List<DoShopProduct> data) {
        this.data = data;
    }

    public void addData(List<DoShopProduct> data) {
        if (this.data == null) this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    public void clear() {
        this.data = new ArrayList<>();
    }


    @Override
    public int getItemCount() {
        if (data == null) data = new ArrayList<>();
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView priceStrike;
        TextView price;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            name = (TextView) itemView.findViewById(R.id.item_name_textView);
            priceStrike = (TextView) itemView.findViewById(R.id.price_strikethrough_textView);
            price = (TextView) itemView.findViewById(R.id.price_textView);
        }
    }
}
