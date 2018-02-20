package com.nyelam.android.dodive;

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
import com.nyelam.android.data.Location;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.StrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoDiveSearchDiveCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveCenter> diveCenterList;
    private String diver;
    private String date;
    private String certificate;
    private String type;
    private String diverId;

    public DoDiveSearchDiveCenterAdapter(Activity activity, String diver, String date, String certificate, String type, String diverId) {
        this.activity = activity;
        this.diver = diver;
        this.date = date;
        this.certificate = certificate;
        this.type = type;
        this.diverId = diverId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_dive_center, parent, false);
        return new DoDiveSearchDiveCenterAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(diveCenterList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (diveCenterList != null && !diveCenterList.isEmpty()) {
            count += diveCenterList.size();
        }
        return count;
    }

    public void addResult(DiveCenter diveCenter) {
        if (this.diveCenterList == null) {
            this.diveCenterList = new ArrayList<>();
        }
        this.diveCenterList.add(diveCenter);
    }

    public void addResults(List<DiveCenter> diveCenters) {
        if (this.diveCenterList == null) {
            this.diveCenterList = new ArrayList<>();
        }
        removeTheDuplicates(diveCenters);
    }

    public void clear() {
        this.diveCenterList = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView featuredImageView;
        private TextView diveCenterNameTextView;
        private TextView locationTextView;
        private TextView ratingTextView;
        private RatingBar ratingBar;
        private StrikethroughTextView priceStrikethroughTextView;
        private TextView priceTextView;
        private TextView totalDiveTextView;
        private View itemView;
        private DiveCenter diveCenter;

        public PromoViewHolder(View itemView) {
            super(itemView);

            featuredImageView = (ImageView) itemView.findViewById(R.id.featured_imageView);
            diveCenterNameTextView = (TextView) itemView.findViewById(R.id.dive_center_name_textView);
            locationTextView = (TextView) itemView.findViewById(R.id.location_textView);
            priceStrikethroughTextView = (StrikethroughTextView) itemView.findViewById(R.id.price_strikethrough_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.price_textView);
            totalDiveTextView = (TextView) itemView.findViewById(R.id.total_dive_textView);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_textView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            this.itemView = itemView;
        }

        public void setModel(DiveCenter diveCenter) {
            this.diveCenter = diveCenter;

            if (diveCenter != null){

                ratingBar.setRating((int) diveCenter.getRating());

                ratingTextView.setText(String.valueOf(diveCenter.getRating()));

                if (NYHelper.isStringNotEmpty(diveCenter.getName())) diveCenterNameTextView.setText(diveCenter.getName());

                if (diveCenter.getContact() != null && diveCenter.getContact().getLocation() != null) {

                    Location location = diveCenter.getContact().getLocation();
                    String locString = "";

                    if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locString += location.getCity().getName();
                    if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locString += ", "+location.getProvince().getName();
                    //if (NYHelper.isStringNotEmpty(location.getCountry())) locString += ", "+location.getCountry();

                    locationTextView.setText(locString);
                }

                if (NYHelper.isStringNotEmpty(diveCenter.getStartFromTotalDives()) && NYHelper.isStringNotEmpty(diveCenter.getStartFromDays())){
                    String dives = "";
                    if (Integer.valueOf(diveCenter.getStartFromTotalDives()) > 1){
                        dives += diveCenter.getStartFromTotalDives()+" dives ";
                    } else {
                        dives += diveCenter.getStartFromTotalDives()+" dive ";
                    }

                    if (Integer.valueOf(diveCenter.getStartFromDays()) > 1){
                        dives += diveCenter.getStartFromDays()+" days";
                    } else {
                        dives += diveCenter.getStartFromDays()+" day";
                    }

                    totalDiveTextView.setText(dives);
                }

                double normalPrice = Double.valueOf(diveCenter.getStartFromPrice());
                double specialPrice = Double.valueOf(diveCenter.getStartFromSpecialPrice());

                if (specialPrice < normalPrice && specialPrice > 0){
                    priceTextView.setText(NYHelper.priceFormatter(specialPrice));
                    priceStrikethroughTextView.setText(NYHelper.priceFormatter(normalPrice));
                    priceStrikethroughTextView.setVisibility(View.VISIBLE);
                } else {
                    priceTextView.setText(NYHelper.priceFormatter(normalPrice));
                    priceStrikethroughTextView.setVisibility(View.GONE);
                }

                //SET IMAGE
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
                if (NYHelper.isStringNotEmpty(diveCenter.getFeaturedImage())) {
                    ImageLoader.getInstance().loadImage(diveCenter.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            featuredImageView.setImageResource(R.drawable.example_pic);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            featuredImageView.setImageBitmap(loadedImage);
                            //activity.getCache().put(imageUri, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            featuredImageView.setImageResource(R.drawable.example_pic);
                        }
                    });

                    ImageLoader.getInstance().displayImage(diveCenter.getFeaturedImage(), featuredImageView, NYHelper.getOption());

                } else {
                    featuredImageView.setImageResource(R.drawable.example_pic);
                }
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, DiveCenterDetailActivity.class);
            intent.putExtra(NYHelper.DIVE_CENTER, diveCenter.toString());
            intent.putExtra(NYHelper.DIVER, diver);
            intent.putExtra(NYHelper.ID_DIVER, diverId);
            intent.putExtra(NYHelper.CERTIFICATE, certificate);
            intent.putExtra(NYHelper.SCHEDULE, date);
            intent.putExtra(NYHelper.TYPE, type);
            activity.startActivity(intent);
        }
    }


    private void removeTheDuplicates(List<DiveCenter> newDiveCenterList) {

        List<DiveCenter> temp = new ArrayList<>();

        for (DiveCenter newDs : newDiveCenterList){
            boolean isExist = false;
            for (DiveCenter ds : diveCenterList){
                if (ds.getId().equals(newDs.getId())){
                    isExist = true;
                    break;
                }
            }

            if (!isExist) temp.add(newDs);
            isExist = false;
        }

        diveCenterList.addAll(temp);

    }

}