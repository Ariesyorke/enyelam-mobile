package com.nyelam.android.divecenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.StrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoDiveSearchServiceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveService> searchResults;
    private String diver;
    private String date;
    private String certificate;
    private DiveCenter diveCenter;

    public DoDiveSearchServiceAdapter(Activity activity, String diver,String date, String certificate, DiveCenter diveCenter) {
        this.activity = activity;
        this.diver = diver;
        this.date = date;
        this.certificate = certificate;
        this.diveCenter = diveCenter;
    }

    public void setDiveCenter(DiveCenter diveCenter) {
        this.diveCenter = diveCenter;
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
        removeTheDuplicates(searchResults);
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
        private TextView totalDiveSpotTextView;
        private RatingBar ratingBar;
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
            totalDiveSpotTextView = (TextView) itemView.findViewById(R.id.total_dive_spot_textView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            this.itemView = itemView;
        }

        public void setModel(DiveService diveService) {
            this.diveService = diveService;

            if (diveService != null){

                if (NYHelper.isStringNotEmpty(diveService.getName())) serviceNameTextView.setText(diveService.getName());
                if (diveService.getDiveCenter() != null && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getName())) diveCenterNameTextView.setText(diveService.getDiveCenter().getName());

                totalDiveTextView.setText("Total Dives : "+String.valueOf(diveService.getTotalDives()));
                totalDiveSpotTextView.setText("Dive Spot Option : "+String.valueOf(diveService.getTotalDiveSpots()));

                /*if (diveService.getDiveSpots() != null){
                    totalDiveSpotTextView.setText("Total Dive Spot : "+String.valueOf(diveService.getDiveSpots().size()));
                }*/

                ratingBar.setRating(diveService.getRating());

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
                            serviceImageView.setImageResource(R.drawable.logo_nyelam);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            serviceImageView.setImageBitmap(loadedImage);
                            //activity.getCache().put(imageUri, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            serviceImageView.setImageResource(R.drawable.logo_nyelam);
                        }
                    });

                    ImageLoader.getInstance().displayImage(diveService.getFeaturedImage(), serviceImageView, NYHelper.getOption());

                } else {
                    serviceImageView.setImageResource(R.drawable.logo_nyelam);
                }

            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(activity, DetailServiceActivity.class);
            if (diveService != null ) intent.putExtra(NYHelper.SERVICE, diveService.toString());
            intent.putExtra(NYHelper.DIVER, diver);
            intent.putExtra(NYHelper.SCHEDULE, date);
            intent.putExtra(NYHelper.CERTIFICATE, certificate);
            if (diveCenter != null) {
                intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
            }
            /*intent.putExtra(NYHelper.DIVER, ((DoDiveSearchResultActivity)activity).diver);
            intent.putExtra(NYHelper.SCHEDULE, ((DoDiveSearchResultActivity)activity).date);

            if (((DoDiveSearchResultActivity)activity).type.equals("1")){
                intent.putExtra(NYHelper.DIVE_SPOT_ID, ((DoDiveSearchResultActivity)activity).diverId);
            }*/

            activity.startActivity(intent);
        }
    }


    private void removeTheDuplicates(List<DiveService> newDiveServiceList) {

        List<DiveService> temp = new ArrayList<>();

        for (DiveService newDs : newDiveServiceList){
            boolean isExist = false;
            for (DiveService ds : searchResults){
                if (ds.getId().equals(newDs.getId())){
                    isExist = true;
                    break;
                }
            }

            if (!isExist) temp.add(newDs);
            isExist = false;
        }

        searchResults.addAll(temp);

    }

}