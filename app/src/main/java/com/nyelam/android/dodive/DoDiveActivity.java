package com.nyelam.android.dodive;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.data.LicenseType;
import com.nyelam.android.data.Organization;
import com.nyelam.android.data.SearchResult;
import com.nyelam.android.data.SearchService;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.NYCustomDialog;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;

import org.json.JSONException;
import org.json.JSONObject;

public class DoDiveActivity extends BasicActivity implements
        DoDiveFragment.OnFragmentInteractionListener,
        DoDiveResultDiveCenterFragment.OnFragmentInteractionListener,
        DoDiveResultDiveSpotsFragment.OnFragmentInteractionListener,
        NYCustomDialog.OnDialogFragmentClickListener{

    protected Toolbar toolbar;
    private DoDiveFragment fragment;
    private TextView titleTextView;
    private ImageView searchImageView;
    private ImageView backgroundImageView;
    private boolean ecoTrip = false;
    public boolean isEcoTrip() {
        return ecoTrip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ecoTrip = getIntent().hasExtra(NYHelper.IS_ECO_TRIP);
        setContentView(R.layout.activity_do_dive);
        initView();
        initFragment();
        if (isEcoTrip()){
            toolbar.setBackgroundColor(getResources().getColor(R.color.ny_green3));

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.ny_green3)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
                getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.ny_green3)); //status bar or the time bar at the top
            }

        }
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
        
        titleTextView = (TextView) findViewById(R.id.title_textView);
        searchImageView = (ImageView) findViewById(R.id.search_imageView);
        backgroundImageView = (ImageView) findViewById(R.id.background_imageView);
        String imageUri = "drawable://"+R.drawable.background_blur;
        NYApplication application = (NYApplication)getApplication();
        
        /*if(application.getCache(imageUri) != null) {
            Bitmap bitmap = application.getCache(imageUri);
            backgroundImageView.setImageBitmap(bitmap);
        } else {
            ImageLoader.getInstance().displayImage(imageUri, backgroundImageView, NYHelper.getCompressedOption(this));
        }*/

        ImageLoader.getInstance().displayImage(imageUri, backgroundImageView, NYHelper.getCompressedOption(this));

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

    @Override
    public void onCancelUpdate() {

    }

    @Override
    public void doUpdateVersion(String link) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Bundle b = data.getExtras();

            if (data.hasExtra(NYHelper.SEARCH_RESULT)){
                JSONObject obj = null;
                try {
                    obj = new JSONObject(data.getStringExtra(NYHelper.SEARCH_RESULT));
                    SearchService searchService = new SearchService();
                    searchService.parse(obj);

                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);
                    if(f instanceof DoDiveFragment){
                        //Toast.makeText(this, "hello 1", Toast.LENGTH_SHORT).show();
                        ((DoDiveFragment) f).setSearchResult(searchService);
                    }

                    //Toast.makeText(this, "hello 2", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
