package com.nyelam.android.diveservice;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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

import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Facilities;
import com.nyelam.android.data.Location;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.font.StrikethroughTextView;

public class DetailServiceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayout mainLinearLayout;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private TextView titleTextView, diveSpotsTextView, priceTextView, descriptionTextView, licenseTextView;
    private TextView ratingTextView, visitedTextView, categoryTextView;
    private TextView addressTextView, phoneNumberTextView;
    private TextView totalDivesTextView, tripDurationsTextView, totalDiveSpotsTextView;
    private ImageView icDiveGuideImageView, icEquipmentImageView, icFoodImageView, icTransportationImageView, icTowelImageView;
    private LinearLayout diveGuideLinearLayout, equipmentLinearLayout, foodLinearLayout, transportationLinearLayout, towelLinearLayout, licenseLinearLayout;
    private StrikethroughTextView priceStrikeThroughTextView;

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
        initView(view);
    }

    private void initView(View v) {
        mainLinearLayout = (LinearLayout) v.findViewById(R.id.main_linearLayout);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);

        titleTextView = (TextView) v.findViewById(R.id.title_textView);
        diveSpotsTextView = (TextView) v.findViewById(R.id.dive_spots_textView);
        ratingTextView = (TextView) v.findViewById(R.id.rating_textView);
        visitedTextView = (TextView) v.findViewById(R.id.visited_textView);

        totalDivesTextView = (TextView) v.findViewById(R.id.total_dive_textView);
        tripDurationsTextView = (TextView) v.findViewById(R.id.trip_durations_textView);
        totalDiveSpotsTextView = (TextView) v.findViewById(R.id.total_dive_spot_textView);

        addressTextView = (TextView) v.findViewById(R.id.address_textView);
        phoneNumberTextView = (TextView) v.findViewById(R.id.phone_number_textView);

        priceStrikeThroughTextView = (StrikethroughTextView) v.findViewById(R.id.price_strikethrough_textView);
        priceTextView = (TextView) v.findViewById(R.id.price_textView);
        descriptionTextView = (TextView) v.findViewById(R.id.description_textView);
        licenseTextView = (TextView) v.findViewById(R.id.license_textView);
        categoryTextView = (TextView) v.findViewById(R.id.category_textView);

        icDiveGuideImageView = (ImageView) v.findViewById(R.id.icon_dive_guide_imageView);
        icEquipmentImageView = (ImageView) v.findViewById(R.id.icon_equipment_imageView);
        icFoodImageView = (ImageView) v.findViewById(R.id.icon_food_imageView);
        icTransportationImageView = (ImageView) v.findViewById(R.id.icon_transportation_imageView);
        icTowelImageView = (ImageView) v.findViewById(R.id.icon_towel_imageView);

        diveGuideLinearLayout = (LinearLayout) v.findViewById(R.id.dive_guide_linearLayout);
        equipmentLinearLayout = (LinearLayout) v.findViewById(R.id.equipment_linearLayout);
        foodLinearLayout = (LinearLayout) v.findViewById(R.id.food_linearLayout);
        transportationLinearLayout = (LinearLayout) v.findViewById(R.id.transportation_linearLayout);
        towelLinearLayout = (LinearLayout) v.findViewById(R.id.towel_linearLayout);
        licenseLinearLayout = (LinearLayout) v.findViewById(R.id.license_linearLayout);

    }

    public void setContent(){

        if (((DetailServiceActivity)getActivity()).newDiveService != null){

            DiveService service = ((DetailServiceActivity)getActivity()).newDiveService;
            if(!TextUtils.isEmpty(service.getDescription())) {
                descriptionTextView.setText(Html.fromHtml(service.getDescription()));
            } else {
                descriptionTextView.setText("-");
            }
            DetailServiceActivity activity = ((DetailServiceActivity)getActivity());

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

            if (service.getDiveCenter() != null && service.getDiveCenter().getContact() != null){
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
                        NYHelper.setFacilities(fac.getDiveGuide(), icDiveGuideImageView);
                    } else {
                        //diveGuideLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getDiveEquipment() != null && fac.getDiveEquipment()){
                        NYHelper.setFacilities(fac.getDiveEquipment(), icEquipmentImageView);
                    } else {
                        //equipmentLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getFood() != null && fac.getFood()){
                        NYHelper.setFacilities(fac.getFood(), icFoodImageView);
                    } else {
                        //foodLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getTransportation() != null && fac.getTransportation()){
                        NYHelper.setFacilities(fac.getTransportation(), icTransportationImageView);
                    } else {
                        //diveGuideLinearLayout.setVisibility(View.GONE);
                    }

                    if (fac.getDiveGuide() != null && fac.getDiveGuide()){
                        NYHelper.setFacilities(fac.getDiveGuide(), icTowelImageView);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        //void onFragmentInteraction(Uri uri);
    }
}
