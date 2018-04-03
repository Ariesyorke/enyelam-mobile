package com.nyelam.android.dodive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.NYStrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoDiveDiveServiceSuggestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveService> diveServiceList;

    public DoDiveDiveServiceSuggestionAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_dive_service, parent, false);
        return new DoDiveDiveServiceSuggestionAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(diveServiceList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (diveServiceList != null && !diveServiceList.isEmpty()) {
            count += diveServiceList.size();
        }
        return count;
    }

    public void addResult(DiveService diveService) {
        if (this.diveServiceList == null) {
            this.diveServiceList = new ArrayList<>();
        }
        this.diveServiceList.add(diveService);
    }

    public void addResults(List<DiveService> diveServiceList) {
        if (this.diveServiceList == null) {
            this.diveServiceList = new ArrayList<>();
        }
        removeTheDuplicates(diveServiceList);
    }

    public void clear() {
        this.diveServiceList = new ArrayList<>();
    }

    public DiveService getDiveService(int position){
        return diveServiceList.get(position);
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CardView cardView;
        private ImageView featuredImageView;
        private TextView diveCenterNameTextView;
        private TextView serviceNameTextView;
        private TextView ratingTextView;
        private RatingBar ratingBar;
        private NYStrikethroughTextView priceStrikethroughTextView;
        private TextView priceTextView;
        private TextView totalDiveTextView;
        private TextView totalDiveSpotTextView;
        private TextView totalDayTextView;
        private TextView visitedTextView;
        private View itemView;
        private DiveService diveService;

        public PromoViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            featuredImageView = (ImageView) itemView.findViewById(R.id.featured_imageView);
            serviceNameTextView = (TextView) itemView.findViewById(R.id.service_name_textView);
            diveCenterNameTextView = (TextView) itemView.findViewById(R.id.dive_center_name_textView);
            priceStrikethroughTextView = (NYStrikethroughTextView) itemView.findViewById(R.id.price_strikethrough_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.price_textView);
            totalDiveTextView = (TextView) itemView.findViewById(R.id.total_dive_textView);
            totalDiveSpotTextView = (TextView) itemView.findViewById(R.id.total_dive_spot_textView);
            totalDayTextView = (TextView) itemView.findViewById(R.id.total_day_textView);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_textView);
            visitedTextView = (TextView) itemView.findViewById(R.id.visitor_textView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

            /*DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)
                    cardView.getLayoutParams();
            layoutParams.width = width*3/4;*/

            this.itemView = itemView;
        }

        public void setModel(DiveService diveService) {
            this.diveService = diveService;

            if (diveService != null){

                ratingBar.setRating((int) diveService.getRating());

                ratingTextView.setText(String.valueOf(diveService.getRating()));

                if (NYHelper.isStringNotEmpty(diveService.getName())) serviceNameTextView.setText(diveService.getName());


                double normalPrice = Double.valueOf(diveService.getNormalPrice());
                double specialPrice = Double.valueOf(diveService.getSpecialPrice());

                if (specialPrice < normalPrice && specialPrice > 0){
                    priceTextView.setText(NYHelper.priceFormatter(specialPrice));
                    priceStrikethroughTextView.setText(NYHelper.priceFormatter(normalPrice));
                    priceStrikethroughTextView.setVisibility(View.VISIBLE);
                } else {
                    priceTextView.setText(NYHelper.priceFormatter(normalPrice));
                    priceStrikethroughTextView.setVisibility(View.GONE);
                }


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

                if (diveService.getDays() > 1){
                    totalDayTextView.setText(String.valueOf(diveService.getDays())+" Days");
                } else {
                    totalDayTextView.setText(String.valueOf(diveService.getDays())+" Day");
                }

                visitedTextView.setText(" / "+String.valueOf(diveService.getVisited())+" Visited");

                if (diveService.getDiveCenter() != null){

                    if (NYHelper.isStringNotEmpty(diveService.getDiveCenter().getName())) diveCenterNameTextView.setText("by "+diveService.getDiveCenter().getName());

                    if (NYHelper.isStringNotEmpty(diveService.getDiveCenter().getStartFromTotalDives()) && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getStartFromDays())){
                        String dives = "";
                        if (Integer.valueOf(diveService.getDiveCenter().getStartFromTotalDives()) > 1){
                            dives += diveService.getDiveCenter().getStartFromTotalDives()+" dives ";
                        } else {
                            dives += diveService.getDiveCenter().getStartFromTotalDives()+" dive ";
                        }

                        if (Integer.valueOf(diveService.getDiveCenter().getStartFromDays()) > 1){
                            dives += diveService.getDiveCenter().getStartFromDays()+" days";
                        } else {
                            dives += diveService.getDiveCenter().getStartFromDays()+" day";
                        }

                        totalDiveTextView.setText(dives);
                    }



                }


                //SET IMAGE
                NYApplication application = (NYApplication) activity.getApplication();
                Bitmap b = application.getCache("drawable://"+R.drawable.bg_placeholder);
                if(b != null) {
                    featuredImageView.setImageBitmap(b);
                } else {
                    featuredImageView.setImageResource(R.drawable.bg_placeholder);
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
                            //featuredImageView.setImageResource(R.drawable.bg_placeholder);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            featuredImageView.setImageBitmap(loadedImage);
                            //activity.getCache().put(imageUri, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            //featuredImageView.setImageResource(R.drawable.bg_placeholder);
                        }
                    });

                    ImageLoader.getInstance().displayImage(diveService.getFeaturedImage(), featuredImageView, NYHelper.getOption());

                } else {
                    featuredImageView.setImageResource(R.drawable.bg_placeholder);
                }
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // TODO: fill filed in search box
        }
    }


    private void removeTheDuplicates(List<DiveService> newDiveServiceList) {

        List<DiveService> temp = new ArrayList<>();

        for (DiveService newDs : newDiveServiceList){
            boolean isExist = false;
            for (DiveService ds : diveServiceList){
                if (ds.getId().equals(newDs.getId())){
                    isExist = true;
                    break;
                }
            }

            if (!isExist) temp.add(newDs);
            isExist = false;
        }

        diveServiceList.addAll(temp);

    }

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

}