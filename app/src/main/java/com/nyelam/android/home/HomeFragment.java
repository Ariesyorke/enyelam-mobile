package com.nyelam.android.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.Banner;
import com.nyelam.android.data.BannerList;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
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
import com.nyelam.android.docourse.DoCourseActivity;
import com.nyelam.android.dodive.DoDiveActivity;
import com.nyelam.android.ecotrip.EcoTripActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYGetBannerRequest;
import com.nyelam.android.http.NYHomepageModuleRequest;
import com.nyelam.android.storage.EmailLoginStorage;
import com.nyelam.android.storage.ModulHomepageStorage;
import com.nyelam.android.view.NYBannerViewPager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
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
    private RelativeLayout doDiveRelativeLayout, doCourseRelativeLayout, doShopRelativeLayout, ecoTripRelativeLayout;
    private RecyclerView recyclerView;
    private HomePageAdapter adapter;
    private DoTripRecyclerViewAdapter doTripAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        adapter = new HomePageAdapter(getActivity(), this);
        initView(view);
        initBanner();
        initControl();
        initAdapter();
        initCacheModule();
        //getHomepageModule();
    }

    private void initAdapter() {
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(verticalLayoutManager);
        recyclerView.addItemDecoration(new NYSpacesItemDecoration(0,0,0,0));
        recyclerView.setAdapter(adapter);
    }

    private void getHomepageModule() {
        NYHomepageModuleRequest req = new NYHomepageModuleRequest(getActivity());
        spcMgr.execute(req, onGetModulesHomepageRequest());
    }

    private RequestListener<ModuleList> onGetModulesHomepageRequest() {
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

                    ModulHomepageStorage modulStorage = new ModulHomepageStorage(getActivity());
                    modulStorage.setModuleList(results);
                    modulStorage.save();
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

        doDiveRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DoDiveActivity.class);
                startActivity(intent);
            }
        });

        doCourseRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Coming soon", Toast.LENGTH_SHORT).show();
                //NYHelper.handlePopupMessage(getActivity(), getResources().getString(R.string.coming_soon), null);
                Intent intent = new Intent(getActivity(), DoCourseActivity.class);
                startActivity(intent);
            }
        });

        doShopRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYHelper.handlePopupMessage(getActivity(), getString(R.string.coming_soon), null);
            }
        });

        ecoTripRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), DoDiveActivity.class);
                Intent intent = new Intent(getActivity(), EcoTripActivity.class);
                intent.putExtra(NYHelper.IS_ECO_TRIP, 1);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                initBanner();
                initControl();
                initAdapter();
                initCacheModule();
                loadBanners();
                loadDoTrip();
                if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) swipeRefreshLayout.setRefreshing(false);
            }
        });

    }


    private void initBanner() {
        bannerViewPagerAdapter = new BannerViewPagerAdapter(getChildFragmentManager());
        bannerViewPager.setAdapter(bannerViewPagerAdapter);
        circleIndicator.setViewPager(bannerViewPager);

        //craete TEMP data banner
//        BannerList bannerList = new BannerList();
//        List<Banner> banners = new ArrayList<>();
//        public Banner(String id, String imageUrl, String serviceId, String serviceName, boolean isLicense, long date, boolean isEcoTrip, boolean isDoTrip){
//        banners.add(new Banner("1", "drawable://" + String.valueOf(R.drawable.banner_1), "1", "Service Ku", true, 189282822, true, true));
//        banners.add(new Banner("2", "drawable://" + String.valueOf(R.drawable.banner_2), "captio", "http://www.nyelam.com"));
//        banners.add(new Banner("3", "drawable://" + String.valueOf(R.drawable.banner_1), "1", "Service Ku", true));
//        bannerList.setList(banners);
//        bannerViewPagerAdapter.setBannerList(filterBanners(bannerList));
//        bannerViewPagerAdapter.notifyDataSetChanged();
//        bannerViewPager.setOffscreenPageLimit(bannerList.getList().size());
//        circleIndicator.setViewPager(bannerViewPager);
    }

    private void initView(View view) {
        menuItemImageView = (ImageView) view.findViewById(R.id.menu_item_imageView);
        bannerViewPager = (NYBannerViewPager) view.findViewById(R.id.promotion_view_pager);
        circleIndicator = (CircleIndicator) view.findViewById(R.id.circle_indicator);
        doDiveRelativeLayout = (RelativeLayout) view.findViewById(R.id.do_dive_relativeLayout);
        doCourseRelativeLayout = (RelativeLayout) view.findViewById(R.id.do_course_relativeLayout);
        doShopRelativeLayout = (RelativeLayout) view.findViewById(R.id.do_shop_relativeLayout);
        ecoTripRelativeLayout = (RelativeLayout)view.findViewById(R.id.eco_trip_relativeLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
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
        loadDoTrip();
        loadBanners();
    }

    public void loadBanners() {
        NYGetBannerRequest req = null;
        try {
            req = new NYGetBannerRequest(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        spcMgr.execute(req, onGetBannersRequest());
    }

    private RequestListener<BannerList> onGetBannersRequest() {
        return new RequestListener<BannerList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                /*if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void onRequestSuccess(BannerList results) {
                if (results != null && results.getList() != null && !results.getList().isEmpty()){

//                    Toast.makeText(getActivity(), "sukses", Toast.LENGTH_SHORT).show();
//                    NYLog.e("isinya apa : "+results.getList().toString());
//                    NYLog.e("isinya apa baru : "+filterBanners(results).getList().toString());

                    BannerList bannerList = filterBanners(results);
                    bannerViewPagerAdapter.clear();
                    bannerViewPagerAdapter.setBannerList(bannerList);
                    bannerViewPagerAdapter.notifyDataSetChanged();
                    bannerViewPager.setOffscreenPageLimit(bannerList.getList().size());
                    circleIndicator.setViewPager(bannerViewPager);
                } else {
//                    bannerViewPagerAdapter.clear();
//                    bannerViewPagerAdapter.setBannerList(null);
//                    bannerViewPagerAdapter.notifyDataSetChanged();
//                    circleIndicator.setViewPager(bannerViewPager);
                }
            }
        };
    }


    public void loadDoTrip(){

        doTripAdapter = new DoTripRecyclerViewAdapter(getActivity());
        NYHomepageModuleRequest req = new NYHomepageModuleRequest(getActivity());
        spcMgr.execute(req, onGetModulesHomepageRequest());
//        try {
//
//
//            //JSONObject obj = new JSONObject(loadJSONFromAsset(this));
//            JSONObject obj = new JSONObject(loadJSONFromAsset(getActivity()));
//            JSONArray array = obj.getJSONArray("modules");
//
//            ModuleList moduleList = new ModuleList();
//            moduleList.parse(array);
//
//
//            //DiveServiceList results = new DiveServiceList();
//            //results.parse(array);
//
//            if (moduleList != null && moduleList.getList() != null && moduleList.getList().size() > 0){
//                //noResultTextView.setVisibility(View.GONE);
//                /*doTripAdapter.clear();
//                doTripAdapter.addResults(results.getList());
//                doTripAdapter.notifyDataSetChanged();*/
//
//
//                adapter.clear();
//                adapter.addModules(moduleList.getList());
//                adapter.notifyDataSetChanged();
//
//
//            } else {
//
//                /*doTripAdapter.clear();
//                doTripAdapter.notifyDataSetChanged();*/
//                //noResultTextView.setVisibility(View.VISIBLE);
//            }
//
//        } catch (JSONException e) {
//            NYLog.e("cek error : "+e.getMessage());
//            e.printStackTrace();
//        }
    }


    public String loadJSONFromAsset( Context context ) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("homepage_module.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private BannerList filterBanners(BannerList bannerList){

        BannerList tempBannerList = new BannerList();

        // TODO: cek tipe banner, jika dikurang dari sama dengan 4 masukkan ke array
        List<Banner> temp = new ArrayList<>();
        if (bannerList != null && bannerList.getList() != null){
            for (Banner banner : bannerList.getList()){
                if (banner != null && banner.getType() <= 4) temp.add(banner);
            }
        }

        tempBannerList.setList(temp);

        return tempBannerList;
    }


}
