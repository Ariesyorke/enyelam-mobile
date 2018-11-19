package com.nyelam.android.doshoporderhistory;

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
import com.nyelam.android.data.DoShopOrder;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.doshop.DoShopDetailItemActivity;
import com.nyelam.android.doshop.DoShopRecommendedAdapter;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 11/19/2018.
 */
public class DoShopOrderHistoryAdapter extends RecyclerView.Adapter<DoShopOrderHistoryAdapter.MyViewHolder> {

    private List<DoShopOrder> data = new ArrayList<>();
    private Context context;

    public DoShopOrderHistoryAdapter(Context contexts) {
        this.context = contexts;
    }

    @Override
    public DoShopOrderHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_do_shop_order, parent, false);
        return new DoShopOrderHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DoShopOrderHistoryAdapter.MyViewHolder holder, int position) {
        //holder.image.setImageResource(data.get(position).getImage());
        //holder.title.setText(data.get(position).getHeader());
        //holder.description.setText(data.get(position).getSubHeader());

        final DoShopOrder order = data.get(position);

        if (order != null){
            if (order.getShippingAddress() != null && NYHelper.isStringNotEmpty(order.getShippingAddress().getFullName())) holder.name.setText(order.getShippingAddress().getFullName());
            if (order.getShippingAddress() != null && NYHelper.isStringNotEmpty(order.getShippingAddress().getAddress())) holder.address.setText(order.getShippingAddress().getAddress());
            //if (order.getCart(). != null && NYHelper.isStringNotEmpty(order.getShippingAddress().getAddress())) holder.date.setText(order.getShippingAddress().getAddress());
            if (order.getCart() != null) holder.totalPrice.setText(NYHelper.priceFormatter(order.getCart().getTotal()));

//            if (NYHelper.isStringNotEmpty(order.getFeaturedImage())){
//                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
//                ImageLoader.getInstance().loadImage(product.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
//                    @Override
//                    public void onLoadingStarted(String imageUri, View view) {
//
//                    }
//
//                    @Override
//                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                        holder.image.setImageResource(R.drawable.example_pic);
//                    }
//
//                    @Override
//                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
//                    }
//
//                    @Override
//                    public void onLoadingCancelled(String imageUri, View view) {
//                        holder.image.setImageResource(R.drawable.example_pic);
//                    }
//                });
//
//                ImageLoader.getInstance().displayImage(product.getFeaturedImage(), holder.image, NYHelper.getOption());
//            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order != null && NYHelper.isStringNotEmpty(order.getOrderId())){
                    Intent intent = new Intent(context, DoShopDetailItemActivity.class);
                    intent.putExtra(NYHelper.ORDER, order.toString());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Sorry, this item is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public List<DoShopOrder> getData() {
        if (data == null) data = new ArrayList<>();
        return data;
    }

    public void setData(List<DoShopOrder> data) {
        this.data = data;
    }

    public void addData(List<DoShopOrder> data) {
        if (this.data == null) this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    @Override
    public int getItemCount() {
        if (data == null) data = new ArrayList<>();
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView address;
        TextView date;
        TextView totalPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.iv_picture);
            name = (TextView) itemView.findViewById(R.id.tv_name);
            address = (TextView) itemView.findViewById(R.id.tv_address);
            date = (TextView) itemView.findViewById(R.id.tv_date);
            totalPrice = (TextView) itemView.findViewById(R.id.tv_total);
        }
    }

}

