package com.nyelam.android.helper;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nyelam.android.R;
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DAODataBridge;
import com.nyelam.android.http.NYStatusInvalidTokenException;
import com.nyelam.android.storage.LoginStorage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYHelper {

    public static final String ASC = "asc";
    public static final String SEARCH_RESULT = "search_result";
    public static final String KEYWORD = "keyword";
    public static final String ID_DIVER = "id_diver";
    public static final String ID_SERVICE = "id_service";
    public static final String SERVICE = "id_service";
    public static final String DIVER = "diver";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String CERTIFICATE = "certificate";


    public static boolean isStringNotEmpty(String string) {
        return (string != null && !TextUtils.isEmpty(string));
    }

    public static String setMillisToDate(long millis) {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis*1000);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return (String.valueOf(day) + "/" + String.valueOf(month+1) + "/" + String.valueOf(year));
    }

    public static String priceFormatter(double price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return "Rp "+String.valueOf(formatter.format(price));
    }

    public static String getDevice() {
        StringBuffer b = new StringBuffer();
        b.append(Build.MANUFACTURER).append("/");
        b.append(Build.MODEL);
        return b.toString();
    }

    public static boolean saveUserData(Context context, AuthReturn authReturn) {
        LoginStorage storage = new LoginStorage(context);
        storage.nyelamToken = authReturn.getToken();
        storage.user = authReturn.getUser();
        return storage.save();
    }

    public static final <RESULT extends DAODataBridge<RAW>, RAW> List<RESULT> generateList(List<RAW> raws, Class<RESULT> resultClass) {
        if (raws != null && !raws.isEmpty()) {
            List<RESULT> results = new ArrayList<>();
            for (RAW raw : raws) {
                try {
                    RESULT result = resultClass.newInstance();
                    result.copyFrom(raw);
                    results.add(result);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            return results;
        }
        return null;
    }

    public static final void handleAPIException(final Context context, Exception e, DialogInterface.OnClickListener listener) {
        String message = context.getResources().getString(R.string.warn_no_connection);
        if (e.getCause() != null) {
            message = e.getCause().getMessage();
        }

        /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setNeutralButton(android.R.string.ok,
                        listener != null ? listener : new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setCancelable(true)
                .create()
                .show();*/

        if (listener != null) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
            mBuilder.setMessage(message)
                    .setNeutralButton(android.R.string.ok,
                            listener != null ? listener : new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                    .setCancelable(true);
            if (!mBuilder.create().isShowing()) {
                mBuilder.create().show();
            }
        } else {
            if (e.getCause() instanceof NYStatusInvalidTokenException) {
                LoginStorage loginStorage = new LoginStorage(context);
                loginStorage.clear();
                Intent intent = new Intent(context, AuthActivity.class);
                intent.putExtra(AuthActivity.REQ_INVALID_TOKEN, message);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            } else {
                // TODO: helper ini saya comment
                /*Intent intent = new Intent(context, HelperActivity.class);
                intent.putExtra(HelperActivity.ERROR_MESSAGE, message);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(intent);*/

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(message)
                .setNeutralButton(android.R.string.ok,
                        listener != null ? listener : new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setCancelable(true)
                .create()
                .show();

            }
        }

    }

    public static final void handlePopupMessage(Context context, String message, boolean isCancelable , DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setNeutralButton(android.R.string.ok,
                        listener!=null? listener : new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setCancelable(isCancelable)
                .create()
                .show();
    }


    public static final void handlePopupMessage(Context context, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setNeutralButton(android.R.string.ok,
                        listener!=null? listener : new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .setCancelable(true)
                .create()
                .show();
    }

    public static final void handleErrorMessage(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(true)
                .create()
                .show();
    }

    public  static DisplayImageOptions getOption (){
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();
        return options;
    }

    public static String trim(final String s) {
        final StringBuilder sb = new StringBuilder(s);
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(0)))
            sb.deleteCharAt(0); // delete from the beginning
        while (sb.length() > 0 && Character.isWhitespace(sb.charAt(sb.length() - 1)))
            sb.deleteCharAt(sb.length() - 1); // delete from the end
        return sb.toString();
    }

}
