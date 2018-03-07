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
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Location;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.NYStrikethroughTextView;

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
        private TextView locationTextView;
        private TextView serviceNameTextView;
        private TextView diveCenterNameTextView;
        private NYStrikethroughTextView priceStrikethroughTextView;
        private TextView priceTextView;
        private TextView totalDiveTextView;
        private TextView totalDiveSpotTextView;
        private TextView totalDayTextView;
        private RatingBar ratingBar;
        private TextView ratingTextView;
        private TextView visitedTextView;
        private ImageView licenseImageView;
        private View itemView;
        private DiveService diveService;

        public PromoViewHolder(View itemView) {
            super(itemView);

            serviceImageView = (ImageView) itemView.findViewById(R.id.service_imageView);
            locationTextView = (TextView) itemView.findViewById(R.id.location_textView);
            serviceNameTextView = (TextView) itemView.findViewById(R.id.service_name_textView);
            diveCenterNameTextView = (TextView) itemView.findViewById(R.id.dive_center_name_textView);
            priceStrikethroughTextView = (NYStrikethroughTextView) itemView.findViewById(R.id.price_strikethrough_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.price_textView);
            totalDiveTextView = (TextView) itemView.findViewById(R.id.total_dive_textView);
            totalDiveSpotTextView = (TextView) itemView.findViewById(R.id.total_dive_spot_textView);
            totalDayTextView = (TextView) itemView.findViewById(R.id.total_day_textView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_textView);
            visitedTextView = (TextView) itemView.findViewById(R.id.visited_textView);
            licenseImageView = (ImageView) itemView.findViewById(R.id.license_imageView);

            this.itemView = itemView;
        }

        public void setModel(DiveService diveService) {
            this.diveService = diveService;

            if (diveService != null){

                if (diveService.getDiveCenter() != null && diveService.getDiveCenter().getContact() != null &&
                        diveService.getDiveCenter().getContact().getLocation() != null ){

                    String locationString = "";
                    Location location = diveService.getDiveCenter().getContact().getLocation();
                    if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())){
                        locationString = locationString+", "+location.getCity().getName();
                    }

                    if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())){
                        locationString = locationString+", "+location.getProvince().getName();
                    }

                    if (NYHelper.isStringNotEmpty(locationString)){
                        locationTextView.setText(locationString);
                    } else {
                        locationTextView.setText("-");
                    }

                }

                if (NYHelper.isStringNotEmpty(diveService.getName())) serviceNameTextView.setText(diveService.getName());
                if (diveService.getDiveCenter() != null && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getName())) diveCenterNameTextView.setText(diveService.getDiveCenter().getName());

                if (diveService.getTotalDives() > 1){
                    totalDiveTextView.setText(String.valueOf(diveService.getTotalDives())+" Dives");
                } else {
                    totalDiveTextView.setText(String.valueOf(diveService.getTotalDives())+" Dive");
                }

                if (diveService.getTotalDiveSpots() > 1){
                    totalDiveSpotTextView.setText(String.valueOf(diveService.getTotalDiveSpots())+" Dive Spot Options");
                } else {
                    totalDiveSpotTextView.setText(String.valueOf(diveService.getTotalDiveSpots())+" Dive Spot Option");
                }

                if (diveService.getTotalDiveSpots() > 1){
                    totalDayTextView.setText(String.valueOf(diveService.getDays())+" Days");
                } else {
                    totalDayTextView.setText(String.valueOf(diveService.getDays())+" Day");
                }

                visitedTextView.setText(String.valueOf(diveService.getVisited())+" Visited");


                if (diveService.isLicense()){
                    licenseImageView.setImageResource(R.drawable.ic_license_orange);
                } else {
                    licenseImageView.setImageResource(R.drawable.ic_license_grey);
                }

                /*if (diveService.getDiveSpots() != null){
                    totalDiveSpotTextView.setText("Total Dive Spot : "+String.valueOf(diveService.getDiveSpots().size()));
                }*/

                ratingBar.setRating(diveService.getRating());
                ratingTextView.setText(String.valueOf(diveService.getRating()));


                if (diveService.getSpecialPrice() < diveService.getNormalPrice() && diveService.getSpecialPrice() > 0){
                    priceTextView.setText(NYHelper.priceFormatter(diveService.getSpecialPrice()));
                    priceStrikethroughTextView.setText(NYHelper.priceFormatter(diveService.getNormalPrice()));
                    priceStrikethroughTextView.setVisibility(View.VISIBLE);
                } else {
                    priceTextView.setText(NYHelper.priceFormatter(diveService.getNormalPrice()));
                    priceStrikethroughTextView.setVisibility(View.GONE);
                }

                //SET IMAGE
                NYApplication application = (NYApplication) activity.getApplication();
                Bitmap b = application.getCache("drawable://"+R.drawable.bg_placeholder);
                if(b != null) {
                    serviceImageView.setImageBitmap(b);
                } else {
                    serviceImageView.setImageResource(R.drawable.bg_placeholder);
                }

                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
                if (NYHelper.isStringNotEmpty(diveService.getFeaturedImage())) {
                    ImageLoader.getInstance().loadImage(diveService.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            serviceImageView.setImageBitmap(loadedImage);
                            //activity.getCache().put(imageUri, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });

                    ImageLoader.getInstance().displayImage(diveService.getFeaturedImage(), serviceImageView, NYHelper.getOption());

                } else {
                    serviceImageView.setImageResource(R.drawable.bg_placeholder);
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
            NYLog.e("BEFORE DIVE CENTER " + diveCenter);
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