package com.nyelam.android.view;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyelam.android.R;

/**
 * Created by Aprilian Nur on 12/6/2017.
 */

public class NYMenuDrawerFragment extends Fragment {

    //private LoginStorage loginStorage;
    private static ImageView homeImageView, bookingImageView;
    //private SpiceManager spcMgr = new SpiceManager(GKSpiceService.class);
    private ProgressDialog progressDialog;
    private OnFragmentInteractionListener mListener;
    private TextView nameProfileTextView;
    private LinearLayout accountLinearLayout, logoutLinearLayout;
    //private LoginStorage loginStorage;

    public static NYMenuDrawerFragment newInstance() {
        NYMenuDrawerFragment fragment = new NYMenuDrawerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu_drawer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initControl();
    }

    private void initView(View v) {
        accountLinearLayout = (LinearLayout) v.findViewById(R.id.action_account_linearLayout);
        logoutLinearLayout = (LinearLayout) v.findViewById(R.id.action_logout_linearLayout);
    }

    private void initControl() {
        accountLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //mListener.onIntentCareer();
            }
        });

        logoutLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //mListener.onIntentVideos();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        //void onIntentCareer();
        //void onIntentVideos();
        // TODO: Update argument type and name
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
    public void onResume() {
        super.onResume();




    }



}