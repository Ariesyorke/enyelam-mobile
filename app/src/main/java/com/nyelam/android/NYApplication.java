package com.nyelam.android;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.SdkCoreFlowBuilder;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.nyelam.android.data.dao.DaoMaster;
import com.nyelam.android.data.dao.DaoSession;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.HomeActivity;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/10/2018.
 */

public class NYApplication extends MultiDexApplication implements TransactionFinishedCallback {

    public DaoSession daoSession;
    public DaoSession getDaoSession() {
        return daoSession;
    }
    public LruCache<String, Bitmap> imageCache;
    public Calendar calendar;

    @Override
    public void onCreate() {
        super.onCreate();
        //calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT +7"));
        calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "nyelam-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        File cacheDir = StorageUtils.getCacheDirectory(this);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);


        /*SdkUIFlowBuilder.init(this,getResources().getString(R.string.client_key),getResources().getString(R.string.api_veritrans_development),this)
                //.setSelectedPaymentMethods(ArrayList<PaymentMethodsModel > )
                //.setUIkitCustomSetting(new UIKitCustomSetting())
                .enableLog(true) // enable sdk log (optional)
                .buildSDK();*/

        //SdkUIFlowBuilder.init(getApplicationContext(),getResources().getString(R.string.client_key_development),getResources().getString(R.string.api_veritrans_development),this);

        /*SdkCoreFlowBuilder.init()
                .setContext(this)
                .enableLog(true)
                .setClientKey(getResources().getString(R.string.client_key_development))
                .setMerchantBaseUrl(getResources().getString(R.string.api_veritrans_development))
                .buildSDK();*/

    }

    @Override
    public void onTransactionFinished(TransactionResult transactionResult) {
        //Toast.makeText(this, getResources().getString(R.string.search_results), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, HomeActivity.class);
        //if (gooLocation != null)intent.putExtra(MainActivity.ARG_ADDRESS, gooLocation.toString());
        if (transactionResult.getResponse().getFraudStatus().equals(NYHelper.NY_ACCEPT_FRAUD_STATUS)) {
            if(transactionResult.getResponse().getTransactionStatus().equals(NYHelper.NY_TRANSACTION_STATUS_CAPTURE)) {
                intent.putExtra(NYHelper.TRANSACTION_COMPLETED, true);
            } else if (transactionResult.getResponse().getTransactionStatus().equals(NYHelper.TRANSACTION_PENDING)){
                intent.putExtra(NYHelper.TRANSACTION_COMPLETED, false);
            }
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void addCache(String url, Bitmap image) {
        if(imageCache == null) {
            int cacheSize = 4 * 1024 * 1024; // 4MiB
            imageCache = new LruCache<>(cacheSize);
        }
        imageCache.put(url,image);
    }

    public Bitmap getCache(String url) {
        if(imageCache == null) {
            return null;
        }
        return imageCache.get(url);
    }
}
