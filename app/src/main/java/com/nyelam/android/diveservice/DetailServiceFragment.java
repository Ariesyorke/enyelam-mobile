package com.nyelam.android.diveservice;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.booking.BookingServiceActivity;
import com.nyelam.android.booking.BookingServiceParticipantActivity;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.EquipmentRent;
import com.nyelam.android.data.Facilities;
import com.nyelam.android.data.Location;
import com.nyelam.android.data.Participant;
import com.nyelam.android.data.Schedule;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.dodive.DoDiveDiveServiceSuggestionAdapter;
import com.nyelam.android.dodive.EquipmentRentActivity;
import com.nyelam.android.dodive.RecyclerViewTouchListener;
import com.nyelam.android.dodive.TotalDiverSpinnerAdapter;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.helper.NYSpacesItemDecoration;
import com.nyelam.android.http.NYDoDiveRelatedServiceRequest;
import com.nyelam.android.http.NYDoDiveSuggestionServiceRequest;
import com.nyelam.android.http.NYDoTripSearchServiceResultRequest;
import com.nyelam.android.view.font.NYStrikethroughTextView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class DetailServiceFragment extends Fragment {

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private DetailServiceActivity activity;
    private OnFragmentInteractionListener mListener;
    private LinearLayout mainLinearLayout;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private ImageView diveCenterImageView;
    private TextView titleTextView, scheduleTextView, diveCenterNameTextView, diveSpotsTextView, priceTextView, descriptionTextView, licenseTextView;
    private TextView ratingTextView, visitedTextView, categoryTextView;
    private TextView addressTextView, phoneNumberTextView, aboutTextView;
    private TextView totalDivesTextView, tripDurationsTextView, totalDiveSpotsTextView, openWaterTextView, slotDiversTextView;
    private ImageView icDiveGuideImageView, icEquipmentImageView, icFoodImageView, icTransportationImageView, icTowelImageView, icAccomodationImageView;
    private LinearLayout diveGuideLinearLayout, equipmentLinearLayout, foodLinearLayout, transportationLinearLayout, towelLinearLayout, licenseLinearLayout, diveCenterLinearLayout;
    private NYStrikethroughTextView priceStrikeThroughTextView;
    private TextView availabilityStockTextView;

    private DoDiveDiveServiceSuggestionAdapter relatedDiveServiceAdapter;
    private RecyclerView relatedPostRecyclerView;
    private LinearLayout relatedPostLinearLayout, totalDiveLinearLayout, tripDurationsLinearLayout, totalDiveSpotLinearLayout, openWaterLinearLayout, bannerLinearLayout, slotDiversLinearLayout, equipmenRentContainerLinearLayoutt;

    public DetailServiceFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailServiceFragment newInstance() {
        DetailServiceFragment fragment = new DetailServiceFragment();
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
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (DetailServiceActivity)getActivity();
        initView(view);
        initAdapter();
        initControl();
        initStateDoCourse();
        setEquipmentRent();
    }

    private void initStateDoCourse() {
        if (activity.isDoCourse){
            diveSpotsTextView.setVisibility(View.GONE);
            totalDiveSpotLinearLayout.setVisibility(View.GONE);
            totalDiveLinearLayout.setVisibility(View.GONE);
            openWaterLinearLayout.setVisibility(View.VISIBLE);
            bannerLinearLayout.setVisibility(View.GONE);
            aboutTextView.setText(getString(R.string.about_course));
        }
    }

    private void initControl() {
        diveCenterLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DiveService diveService = activity.getDiveService();

                if (diveService != null && diveService.getDiveCenter() != null){
                    Intent intent = new Intent(getActivity(), DiveCenterDetailActivity.class);
                    if (diveService != null)intent.putExtra(NYHelper.DIVE_CENTER, diveService.getDiveCenter().toString());
                    intent.putExtra(NYHelper.CERTIFICATE, activity.certificate);
                    intent.putExtra(NYHelper.DIVER, String.valueOf(activity.diver));
                    intent.putExtra(NYHelper.SCHEDULE, activity.schedule);
                    if (activity.diveService != null)intent.putExtra(NYHelper.SERVICE, activity.diveService.toString());
                    intent.putExtra(NYHelper.IS_DO_TRIP, activity.isDoTrip);
                    startActivity(intent);
                }

            }
        });
    }

    private void initView(View v) {
        mainLinearLayout = (LinearLayout) v.findViewById(R.id.main_linearLayout);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        diveCenterImageView = (ImageView) v.findViewById(R.id.dive_center_imageView);

        titleTextView = (TextView) v.findViewById(R.id.title_textView);
        scheduleTextView = (TextView) v.findViewById(R.id.schedule_textView);
        diveCenterNameTextView = (TextView) v.findViewById(R.id.dive_center_name_textView);
        diveSpotsTextView = (TextView) v.findViewById(R.id.dive_spots_textView);
        ratingTextView = (TextView) v.findViewById(R.id.rating_textView);
        visitedTextView = (TextView) v.findViewById(R.id.visited_textView);
        aboutTextView = (TextView) v.findViewById(R.id.about_textView);

        totalDivesTextView = (TextView) v.findViewById(R.id.total_dive_textView);
        tripDurationsTextView = (TextView) v.findViewById(R.id.trip_durations_textView);
        totalDiveSpotsTextView = (TextView) v.findViewById(R.id.total_dive_spot_textView);
        openWaterTextView = (TextView) v.findViewById(R.id.open_water_textView);
        slotDiversTextView = (TextView) v.findViewById(R.id.slot_divers_textView);

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

        diveCenterLinearLayout = (LinearLayout) v.findViewById(R.id.dive_center_linearLayout);
        diveGuideLinearLayout = (LinearLayout) v.findViewById(R.id.dive_guide_linearLayout);
        equipmentLinearLayout = (LinearLayout) v.findViewById(R.id.equipment_linearLayout);
        foodLinearLayout = (LinearLayout) v.findViewById(R.id.food_linearLayout);
        transportationLinearLayout = (LinearLayout) v.findViewById(R.id.transportation_linearLayout);
        towelLinearLayout = (LinearLayout) v.findViewById(R.id.towel_linearLayout);
        licenseLinearLayout = (LinearLayout) v.findViewById(R.id.license_linearLayout);

        relatedPostLinearLayout = (LinearLayout) v.findViewById(R.id.related_service_linearLayout);
        relatedPostRecyclerView = (RecyclerView) v.findViewById(R.id.related_service_recyclerView);

        totalDiveLinearLayout = (LinearLayout) v.findViewById(R.id.total_dive_linearLayout);
        tripDurationsLinearLayout = (LinearLayout) v.findViewById(R.id.trip_durations_linearLayout);
        totalDiveSpotLinearLayout = (LinearLayout) v.findViewById(R.id.total_dive_spot_linearLayout);
        openWaterLinearLayout = (LinearLayout) v.findViewById(R.id.open_water_linearLayout);
        slotDiversLinearLayout = (LinearLayout) v.findViewById(R.id.slot_divers_linearLayout);
        bannerLinearLayout = (LinearLayout) v.findViewById(R.id.banner_linearLayout);
        equipmenRentContainerLinearLayoutt = (LinearLayout) v.findViewById(R.id.equipemnt_rent_container_linearLayout);

    }


    public void setContent(){

        getRelatedServiceRequest();

        if (activity.isDoTrip){
            scheduleTextView.setVisibility(View.VISIBLE);
        } else{
            scheduleTextView.setVisibility(View.GONE);
        }




        if (activity.newDiveService != null){

            DiveService service = activity.getDiveService();
            if(!TextUtils.isEmpty(service.getDescription())) {
                descriptionTextView.setText(Html.fromHtml(service.getDescription()));
            } else {
                descriptionTextView.setText("-");
            }

            slotDiversTextView.setText(": "+String.valueOf(activity.diveService.getAvailabilityStock()));

            if (NYHelper.isStringNotEmpty(service.getName())){
                String days = " Day";
                if (service.getDays() > 1 ) days = " Days";
                titleTextView.setText(service.getName()+" / "+String.valueOf(service.getDays())+days);
            }

            if (service.getSchedule() != null){
                Schedule schedule = service.getSchedule();
                String fromDate = NYHelper.setMillisToDateMonth(schedule.getStartDate());
                String endDate = NYHelper.setMillisToDateMonth(schedule.getEndDate());
                if (activity.isDoTrip){
                    scheduleTextView.setText(fromDate+" - "+endDate);
                }
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


            if (service.isOpenWater()){
                openWaterTextView.setText(": "+getString(R.string.yes));
            } else {
                openWaterTextView.setText(": "+getString(R.string.no));
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

                //SET IMAGE
                final NYApplication application = (NYApplication) activity.getApplication();
                Bitmap b = application.getCache("drawable://"+R.drawable.logo_nyelam);
                if(b != null) {
                    diveCenterImageView.setImageBitmap(b);
                } else {
                    diveCenterImageView.setImageResource(R.drawable.bg_placeholder);
                }

                ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(activity));
                if (NYHelper.isStringNotEmpty(service.getDiveCenter().getImageLogo())) {

                    if (application.getCache(service.getDiveCenter().getImageLogo()) != null){
                        diveCenterImageView.setImageBitmap(application.getCache(service.getDiveCenter().getImageLogo()));
                    } else {

                        ImageLoader.getInstance().loadImage(service.getDiveCenter().getImageLogo(), NYHelper.getOption(), new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                diveCenterImageView.setImageResource(R.drawable.example_pic);
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                diveCenterImageView.setImageBitmap(loadedImage);
                                application.addCache(imageUri, loadedImage);
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                diveCenterImageView.setImageResource(R.drawable.example_pic);
                            }
                        });

                        ImageLoader.getInstance().displayImage(service.getDiveCenter().getImageLogo(), diveCenterImageView, NYHelper.getOption());
                    }

                } else {
                    diveCenterImageView.setImageResource(R.drawable.example_pic);
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




    public void setEquipmentRent(){

        final List<EquipmentRent> equipmentRents = new ArrayList<>();

        EquipmentRent equipmentRent1 = new EquipmentRent();
        equipmentRent1.setId("1");
        equipmentRent1.setName("BCD Gear x1");
        equipmentRent1.setNormalPrice(25000);
        equipmentRent1.setSpecialPrice(10000);
        equipmentRent1.setAvailabilityStock(3);
        equipmentRents.add(equipmentRent1);


        equipmenRentContainerLinearLayoutt.removeAllViews();
        int pos = 0;
        for (final EquipmentRent equipmentRent : equipmentRents) {

            final int position = pos;

            LayoutInflater linflaterAddons = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myParticipantsView = linflaterAddons.inflate(R.layout.view_item_equipment_rent, null); //here item is the the layout you want to inflate

            LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParamsAddons.setMargins(0, 0, 0, NYHelper.integerToDP(getActivity(), 10));
            myParticipantsView.setLayoutParams(layoutParamsAddons);

            //myViewAddons.setId(0);
            LinearLayout linearLayout = (LinearLayout) myParticipantsView.findViewById(R.id.linearLayout);
            TextView nameTextView = (TextView) myParticipantsView.findViewById(R.id.name_textView);

            if (equipmentRent != null && NYHelper.isStringNotEmpty(equipmentRent.getName())) {
                nameTextView.setText(equipmentRent.getName());
            }

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EquipmentRentActivity.class);
                    if (equipmentRents != null && NYHelper.isStringNotEmpty(equipmentRents.toString()))intent.putExtra(NYHelper.EQUIPMENT_RENT, equipmentRents.toString());
                    startActivityForResult(intent, RESULT_OK);
                }
            });

            pos++;
            equipmenRentContainerLinearLayoutt.addView(myParticipantsView);

        }
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        relatedPostRecyclerView.setLayoutManager(layoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.padding_half);
        int spacing2InPixels = getResources().getDimensionPixelSize(R.dimen.padding);
        relatedPostRecyclerView.addItemDecoration(new NYSpacesItemDecoration(spacing2InPixels,0,spacingInPixels,0));

        relatedDiveServiceAdapter = new DoDiveDiveServiceSuggestionAdapter(getActivity(), true);
        relatedPostRecyclerView.setAdapter(relatedDiveServiceAdapter);

        relatedPostRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(),relatedPostRecyclerView, new DoDiveDiveServiceSuggestionAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                DiveService diveService = relatedDiveServiceAdapter.getDiveService(position);
                if (diveService != null && diveService.getDiveCenter() != null){
                    Intent intent = new Intent(activity, DetailServiceActivity.class);
                    intent.putExtra(NYHelper.SERVICE, diveService.toString());
                    intent.putExtra(NYHelper.DIVER, activity.diver);
                    intent.putExtra(NYHelper.SCHEDULE, activity.schedule);
                    intent.putExtra(NYHelper.CERTIFICATE, activity.certificate);
                    intent.putExtra(NYHelper.DIVE_CENTER, diveService.getDiveCenter().toString());
                    intent.putExtra(NYHelper.IS_DO_TRIP, activity.isDoTrip);
                    activity.startActivity(intent);
                }

            }

            @Override
            public void onLongClick(View view, int position) {
                DiveService diveService = relatedDiveServiceAdapter.getDiveService(position);
            }
        }));
    }


    private void getRelatedServiceRequest() {
        DiveService diveService = activity.getDiveService();
        String apiPath = getString(R.string.api_path_dodive_search_service_by_divecenter);
        if (activity.isDoTrip){
            apiPath = getString(R.string.api_path_dotrip_service_list_by_divecenter);
            if (diveService != null && diveService.getDiveCenter() != null && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getId())){
                // TODO: realted service belum
                NYDoTripSearchServiceResultRequest req = new NYDoTripSearchServiceResultRequest(getActivity(), apiPath, String.valueOf(1), diveService.getDiveCenter().getId(), "3", activity.diver, activity.certificate, activity.schedule, String.valueOf(2), null, null, null, -1, -1);
                spcMgr.execute(req, onSearchServiceRequest());
                // TODO: load data dummy, to test and waitting for API request
                //loadJSONAsset();
            }
        } else {
            if (diveService != null && diveService.getDiveCenter() != null && NYHelper.isStringNotEmpty(diveService.getDiveCenter().getId())){
                // TODO: realted service belum
                NYDoDiveRelatedServiceRequest req = new NYDoDiveRelatedServiceRequest(getActivity(), apiPath, String.valueOf(1), diveService.getDiveCenter().getId(), "3", activity.diver, activity.certificate, activity.schedule, null, null, null, null, -1, -1, String.valueOf(0));
                spcMgr.execute(req, onSearchServiceRequest());
                // TODO: load data dummy, to test and waitting for API request
                //loadJSONAsset();
            }
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




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Bundle b = data.getExtras();

            if (data.hasExtra(NYHelper.EQUIPMENT_RENT)){
                JSONObject obj = null;
                /*try {
                    obj = new JSONObject(data.getStringExtra(NYHelper.SEARCH_RESULT));
                    searchService = new SearchService();
                    searchService.parse(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            }

        }

    }

}
