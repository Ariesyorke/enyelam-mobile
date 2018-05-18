package com.nyelam.android.dodive;

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
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Location;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoDiveSearchDiveSpotAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveSpot> diveSpotList;
    private String diver;
    private String date;
    private String certificate;
    private String type;

    public DoDiveSearchDiveSpotAdapter(Activity activity, String diver, String date, String certificate, String type) {
        this.activity = activity;
        this.diver = diver;
        this.date = date;
        this.certificate = certificate;
        this.type = type;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_search_dive_spot, parent, false);
        return new DoDiveSearchDiveSpotAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(diveSpotList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (diveSpotList != null && !diveSpotList.isEmpty()) {
            count += diveSpotList.size();
        }
        return count;
    }

    public void addResult(DiveSpot diveSpot) {
        if (this.diveSpotList == null) {
            this.diveSpotList = new ArrayList<>();
        }
        this.diveSpotList.add(diveSpot);
    }

    public void addResults(List<DiveSpot> diveSpots) {
        if (this.diveSpotList == null) {
            this.diveSpotList = new ArrayList<>();
        }
        removeTheDuplicates(diveSpots);
    }

    public void clear() {
        this.diveSpotList = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView featuredImageView;
        private TextView nameTextView;
        private TextView locationTextView;
        private View itemView;
        private DiveSpot diveSpot;

        public PromoViewHolder(View itemView) {
            super(itemView);

            featuredImageView = (ImageView) itemView.findViewById(R.id.featured_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textView);
            locationTextView = (TextView) itemView.findViewById(R.id.location_textView);

            this.itemView = itemView;
        }

        public void setModel(DiveSpot diveSpot) {
            this.diveSpot = diveSpot;

            if (diveSpot != null){

                if (NYHelper.isStringNotEmpty(diveSpot.getName())) nameTextView.setText(diveSpot.getName());

                if (diveSpot.getLocation() != null) {

                    Location location = diveSpot.getLocation();
                    String locString = "";

                    if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locString += location.getCity().getName();
                    if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locString += ", "+location.getProvince().getName();
                    //if (NYHelper.isStringNotEmpty(location.getCountry())) locString += ", "+location.getCountry();

                    locationTextView.setText(locString);
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
                if (NYHelper.isStringNotEmpty(diveSpot.getFeaturedImage())) {

                    if (application.getCache(diveSpot.getFeaturedImage()) != null){
                        featuredImageView.setImageBitmap(application.getCache(diveSpot.getFeaturedImage()));
                    } else {

                        ImageLoader.getInstance().loadImage(diveSpot.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
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

                        ImageLoader.getInstance().displayImage(diveSpot.getFeaturedImage(), featuredImageView, NYHelper.getOption());
                    }

                } else {
                    featuredImageView.setImageResource(R.drawable.example_pic);
                }



            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(activity, DoDiveSearchResultActivity.class);
            //intent.putExtra(NYHelper.DIVE_SPOT, diveSpot.toString());
            intent.putExtra(NYHelper.ID_DIVER, diveSpot.getId());
            intent.putExtra(NYHelper.KEYWORD, diveSpot.getName());
            intent.putExtra(NYHelper.DIVER, diver);
            intent.putExtra(NYHelper.SCHEDULE, date);
            intent.putExtra(NYHelper.CERTIFICATE, certificate);
            intent.putExtra(NYHelper.TYPE, "1");
            activity.startActivity(intent);
        }
    }


    private void removeTheDuplicates(List<DiveSpot> newDiveSpotList) {

        List<DiveSpot> temp = new ArrayList<>();

        for (DiveSpot newDs : newDiveSpotList){
            boolean isExist = false;
            for (DiveSpot ds : diveSpotList){
                if (ds.getId().equals(newDs.getId())){
                    isExist = true;
                    break;
                }
            }

            if (!isExist) temp.add(newDs);
            isExist = false;
        }

        diveSpotList.addAll(temp);

    }

}