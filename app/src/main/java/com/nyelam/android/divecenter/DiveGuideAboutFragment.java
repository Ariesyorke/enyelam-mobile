package com.nyelam.android.divecenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.nyelam.android.R;
import com.nyelam.android.data.DiveGuide;
import com.nyelam.android.helper.NYHelper;

public class DiveGuideAboutFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView diveGuideAboutTextView;

    public DiveGuideAboutFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DiveGuideAboutFragment newInstance() {
        DiveGuideAboutFragment fragment = new DiveGuideAboutFragment();
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
        return inflater.inflate(R.layout.fragment_dive_guide_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        diveGuideAboutTextView = (TextView) view.findViewById(R.id.dive_guide_about_textView);
    }

    public void setContent(DiveGuide diveGuide) {

        Toast.makeText(getActivity(), "ABOUT", Toast.LENGTH_SHORT).show();

        if (diveGuide != null && NYHelper.isStringNotEmpty(diveGuide.getFullName())){
            diveGuideAboutTextView.setText(diveGuide.getFullName());
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
