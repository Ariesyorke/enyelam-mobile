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

import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.helper.NYHelper;

public class DetailServiceDiveCenterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView nameTextView, ratingTextView;
    private ImageView circleImageView;

    public DetailServiceDiveCenterFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailServiceDiveCenterFragment newInstance() {
        DetailServiceDiveCenterFragment fragment = new DetailServiceDiveCenterFragment();
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
        return inflater.inflate(R.layout.fragment_detail_service_dive_center, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        //setDiveCenter();
    }

    private void initView(View v) {
        nameTextView = (TextView) v.findViewById(R.id.name_textView);
        ratingTextView = (TextView) v.findViewById(R.id.rating_textView);
        //circleImageView = (ImageView) v.findViewById(R.id.picture_imageView);
    }

    public void setDiveCenter(DiveService service){
        if (service != null && service.getDiveCenter() != null){
            if (NYHelper.isStringNotEmpty(service.getDiveCenter().getName()))nameTextView.setText(service.getDiveCenter().getName());
        }
    }


    public void setView(DiveCenter diveCenter){
        if (diveCenter != null){
            if (NYHelper.isStringNotEmpty(diveCenter.getName())) nameTextView.setText(diveCenter.getName());
            //if (NYHelper.isStringNotEmpty(diveCenter.Name())) nameTextView.setText(diveCenter.getName());
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
