package com.nyelam.android.ecotrip;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;

public class EcoTripOnboardingFragment extends Fragment {

    private static final String KEY_BACKGROUND_RESOURCE = "background_resource";
    private static final String KEY_ICON_RESOURCE = "icon_resource";
    private static final String KEY_POSITION = "position";
    private int layouts[] = {
            R.layout.fragment_eco_trip_onboarding_1,
            R.layout.fragment_eco_trip_onboarding_2,
            R.layout.fragment_eco_trip_onboarding_3,
            R.layout.fragment_eco_trip_onboarding_4
    };
    private ImageView backgroundImageView;
    private ImageView iconImageView;

    public EcoTripOnboardingFragment() {
        // Required empty public constructor
    }

    public static EcoTripOnboardingFragment newInstance(int backgroundResource, int iconResource, int position) {
        EcoTripOnboardingFragment fragment = new EcoTripOnboardingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BACKGROUND_RESOURCE, backgroundResource);
        bundle.putInt(KEY_ICON_RESOURCE, iconResource);
        bundle.putInt(KEY_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int position = 0;
        if(getArguments() != null) {
            position = getArguments().getInt(KEY_POSITION);
        }
        return inflater.inflate(layouts[position], container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backgroundImageView = (ImageView)view.findViewById(R.id.background_imageView);
        iconImageView = (ImageView)view.findViewById(R.id.icon_imageView);

        if (getArguments() != null) {
            int backgroundResource = getArguments().getInt(KEY_BACKGROUND_RESOURCE);
            String imageUri = "drawable://" + backgroundResource;
            final NYApplication application = (NYApplication) getActivity().getApplication();
            if(application.getCache(imageUri) != null) {
                Bitmap bitmap = application.getCache(imageUri);
                backgroundImageView.setImageBitmap(bitmap);
            } else {
                ImageLoader.getInstance().displayImage("drawable://" + backgroundResource, backgroundImageView, NYHelper.getCompressedOption(getActivity()), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        application.addCache(imageUri, loadedImage);
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
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
