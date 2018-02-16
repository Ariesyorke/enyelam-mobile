package com.nyelam.android.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.divespot.DiveSpotDetailActivity;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.StrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class PopularDiveSpotsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<DiveSpot> diveSpotList;

    public PopularDiveSpotsRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_modul_popular_dive_spot, parent, false);
        return new PopularDiveSpotsRecyclerViewAdapter.PromoViewHolder(v);
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

        this.diveSpotList = diveSpots;
    }

    public void clear() {
        this.diveSpotList = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView diveSpotImageView;
        private TextView diveSpotNameTextView;
        private LinearLayout containerLinaerLayout;
        private View itemView;
        private DiveSpot diveSpot;

        public PromoViewHolder(View itemView) {
            super(itemView);
            diveSpotImageView = (ImageView) itemView.findViewById(R.id.dive_spot_imageView);
            diveSpotNameTextView = (TextView) itemView.findViewById(R.id.dive_spot_name_textView);
            containerLinaerLayout = (LinearLayout) itemView.findViewById(R.id.container_linaerLayout);
            this.itemView = itemView;
        }

        public void setModel(DiveSpot diveSpot) {
            this.diveSpot = diveSpot;

            if (diveSpot != null){

                List<int[]> drColor = new ArrayList<>();
                drColor.add(new int[] {0xFFAFD201,0xFF1E4001});
                drColor.add(new int[] {0xFF0000E7,0xFF000050});
                drColor.add(new int[] {0xFFB10000,0xFF1E0000});
                drColor.add(new int[] {0xFF00CB00,0xFF013A01});
                drColor.add(new int[] {0xFFA900E0,0xFF1C0052});
                drColor.add(new int[] {0xFFFE0388,0xFFF05825});

                GradientDrawable gd = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        drColor.get(this.getAdapterPosition()%5));

                gd.setCornerRadius(0f);
                gd.setAlpha(180);

                int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    containerLinaerLayout.setBackgroundDrawable(gd);
                } else {
                    containerLinaerLayout.setBackground(gd);
                }

                if (NYHelper.isStringNotEmpty(diveSpot.getName())) diveSpotNameTextView.setText(diveSpot.getName());

                //SET IMAGE
                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
                if (NYHelper.isStringNotEmpty(diveSpot.getFeaturedImage())) {
                    ImageLoader.getInstance().loadImage(diveSpot.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            diveSpotImageView.setImageResource(R.drawable.example_pic);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            diveSpotImageView.setImageBitmap(loadedImage);
                            //activity.getCache().put(imageUri, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            diveSpotImageView.setImageResource(R.drawable.example_pic);
                        }
                    });

                    ImageLoader.getInstance().displayImage(diveSpot.getFeaturedImage(), diveSpotImageView, NYHelper.getOption());

                } else {
                    diveSpotImageView.setImageResource(R.drawable.example_pic);
                }


            }




            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            SearchService searchService = new SearchService();
            searchService.setName(diveSpot.getName());
            searchService.setId(diveSpot.getId());
            searchService.setLicense(false);
            searchService.setType(1);

            Intent intent = new Intent(activity, DoDiveActivity.class);
            intent.putExtra(NYHelper.SEARCH_RESULT, searchService.toString());
            activity.startActivity(intent);

        }
    }

}