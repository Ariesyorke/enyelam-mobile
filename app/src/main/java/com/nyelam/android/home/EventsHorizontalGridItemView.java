package com.nyelam.android.home;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.Location;
import com.nyelam.android.helper.NYHelper;

/**
 * Created by Sonic on 15/01/2018.
 */

public class EventsHorizontalGridItemView extends FrameLayout {

    private Activity activity;
    private ImageView imageView;
    private TextView locationTextView;
    private TextView nameTextView;
    private TextView priceTextView;
    private TextView bookNowTextView;
    private LinearLayout containerLinearLayout;
    private View rootView;
    private Event event;

    @Override
    public View getRootView() {
        return rootView;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        if (event == null) return;

        this.event = event;
        reloadView();
    }

    public EventsHorizontalGridItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context){
        inflate(context, R.layout.view_item_modul_event, this);

        imageView = findViewById(R.id.event_imageView);
        locationTextView = findViewById(R.id.location_textView);
        nameTextView = findViewById(R.id.name_textView);
        containerLinearLayout = findViewById(R.id.container_linaerLayout);
        priceTextView = findViewById(R.id.price_textView);
        bookNowTextView = findViewById(R.id.book_now_textView);

        rootView = findViewById(R.id.rootView);
    }

    private void reloadView(){
        if (event != null){

            if (NYHelper.isStringNotEmpty(event.getName())) nameTextView.setText(event.getName());

            if (event.getLocation() != null) {

                Location location = event.getLocation();
                String locString = "";

                if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locString += location.getCity().getName();
                if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locString += ", "+location.getProvince().getName();

                locationTextView.setText(locString);
            }


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
            final NYApplication application = (NYApplication) activity.getApplication();
            Bitmap b = application.getCache("drawable://"+R.drawable.bg_placeholder);
            if(b != null) {
                imageView.setImageBitmap(b);
            } else {
                imageView.setImageResource(R.drawable.bg_placeholder);
            }

            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
            if (NYHelper.isStringNotEmpty(event.getFeaturedImage())) {

                if (application.getCache(event.getFeaturedImage()) != null){
                    imageView.setImageBitmap(application.getCache(event.getFeaturedImage()));
                } else {

                    ImageLoader.getInstance().loadImage(event.getFeaturedImage(), NYHelper.getOption(), new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            imageView.setImageResource(R.drawable.example_pic);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            imageView.setImageBitmap(loadedImage);
                            application.addCache(imageUri, loadedImage);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            imageView.setImageResource(R.drawable.example_pic);
                        }
                    });

                    ImageLoader.getInstance().displayImage(event.getFeaturedImage(), imageView, NYHelper.getOption());
                }

            } else {
                imageView.setImageResource(R.drawable.example_pic);
            }


        }

    }

}
