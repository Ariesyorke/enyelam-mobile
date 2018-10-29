package com.nyelam.android.doshop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.images.internal.LoadingImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.DoTripRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class DoShopCategoryAdapter extends RecyclerView.Adapter<DoShopCategoryAdapter.MyViewHolder> {

    private Context context;
    private List<DoShopCategory> data = new ArrayList<>();

    public DoShopCategoryAdapter(Context context, List<DoShopCategory> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category_do_shop_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

//        if (holder instanceof MyViewHolder) {
//            MyViewHolder vh = (MyViewHolder) holder;
//            vh.setModel(data.get(position));
//        }

        DoShopCategory cat = data.get(position);

        if (cat == null) return;
        if (NYHelper.isStringNotEmpty(cat.getName())) holder.name.setText(cat.getName());
        if (NYHelper.isStringNotEmpty(cat.getImageUri())){
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
            ImageLoader.getInstance().loadImage(cat.getImageUri(), NYHelper.getOption(), new ImageLoadingListener() {
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

            ImageLoader.getInstance().displayImage(cat.getImageUri(), holder.image, NYHelper.getOption());
        }

        //holder.image.setImageResource(data.get(position).getImage());
        //holder.title.setText(data.get(position).getHeader());
        //holder.description.setText(data.get(position).getSubHeader());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        ProgressBar progressBar;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            name = (TextView) itemView.findViewById(R.id.name_textView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadingView);
        }

//        public void setModel(DoShopCategory model) {
//            this.model = model;
//        }
    }
}
