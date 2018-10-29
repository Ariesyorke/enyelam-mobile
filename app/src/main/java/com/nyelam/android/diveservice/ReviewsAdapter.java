package com.nyelam.android.diveservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Review;
import com.nyelam.android.data.ReviewUser;
import com.nyelam.android.data.User;
import com.nyelam.android.divespot.DiveSpotDetailActivity;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/9/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Review> reviews;

    public ReviewsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_review, parent, false);
        return new PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(reviews.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (reviews != null && !reviews.isEmpty()) {
            count += reviews.size();
        }
        return count;
    }

    public void addResult(Review review) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.add(review);
    }

    public void addResults(List<Review> reviews) {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        this.reviews.addAll(reviews);
    }

    public void clear() {
        this.reviews = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CircleImageView userImageView;
        private TextView nameTextView;
        private TextView contentTextView;
        private TextView dateTextView;
        private RatingBar ratingBar;
        private View itemView;
        private Review review;

        public PromoViewHolder(View itemView) {
            super(itemView);
            userImageView = (CircleImageView) itemView.findViewById(R.id.user_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.user_name_textView);
            contentTextView = (TextView) itemView.findViewById(R.id.content_textView);
            dateTextView = (TextView) itemView.findViewById(R.id.date_textView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            this.itemView = itemView;
        }

        public void setModel(Review review) {
            this.review = review;

            if (this.review != null){
                if (review.getUser() != null){
                    ReviewUser user = review.getUser();
                    if (NYHelper.isStringNotEmpty(user.getFullname())) nameTextView.setText(user.getFullname());

                    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
                    if (NYHelper.isStringNotEmpty(user.getPicture())) {

                        ImageLoader.getInstance().loadImage(user.getPicture(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                userImageView.setImageResource(R.drawable.example_pic);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                userImageView.setImageBitmap(loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                userImageView.setImageResource(R.drawable.example_pic);
                            }
                        });

                        ImageLoader.getInstance().displayImage(user.getPicture(), userImageView, NYHelper.getOption());

                    } else {
                        userImageView.setImageResource(R.mipmap.ic_launcher_rounded);
                    }

                }
                if (NYHelper.isStringNotEmpty(review.getContent())) contentTextView.setText(review.getContent());
                dateTextView.setText(NYHelper.setMillisToDate(review.getDate()));
                ratingBar.setRating(review.getRating());

                itemView.setOnClickListener(this);
            }
            
        }

        @Override
        public void onClick(View v) {
            /*Intent intent = new Intent(context, DiveSpotDetailActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(NYHelper.DIVE_SPOT_ID, diveSpot.getId());
            context.startActivity(intent);*/
        }
    }

}
