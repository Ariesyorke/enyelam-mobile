package com.nyelam.android.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.divecenter.DiveCenterDetailActivity;
import com.nyelam.android.helper.NYHelper;

public class DetailServiceDiveCenterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private CardView mainCardView;
    private ProgressBar progressBar;
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
        initControl();
        //setDiveCenter();
    }

    private void initControl() {
        mainCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DiveCenterDetailActivity.class);
                intent.putExtra(NYHelper.DIVER, String.valueOf(((DetailServiceActivity)getActivity()).diver));
                intent.putExtra(NYHelper.SCHEDULE, ((DetailServiceActivity)getActivity()).schedule);
                intent.putExtra(NYHelper.SERVICE, ((DetailServiceActivity)getActivity()).diveService.toString());
                startActivity(intent);
            }
        });
    }

    private void initView(View v) {
        mainCardView = (CardView) v.findViewById(R.id.main_cardView);
        progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        nameTextView = (TextView) v.findViewById(R.id.name_textView);
        ratingTextView = (TextView) v.findViewById(R.id.rating_textView);
        //circleImageView = (ImageView) v.findViewById(R.id.picture_imageView);
    }

    public void setDiveCenter(DiveService service){
        if (service != null && service.getDiveCenter() != null){
            if (NYHelper.isStringNotEmpty(service.getDiveCenter().getName()))nameTextView.setText(service.getDiveCenter().getName());
            if (service.getDiveCenter().getRating() > 0){
                ratingTextView.setText("*"+String.valueOf(service.getDiveCenter().getRating()));
            } else{
                ratingTextView.setText("no rating");
            }

            progressBar.setVisibility(View.GONE);
            mainCardView.setVisibility(View.VISIBLE);
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
