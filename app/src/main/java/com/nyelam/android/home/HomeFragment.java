package com.nyelam.android.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.bookinghistory.BookingHistoryListAdapter;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Module;
import com.nyelam.android.data.ModuleList;
import com.nyelam.android.detail.DetailServiceActivity;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.divecenter.DiveCenterDetailFragment;
import com.nyelam.android.divecenter.DiveCenterListServiceFragment;
import com.nyelam.android.divecenter.DiveCenterMapFragment;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYDoDiveCenterDetailRequest;
import com.nyelam.android.http.NYHomepageModuleRequest;
import com.nyelam.android.view.NYBannerViewPager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.w3c.dom.Text;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private OnFragmentInteractionListener mListener;
    private NYBannerViewPager bannerViewPager;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private CircleIndicator circleIndicator;
    private CardView doDiveCardView, doCourseCardView, doShopCardView, doTogetherCardView;
    //private LinearLayout doDiveLinearLayout, doCourseLinearLayout, doShopLinearLayout;
    //private View viewBooking;
    private TextView eventsSeeAllTextView;
    private TextView hotOffersSeeAllTextView;
    private TextView popularDiveSpotsSeeAllTextView;
    private RecyclerView  eventsRecyclerView;
    private RecyclerView  hotOffersRecyclerView;
    private RecyclerView popularDiveSpotsRecyclerView;
    private EventsRecyclerViewAdapter eventsRecyclerViewAdapter;
    private List<Module> modules = null;

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
        initAdapter();
        getListData();
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        eventsRecyclerView.setLayoutManager(layoutManager);
        eventsRecyclerViewAdapter = new EventsRecyclerViewAdapter(getActivity());
        eventsRecyclerView.setAdapter(eventsRecyclerViewAdapter);
        //progressBar.setVisibility(View.VISIBLE);
    }

    private void getListData() {
        NYHomepageModuleRequest req = new NYHomepageModuleRequest(getActivity());
        spcMgr.execute(req, onGetDetailDiveCenterRequest());
    }

    private RequestListener<ModuleList> onGetDetailDiveCenterRequest() {
        return new RequestListener<ModuleList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }*/
                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(ModuleList results) {

                if (results != null && results.getList() != null && results.getList().size() > 1){

                    NYLog.e("CEK MODULE 6 "+results.getList().toString());

                    for (Module module : results.getList()){

                        if (module.getName().equals("Event") && module.getEvents() != null && module.getEvents().size() > 0 ){
                            eventsRecyclerViewAdapter.addResults(module.getEvents());
                            eventsRecyclerViewAdapter.notifyDataSetChanged();
                            NYLog.e("CEK MODULE 6 SUCCESS");
                        } /*else if (module.getName().equals("Hot Offer") && module.getEvents() != null && module.getEvents().size() > 0 ){
                            eventsRecyclerViewAdapter.addResults(module.getEvents());
                            eventsRecyclerViewAdapter.notifyDataSetChanged();
                        } else if (module.getName().equals("Popular Dive Spots") && module.getEvents() != null && module.getEvents().size() > 0 ){
                            eventsRecyclerViewAdapter.addResults(module.getEvents());
                            eventsRecyclerViewAdapter.notifyDataSetChanged();
                        }*/

                    }


                }




            }
        };
    }


    private void initControl() {
        doDiveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoDiveActivity.class);
                startActivity(intent);
            }
        });

        doCourseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Cooming soon", Toast.LENGTH_SHORT).show();
                NYHelper.handlePopupMessage(getActivity(), "Cooming soon", null);
            }
        });

        doShopCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYHelper.handlePopupMessage(getActivity(), "Cooming soon", null);
            }
        });

        doTogetherCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYHelper.handlePopupMessage(getActivity(), "Cooming soon", null);
            }
        });

        /*viewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailServiceActivity.class);
                startActivity(intent);
            }
        });*/
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
        doDiveCardView = (CardView) view.findViewById(R.id.do_dive_cardView);
        doCourseCardView = (CardView) view.findViewById(R.id.do_course_cardView);
        doShopCardView = (CardView) view.findViewById(R.id.do_shop_cardView);
        doTogetherCardView = (CardView) view.findViewById(R.id.do_shop_cardView);
        eventsSeeAllTextView = (TextView) view.findViewById(R.id.events_see_all_textView);
        hotOffersSeeAllTextView = (TextView) view.findViewById(R.id.hot_offers_see_all_textView);
        popularDiveSpotsSeeAllTextView = (TextView) view.findViewById(R.id.popular_dive_spots_see_all_textView);
        eventsRecyclerView = (RecyclerView) view.findViewById(R.id.events_recyler_view);
        hotOffersRecyclerView = (RecyclerView) view.findViewById(R.id.hot_offers_recyler_view);
        popularDiveSpotsRecyclerView = (RecyclerView) view.findViewById(R.id.popular_dive_spots_recyler_view);
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
        spcMgr.start(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()) spcMgr.shouldStop();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }


}
