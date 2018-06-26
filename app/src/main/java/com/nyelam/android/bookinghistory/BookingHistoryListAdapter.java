package com.nyelam.android.bookinghistory;

import android.app.Activity;
import android.content.Context;
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
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Order;
import com.nyelam.android.data.Summary;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class BookingHistoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity context;
    private List<Summary> summaryList;
    private boolean isPast;

    public BookingHistoryListAdapter(Activity context, boolean isPast) {
        this.context = context;
        this.isPast = isPast;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item_booking, parent, false);
        return new BookingHistoryListAdapter.PromoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PromoViewHolder) {
            PromoViewHolder vh = (PromoViewHolder) holder;
            vh.setModel(summaryList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (summaryList != null && !summaryList.isEmpty()) {
            count += summaryList.size();
        }
        return count;
    }

    public void addResult(Summary summary) {
        if (this.summaryList == null) {
            this.summaryList = new ArrayList<>();
        }
        this.summaryList.add(summary);
    }

    public void addResults(List<Summary> summaryList) {
        if (this.summaryList == null) {
            this.summaryList = new ArrayList<>();
        }
        this.summaryList.addAll(summaryList);
    }

    public void clear() {
        this.summaryList = new ArrayList<>();
    }

    class PromoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView serviceImageView;
        private TextView serviceNameTextView;
        private TextView locationTextView;
        private TextView priceTextView;
        private TextView scheduleTextView;
        private View itemView;
        private Summary summary;

        public PromoViewHolder(View itemView) {
            super(itemView);

            serviceImageView = (ImageView) itemView.findViewById(R.id.service_imageView);
            serviceNameTextView = (TextView) itemView.findViewById(R.id.service_name_textView);
            locationTextView = (TextView) itemView.findViewById(R.id.location_textView);
            priceTextView = (TextView) itemView.findViewById(R.id.price_textView);
            scheduleTextView = (TextView) itemView.findViewById(R.id.date_textView);

            this.itemView = itemView;
        }

        public void setModel(Summary summary) {
            this.summary = summary;

            if (summary != null){

                Order order = summary.getOrder();
                DiveService diveService = summary.getDiveService();

                if (order != null){
                    //if (NYHelper.isStringNotEmpty(order.getStatus())) serviceNameTextView.setText(diveService.getName());
                    if (order.getCart() != null) priceTextView.setText(NYHelper.priceFormatter(order.getCart().getTotal()));
                    scheduleTextView.setText(NYHelper.setMillisToDate(order.getSchedule()));
                }

                if (diveService != null){
                    if (NYHelper.isStringNotEmpty(diveService.getName())) serviceNameTextView.setText(diveService.getName());

                    // TODO: location from where ?
                    if (diveService.getDiveCenter() != null && diveService.getDiveCenter().getContact() != null && diveService.getDiveCenter().getContact().getLocation() != null && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getContact().getLocation().getCountry())) {
                        Location loc = new Location();
                        loc = diveService.getDiveCenter().getContact().getLocation();
                        locationTextView.setText(loc.getCity()+", "+loc.getProvince()+", "+loc.getCountry());
                    } else{
                        locationTextView.setText("");
                    }
                }




                //SET IMAGE
                final NYApplication application = (NYApplication) context.getApplicationContext();
                Bitmap b = application.getCache("drawable://"+R.drawable.bg_placeholder);
                if(b != null) {
                    serviceImageView.setImageBitmap(b);
                } else {
                    serviceImageView.setImageResource(R.drawable.bg_placeholder);
                }

                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
                if (NYHelper.isStringNotEmpty(diveService.getFeaturedImage())) {

                    if (application.getCache(diveService.getFeaturedImage()) != null){
                        serviceImageView.setImageBitmap(application.getCache(diveService.getFeaturedImage()));
                    } else {

                        ImageLoader.getInstance().loadImage(diveService.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                serviceImageView.setImageResource(R.drawable.example_pic);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                serviceImageView.setImageBitmap(loadedImage);
                                application.addCache(imageUri, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                serviceImageView.setImageResource(R.drawable.example_pic);
                            }
                        });

                        ImageLoader.getInstance().displayImage(diveService.getFeaturedImage(), serviceImageView, NYHelper.getOption());
                    }

                } else {
                    serviceImageView.setImageResource(R.drawable.example_pic);
                }

            }

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, BookingHistoryDetailActivity.class);
            if (summary != null) intent.putExtra(NYHelper.SERVICE, summary.toString());
            if (summary != null && summary.getOrder() != null && NYHelper.isStringNotEmpty(summary.getOrder().getOrderId()))intent.putExtra(NYHelper.ID_ORDER, summary.getOrder().getOrderId());
            intent.putExtra(NYHelper.IS_PAST, isPast);
            context.startActivity(intent);
        }

    }

}