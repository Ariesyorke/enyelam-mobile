package com.nyelam.android.ecotrip;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;

public class EcoTripOnboardingFragment extends Fragment {
    private static final String KEY_BACKGROUND_RESOURCE = "background_resource";
    private static final String KEY_ICON_RESOURCE = "icon_resource";

    private ImageView backgroundImageView;
    private ImageView iconImageView;

    public EcoTripOnboardingFragment() {
        // Required empty public constructor
    }

    public static EcoTripOnboardingFragment newInstance(int backgroundResource, int iconResource) {
        EcoTripOnboardingFragment fragment = new EcoTripOnboardingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BACKGROUND_RESOURCE, backgroundResource);
        bundle.putInt(KEY_ICON_RESOURCE, iconResource);
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
        return inflater.inflate(R.layout.fragment_eco_trip_onboarding, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backgroundImageView = (ImageView)view.findViewById(R.id.background_imageView);
        iconImageView = (ImageView)view.findViewById(R.id.icon_imageView);

        if (getArguments() != null) {
            int backgroundResource = getArguments().getInt(KEY_BACKGROUND_RESOURCE);
            int iconResource = getArguments().getInt(KEY_ICON_RESOURCE);
            ImageLoader.getInstance().displayImage("drawable://"+backgroundResource, backgroundImageView, NYHelper.getCompressedOption(getActivity()));
            iconImageView.setBackgroundResource(iconResource);
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
