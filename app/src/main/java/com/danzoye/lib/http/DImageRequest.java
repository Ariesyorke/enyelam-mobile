package com.danzoye.lib.http;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.danzoye.lib.util.ImageHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ramdhany Dwi Nugroho on Feb 2015.
 */
public class DImageRequest<MODEL> implements Runnable {
    private Context context;
    private MODEL model;
    private ImageRequestOption<MODEL> option;
    private View view;
    private Bitmap bitmap;
    private Callback<MODEL> callback;

    public DImageRequest(Context context, MODEL model, ImageRequestOption<MODEL> option, View view) {
        this.context = context;
        this.model = model;
        this.option = option;
        this.view = view;
    }

    public Callback<MODEL> getCallback() {
        return callback;
    }

    public void setCallback(Callback<MODEL> callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        String url = option.getUrl(model);
        if (url == null) {
            throw new NullPointerException("ImageRequestOption.getUrl() can't return null");
        }

        File file = null;
        File root;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            root = context.getExternalCacheDir();
        } else {
            Log.w("danzoye", "unable to write external storage, cache saved to internal storage directory.");
            root = context.getCacheDir();
        }
        File media = new File(new StringBuffer(root.getAbsolutePath())
                .append("/media/").toString());
        if (!media.exists() && !media.mkdirs()) {
            // folder ga ada dan ga berhasil bikin, artinya jangan di
            // save
            // di
            // local sama skali
            // Log.d("danzoye", "folder not found and can't be created : "
            //        + media.getAbsolutePath());
        } else {
            file = new File(media, ImageHelper.generateLocalFilenameFromURLImage(url));
        }
        if (file != null && file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.onImageLoadDone(model, bitmap, view);
                }
            });
            return;
        } else {
            byte[] data = null;
            HttpURLConnection conn = null;
            try {

                // Log.d("danzoye", "connect to " + url);

                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setReadTimeout(60000);
                conn.setConnectTimeout(60000);
                conn.addRequestProperty("User-Agent",
                        context.getPackageName());

                int response = conn.getResponseCode();

                // Log.d("danzoye",
                //        "got response : " + response + " "
                //                + conn.getResponseMessage());

                if (response >= 200 && response < 300) {
                    InputStream is = conn.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();

                    int ch;

                    while ((ch = is.read()) != -1) {
                        bos.write((char) ch);
                    }
                    data = bos.toByteArray();

                    is.close();
                    bos.close();
                    conn.disconnect();
                } else {
                    int ch;
                    InputStream errorStream = conn.getErrorStream();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while ((ch = errorStream.read()) != -1) {
                        baos.write((char) ch);
                    }
                    byte[] errorData = baos.toByteArray();
                    errorStream.close();
                    baos.close();
                    conn.disconnect();
                    throw new IOException(new StringBuffer(
                            conn.getResponseMessage() + " = "
                                    + new String(errorData)).toString());
                }
            } catch (MalformedURLException e) {
                // Log.e("danzoye", e.toString());
            } catch (IOException e) {
                // Log.e("danzoye", e.toString());
            }

            if (data != null) {
                bitmap = option.processingImage(context, model, data);
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callback.onImageLoadDone(model, bitmap, view);
                    }
                });
                return;
            }

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    callback.onImageLoadDone(model, null, view);
                }
            });
        }
    }

    public static interface Callback<MODEL> {
        public void onImageLoadDone(MODEL model, Bitmap bitmap, View view);
    }

    public static abstract class ImageRequestOption<MODEL> {
        public abstract String getUrl(MODEL model);

        public int maxImageWidth() {
            return -1;
        }

        protected Bitmap processingImage(Context context, MODEL model, byte[] data) {
            Bitmap bitmap = null;
            int targetW = maxImageWidth();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(data, 0, data.length, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // only scale down if larger than max width
            if (targetW > 0 && photoW > targetW) {
                int targetH = (photoH * targetW) / photoW;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

                // Decode the image file into a Bitmap sized to fill the
                // View
                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;
                bmOptions.inPurgeable = true;

                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
                        bmOptions);
            } else {
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            }

            ByteArrayOutputStream imageStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, imageStream);

            System.gc();

            File file = null;
            File root;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                root = context.getExternalCacheDir();
            } else {
                Log.w("danzoye", "unable to write external storage, cache saved to internal storage directory.");
                root = context.getCacheDir();
            }
            File media = new File(new StringBuffer(root.getAbsolutePath())
                    .append("/media/").toString());
            if (!media.exists() && !media.mkdirs()) {
                // folder ga ada dan ga berhasil bikin, artinya jangan di
                // save
                // di
                // local sama skali
                // Log.d("danzoye", "folder not found and can't be created : "
                //        + media.getAbsolutePath());
            } else {
                file = new File(media, ImageHelper.generateLocalFilenameFromURLImage(getUrl(model)));
            }

            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(imageStream.toByteArray());
                fos.close();

                return bitmap;

            } catch (FileNotFoundException e) {
                // Log.e("danzoye", "File not found: " + e.getMessage());
            } catch (IOException e) {
                // Log.e("danzoye", "Error accessing file: " + e.getMessage());
            } finally {
                System.gc();
            }
            return null;
        }
    }
}
