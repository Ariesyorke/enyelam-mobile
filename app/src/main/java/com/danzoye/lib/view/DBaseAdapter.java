package com.danzoye.lib.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.BaseAdapter;

import com.danzoye.lib.util.ImageCacheAndWeakMapHelper;
import com.danzoye.lib.util.RemoteHelper;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Ramdhany Dwi Nugroho on Apr 2015.
 */
public abstract class DBaseAdapter extends BaseAdapter {
    private Activity context;
    private Map<View, String> viewsWithImageMap = Collections.synchronizedMap(new WeakHashMap<View, String>());
    private LruCache<String, Bitmap> imageCache;

    protected DBaseAdapter(Activity context) {
        this.context = context;

        final int cacheSize = getCacheSize();
        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public Activity getContext() {
        return context;
    }

    protected int getCacheSize() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        return maxMemory / 8;
    }

    protected LruCache<String, Bitmap> getImageCache() {
        return imageCache;
    }

    protected String getImageUrl(Object model) {
        Log.w("danzoye", "did you forget to implement getImageUrl(T model) ?");
        return null;
    }

    protected int getMaxImageHeight(int viewType) {
        return -1;
    }

    protected int getMaxImageWidth(int viewType) {
        return -1;
    }

    protected RemoteHelper.Setting getRemoteHelperSetting(int viewType) {
        return null;
    }

    protected Map<View, String> getViewsWithImageMap() {
        return viewsWithImageMap;
    }

    protected void loadImage(int viewType, View view, Object model) {
        ImageCacheAndWeakMapHelper helper = onCreateCacheAndWeakMapHelper(viewType, view, model,
                getViewsWithImageMap(), getImageCache());

        if (!helper.checkOnRAMCache())
            onQueueHelperToBackgroundPooler(helper);
    }

    protected ImageCacheAndWeakMapHelper onCreateCacheAndWeakMapHelper(
            int viewType,
            View view,
            Object model,
            Map<View, String> map,
            LruCache<String, Bitmap> cache) {

        return new PImageCacheAndWeakMapHelper(
                getContext(),
                view,
                model,
                map,
                cache,
                viewType
        ) {

            @Override
            protected int getMaxImageHeight() {
                return DBaseAdapter.this.getMaxImageHeight(viewType);
            }

            @Override
            protected int getMaxImageWidth() {
                return DBaseAdapter.this.getMaxImageWidth(viewType);
            }

            @Override
            protected RemoteHelper.Setting getRemoteHelperSetting() {
                return DBaseAdapter.this.getRemoteHelperSetting(viewType);
            }

            @Override
            protected void onSetImageToView(Bitmap bitmap, View view) {
                DBaseAdapter.this.onSetImageToView(viewType, bitmap, view);
                onDequeHelperFromBackgroundPooler(this);
            }

            @Override
            protected String url(Object model) {
                return DBaseAdapter.this.getImageUrl(model);
            }
        };
    }

    protected void onDequeHelperFromBackgroundPooler(Runnable runnable) {
        // default, do nothing
    }

    protected void onQueueHelperToBackgroundPooler(Runnable runnable) {
        AsyncTask.execute(runnable);
    }

    protected void onSetImageToView(int viewType, Bitmap bitmap, View view) {
        Log.w("danzoye", "did you forget to implement onSetImageToView(int viewType, Bitmap bitmap, View view) ?");
    }

    private abstract static class PImageCacheAndWeakMapHelper extends ImageCacheAndWeakMapHelper<Object> {
        int viewType;

        protected PImageCacheAndWeakMapHelper(Context context, View view, Object model, Map<View, String> viewMap,
                                              LruCache<String, Bitmap> coverCache, int viewType) {
            super(context, view, model, viewMap, coverCache);
            this.viewType = viewType;
        }
    }
}
