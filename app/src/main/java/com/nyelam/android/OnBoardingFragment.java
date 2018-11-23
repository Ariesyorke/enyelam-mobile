package com.nyelam.android;

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

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nyelam.android.helper.NYHelper;


public class OnBoardingFragment extends Fragment {

    private static final String ARG_POS = "arg_pos";
    private static final String ARG_DRAWABLE = "arg_drawable";
    private static final String ARG_TITLE = "arg_title";
    private static final String ARG_INFO = "arg_info";

//    private Intro intro;

    private ImageView ivPicture;
    private TextView tvTitle;
    private TextView tvInfo;

    public OnBoardingFragment() {
        // Required empty public constructor
    }

    public static OnBoardingFragment newInstance() {
        OnBoardingFragment fragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static OnBoardingFragment newInstance(int position, int drawableId) {
        OnBoardingFragment fragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS, position);
        args.putInt(ARG_DRAWABLE, drawableId);
        fragment.setArguments(args);
        return fragment;
    }

    public static Fragment newInstance(int pos, int featured_picture, String title, String info) {
        OnBoardingFragment fragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POS, pos);
        args.putInt(ARG_DRAWABLE, featured_picture);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_INFO, info);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {

        ivPicture = (ImageView) view.findViewById(R.id.iv_picture);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvInfo = (TextView) view.findViewById(R.id.tv_info);

        if (getArguments() != null){
            ivPicture.setImageResource(getArguments().getInt(ARG_DRAWABLE));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_on_boarding, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }




}