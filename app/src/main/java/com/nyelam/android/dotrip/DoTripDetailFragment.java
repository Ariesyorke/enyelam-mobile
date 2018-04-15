package com.nyelam.android.dotrip;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Facilities;
import com.nyelam.android.data.Location;
import com.nyelam.android.dodive.DoDiveDiveServiceSuggestionAdapter;
import com.nyelam.android.dodive.RecyclerViewTouchListener;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveSearchServiceResultRequest;
import com.nyelam.android.view.font.NYStrikethroughTextView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

public class DoTripDetailFragment extends Fragment {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private OnFragmentInteractionListener mListener;
    private LinearLayout mainLinearLayout;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private ImageView diveCenterImageView;
    private TextView titleTextView, diveCenterNameTextView, diveSpotsTextView, priceTextView, descriptionTextView, licenseTextView;
    private TextView ratingTextView, visitedTextView, categoryTextView;
    private TextView addressTextView, phoneNumberTextView;
    private TextView totalDivesTextView, tripDurationsTextView, totalDiveSpotsTextView;
    private ImageView icDiveGuideImageView, icEquipmentImageView, icFoodImageView, icTransportationImageView, icTowelImageView, icAccomodationImageView;
    private LinearLayout diveGuideLinearLayout, equipmentLinearLayout, foodLinearLayout, transportationLinearLayout, towelLinearLayout, licenseLinearLayout;
    private NYStrikethroughTextView priceStrikeThroughTextView;
    private TextView availabilityStockTextView;

    private DoDiveDiveServiceSuggestionAdapter relatedDiveServiceAdapter;
    private RecyclerView relatedPostRecyclerView;
    private LinearLayout relatedPostLinearLayout;

    public DoTripDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DoTripDetailFragment newInstance() {
        DoTripDetailFragment fragment = new DoTripDetailFragment();
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
        return inflater.inflate(R.layout.fragment_do_trip_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAdapter();
        getRelatedServiceRequest();
    }

    private void initView(View v) {
        mainLinearLayout = (LinearLayout) v.findViewById(R.id.main_linearLayout);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        diveCenterImageView = (ImageView) v.findViewById(R.id.dive_center_imageView);

        titleTextView = (TextView) v.findViewById(R.id.title_textView);
        diveCenterNameTextView = (TextView) v.findViewById(R.id.dive_center_name_textView);
        diveSpotsTextView = (TextView) v.findViewById(R.id.dive_spots_textView);
        ratingTextView = (TextView) v.findViewById(R.id.rating_textView);
        visitedTextView = (TextView) v.findViewById(R.id.visited_textView);

        totalDivesTextView = (TextView) v.findViewById(R.id.total_dive_textView);
        tripDurationsTextView = (TextView) v.findViewById(R.id.trip_durations_textView);
        totalDiveSpotsTextView = (TextView) v.findViewById(R.id.total_dive_spot_textView);

        addressTextView = (TextView) v.findViewById(R.id.address_textView);
        phoneNumberTextView = (TextView) v.findViewById(R.id.phone_number_textView);

        priceStrikeThroughTextView = (NYStrikethroughTextView) v.findViewById(R.id.price_strikethrough_textView);
        priceTextView = (TextView) v.findViewById(R.id.price_textView);
        descriptionTextView = (TextView) v.findViewById(R.id.description_textView);
        licenseTextView = (TextView) v.findViewById(R.id.license_textView);
        categoryTextView = (TextView) v.findViewById(R.id.category_textView);
        availabilityStockTextView = (TextView) v.findViewById(R.id.availability_stock_textView);

        icDiveGuideImageView = (ImageView) v.findViewById(R.id.icon_dive_guide_imageView);
        icEquipmentImageView = (ImageView) v.findViewById(R.id.icon_equipment_imageView);
        icFoodImageView = (ImageView) v.findViewById(R.id.icon_food_imageView);
        icTransportationImageView = (ImageView) v.findViewById(R.id.icon_transportation_imageView);
        icTowelImageView = (ImageView) v.findViewById(R.id.icon_towel_imageView);
        icAccomodationImageView = (ImageView) v.findViewById(R.id.icon_accomodation_imageView);

        diveGuideLinearLayout = (LinearLayout) v.findViewById(R.id.dive_guide_linearLayout);
        equipmentLinearLayout = (LinearLayout) v.findViewById(R.id.equipment_linearLayout);
        foodLinearLayout = (LinearLayout) v.findViewById(R.id.food_linearLayout);
        transportationLinearLayout = (LinearLayout) v.findViewById(R.id.transportation_linearLayout);
        towelLinearLayout = (LinearLayout) v.findViewById(R.id.towel_linearLayout);
        licenseLinearLayout = (LinearLayout) v.findViewById(R.id.license_linearLayout);

        relatedPostLinearLayout = (LinearLayout) v.findViewById(R.id.related_service_linearLayout);
        relatedPostRecyclerView = (RecyclerView) v.findViewById(R.id.related_service_recyclerView);
    }

    public void setContent(){

        if (((DoTripDetailActivity)getActivity()).newDiveService != null){

            DiveService service = ((DoTripDetailActivity)getActivity()).newDiveService;
            if(!TextUtils.isEmpty(service.getDescription())) {
                descriptionTextView.setText(Html.fromHtml(service.getDescription()));
            } else {
                descriptionTextView.setText("-");
            }
            DoTripDetailActivity activity = ((DoTripDetailActivity)getActivity());

            if (NYHelper.isStringNotEmpty(service.getName())){
                String days = " Day";
                if (service.getDays() > 1 ) days = " Days";
                titleTextView.setText(service.getName()+" / "+String.valueOf(service.getDays())+days);
            }

            if (service.getDiveSpots() != null && service.getDiveSpots().size() > 0 ){
                String diveSpotsText = "";
                int pos = 1;
                for (DiveSpot diveSpot : service.getDiveSpots()){
                    if (pos == 1 && NYHelper.isStringNotEmpty(diveSpot.getName())){
                        diveSpotsText = diveSpot.getName();
                        pos++;
                    } else if ( NYHelper.isStringNotEmpty(diveSpot.getName())){
                        diveSpotsText = diveSpotsText +", "+diveSpot.getName();
                        pos++;
                    }
                }
                diveSpotsTextView.setText(diveSpotsText);
            }


            ratingBar.setRating(service.getRating());
            ratingTextView.setText(String.valueOf(service.getRating()));
            //visitedTextView.setText(String.valueOf(service.getVisited()));

            totalDivesTextView.setText(": "+String.valueOf(service.getTotalDives()));
            //totalDiveSpotsTextView.setText(": "+String.valueOf(service.getTotalDiveSpots()));


            totalDiveSpotsTextView.setText(": 0");
            if (service.getDiveSpots() != null)totalDiveSpotsTextView.setText(": "+String.valueOf(service.getDiveSpots().size()));


            if (service.getDays() > 1){
                tripDurationsTextView.setText(": "+String.valueOf(service.getDays())+" Days");
            } else {
                tripDurationsTextView.setText(": "+String.valueOf(service.getDays())+" Day");
            }

            if (service.getDiveCenter() != null && service.getDiveCenter().getContact() !=  null){
                if (NYHelper.isStringNotEmpty(service.getDiveCenter().getContact().getPhoneNumber()))phoneNumberTextView.setText(service.getDiveCenter().getContact().getPhoneNumber());
                if (service.getDiveCenter().getContact().getLocation() != null){
                    Location location = service.getDiveCenter().getContact().getLocation();
                    String locationText = "";
                    if (location.getCity() != null && NYHelper.isStringNotEmpty(location.getCity().getName())) locationText=locationText+location.getCity().getName();
                    if (location.getProvince() != null && NYHelper.isStringNotEmpty(location.getProvince().getName())) locationText=locationText+", "+location.getCity().getName();
                    if (location.getCountry() != null && NYHelper.isStringNotEmpty(location.getCountry())) locationText=locationText+", "+location.getCountry();
                    addressTextView.setText(locationText);
                }
            }

            if (service.getDiveCenter() != null){
                if (NYHelper.isStringNotEmpty(service.getDiveCenter().getName()))diveCenterNameTextView.setText(service.getDiveCenter().getName());
                if (NYHelper.isStringNotEmpty(service.getDiveCenter().getImageLogo())){
                    //SET IMAGE
                    ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
                    if (NYHelper.isStringNotEmpty(service.getDiveCenter().getImageLogo())) {
                        ImageLoader.getInstance().loadImage(service.getDiveCenter().getImageLogo(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                //featuredImageView.setImageResource(R.drawable.bg_placeholder);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                diveCenterImageView.setImageBitmap(loadedImage);
                                //activity.getCache().put(imageUri, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                //featuredImageView.setImageResource(R.drawable.bg_placeholder);
                            }
                        });

                        ImageLoader.getInstance().displayImage(service.getDiveCenter().getImageLogo(), diveCenterImageView, NYHelper.getOption());

                    } else {
                        diveCenterImageView.setImageResource(R.drawable.bg_placeholder);
                    }

                }
            }


            if (activity.certificate.equals("1")){
                licenseTextView.setText("License Needed");
            } else  {
                licenseTextView.setText("No Need License");
                //licenseLinearLayout.setBackgroundResource(R.drawable.ny_book);
                //licenseLinearLayout.setBackgroundColor(getResources().getColor(R.color.ny_grey1));
                licenseLinearLayout.setBackgroundColor(getResources().getColor(R.color.ny_grey7));
                licenseTextView.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
            }


            if (service != null){

                if (service.getCategories() != null && !service.getCategories().isEmpty() ){
                    categoryTextView.setText(service.getCategories().get(0).getName());
                } else {
                    categoryTextView.setText("-");
                }

                if (service.getAvailabilityStock() > 0){
                    availabilityStockTextView.setText(String.valueOf(service.getAvailabilityStock()));
                }

                if (service.getSpecialPrice() < service.getNormalPrice() && service.getSpecialPrice() > 0){
                    priceTextView.setText(NYHelper.priceFormatter(service.getSpecialPrice()));
                    priceStrikeThroughTextView.setText(NYHelper.priceFormatter(service.getNormalPrice()));
                    priceStrikeThroughTextView.setVisibility(View.VISIBLE);
                } else {
                    priceTextView.setText(NYHelper.priceFormatter(service.getNormalPrice()));
                    priceStrikeThroughTextView.setVisibility(View.GONE);
                }

                Facilities fac = service.getFacilities();
                if (fac != null){
                    if (fac.getLicense() != null && fac.getLicense()){
                        //licenseTextView.setText("License needed");
                    }

                    if (fac.getDiveGuide() != null && fac.getDiveGuide()){
                        NYHelper.setFacilities(fac.getDiveGuide(), R.drawable.ic_dive_guide_active, R.drawable.ic_dive_guide_unactive, icDiveGuideImageView);
                    } else {
                        //diveGuideLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getDiveEquipment() != null && fac.getDiveEquipment()){
                        NYHelper.setFacilities(fac.getDiveEquipment(), R.drawable.ic_equipment_active, R.drawable.ic_equipment_unactive, icEquipmentImageView);
                    } else {
                        //equipmentLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getFood() != null && fac.getFood()){
                        NYHelper.setFacilities(fac.getFood(), R.drawable.ic_food_and_drink_active, R.drawable.ic_food_and_drink_unactive, icFoodImageView);
                    } else {
                        //foodLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getTransportation() != null && fac.getTransportation()){
                        NYHelper.setFacilities(fac.getTransportation(), R.drawable.ic_transportation_active, R.drawable.ic_transportation_unactive, icTransportationImageView);
                    } else {
                        //diveGuideLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getTowel() != null && fac.getTowel()){
                        NYHelper.setFacilities(fac.getTowel(), R.drawable.ic_towel_active, R.drawable.ic_towel_unactive, icTowelImageView);
                    } else {
                        //towelLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getAccomodation() != null && fac.getAccomodation()){
                        NYHelper.setFacilities(fac.getTowel(), R.drawable.ic_accomodation_active, R.drawable.ic_accomodation_unactive, icAccomodationImageView);
                    } else {
                        //towelLinearLayout.setVisibility(View.GONE);
                    }

                }

                progressBar.setVisibility(View.GONE);
                mainLinearLayout.setVisibility(View.VISIBLE);

            } else {
                progressBar.setVisibility(View.GONE);
                mainLinearLayout.setVisibility(View.GONE);
            }

        }

    }



    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        relatedPostRecyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding_half);
        relatedPostRecyclerView.addItemDecoration(new NYSpacesItemDecoration(spacingInPixels,0,spacingInPixels,0));

        relatedDiveServiceAdapter = new DoDiveDiveServiceSuggestionAdapter(getActivity());
        relatedPostRecyclerView.setAdapter(relatedDiveServiceAdapter);

        relatedPostRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(),relatedPostRecyclerView, new DoDiveDiveServiceSuggestionAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                DiveService diveService = relatedDiveServiceAdapter.getDiveService(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                DiveService diveService = relatedDiveServiceAdapter.getDiveService(position);
            }
        }));
    }


    private void getRelatedServiceRequest() {


        if (((DoTripDetailActivity)getActivity()).newDiveService != null){
            DoTripDetailActivity activity = ((DoTripDetailActivity)getActivity());
            DiveService diveService = ((DoTripDetailActivity)getActivity()).newDiveService;

            String apiPath = getString(R.string.api_path_dodive_service_list);
            apiPath = getString(R.string.api_path_dodive_search_service_by_divecenter);

            NYDoDiveSearchServiceResultRequest req = new NYDoDiveSearchServiceResultRequest(getActivity(), apiPath, String.valueOf(1), diveService.getDiveCenter().getId(), "3", activity.diver, activity.certificate, activity.schedule, null, null,  null, null, -1, -1, String.valueOf(0));
            //NYDoDiveSuggestionServiceRequest req = new NYDoDiveSuggestionServiceRequest(getActivity());
            spcMgr.execute(req, onSearchServiceRequest());

            // TODO: load data dummy, to test and waitting for API request
            //loadJSONAsset();
        }
    }

    private RequestListener<DiveServiceList> onSearchServiceRequest() {
        return new RequestListener<DiveServiceList>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                relatedDiveServiceAdapter.clear();
                relatedDiveServiceAdapter.notifyDataSetChanged();
                relatedPostLinearLayout.setVisibility(View.GONE);
                //NYHelper.handleAPIException(DoDiveSearchActivity.this, spiceException, null);
            }

            @Override
            public void onRequestSuccess(DiveServiceList results) {
                if (results != null){

                    DoTripDetailActivity activity = ((DoTripDetailActivity)getActivity());
                    List<DiveService> temp = new ArrayList<>();

                    int total = 0;
                    for (DiveService diveService : results.getList()){
                        if (total >= 3) break;
                        if (!diveService.getId().equals(activity.diveService.getId())){
                            temp.add(diveService);
                            total++;
                        }
                    }

                    relatedPostLinearLayout.setVisibility(View.VISIBLE);
                    relatedDiveServiceAdapter.clear();
                    relatedDiveServiceAdapter.addResults(temp);
                    relatedDiveServiceAdapter.notifyDataSetChanged();
                } else {
                    relatedDiveServiceAdapter.clear();
                    relatedDiveServiceAdapter.notifyDataSetChanged();
                    relatedPostLinearLayout.setVisibility(View.GONE);
                }

            }
        };
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
