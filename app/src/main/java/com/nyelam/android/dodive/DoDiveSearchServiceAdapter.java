package com.nyelam.android.dodive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.detail.DetailServiceActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.StrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoDiveSearchServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveService> searchResults;

    public DoDiveSearchServiceAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_service, parent, false);
        return new DoDiveSearchServiceAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(searchResults.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (searchResults != null && !searchResults.isEmpty()) {
            count += searchResults.size();
        }
        return count;
    }

    public void addResult(DiveService searchResult) {
        if (this.searchResults == null) {
            this.searchResults = new ArrayList<>();
        }
        this.searchResults.add(searchResult);
    }

    public void addResults(List<DiveService> searchResults) {
        if (this.searchResults == null) {
            this.searchResults = new ArrayList<>();
        }
        this.searchResults.addAll(searchResults);
    }

    public void clear() {
        this.searchResults = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView serviceImageView;
        private TextView serviceNameTextView;
        private TextView diveCenterNameTextView;
        private StrikethroughTextView priceStrikethroughTextView;
        private TextView priceTextView;
        private TextView totalDiveTextView;
        private View itemView;
        private DiveService diveService;

        public PromoViewHolder(View itemView) {
            super(itemView);

            serviceImageView = (ImageView) itemView.findViewById(R.id.service_imageView);
            serviceNameTextView = (TextView) itemView.findViewById(R.id.service_name_textView);
            diveCenterNameTextView = (TextView) itemView.findViewById(R.id.dive_center_name_textView);
            priceStrikethroughTextView = (StrikethroughTextView) itemView.findViewById(R.id.price_strikethrough_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.price_textView);
            totalDiveTextView = (TextView) itemView.findViewById(R.id.total_dive_textView);

            this.itemView = itemView;
        }

        public void setModel(DiveService diveService) {
            this.diveService = diveService;

            if (NYHelper.isStringNotEmpty(diveService.getName())) serviceNameTextView.setText(diveService.getName());
            if (diveService.getDiveCenter() != null && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getName())) diveCenterNameTextView.setText(diveService.getDiveCenter().getName());
            totalDiveTextView.setText(String.valueOf(diveService.getTotalDives()));

            if (diveService.getSpecialPrice() < diveService.getNormalPrice() && diveService.getSpecialPrice() > 0){
                priceTextView.setText(NYHelper.priceFormatter(diveService.getSpecialPrice()));
                priceStrikethroughTextView.setText(NYHelper.priceFormatter(diveService.getNormalPrice()));
                priceStrikethroughTextView.setVisibility(View.VISIBLE);
            } else {
                priceTextView.setText(NYHelper.priceFormatter(diveService.getNormalPrice()));
                priceStrikethroughTextView.setVisibility(View.GONE);
            }

            //SET IMAGE
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
            if (NYHelper.isStringNotEmpty(diveService.getFeaturedImage())) {
                ImageLoader.getInstance().loadImage(diveService.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        serviceImageView.setImageResource(R.mipmap.ic_launcher);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        serviceImageView.setImageBitmap(loadedImage);
                        //activity.getCache().put(imageUri, loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        serviceImageView.setImageResource(R.mipmap.ic_launcher);
                    }
                });

                ImageLoader.getInstance().displayImage(diveService.getFeaturedImage(), serviceImageView, NYHelper.getOption());

            } else {
                serviceImageView.setImageResource(R.mipmap.ic_launcher);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, DetailServiceActivity.class);
            intent.putExtra(NYHelper.SERVICE, diveService.toString());
            intent.putExtra(NYHelper.DIVER, ((DoDiveSearchResultActivity)activity).diver);
            intent.putExtra(NYHelper.SCHEDULE, ((DoDiveSearchResultActivity)activity).date);

            if (((DoDiveSearchResultActivity)activity).type.equals("1")){
                intent.putExtra(NYHelper.DIVE_SPOT_ID, ((DoDiveSearchResultActivity)activity).diverId);
            }

            activity.startActivity(intent);
        }
    }

}