package com.nyelam.android.docourse;

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
import com.nyelam.android.NYPagingBridge;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.dodive.DoDiveSearchResultActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.NYStrikethroughTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoCourseSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements NYPagingBridge<DiveService> {

    private DoCourseResultActivity activity;
    private List<DiveService> diveServiceList;
    private String diver;
    private String date;
    private String certificate;
    private String type;
    private String diverId;
    private int sortType = 2;

    private int visibility = View.GONE;

    public void changeVisibility(int visibility) {
        this.visibility = visibility;
    }

    public DoCourseSearchAdapter(DoCourseResultActivity activity, String diver, String date, String certificate, String type, String diverId) {
        this.activity = activity;
        this.diver = diver;
        this.date = date;
        this.certificate = certificate;
        this.type = type;
        this.diverId = diverId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_do_course, parent, false);
        return new DoCourseSearchAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(diveServiceList.get(position));
        }
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        if (diveServiceList != null){
            return diveServiceList.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void addServices(List<DiveService> events, boolean isLatest) {
        if(this.diveServiceList == null) {
            this.diveServiceList = new ArrayList<>();
        }
        List<DiveService> temp = removeSameDatas(events);
        if(temp != null && !temp.isEmpty()) {
            if (isLatest) {
                if(!this.diveServiceList.isEmpty()) {
                    addListIntoTop(temp);
                } else {
                    this.diveServiceList.addAll(temp);
                }
            } else {
                addListIntoBottom(temp);
            }
        }
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

    public void setSortType(int sortType){
        this.sortType = sortType;
    }

    public void sortData(){
        Collections.sort(diveServiceList, new StudentDateComparator());
    }

    public void clear() {
        this.diveServiceList = new ArrayList<>();
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

                if (diveService.isLicense())
                    divingLicenseImageView.setImageResource(R.drawable.ic_license_orange);

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

                if (diveService.getDiveCenter() != null && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getName())){
                    diveCenterNameTextView.setText(diveService.getDiveCenter().getName());
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
            // TODO: intent ke detail service
            Intent intent = new Intent(activity, DetailServiceActivity.class);
            if (diveService != null ) intent.putExtra(NYHelper.SERVICE, diveService.toString());
            intent.putExtra(NYHelper.DIVER, diver);
            intent.putExtra(NYHelper.SCHEDULE, date);
            intent.putExtra(NYHelper.CERTIFICATE, certificate);
            if (diveService != null && diveService.getDiveCenter() != null) {
                intent.putExtra(NYHelper.DIVE_CENTER, diveService.getDiveCenter().toString());
            }
            intent.putExtra(NYHelper.IS_DO_COURSE, true);
            activity.startActivity(intent);
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

    private View onCreateLoadingView(View view) {
        if(view == null) {
            view = View.inflate(activity, R.layout.view_loading, null);
        }
        view.setVisibility(visibility);
        return view;
    }

    @Override
    public void addItemIntoTop(DiveService item) {

    }

    @Override
    public void addItemIntoBottom(DiveService item) {

    }

    @Override
    public void addListIntoTop(List<DiveService> list) {
        Collections.reverse(diveServiceList);
        diveServiceList.addAll(list);
        Collections.reverse(diveServiceList);
    }

    @Override
    public void addListIntoBottom(List<DiveService> list) {
        diveServiceList.addAll(list);
    }

    @Override
    public List<DiveService> removeSameDatas(List<DiveService> latest) {
        if(latest == null || latest.isEmpty()) {
            return null;
        }

        List<DiveService> temp = latest;
        if(diveServiceList != null && !diveServiceList.isEmpty() && diveServiceList.size() > 0){
            for(int i = 0; i < diveServiceList.size(); i++) {
                for(int j = 0; j < temp.size(); j++) {
                    if(diveServiceList.get(i).getId().equals(temp.get(j).getId())) {
                        temp.remove(j);
                        break;
                    }
                }
            }
        }

        return temp;
    }

    @Override
    public boolean isDataSame(DiveService item) {
        if(item == null) {
            return true;
        }
        for(DiveService diveService : diveServiceList) {
            if(diveService.getId().equals(item.getId())) {
                return true;
            }
        }
        return false;
    }

    /*@Override
    public List<Event> removeSameDatas(List<Event> latest) {
        if(latest == null || latest.isEmpty()) {
            return null;
        }
        List<Event> temp = latest;
        if(events != null && !events.isEmpty() && events.size() > 0){
            for(int i = 0; i < events.size(); i++) {
                for(int j = 0; j < temp.size(); j++) {
                    if(events.get(i).getId().equals(temp.get(j).getId())) {
                        temp.remove(j);
                        break;
                    }
                }
            }
        }

        return temp;
    }*/

    class StudentDateComparator implements Comparator<DiveService> {
        public int compare(DiveService s1, DiveService s2) {
            if (sortType == 2){
                return Double.compare(s1.getSpecialPrice(), s2.getSpecialPrice());
            } else {
                return Double.compare(s2.getSpecialPrice(), s1.getSpecialPrice());
            }
        }
    }

}