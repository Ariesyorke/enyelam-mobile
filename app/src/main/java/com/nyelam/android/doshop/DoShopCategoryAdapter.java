package com.nyelam.android.doshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.images.internal.LoadingImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopCategoryList;
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
    public int getItemCount() {
        return data.size();
    }

    public void clear() {
        data = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final DoShopCategory cat = data.get(position);

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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cat != null && NYHelper.isStringNotEmpty(cat.getId()) && cat.getId().equals("0")){
                    Intent intent = new Intent(context, DoShopCategoryListActivity.class);
                    context.startActivity(intent);
                } else if (cat != null && NYHelper.isStringNotEmpty(cat.getId())){
                    Intent intent = new Intent(context, DoShopCategoryActivity.class);
                    intent.putExtra(NYHelper.CATEGORY, cat.toString());
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Sorry, this category is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
    }
}
