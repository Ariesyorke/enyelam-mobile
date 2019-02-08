package com.nyelam.android.divespot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.helper.NYHelper;

public class DiveSpotDetailFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private DiveSpot diveSpot;
    private ProgressBar progressBar;
    private LinearLayout containerLayout, mainLinearLayout;
    private TextView subTitleTextView, emailTextView, phoneNumberTextView, cityTextView, provinceTextView, countryTextView;

    public DiveSpotDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiveSpotDetailFragment newInstance() {
        DiveSpotDetailFragment fragment = new DiveSpotDetailFragment();
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
        return inflater.inflate(R.layout.fragment_dive_spot_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    protected void setContent(DiveSpot diveSpot) {
        //this.content = content;

        if (diveSpot != null) {
            containerLayout.removeAllViews();

            if (NYHelper.isStringNotEmpty(diveSpot.getCurrentStatus())) {
                setDetails(containerLayout, "Current Status", diveSpot.getCurrentStatus());
            }

            if (diveSpot.getLocation() != null && diveSpot.getLocation().getCity() != null  && NYHelper.isStringNotEmpty(diveSpot.getLocation().getCity().getName())) {
                setDetails(containerLayout, "Location", diveSpot.getLocation().getCity().getName()+","+diveSpot.getLocation().getProvince().getName());
            }

            if (NYHelper.isStringNotEmpty(diveSpot.getGreatFor()))setDetails(containerLayout, "Great For", diveSpot.getGreatFor());
            setDetails(containerLayout, "Depth", String.valueOf(diveSpot.getDepthMin())+" - "+String.valueOf(diveSpot.getDepthMax())+" meter");
            setDetails(containerLayout, "Visibility", String.valueOf(diveSpot.getVisiblityMin())+" - "+String.valueOf(diveSpot.getVisibilityMax())+" meter");
            if (diveSpot.getExperienceMin() != null && diveSpot.getExperienceMax() != null &&   NYHelper.isStringNotEmpty(diveSpot.getExperienceMin().getName()) && NYHelper.isStringNotEmpty(diveSpot.getExperienceMax().getName()) )setDetails(containerLayout, "Experience", String.valueOf(diveSpot.getExperienceMin().getName())+" - "+String.valueOf(diveSpot.getExperienceMax().getName()));
            setDetails(containerLayout, "Recomended Stay", String.valueOf(diveSpot.getRecommendedStayMin())+" - "+String.valueOf(diveSpot.getRecommendedStayMax())+" days");
            setDetails(containerLayout, "Temperature", String.valueOf(diveSpot.getTemperatureMin())+" - "+String.valueOf(diveSpot.getTemperatureMax())+" Celcius");

            if (diveSpot.getDescription() != null && diveSpot.getLocation().getCity() != null  && NYHelper.isStringNotEmpty(diveSpot.getDescription())) {
                setDetails(containerLayout, "Description", diveSpot.getDescription());
            }

        }

        progressBar.setVisibility(View.GONE);
        mainLinearLayout.setVisibility(View.VISIBLE);
    }

    private void setDetails(LinearLayout containerLayout, final String label, final String text) {
        LayoutInflater linflaterAddons = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myItemView = linflaterAddons.inflate(R.layout.view_item_detail, null);

        LinearLayout.LayoutParams layoutParamsAddons = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParamsAddons.setMargins(0, 0, 0, 0);
        //layoutParamsAddons.setMargins(NYHelper.integerToDP(getActivity(), 10), NYHelper.integerToDP(getActivity(), 10), NYHelper.integerToDP(getActivity(), 10), 0);
        myItemView.setLayoutParams(layoutParamsAddons);

        TextView labelTextView = (TextView) myItemView.findViewById(R.id.label_textView);
        TextView textTextView = (TextView) myItemView.findViewById(R.id.text_textView);

        if (NYHelper.isStringNotEmpty(label)) labelTextView.setText(label);
        if (NYHelper.isStringNotEmpty(text)) textTextView.setText(text);

        myItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (label.equals("Email") && NYHelper.isStringNotEmpty(text)) {

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{text});
                    i.putExtra(Intent.EXTRA_SUBJECT, "");
                    i.putExtra(Intent.EXTRA_TEXT, "");
                    try {
                        startActivity(Intent.createChooser(i, "Send mail..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getActivity(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                }
//                else if (label.equals("Phone Number") && NYHelper.isStringNotEmpty(text)) {
//
//                    Intent intent = new Intent(Intent.ACTION_CALL);
//                    intent.setData(Uri.parse("tel:" + text));
//                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return;
//                    }
//                    getActivity().startActivity(intent);
//
//                }

            }
        });

        containerLayout.addView(myItemView);
    }





    private void initView(View view) {
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mainLinearLayout = (LinearLayout) view.findViewById(R.id.main_linearLayout);
        containerLayout = (LinearLayout) view.findViewById(R.id.container_layout);
        subTitleTextView = (TextView) view.findViewById(R.id.subtitle_textView);
        emailTextView = (TextView) view.findViewById(R.id.email_textView);
        phoneNumberTextView = (TextView) view.findViewById(R.id.contact_phone_number_textView);
        cityTextView = (TextView) view.findViewById(R.id.city_textView);
        provinceTextView = (TextView) view.findViewById(R.id.province_textView);
        countryTextView = (TextView) view.findViewById(R.id.country_textView);
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
