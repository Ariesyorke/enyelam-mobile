package com.nyelam.android.home;

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
import com.nyelam.android.data.Event;
import com.nyelam.android.data.Location;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.StrikethroughTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private List<Event> eventList;

    public EventsRecyclerViewAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_modul_event, parent, false);
        return new EventsRecyclerViewAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(eventList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (eventList != null && !eventList.isEmpty()) {
            count += eventList.size();
        }
        return count;
    }

    public void addResult(Event event) {
        if (this.eventList == null) {
            this.eventList = new ArrayList<>();
        }
        this.eventList.add(event);
    }

    public void addResults(List<Event> events) {
        if (this.eventList == null) {
            this.eventList = new ArrayList<>();
        }

        this.eventList = events;
    }

    public void clear() {
        this.eventList = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView eventImageView;
        private TextView nameTextView;
        private TextView locationTextView;
        private StrikethroughTextView priceStrikethroughTextView;
        private TextView priceTextView;
        private View itemView;
        private Event event;

        public PromoViewHolder(View itemView) {
            super(itemView);
            eventImageView = (ImageView) itemView.findViewById(R.id.event_imageView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_textView);
            locationTextView = (TextView) itemView.findViewById(R.id.location_textView);
            priceStrikethroughTextView = (StrikethroughTextView) itemView.findViewById(R.id.price_strikethrough_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.price_textView);
            this.itemView = itemView;
        }

        public void setModel(Event event) {
            this.event = event;

            if (event != null){


                if (NYHelper.isStringNotEmpty(event.getName())) nameTextView.setText(event.getName());

                /*if (event.getgetContact() != null && event.getContact().getLocation() != null) {

                    Location location = diveCenter.getContact().getLocation();
                    String locString = "";

                    if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locString += location.getCity().getName();
                    if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locString += ", "+location.getProvince().getName();
                    //if (NYHelper.isStringNotEmpty(location.getCountry())) locString += ", "+location.getCountry();

                    locationTextView.setText(locString);
                }*/


                double normalPrice = Double.valueOf(event.getNormalPrice());
                double specialPrice = Double.valueOf(event.getSpecialPrice());

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
                if (NYHelper.isStringNotEmpty(event.getFeaturedImage())) {
                    ImageLoader.getInstance().loadImage(event.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
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

                    ImageLoader.getInstance().displayImage(event.getFeaturedImage(), eventImageView, NYHelper.getOption());

                } else {
                    eventImageView.setImageResource(R.drawable.example_pic);
                }


            }




            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, DiveCenterDetailActivity.class);
            intent.putExtra(NYHelper.EVENT, event.toString());
            activity.startActivity(intent);
        }
    }

}