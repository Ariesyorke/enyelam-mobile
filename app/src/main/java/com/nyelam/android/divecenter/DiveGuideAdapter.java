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
import com.nyelam.android.data.DiveGuide;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Location;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.NYStrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DiveGuideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveGuide> diveGuides;

    public DiveGuideAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_dive_guide, parent, false);
        return new DiveGuideAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(diveGuides.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (diveGuides != null && !diveGuides.isEmpty()) {
            count += diveGuides.size();
        }
        return count;
    }

    public void addResult(DiveGuide searchResult) {
        if (this.diveGuides == null) {
            this.diveGuides = new ArrayList<>();
        }
        this.diveGuides.add(searchResult);
    }

    public void addResults(List<DiveGuide> diveGuides) {
        if (this.diveGuides == null) {
            this.diveGuides = new ArrayList<>();
        }
        this.diveGuides = diveGuides;
    }

    public void clear() {
        this.diveGuides = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View itemView;
        private DiveGuide diveGuide;

        private CircleImageView diveGuidePhotoCircleImageView;
        private TextView diveGuideNameTextView;
        private TextView diveGuideLicenseTextView;

        public PromoViewHolder(View itemView) {
            super(itemView);

            diveGuidePhotoCircleImageView = (CircleImageView) itemView.findViewById(R.id.dive_guide_photo_circleImageView);
            diveGuideNameTextView = (TextView) itemView.findViewById(R.id.dive_guide_name_textView);
            diveGuideLicenseTextView = (TextView) itemView.findViewById(R.id.dive_guide_license_textView);

            this.itemView = itemView;
        }

        public void setModel(DiveGuide diveGuide) {
            this.diveGuide = diveGuide;

            if (diveGuide != null){

                if (NYHelper.isStringNotEmpty(diveGuide.getFullName())){
                    diveGuideNameTextView.setText(diveGuide.getFullName());
                }

                if (diveGuide.getCertificateDiver() != null &&
                        NYHelper.isStringNotEmpty(diveGuide.getCertificateDiver().getName())){
                    diveGuideLicenseTextView.setText(diveGuide.getCertificateDiver().getName());
                }

                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
                if (NYHelper.isStringNotEmpty(diveGuide.getPicture())) {

                    ImageLoader.getInstance().loadImage(diveGuide.getPicture(), NYHelper.getOption(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            diveGuidePhotoCircleImageView.setImageResource(R.mipmap.ic_launcher);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            diveGuidePhotoCircleImageView.setImageBitmap(loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            diveGuidePhotoCircleImageView.setImageResource(R.mipmap.ic_launcher);
                        }
                    });

                    ImageLoader.getInstance().displayImage(diveGuide.getPicture(), diveGuidePhotoCircleImageView, NYHelper.getOption());

                } else {
                    diveGuidePhotoCircleImageView.setImageResource(R.mipmap.ic_launcher);
                }

            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(activity, DiveGuideActivity.class);
            if (diveGuide != null ) intent.putExtra(NYHelper.SERVICE, diveGuide.toString());
            activity.startActivity(intent);

        }
    }




}