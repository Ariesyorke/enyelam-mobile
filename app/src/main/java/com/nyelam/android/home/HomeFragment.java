package com.nyelam.android.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nyelam.android.R;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.view.NYBannerViewPager;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private NYBannerViewPager bannerViewPager;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private CircleIndicator circleIndicator;
    private LinearLayout doDiveLinearLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initBanner();
        initControl();
    }

    private void initControl() {
        doDiveLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoDiveActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initBanner() {
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getChildFragmentManager());
        bannerViewPager.setAdapter(bannerViewPagerAdapter);
        circleIndicator.setViewPager(bannerViewPager);

        //craete TEMP data banner
        BannerList bannerList = new BannerList();
        List<Banner> banners = new ArrayList<>();
        banners.add(new Banner("1", "https://nyelam-com-apk-android-application-mobile.rembon.co.id/v100/new_assets/images/pictures/slider-a.jpg", "captio", "http://www.nyelam.com"));
        banners.add(new Banner("2", "https://nyelam-com-apk-android-application-mobile.rembon.co.id/v100/new_assets/images/pictures/slider-b.jpg", "captio", "http://www.nyelam.com"));
        banners.add(new Banner("3", "https://nyelam-com-apk-android-application-mobile.rembon.co.id/v100/new_assets/images/pictures/slider-x1.jpg", "captio", "http://www.nyelam.com"));
        banners.add(new Banner("4", "https://nyelam-com-apk-android-application-mobile.rembon.co.id/v100/new_assets/images/pictures/slider-x2.jpg", "captio", "http://www.nyelam.com"));
        banners.add(new Banner("5", "https://nyelam-com-apk-android-application-mobile.rembon.co.id/v100/new_assets/images/pictures/slider-x3.jpg", "captio", "http://www.nyelam.com"));
        bannerList.setList(banners);
        //input data data
        bannerViewPagerAdapter.setBannerList(bannerList);
        bannerViewPagerAdapter.notifyDataSetChanged();
        bannerViewPager.setOffscreenPageLimit(bannerList.getList().size());
        circleIndicator.setViewPager(bannerViewPager);

    }

    private void initView(View view) {
        bannerViewPager = (NYBannerViewPager) view.findViewById(R.id.promotion_view_pager);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        doDiveLinearLayout = (LinearLayout) view.findViewById(R.id.do_dive_linearLayout);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }


}
