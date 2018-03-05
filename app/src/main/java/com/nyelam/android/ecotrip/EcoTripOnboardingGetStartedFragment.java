package com.nyelam.android.ecotrip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nyelam.android.R;
import com.nyelam.android.data.City;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.Coordinate;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Province;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.helper.NYHelper;

public class EcoTripOnboardingGetStartedFragment extends Fragment {
    private View bookNowButton;
    private int backgroundResource = R.drawable.eco_trip_5_bg;
    private ImageView backgroundImageView;
    public EcoTripOnboardingGetStartedFragment() {
        // Required empty public constructor
    }


    public static EcoTripOnboardingGetStartedFragment newInstance() {
        EcoTripOnboardingGetStartedFragment fragment = new EcoTripOnboardingGetStartedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_eco_trip_onboarding_get_started, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        backgroundImageView = (ImageView)view.findViewById(R.id.background_imageView);
        bookNowButton = view.findViewById(R.id.book_eco_trip_button);
        bookNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO HARCODE ECOTRIP!!
                Intent intent = new Intent(getActivity(), DoDiveActivity.class);
                intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                startActivity(intent);
            }
        });

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
