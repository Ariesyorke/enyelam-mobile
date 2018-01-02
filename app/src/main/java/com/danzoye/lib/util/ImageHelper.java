package com.danzoye.lib.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageHelper {
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // if (width > height) {
            // inSampleSize = (int) Math
            // .ceil((float) width / (float) reqWidth);
            // } else {
            // inSampleSize = (int) Math.ceil((float) height
            // / (float) reqHeight);
            // }

            inSampleSize = 2;

        }

        return inSampleSize;
    }

    public static Bitmap cropBitmapToCircle(Context context, Bitmap src) {
        int size = Math.min(src.getWidth(), src.getHeight());

        // scale incoming bitmap to appropriate px size given arguments and
        // display dpi
        Bitmap bitmap = Bitmap.createScaledBitmap(src, src.getWidth(),
                src.getHeight(), true);

        // create empty bitmap for drawing
        Bitmap output = Bitmap.createBitmap(size, size, Config.ARGB_8888);

        // get canvas for empty bitmap
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        // fill the canvas with transparency
        canvas.drawARGB(0, 0, 0, 0);

        // draw the rounded corners around the image rect. clockwise, starting
        // in upper left.
        canvas.drawCircle(size / 2, size / 2, size / 2, paint);

        // set up paint object such that it only paints on Color.WHITE
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        // draw resized bitmap onto imageRect in canvas, using paint as
        // configured above
        canvas.drawBitmap(bitmap, rect, rect, paint);

        System.gc();

        return output;
    }

    public static final Bitmap decodeSampledBitmapFromByteArray(byte[] data,
                                                                int maxWidth, int maxHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, maxWidth,
                maxHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static final Bitmap decodeSampledBitmapFromFile(File file,
                                                           int maxWidth, int maxHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, maxWidth,
                maxHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getPath(), options);
    }

    public static final String generateLocalFilenameFromURLImage(String url) {
        try {
            URL tempUrl = new URL(url);

            String host = tempUrl.getHost();
            String path = tempUrl.getPath();

            StringBuffer buf = new StringBuffer(host.replace(".", "_"));
            buf.append(path.replace("/", "_"));
            return buf.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Use this method to scale a bitmap and give it specific rounded corners.
     *
     * @param context    Context object used to ascertain display density.
     * @param src        The original bitmap that will be scaled and have rounded
     *                   corners applied to it.
     * @param upperLeft  Corner radius for upper left.
     * @param upperRight Corner radius for upper right.
     * @param lowerRight Corner radius for lower right.
     * @param lowerLeft  Corner radius for lower left.
     * @param endWidth   Width to which to scale original bitmap.
     * @param endHeight  Height to which to scale original bitmap.
     * @return Scaled bitmap with rounded corners.
     */
    public static Bitmap getRoundedCornerBitmap(Context context, Bitmap src,
                                                float upperLeft, float upperRight, float lowerRight,
                                                float lowerLeft, int endWidth, int endHeight) {
        float densityMultiplier = context.getResources().getDisplayMetrics().density;

        // scale incoming bitmap to appropriate px size given arguments and
        // display dpi
        Bitmap bitmap = Bitmap.createScaledBitmap(src,
                Math.round(endWidth * densityMultiplier),
                Math.round(endHeight * densityMultiplier), true);

        // create empty bitmap for drawing
        Bitmap output = Bitmap.createBitmap(
                Math.round(endWidth * densityMultiplier),
                Math.round(endHeight * densityMultiplier), Config.ARGB_8888);

        // get canvas for empty bitmap
        Canvas canvas = new Canvas(output);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // scale the rounded corners appropriately given dpi
        upperLeft *= densityMultiplier;
        upperRight *= densityMultiplier;
        lowerRight *= densityMultiplier;
        lowerLeft *= densityMultiplier;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        // fill the canvas with transparency
        canvas.drawARGB(0, 0, 0, 0);

        // draw the rounded corners around the image rect. clockwise, starting
        // in upper left.
        canvas.drawCircle(upperLeft, upperLeft, upperLeft, paint);
        canvas.drawCircle(width - upperRight, upperRight, upperRight, paint);
        canvas.drawCircle(width - lowerRight, height - lowerRight, lowerRight,
                paint);
        canvas.drawCircle(lowerLeft, height - lowerLeft, lowerLeft, paint);

        // fill in all the gaps between circles. clockwise, starting at top.
        RectF rectT = new RectF(upperLeft, 0, width - upperRight, height / 2);
        RectF rectR = new RectF(width / 2, upperRight, width, height
                - lowerRight);
        RectF rectB = new RectF(lowerLeft, height / 2, width - lowerRight,
                height);
        RectF rectL = new RectF(0, upperLeft, width / 2, height - lowerLeft);

        canvas.drawRect(rectT, paint);
        canvas.drawRect(rectR, paint);
        canvas.drawRect(rectB, paint);
        canvas.drawRect(rectL, paint);

        // set up the rect for the image
        Rect imageRect = new Rect(0, 0, width, height);

        // set up paint object such that it only paints on Color.WHITE
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        // draw resized bitmap onto imageRect in canvas, using paint as
        // configured above
        canvas.drawBitmap(bitmap, imageRect, imageRect, paint);

        System.gc();

        return output;
    }

    /**
     * Load image (coba load dari local storage dulu, kalo ga ada, baru fetch ke
     * remote) tanpa tambahan setting di http dan file di cache/save di default directory.
     *
     * @param context
     * @param url
     * @return
     */
    public static Bitmap loadRemoteImage(Context context, String url) {
        return loadRemoteImage(context, url, null, null);
    }

    /**
     * Load image (coba load dari local storage dulu, kalo ga ada, baru fetch ke
     * remote) sekalian langsung save ke storage jika bisa. tanpa batasan maksimum width or height
     * dari gambar.
     *
     * @param context tidak boleh null
     * @param url     tidak boleh null
     * @param setting boleh null
     * @param saveTo  jika null maka akan pake default folder.
     * @return
     */
    public static Bitmap loadRemoteImage(Context context, String url, RemoteHelper.Setting setting, File saveTo) {
        return loadRemoteImage(context, url, setting, saveTo, -1, -1);
    }

    /**
     * Load image (coba load dari local storage dulu, kalo ga ada, baru fetch ke
     * remote) sekalian langsung save ke storage jika bisa. dengan batasn max width or height dari
     * gambar. Jika gambar melebihi batasan max width atau height, maka gambar akan diresize dulu
     * sebelum di save di local/cache.
     *
     * @param context   tidak boleh null
     * @param url       tidak boleh null
     * @param setting   boleh null
     * @param saveTo    jika null maka akan pake default folder.
     * @param maxWidth  masukin < 1 jika ingin tanpa batasan width
     * @param maxHeight masukin < 1 jika ingin tanpa batasan height
     * @return
     */
    public static Bitmap loadRemoteImage(Context context, String url, RemoteHelper.Setting setting,
                                         File saveTo, int maxWidth, int maxHeight) {
        if (url == null) {
            return null;
        }

        if (saveTo == null) {
            String filename = generateLocalFilenameFromURLImage(url);

            if (filename != null) {
                File root = null;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    root = context.getExternalCacheDir();
                } else {
                    // Log.w("danzoye", "unable to write external storage, cache saved to internal storage directory.");
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
                    saveTo = new File(media, filename);
                }
            }
        }

        if (saveTo != null && saveTo.exists()) {
            // Log.d("danzoye",
            //        "load image from local : " + saveTo.getAbsolutePath());
            return BitmapFactory.decodeFile(saveTo.getAbsolutePath());
        }

        byte[] imageData = RemoteHelper.httpGet(url, setting);
        if (imageData != null) {

            // Log.d("danzoye", "fetch image done : " + url);

            if (maxWidth > 0 && maxHeight > 0) {
                // TODO
                // masih slow, karena ga pernah cek apakah gambar nya beneran di kecilin ato engga.
                // harusnya kalo ga terjadi resize gambar, ga perlu melakukan ini.
                Bitmap resizedBitmap = decodeSampledBitmapFromByteArray(imageData, maxWidth, maxHeight);


                /*
                Ga berhasil, coba pake cara yang katanya lebih slow dulu.

                int bytes = resizedBitmap.getByteCount();
                ByteBuffer byteBuffer = ByteBuffer.allocate(bytes);
                resizedBitmap.copyPixelsToBuffer(byteBuffer);

                imageData = byteBuffer.array();

                */

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                imageData = stream.toByteArray();

                // Log.d("danzoye", "image resized!");
            }

            if (saveTo != null) {
                // Log.d("danzoye", "trying save image to local:"
                //        + saveTo.getAbsolutePath());
                // Log.d("daznoye", "check if cache storage is full. cache folder path = " + saveTo.getParent());

                CommonHelper.checkStorageAndDeleteCacheIfNeeded(
                        context, saveTo.getParentFile());

                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(saveTo, false);
                    os.write(imageData);
                } catch (IOException e) {
                    e.printStackTrace();
                    // Log.d("danzoye", e.toString());
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            // Log.d("danzoye", e.toString());
                        }
                    }
                }

                // Log.d("danzoye", "save image to local done/failed : "
                //        + saveTo.getAbsolutePath());
            }

            return BitmapFactory
                    .decodeByteArray(imageData, 0, imageData.length);
        }

        return null;
    }
}
