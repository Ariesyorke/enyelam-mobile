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
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.Facilities;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.StrikethroughTextView;

public class DetailServiceFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView priceTextView, dateTextView, maxPersonTextView, minPersonTextView, descriptionTextView, licenseTextView;
    private ImageView icDiveGuideImageView, icEquipmentImageView, icFoodImageView, icTransportationImageView, icTowelImageView;
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
    }

    public void setContent(DiveService service){

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
               if (fac.isLicense())licenseTextView.setText("License needed");
               NYHelper.setFacilities(fac.isDiveGuide(), icDiveGuideImageView);
               NYHelper.setFacilities(fac.isDiveEquipment(), icEquipmentImageView);
               NYHelper.setFacilities(fac.isFood(), icFoodImageView);
               NYHelper.setFacilities(fac.isTransportation(), icTransportationImageView);
               NYHelper.setFacilities(fac.isTowel(), icTowelImageView);
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
