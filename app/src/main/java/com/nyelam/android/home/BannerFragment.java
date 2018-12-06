package com.nyelam.android.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.doshop.DoShopDetailItemActivity;
import com.nyelam.android.doshop.DoShopDetailItemImagesActivity;
import com.nyelam.android.dotrip.DoTripActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.NYBannerViewPager;
import com.nyelam.android.view.NYImageRatioImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BannerFragment extends Fragment {

    private static final String ARG_POS = "arg_pos";
    private static final String ARG_BANNER = "arg_banner";
    private NYImageRatioImageView imageView;
    private Banner banner;
    private boolean isGallery;

    private int position;
    private ArrayList<String> imageList;
    private boolean isOnclick;

    public BannerFragment(){}

    @SuppressLint("ValidFragment")
    public BannerFragment(int position, Banner retBanner) {
        this.banner = retBanner;
//        Bundle args = new Bundle();
//        args.putInt(ARG_POS, position);
//        if (banner != null)args.putString(ARG_BANNER, banner.toString());
//        this.setArguments(args);
    }

    @SuppressLint("ValidFragment")
    public BannerFragment(int position, Banner retBanner, boolean isGallery) {
        this.banner = retBanner;
        this.isGallery = isGallery;
    }

    @SuppressLint("ValidFragment")
    public BannerFragment(int position, Banner retBanner, boolean isGallery, ArrayList<String> imageList, boolean isOnlick) {
        this.position = position;
        this.banner = retBanner;
        this.isGallery = isGallery;
        this.imageList = imageList;
        this.isOnclick = isOnlick;
    }

    public  BannerFragment newInstance(int position, Banner banner) {
        BannerFragment fragment = new BannerFragment(position, banner);
        Bundle args = new Bundle();
        args.putInt(ARG_POS, position);
        if (banner != null)args.putString(ARG_BANNER, banner.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = (NYImageRatioImageView)view.findViewById(R.id.imageView);

//        if (banner != null)banner = new Banner();
        if(banner != null){
            setDrawable(banner);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open browser on click if not null
                if (isGallery && isOnclick && imageList.size() > 0){
                    Intent intent = new Intent(getActivity(), DoShopDetailItemImagesActivity.class);
                    intent.putStringArrayListExtra(DoShopDetailItemImagesActivity.KEY_IMAGES,imageList);
                    intent.putExtra(DoShopDetailItemImagesActivity.POSITION, position);
                    startActivity(intent);
                }
//                else if (isGallery){
//                    Intent intent = new Intent(getActivity(), DoShopDetailItemActivity.class);
//                    startActivity(intent);
//                }
                else if (banner != null){
//                    if (banner.getType() == 1 && NYHelper.isStringNotEmpty(banner.getServiceId()) && NYHelper.isStringNotEmpty(banner.getServiceName())){
                    if (banner.getType() == 1 && NYHelper.isStringNotEmpty(banner.getServiceId())){

                        // TODO: intent ke detail service (ECO TRIP)
                        DiveService service = new DiveService();
                        service.setId(banner.getServiceId());
                        service.setName(banner.getServiceName());

                        Intent intent = new Intent(getActivity(), DetailServiceActivity.class);
                        intent.putExtra(NYHelper.SERVICE, service.toString());
                        intent.putExtra(NYHelper.SCHEDULE, String.valueOf(banner.getDate()));

                        if (banner.isLicense()){
                            intent.putExtra(NYHelper.CERTIFICATE, "1");
                        } else {
                            intent.putExtra(NYHelper.CERTIFICATE, "0");
                        }

                        intent.putExtra(NYHelper.IS_DO_COURSE, banner.isDoCourse());
                        intent.putExtra(NYHelper.IS_DO_TRIP, banner.isDoTrip());
                        intent.putExtra(NYHelper.DIVER, "1");
                        startActivity(intent);

                    } else if (banner.getType() == 2 && NYHelper.isStringNotEmpty(banner.getUrl())){

                        String url = banner.getUrl();

                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
//                    } else if (banner.getType() == 3 && NYHelper.isStringNotEmpty(banner.getServiceId()) && NYHelper.isStringNotEmpty(banner.getServiceName())){
                    } else if (banner.getType() == 3 && NYHelper.isStringNotEmpty(banner.getServiceId())){

                        // TODO: intent ke detail service (DO DIVE)
                        SearchService service = new SearchService();
                        service.setId(banner.getServiceId());
                        service.setName(banner.getServiceName());
                        service.setType(4);
                        service.setLicense(banner.isLicense());

                        Intent intent = new Intent(getActivity(), DoDiveActivity.class);
                        intent.putExtra(NYHelper.SEARCH_RESULT, service.toString());
//                        intent.putExtra(NYHelper.SCHEDULE, String.valueOf(banner.getDate()));

                        intent.putExtra(NYHelper.CERTIFICATE, banner.isLicense());

                        intent.putExtra(NYHelper.DIVER, "1");

                        if (banner.isDoTrip()){
                            intent.putExtra(NYHelper.IS_DO_TRIP, 1);
                        } else {
                            intent.putExtra(NYHelper.IS_DO_TRIP, 0);
                        }

                        if (banner.isEcoTrip()){
                            intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                        } else {
                            intent.putExtra(NYHelper.IS_ECO_TRIP, 0);
                        }

                        startActivity(intent);
                    } else if (banner.getType() == 4 && NYHelper.isStringNotEmpty(banner.getServiceId())){

                        // TODO: intent ke detail service (DO COURSE)
                        DiveService service = new DiveService();
                        service.setId(banner.getServiceId());
                        service.setName(banner.getServiceName());

                        Intent intent = new Intent(getActivity(), DetailServiceActivity.class);
                        if (service != null ) intent.putExtra(NYHelper.SERVICE, service.toString());
                        intent.putExtra(NYHelper.DIVER, "1");
                        intent.putExtra(NYHelper.SCHEDULE, String.valueOf(banner.getDate()));

                        if (banner.isLicense()){
                            intent.putExtra(NYHelper.CERTIFICATE, "1");
                        } else {
                            intent.putExtra(NYHelper.CERTIFICATE, "0");
                        }

                        if (service != null && service.getDiveCenter() != null) {
                            intent.putExtra(NYHelper.DIVE_CENTER, service.getDiveCenter().toString());
                        }
                        intent.putExtra(NYHelper.IS_DO_COURSE, true);
                        startActivity(intent);
                    }

                } else {
                    NYHelper.handlePopupMessage(getActivity(), getString(R.string.coming_soon), null);
                }



            }
        });


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_banner, container, false);
    }

    public void setDrawable(Banner banner) {

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));

        //SET IMAGE
        final NYApplication application = (NYApplication) getActivity().getApplication();
        Bitmap b = application.getCache("drawable://"+R.drawable.bg_placeholder);

        if (imageView != null){
            if(b != null) {
                imageView.setImageBitmap(b);
            } else {
                imageView.setImageResource(R.drawable.bg_placeholder);
            }
        }



        if (banner.getImageUrl() == null || TextUtils.isEmpty(banner.getImageUrl())) {
            imageView.setImageResource(R.drawable.bg_placeholder);
        } else {

            if (application.getCache(banner.getImageUrl()) != null){
                imageView.setImageBitmap(application.getCache(banner.getImageUrl()));
            } else {

                ImageLoader.getInstance().loadImage(banner.getImageUrl(), NYHelper.getOption(), new ImageLoadingListener() {
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

                ImageLoader.getInstance().displayImage(banner.getImageUrl(), imageView, NYHelper.getOption());
            }

        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
