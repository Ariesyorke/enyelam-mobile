package com.nyelam.android.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nyelam.android.R;
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.home.TermsAndConditionActivity;
import com.nyelam.android.profile.ProfileActivity;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;

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
    private LinearLayout loginLinearLayout, messageLinearLayout, accountLinearLayout, diveGuidanceLinearLayout, settingLinearLayout, logoutLinearLayout, termsAndConditionLinearLayout, contactUsLinearLayout;
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
        loginLinearLayout = (LinearLayout) v.findViewById(R.id.action_login_linearLayout);
        accountLinearLayout = (LinearLayout) v.findViewById(R.id.action_account_linearLayout);
        messageLinearLayout = (LinearLayout) v.findViewById(R.id.action_message_linearLayout);
        diveGuidanceLinearLayout = (LinearLayout) v.findViewById(R.id.action_dive_guideance_linearLayout);
        settingLinearLayout = (LinearLayout) v.findViewById(R.id.action_setting_linearLayout);
        logoutLinearLayout = (LinearLayout) v.findViewById(R.id.action_logout_linearLayout);
        termsAndConditionLinearLayout = (LinearLayout) v.findViewById(R.id.action_terms_and_condition_linearLayout);
        contactUsLinearLayout = (LinearLayout) v.findViewById(R.id.action_contact_us_linearLayout);
    }

    private void initControl() {
        accountLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //mListener.onIntentCareer();
                mListener.openAccount();
//                if (getActivity() instanceof HomeActivity) {
//                    HomeActivity activity = (HomeActivity) getActivity();
////                    activity.setSelectedTab(3);
//                }

            }
        });

        loginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openLoginRegister();

            }
        });

        logoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.logout));
                builder.setMessage(getString(R.string.warn_logout));
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        NYHelper.logout(getActivity());
                    }
                });

                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        messageLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYHelper.handlePopupMessage(getActivity(), getString(R.string.coming_soon), null);
            }
        });

        diveGuidanceLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYHelper.handlePopupMessage(getActivity(), getString(R.string.coming_soon), null);
            }
        });

        settingLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NYHelper.handlePopupMessage(getActivity(), getString(R.string.coming_soon), null);
            }
        });

        termsAndConditionLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openTermsAndConditions();
            }
        });

        contactUsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openContactUs();
            }
        });
    }

    public interface OnFragmentInteractionListener {
        void openLoginRegister();
        void openAccount();
        void openTermsAndConditions();
        void openContactUs();
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

        LoginStorage loginStorage = new LoginStorage(getActivity());
        if (loginStorage.isUserLogin()){
            loginLinearLayout.setVisibility(View.GONE);
            accountLinearLayout.setVisibility(View.VISIBLE);
            logoutLinearLayout.setVisibility(View.VISIBLE);
        } else {
            loginLinearLayout.setVisibility(View.VISIBLE);
            accountLinearLayout.setVisibility(View.GONE);
            logoutLinearLayout.setVisibility(View.GONE);
        }

    }



}