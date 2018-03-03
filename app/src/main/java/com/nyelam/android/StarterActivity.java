package com.nyelam.android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.CountryCode;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.data.dao.NYCountryCode;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.storage.NYMasterDataStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.SpiceService;

import java.util.List;

public class StarterActivity extends AppCompatActivity  implements NYMasterDataStorage.LoadDataListener<CountryCode>{
    private int[] backgroundDrawables = {
            R.drawable.eco_trip_1_bg,
            R.drawable.eco_trip_2_bg,
            R.drawable.eco_trip_3_bg,
            R.drawable.eco_trip_4_bg,
            R.drawable.background_blur
    };
    private final int SPLASH_TIME = 3000;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private SpiceManager spcMgr = new SpiceManager(SpiceService.class);
    private NYMasterDataStorage masterDataStorage;
    //    private Province province;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        getCacheBackground(0);

    }

    private void getCacheBackground(final int index) {
        ImageLoader.getInstance().loadImage("drawable://" + backgroundDrawables[index], NYHelper.getCompressedOption(this), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                int nextIndex = index;
                nextIndex += 1;
                if (nextIndex < backgroundDrawables.length) {
                    getCacheBackground(nextIndex);
                } else {
                    checkConnection();
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                NYApplication application = (NYApplication)getApplication();
                application.addCache(imageUri, loadedImage);
                int nextIndex = index;
                nextIndex += 1;
                if (nextIndex < backgroundDrawables.length) {
                    getCacheBackground(nextIndex);
                } else {
                    checkConnection();
                }
            }


            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private void checkConnection() {
        if (NYHelper.checkConnection(StarterActivity.this)) {
            // Its Available...
            startSplashTimer();
        } else {
            // Not Available...
            NYHelper.handlePopupMessage(StarterActivity.this, getString(R.string.warn_no_connection), false,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (NYHelper.checkConnection(StarterActivity.this)){
                                dialog.dismiss();
                                startSplashTimer();
                            } else {
                                checkConnection();
                            }
                        }
                    });
        }
    }

    private void startSplashTimer() {

        DaoSession session = ((NYApplication) getApplicationContext()).getDaoSession();
        List<NYCountryCode> rawProducts = session.getNYCountryCodeDao().queryBuilder().list();
        List<CountryCode> countryCodes = NYHelper.generateList(rawProducts, CountryCode.class);
        if (countryCodes != null && countryCodes.size() > 0){

        } else {
            //countCartTextView.setVisibility(View.GONE);
        }

        if (countryCodes != null && countryCodes.size() > 0) {
            new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        /*startActivity(new Intent(StarterActivity.this, HomeActivity.class));
                        finish();*/
                        onLoadCategories();
                    }
                }, SPLASH_TIME);
        } else  {
            onLoadCountryCodes();
        }

    }

    private void onLoadCountryCodes() {
        if (masterDataStorage == null)masterDataStorage = new NYMasterDataStorage(this);

        masterDataStorage.loadCountries(new NYMasterDataStorage.LoadDataListener<CountryCode>() {
            @Override
            public void onLoadFailed(Exception e) {
                /*NYHelper.handleAPIException(StarterActivity.this, e, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });*/
            }

            @Override
            public void onDataLoaded(List<CountryCode> items) {
                /*Intent intent = new Intent(StarterActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();*/
                onLoadCategories();
            }
        },true);
    }


    private void onLoadCategories() {
        if (masterDataStorage == null)masterDataStorage = new NYMasterDataStorage(this);

        masterDataStorage.loadCategories(new NYMasterDataStorage.LoadDataListener<Category>() {
            @Override
            public void onLoadFailed(Exception e) {
                /*NYHelper.handleAPIException(StarterActivity.this, e, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });*/
            }

            @Override
            public void onDataLoaded(List<Category> items) {
                Intent intent = new Intent(StarterActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        },true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onLoadFailed(Exception e) {

    }

    @Override
    public void onDataLoaded(List<CountryCode> items) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
