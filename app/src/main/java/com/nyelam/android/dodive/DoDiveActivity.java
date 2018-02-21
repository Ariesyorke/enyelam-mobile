package com.nyelam.android.dodive;

import android.app.FragmentManager;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;

public class DoDiveActivity extends BasicActivity implements
        DoDiveFragment.OnFragmentInteractionListener,
        DoDiveResultDiveCenterFragment.OnFragmentInteractionListener,
        DoDiveResultDiveSpotsFragment.OnFragmentInteractionListener,
        NYCustomDialog.OnDialogFragmentClickListener{

    private DoDiveFragment fragment;
    private TextView titleTextView;
    private ImageView searchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_dive);
        initView();
        initFragment();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
        
        titleTextView = (TextView) findViewById(R.id.title_textView);
        searchImageView = (ImageView) findViewById(R.id.search_imageView);
    }

    private void initFragment() {
        /*DoDiveFragment fragmentDoDive = new DoDiveFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,fragmentDoDive);
        fragmentTransaction.addToBackStack(fragmentDoDive.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();*/

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = new DoDiveFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    protected void setTitle(String title, boolean isBold, boolean sortVisible){

        if (NYHelper.isStringNotEmpty(title)){
            titleTextView.setText(title);
            if (isBold){
                titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
            } else {
                titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.NORMAL);
            }
        }

        if (sortVisible){
            searchImageView.setVisibility(View.VISIBLE);
        } else {
            searchImageView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onChooseListener(Object position) {
        fragment.setDiver((String) position);
        //Toast.makeText(this, (String) position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptAgreementListener() {

    }


}
