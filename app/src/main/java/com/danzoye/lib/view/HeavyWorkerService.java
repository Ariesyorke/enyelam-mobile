package com.danzoye.lib.view;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.danzoye.lib.util.DRunnable;
import com.danzoye.lib.util.ImageHelper;
import com.danzoye.lib.util.RemoteHelper;
import com.danzoye.lib.util.RunnablePooler;


public class HeavyWorkerService extends Service {
    private Messenger receivingMessenger;
    private RunnablePooler runnablePooler;

    public HeavyWorkerService() {
        runnablePooler = new RunnablePooler();
        runnablePooler.setLimit(2);
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (receivingMessenger == null) {
            receivingMessenger = new Messenger(createHandler());
        }
        return receivingMessenger.getBinder();
    }

    /**
     * Invoked in background process to load image.
     *
     * @param url
     * @param maxImgWidth
     * @param maxImgHeight
     * @param setting
     * @param replyTo
     * @return
     */
    protected Bitmap onLoadRemoteImageInBackground(String url, int maxImgWidth, int maxImgHeight,
                                                   RemoteHelper.Setting setting, Messenger replyTo) {
        return ImageHelper.loadRemoteImage(this, url, setting, null,
                maxImgWidth, maxImgHeight);
    }

    /**
     * Invoked when this service receive a HeavyWorkerBaseAdapter.SERVICE_REQUEST_LOAD_IMAGE request.
     * Override this method if you want to implement your own background process for loading image.
     * This method run on Main thread, must not have a blocking process.
     *
     * @param url
     * @param maxImgWidth
     * @param maxImgHeight
     * @param setting
     * @param replyTo
     */
    protected void onRequestLoadRemoteImage(String url, int maxImgWidth, int maxImgHeight,
                                            RemoteHelper.Setting setting, Messenger replyTo) {

        DRunnable<Object, Void, Bitmap> r = new DRunnable<Object, Void, Bitmap>(
                url, new Integer(maxImgWidth), new Integer(maxImgHeight), setting, replyTo
        ) {
            String url;
            Messenger replyTo;

            @Override
            protected void onOutput(Bitmap bitmap) {
                Bundle data = new Bundle();
                data.putParcelable(HeavyWorkerBaseAdapter.KEY_RESULT_BITMAP, bitmap);

                Message msg = Message.obtain();
                msg.setData(data);

                try {
                    replyTo.send(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    // Log.e("danzoye", e);
                }

                runnablePooler.remove(this);
            }

            @Override
            protected Bitmap runInBackground(Object... objects) {
                url = (String) objects[0];
                int maxImgWidth = (Integer) objects[1];
                int maxImgHeight = (Integer) objects[2];
                RemoteHelper.Setting setting = (RemoteHelper.Setting) objects[3];
                replyTo = (Messenger) objects[4];
                return onLoadRemoteImageInBackground(url, maxImgWidth, maxImgHeight, setting, replyTo);
            }
        };
        runnablePooler.add(r);
    }

    private Handler createHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == HeavyWorkerBaseAdapter.SERVICE_REQUEST_LOAD_IMAGE) {
                    Bundle data = msg.getData();

                    int maxImgH = data.getInt(HeavyWorkerBaseAdapter.KEY_MAX_IMG_HEIGHT);
                    int maxImgW = data.getInt(HeavyWorkerBaseAdapter.KEY_MAX_IMG_WIDTH);
                    RemoteHelper.Setting setting = new RemoteHelper.Setting(data.getString(HeavyWorkerBaseAdapter.KEY_REMOTE_SETTING));
                    String url = data.getString(HeavyWorkerBaseAdapter.KEY_URL);

                    onRequestLoadRemoteImage(url, maxImgW, maxImgH, setting, msg.replyTo);
                }
            }
        };
    }
}
