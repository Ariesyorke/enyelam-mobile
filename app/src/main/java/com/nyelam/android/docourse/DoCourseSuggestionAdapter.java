package com.nyelam.android.docourse;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import com.nyelam.android.data.DiveService;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.NYStrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoCourseSuggestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveService> diveServiceList;
    private boolean isHorizontal;

    public DoCourseSuggestionAdapter(Activity activity) {
        this.activity = activity;
    }

    public DoCourseSuggestionAdapter(Activity activity, boolean isHorizontal) {
        this.activity = activity;
        this.isHorizontal = isHorizontal;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_do_course, parent, false);
        return new DoCourseSuggestionAdapter.PromoViewHolder(v);
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

        this.diveServiceList.addAll(diveServiceList);
        //removeTheDuplicates(diveServiceList);
    }

    public void clear() {
        this.diveServiceList = new ArrayList<>();
    }

    public DiveService getDiveService(int position){
        return diveServiceList.get(position);
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView featuredImageView;
        private ImageView divingLicenseImageView;
        private TextView diveCenterNameTextView;
        private TextView serviceNameTextView;
        private TextView ratingTextView;
        private RatingBar ratingBar;
        private NYStrikethroughTextView priceStrikethroughTextView;
        private TextView priceTextView;
        private TextView totalDayClassTextView;
        private TextView totalDayOnSiteTextView;
        private View itemView;
        private DiveService diveService;

        public PromoViewHolder(View itemView) {
            super(itemView);
            featuredImageView = (ImageView) itemView.findViewById(R.id.featured_imageView);
            divingLicenseImageView = (ImageView) itemView.findViewById(R.id.diving_license_imageView);
            serviceNameTextView = (TextView) itemView.findViewById(R.id.service_name_textView);
            diveCenterNameTextView = (TextView) itemView.findViewById(R.id.dive_center_name_textView);
            priceStrikethroughTextView = (NYStrikethroughTextView) itemView.findViewById(R.id.price_strikethrough_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.price_textView);
            totalDayClassTextView = (TextView) itemView.findViewById(R.id.day_class_textView);
            totalDayOnSiteTextView = (TextView) itemView.findViewById(R.id.day_onsite_textView);
            ratingTextView = (TextView) itemView.findViewById(R.id.rating_textView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);


            /*if (isHorizontal){
                DisplayMetrics displayMetrics = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int width = displayMetrics.widthPixels;

                RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams)
                        cardView.getLayoutParams();
                layoutParams.width = width*3/4;
            }*/

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

                if (diveService.isLicense()){
                    divingLicenseImageView.setVisibility(View.VISIBLE);
                } else {
                    divingLicenseImageView.setVisibility(View.GONE);
                }

                if (specialPrice < normalPrice && specialPrice > 0){
                    priceTextView.setText(NYHelper.priceFormatter(specialPrice));
                    priceStrikethroughTextView.setText(NYHelper.priceFormatter(normalPrice));
                    priceStrikethroughTextView.setVisibility(View.VISIBLE);
                } else {
                    priceTextView.setText(NYHelper.priceFormatter(normalPrice));
                    priceStrikethroughTextView.setVisibility(View.GONE);
                }


                if (diveService.getDays() > 1){
                    totalDayClassTextView.setText(String.valueOf(diveService.getDays())+" Day(s) class");
                } else {
                    totalDayClassTextView.setText(String.valueOf(diveService.getDays())+" Day class");
                }

                if (diveService.getDayOnSite() > 1){
                    totalDayOnSiteTextView.setText(String.valueOf(diveService.getDayOnSite())+" Day(s) on-site");
                } else {
                    totalDayOnSiteTextView.setText(String.valueOf(diveService.getDayOnSite())+" Day on-site");
                }


                //SET IMAGE
                final NYApplication application = (NYApplication) activity.getApplication();
                Bitmap b = application.getCache("drawable://"+R.drawable.bg_placeholder);
                if(b != null) {
                    featuredImageView.setImageBitmap(b);
                } else {
                    featuredImageView.setImageResource(R.drawable.bg_placeholder);
                }

                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
                if (NYHelper.isStringNotEmpty(diveService.getFeaturedImage())) {

                    if (application.getCache(diveService.getFeaturedImage()) != null){
                        featuredImageView.setImageBitmap(application.getCache(diveService.getFeaturedImage()));
                    } else {

                        ImageLoader.getInstance().loadImage(diveService.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
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
                                application.addCache(imageUri, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                featuredImageView.setImageResource(R.drawable.example_pic);
                            }
                        });

                        ImageLoader.getInstance().displayImage(diveService.getFeaturedImage(), featuredImageView, NYHelper.getOption());
                    }

                } else {
                    featuredImageView.setImageResource(R.drawable.example_pic);
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