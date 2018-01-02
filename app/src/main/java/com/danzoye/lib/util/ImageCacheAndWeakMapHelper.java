package com.danzoye.lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.View;

import java.util.Map;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public abstract class ImageCacheAndWeakMapHelper<T> implements Runnable {
    private Context context;
    private View view;
    private T model;
    private Bitmap bitmap;
    private Map<View, String> viewMap;
    private LruCache<String, Bitmap> coverCache;
    private boolean isRAMCacheChecked = false;
    private String mUrl;

    public ImageCacheAndWeakMapHelper(Context context,
                                      View view,
                                      T model,
                                      Map<View, String> viewMap,
                                      LruCache<String, Bitmap> coverCache) {
        this.context = context;
        this.view = view;
        this.model = model;
        this.viewMap = viewMap;
        this.coverCache = coverCache;

        mUrl = url(this.model);
        if (mUrl == null) {
            dispatchResultInUIThread();
            return;
        }

        this.viewMap.put(view, mUrl);
    }

    public boolean checkOnRAMCache() {
        isRAMCacheChecked = true;
        if (mUrl == null) {
            dispatchResultInUIThread();
            return true;
        }
        bitmap = this.coverCache.get(mUrl);
        if (bitmap != null) {
            dispatchResultInUIThread();
            return true;
        } else {
            return false;
        }
    }

    public Context getContext() {
        return context;
    }

    public T getModel()

    {
        return model;
    }

    public View getView() {
        return view;
    }

    @Override
    public void run() {
        if (!willLoadRemoteImage()) {
            return;
        }

        bitmap = ImageHelper.loadRemoteImage(context, mUrl, getRemoteHelperSetting(), null,
                getMaxImageWidth(), getMaxImageHeight());

        onLoadRemoteImageDone(bitmap);
    }

    protected int getMaxImageHeight() {
        return -1;
    }

    protected int getMaxImageWidth() {
        return -1;
    }

    /**
     * override method ini jika ingin pasang setting untuk akses ke server nya.
     *
     * @return
     */
    protected RemoteHelper.Setting getRemoteHelperSetting() {
        // by default return null
        return null;
    }

    protected void onLoadRemoteImageDone(Bitmap bitmap) {
        this.bitmap = bitmap;
        if (bitmap != null) {
            coverCache.put(mUrl, bitmap);
        }
        String tag = viewMap.get(view);

        if (tag == null || !tag.equals(mUrl)) {
            this.bitmap = null;
            dispatchResultInUIThread();
            return;
        }

        dispatchResultInUIThread();
    }

    protected abstract void onSetImageToView(Bitmap bitmap, View view);

    protected abstract String url(T model);

    protected boolean willLoadRemoteImage() {
        if (!isRAMCacheChecked) {
            Log.w("danzoye", "did you forget check cache on RAM?");
        } else if (bitmap != null) {
            // result has been dispatched. do nothing
            return false;
        }

        if (mUrl == null) {
            dispatchResultInUIThread();
            return false;
        }

        return true;
    }

    private void dispatchResultInUIThread() {
        if (context instanceof Activity) {
            ((Activity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    onSetImageToView(bitmap, ImageCacheAndWeakMapHelper.this.view);
                }
            });
        } else {
            onSetImageToView(bitmap, ImageCacheAndWeakMapHelper.this.view);
        }
    }
}
