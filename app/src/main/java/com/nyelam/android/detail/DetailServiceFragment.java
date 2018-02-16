package com.nyelam.android.detail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Facilities;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.StrikethroughTextView;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;

public class DetailServiceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private LinearLayout mainLinearLayout;
    private ProgressBar progressBar;
    private TextView priceTextView, dateTextView, maxPersonTextView, minPersonTextView, descriptionTextView, licenseTextView;
    private ImageView icDiveGuideImageView, icEquipmentImageView, icFoodImageView, icTransportationImageView, icTowelImageView;
    private RelativeLayout diveGuideRelativeLayout, equipmentRelativeLayout, foodRelativeLayout, transportationRelativeLayout, towelRelativeLayout;
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
        priceStrikeThroughTextView = (StrikethroughTextView) v.findViewById(R.id.price_strikethrough_textView);
        priceTextView = (TextView) v.findViewById(R.id.price_textView);
        dateTextView = (TextView) v.findViewById(R.id.date_textView);
        maxPersonTextView = (TextView) v.findViewById(R.id.max_person_textView);
        minPersonTextView = (TextView) v.findViewById(R.id.min_person_textView);
        descriptionTextView = (TextView) v.findViewById(R.id.description_textView);
        licenseTextView = (TextView) v.findViewById(R.id.license_textView);

        icDiveGuideImageView = (ImageView) v.findViewById(R.id.icon_dive_guide_imageView);
        icEquipmentImageView = (ImageView) v.findViewById(R.id.icon_equipment_imageView);
        icFoodImageView = (ImageView) v.findViewById(R.id.icon_food_imageView);
        icTransportationImageView = (ImageView) v.findViewById(R.id.icon_transportation_imageView);
        icTowelImageView = (ImageView) v.findViewById(R.id.icon_towel_imageView);

        diveGuideRelativeLayout = (RelativeLayout) v.findViewById(R.id.dive_guide_relativeLayout);
        equipmentRelativeLayout = (RelativeLayout) v.findViewById(R.id.equipment_relativeLayout);
        foodRelativeLayout = (RelativeLayout) v.findViewById(R.id.food_relativeLayout);
        transportationRelativeLayout = (RelativeLayout) v.findViewById(R.id.transportation_relativeLayout);
        towelRelativeLayout = (RelativeLayout) v.findViewById(R.id.towel_relativeLayout);

    }

    public void setContent(DiveService service){

        DetailServiceActivity activity = ((DetailServiceActivity)getActivity());

        if (activity.certificate.equals("1")){
            licenseTextView.setText("Need a License");
        } else  {
            licenseTextView.setText("No Need License");
        }

       if (service != null){

            if (service.getSchedule() != null)dateTextView.setText(NYHelper.setMillisToDate(service.getSchedule().getStartDate())+" - "+NYHelper.setMillisToDate(service.getSchedule().getEndDate()));
            if (service.getSpecialPrice() < service.getNormalPrice() && service.getSpecialPrice() > 0){
                priceTextView.setText(NYHelper.priceFormatter(service.getSpecialPrice()));
                priceStrikeThroughTextView.setText(NYHelper.priceFormatter(service.getNormalPrice()));
                priceStrikeThroughTextView.setVisibility(View.VISIBLE);
            } else {
                priceTextView.setText(NYHelper.priceFormatter(service.getNormalPrice()));
                priceStrikeThroughTextView.setVisibility(View.GONE);
            }
            minPersonTextView.setText(String.valueOf(service.getMinPerson()));
            minPersonTextView.setText(String.valueOf(service.getMinPerson()));

           Facilities fac = service.getFacilities();
           if (fac != null){
               if (fac.getLicense() != null && fac.getLicense()){
                   licenseTextView.setText("License needed");
               }

               if (fac.getDiveGuide() != null && fac.getDiveGuide()){
                   NYHelper.setFacilities(fac.getDiveGuide(), icDiveGuideImageView);
               } else {
                   diveGuideRelativeLayout.setVisibility(View.GONE);
               }

               if (fac.getDiveEquipment() != null && fac.getDiveEquipment()){
                   NYHelper.setFacilities(fac.getDiveEquipment(), icEquipmentImageView);
               } else {
                   equipmentRelativeLayout.setVisibility(View.GONE);
               }

               if (fac.getFood() != null && fac.getFood()){
                   NYHelper.setFacilities(fac.getFood(), icFoodImageView);
               } else {
                   foodRelativeLayout.setVisibility(View.GONE);
               }

               if (fac.getTransportation() != null && fac.getTransportation()){
                   NYHelper.setFacilities(fac.getTransportation(), icTransportationImageView);
               } else {
                   diveGuideRelativeLayout.setVisibility(View.GONE);
               }

               if (fac.getDiveGuide() != null && fac.getDiveGuide()){
                   NYHelper.setFacilities(fac.getDiveGuide(), icTowelImageView);
               } else {
                   towelRelativeLayout.setVisibility(View.GONE);
               }

           }

           progressBar.setVisibility(View.GONE);
           mainLinearLayout.setVisibility(View.VISIBLE);

        } else {
           progressBar.setVisibility(View.GONE);
           mainLinearLayout.setVisibility(View.GONE);
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
