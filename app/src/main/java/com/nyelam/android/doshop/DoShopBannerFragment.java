package com.nyelam.android.doshop;


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
import com.nyelam.android.BasicFragment;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.Brand;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DoShopBanner;
import com.nyelam.android.data.DoShopCategory;
import com.nyelam.android.data.DoShopCategoryList;
import com.nyelam.android.data.DoShopProduct;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.diveservice.DetailServiceActivity;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.BannerFragment;
import com.nyelam.android.view.NYImageRatioImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoShopBannerFragment extends BasicFragment {

    private static final String ARG_POS = "arg_pos";
    private static final String ARG_BANNER = "arg_banner";
    private NYImageRatioImageView imageView;
    private DoShopBanner banner;

    public DoShopBannerFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public DoShopBannerFragment(int position, DoShopBanner retBanner) {
        this.banner = retBanner;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_do_shop_banner;
    }

    public BannerFragment newInstance(int position, Banner banner) {
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

        if(banner != null){
            setDrawable(banner);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open browser on click if not null
                if (banner != null){
                    if (banner.getType().equals("detail")  && NYHelper.isStringNotEmpty(banner.getTarget())){

                        // TODO: intent ke detail product
                        DoShopProduct product = new DoShopProduct();
                        product.setId(banner.getTarget());
                        product.setProductName(banner.getTargetName());

                        Intent intent = new Intent(getActivity(), DoShopDetailItemActivity.class);
                        intent.putExtra(NYHelper.PRODUCT, product.toString());
                        startActivity(intent);

                    } else if (banner.getType().equals("category") && NYHelper.isStringNotEmpty(banner.getTarget())){

                        // TODO: intent ke detail category
                        DoShopCategory category = new DoShopCategory();
                        category.setId(banner.getTarget());
                        category.setName(banner.getTargetName());

                        Intent intent = new Intent(getActivity(), DoShopCategoryActivity.class);
                        intent.putExtra(NYHelper.CATEGORY, category.toString());
                        startActivity(intent);

                    } else if (banner.getType().equals("brand") && NYHelper.isStringNotEmpty(banner.getTarget())){

                        // TODO: intent ke detail brand
                        Brand brand = new Brand();
                        brand.setId(banner.getTarget());
                        brand.setName(banner.getTargetName());

                        Intent intent = new Intent(getActivity(), DoShopBrandActivity.class);
                        intent.putExtra(NYHelper.BRAND, brand.toString());
                        startActivity(intent);

                    } else if (banner.getType().equals("url") && NYHelper.isStringNotEmpty(banner.getTarget())){

                        // TODO: intent ke website
                        String url = banner.getTarget();

                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
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

    public void setDrawable(DoShopBanner banner) {

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

        if (banner.getImage() == null || TextUtils.isEmpty(banner.getImage())) {
            imageView.setImageResource(R.drawable.bg_placeholder);
        } else {

            if (application.getCache(banner.getImage()) != null){
                imageView.setImageBitmap(application.getCache(banner.getImage()));
            } else {

                ImageLoader.getInstance().loadImage(banner.getImage(), NYHelper.getOption(), new ImageLoadingListener() {
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

                ImageLoader.getInstance().displayImage(banner.getImage(), imageView, NYHelper.getOption());
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
