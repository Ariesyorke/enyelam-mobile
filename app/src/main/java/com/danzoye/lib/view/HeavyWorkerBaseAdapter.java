package com.danzoye.lib.view;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.LruCache;
import android.view.View;

import com.danzoye.lib.util.ImageCacheAndWeakMapHelper;
import com.danzoye.lib.util.RemoteHelper;

import java.util.Map;

/**
 * Created by Ramdhany Dwi Nugroho on May 2015.
 */
public abstract class HeavyWorkerBaseAdapter extends DCommonBaseAdapter implements ServiceConnection {
    public static final int SERVICE_REQUEST_LOAD_IMAGE = 100;
    public static final String KEY_MAX_IMG_HEIGHT = "key:max_img_height";
    public static final String KEY_MAX_IMG_WIDTH = "key:max_img_width";
    public static final String KEY_REMOTE_SETTING = "key:remote_setting";
    public static final String KEY_URL = "key:url";
    public static final String KEY_RESULT_BITMAP = "key:result_bitmap";
    private Messenger sender;
    private Class<? extends Service> service;
    private boolean isStopped = true;

    public HeavyWorkerBaseAdapter(Activity context, Class<? extends HeavyWorkerService> service) {
        super(context);
        this.service = service;
    }

    public boolean isStarted() {
        return !isStopped;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        sender = new Messenger(service);
        notifyDataSetChanged();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        sender = null;
    }

    public void start() {
        isStopped = false;
        getContext().bindService(new Intent(getContext(), service), this, Context.BIND_AUTO_CREATE);
    }

    public void stop() {
        isStopped = true;
        getContext().unbindService(this);
    }

    @Override
    protected ImageCacheAndWeakMapHelper onCreateCacheAndWeakMapHelper(int viewType, View view, Object model, Map<View, String> map, LruCache<String, Bitmap> cache) {
        return new PImageCacheAndWeakMapHelper(getContext(), view, model, map, cache, viewType, sender) {
            @Override
            protected void onSetImageToView(Bitmap bitmap, View view) {
                HeavyWorkerBaseAdapter.this.onSetImageToView(viewType, bitmap, view);
                onDequeHelperFromBackgroundPooler(this);
            }

            @Override
            protected String url(Object model) {
                return HeavyWorkerBaseAdapter.this.getImageUrl(model);
            }

            @Override
            protected int getMaxImageHeight() {
                return HeavyWorkerBaseAdapter.this.getMaxImageHeight(viewType);
            }

            @Override
            protected int getMaxImageWidth() {
                return HeavyWorkerBaseAdapter.this.getMaxImageWidth(viewType);
            }

            @Override
            protected RemoteHelper.Setting getRemoteHelperSetting() {
                return HeavyWorkerBaseAdapter.this.getRemoteHelperSetting(viewType);
            }
        };
    }

    @Override
    protected void onDequeHelperFromBackgroundPooler(Runnable runnable) {
        // does nothing
    }

    @Override
    protected void onQueueHelperToBackgroundPooler(Runnable runnable) {
        runnable.run();
    }

    private abstract static class PImageCacheAndWeakMapHelper extends ImageCacheAndWeakMapHelper<Object> {
        int viewType;
        Messenger sender;
        Messenger receiver;

        protected PImageCacheAndWeakMapHelper(Context context, View view, Object model, Map<View, String> viewMap,
                                              LruCache<String, Bitmap> coverCache, int viewType, Messenger sender) {
            super(context, view, model, viewMap, coverCache);
            this.viewType = viewType;
            this.sender = sender;
            this.receiver = new Messenger(new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    String url = url(getModel());
                    Bundle data = msg.getData();
                    if (data.containsKey(KEY_RESULT_BITMAP)) {
                        Bitmap bitmap = (Bitmap) data.getParcelable(KEY_RESULT_BITMAP);
                        onLoadRemoteImageDone(bitmap);
                    } else {
                        onLoadRemoteImageDone(null);
                    }
                }
            });
        }

        @Override
        public void run() {
            String url = url(getModel());
            if (!willLoadRemoteImage()) {
                return;
            }

            if (sender != null) {
                Bundle data = new Bundle();

                int maxImgH = getMaxImageHeight();
                int maxImgW = getMaxImageWidth();
                RemoteHelper.Setting setting = getRemoteHelperSetting();

                data.putInt(KEY_MAX_IMG_HEIGHT, maxImgH);
                data.putInt(KEY_MAX_IMG_WIDTH, maxImgW);
                if (setting != null) data.putString(KEY_REMOTE_SETTING, setting.toString());
                data.putString(KEY_URL, url);

                Message msg = Message.obtain();
                msg.setData(data);
                msg.what = SERVICE_REQUEST_LOAD_IMAGE;
                msg.replyTo = receiver;

                try {
                    sender.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e.toString());
                }
            }
        }
    }
}
