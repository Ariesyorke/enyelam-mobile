package com.nyelam.android.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.Module;
import com.nyelam.android.data.ModuleEvent;
import com.nyelam.android.data.ModuleList;
import com.nyelam.android.data.NYEventsModule;
import com.nyelam.android.data.NYHotOffersModule;
import com.nyelam.android.data.NYModule;
import com.nyelam.android.data.NYPopularDiveSpotModule;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.ecotrip.EcoTripActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYHomepageModuleRequest;
import com.nyelam.android.storage.ModulHomepageStorage;
import com.nyelam.android.view.NYBannerViewPager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import me.relex.circleindicator.CircleIndicator;

public class HomeFragment extends Fragment {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private OnFragmentInteractionListener mListener;
    private ImageView menuItemImageView;
    private NYBannerViewPager bannerViewPager;
    private BannerViewPagerAdapter bannerViewPagerAdapter;
    private CircleIndicator circleIndicator;
    private CardView doDiveCardView, doCourseCardView, doShopCardView, ecoTripCardView;

    private RecyclerView recyclerView;
    private HomePageAdapter adapter;


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
        adapter = new HomePageAdapter(getContext(), this);
        initView(view);
        initBanner();
        initControl();
        initAdapter();
        initCacheModule();
        getListData();
    }

    private void initAdapter() {
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.setAdapter(adapter);
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

                //Toast.makeText(getActivity(), "FAILED", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestSuccess(ModuleList results) {
                if (results != null && results.getList() != null && !results.getList().isEmpty()){
                    adapter.clear();
                    adapter.addModules(results.getList());
                    adapter.notifyDataSetChanged();

                    //Toast.makeText(getActivity(), "SUCCESS", Toast.LENGTH_SHORT).show();

                    ModulHomepageStorage modulStorage = new ModulHomepageStorage(getActivity());
                    modulStorage.setModuleList(results);
                    modulStorage.save();

                    //NYLog.e("CEK MODUL INI "+results.toString());
                }

            }
        };
    }


    private void initControl() {
        recyclerView.setNestedScrollingEnabled(false);
        menuItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).openMenuDrawer();
            }
        });

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
                //Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
                NYHelper.handlePopupMessage(getActivity(), getResources().getString(R.string.coming_soon), null);
            }
        });

        doShopCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYHelper.handlePopupMessage(getActivity(), getString(R.string.coming_soon), null);
            }
        });

        ecoTripCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EcoTripActivity.class);
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
        banners.add(new Banner("1", "drawable://" + String.valueOf(R.drawable.banner_1), "captio", "http://www.nyelam.com"));
        banners.add(new Banner("2", "drawable://" + String.valueOf(R.drawable.banner_2), "captio", "http://www.nyelam.com"));
        banners.add(new Banner("3", "drawable://" + String.valueOf(R.drawable.banner_3), "captio", "http://www.nyelam.com"));
        banners.add(new Banner("4", "drawable://" + String.valueOf(R.drawable.banner_4), "captio", "http://www.nyelam.com"));
        banners.add(new Banner("5", "drawable://" + String.valueOf(R.drawable.banner_5), "captio", "http://www.nyelam.com"));
        bannerList.setList(banners);
        //input data data
        bannerViewPagerAdapter.setBannerList(bannerList);
        bannerViewPagerAdapter.notifyDataSetChanged();
        bannerViewPager.setOffscreenPageLimit(bannerList.getList().size());
        circleIndicator.setViewPager(bannerViewPager);
    }

    private void initView(View view) {
        menuItemImageView = (ImageView) view.findViewById(R.id.menu_item_imageView);
        bannerViewPager = (NYBannerViewPager) view.findViewById(R.id.promotion_view_pager);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        doDiveCardView = (CardView) view.findViewById(R.id.do_dive_cardView);
        doCourseCardView = (CardView) view.findViewById(R.id.do_course_cardView);
        doShopCardView = (CardView) view.findViewById(R.id.do_shop_cardView);
        ecoTripCardView = (CardView)view.findViewById(R.id.eco_trip_cardView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
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

    private void initCacheModule() {
        ModulHomepageStorage modulStorage = new ModulHomepageStorage(getActivity());
        ModuleList moduleList = modulStorage.getModuleList();
        if (moduleList != null && moduleList.getList() != null && !moduleList.getList().isEmpty()) {
            adapter.addModules(moduleList.getList());
            //NYLog.e("CEK MODUL INI "+modulStorage.getModuleList().toString());
        }
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

    @Override
    public void onResume() {
        super.onResume();
    }

}
