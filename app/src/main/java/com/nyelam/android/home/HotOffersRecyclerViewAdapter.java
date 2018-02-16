package com.nyelam.android.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
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
import com.nyelam.android.data.Event;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.detail.DetailServiceActivity;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.StrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class HotOffersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveService> diveServices;

    public HotOffersRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_modul_hot_offers, parent, false);
        return new HotOffersRecyclerViewAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(diveServices.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (diveServices != null && !diveServices.isEmpty()) {
            count += diveServices.size();
        }
        return count;
    }

    public void addResult(DiveService diveService) {
        if (this.diveServices == null) {
            this.diveServices = new ArrayList<>();
        }
        this.diveServices.add(diveService);
    }

    public void addResults(List<DiveService> diveServices) {
        if (this.diveServices == null) {
            this.diveServices = new ArrayList<>();
        }

        this.diveServices = diveServices;
    }

    public void clear() {
        this.diveServices = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView eventImageView;
        private TextView nameTextView;
        private TextView locationTextView;
        private StrikethroughTextView priceStrikethroughTextView;
        private TextView priceTextView;
        private TextView dateTextView;
        private View itemView;
        private DiveService diveService;

        public PromoViewHolder(View itemView) {
            super(itemView);
            eventImageView = (ImageView) itemView.findViewById(R.id.event_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textView);
            locationTextView = (TextView) itemView.findViewById(R.id.location_textView);
            priceStrikethroughTextView = (StrikethroughTextView) itemView.findViewById(R.id.price_strikethrough_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.price_textView);
            dateTextView = (TextView) itemView.findViewById(R.id.date_textView);
            this.itemView = itemView;
        }

        public void setModel(DiveService diveService) {
            this.diveService = diveService;

            if (diveService != null){

                if (NYHelper.isStringNotEmpty(diveService.getName())) nameTextView.setText(diveService.getName());

                if (diveService.getDiveSpots().get(0) != null && diveService.getDiveSpots().get(0).getLocation() != null) {

                    Location location = diveService.getDiveSpots().get(0).getLocation();
                    String locString = "";
                    if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locString += location.getCity().getName();
                    if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locString += ", "+location.getProvince().getName();
                    //if (NYHelper.isStringNotEmpty(location.getCountry())) locString += ", "+location.getCountry();
                    locationTextView.setText(locString);
                }


                if (diveService.getSchedule() != null){
                    dateTextView.setText(NYHelper.setMillisToDate(diveService.getSchedule().getStartDate())+" - "+NYHelper.setMillisToDate(diveService.getSchedule().getStartDate()));
                }

                double normalPrice = Double.valueOf(diveService.getNormalPrice());
                double specialPrice = Double.valueOf(diveService.getSpecialPrice());

                if (specialPrice < normalPrice && specialPrice > 0){
                    priceTextView.setText(NYHelper.priceFormatter(specialPrice));
                    //priceStrikethroughTextView.setText(NYHelper.priceFormatter(normalPrice));
                    //priceStrikethroughTextView.setVisibility(View.VISIBLE);
                } else {
                    priceTextView.setText(NYHelper.priceFormatter(normalPrice));
                    //priceStrikethroughTextView.setVisibility(View.GONE);
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
                            eventImageView.setImageResource(R.drawable.example_pic);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            eventImageView.setImageBitmap(loadedImage);
                            //activity.getCache().put(imageUri, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            eventImageView.setImageResource(R.drawable.example_pic);
                        }
                    });

                    ImageLoader.getInstance().displayImage(diveService.getFeaturedImage(), eventImageView, NYHelper.getOption());

                } else {
                    eventImageView.setImageResource(R.drawable.example_pic);
                }


            }




            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            SearchService searchService = new SearchService();
            searchService.setName(diveService.getName());
            searchService.setId(diveService.getId());
            searchService.setLicense(false);
            searchService.setType(2);

            Intent intent = new Intent(activity, DoDiveActivity.class);
            intent.putExtra(NYHelper.SEARCH_RESULT, searchService.toString());
            activity.startActivity(intent);
        }
    }

}