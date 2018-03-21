package com.nyelam.android.view;

import android.app.Dialog;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nyelam.android.R;

/**
 * Created by Aprilian Nur Wakhid Daini on 3/21/2018.
 */

public class NYCountryDialogFragment extends DialogFragment {
    private View parentView;
    private Toolbar toolbar;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_AppCompat_NoActionBar);
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //The layout xml file contains the toolbar
        parentView = inflater.inflate(R.layout.dialog_choose_country, container, false);
        initView();
        initData();
        return parentView;
    }


    private void initView() {
        toolbar = (Toolbar) parentView.findViewById(R.id.toolbar);

    }

    private void initData() {
        toolbar.setTitle("Post");
        //Set naviagtion icon to back button drawable
//        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // handle back button naviagtion
//                dismiss();
//            }
//        });
    }




}