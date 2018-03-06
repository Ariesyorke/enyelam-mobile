package com.nyelam.android.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nyelam.android.R;
import com.nyelam.android.auth.AuthActivity;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DAODataBridge;
import com.nyelam.android.data.User;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.http.NYStatusInvalidTokenException;
import com.nyelam.android.storage.EmailLoginStorage;
import com.nyelam.android.storage.LoginStorage;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYHelper {

    public static final String GK_SOCMED_TYPE_FACEBOOK = "facebook";
    public static final String GK_SOCMED_TYPE_GOOGLE = "google";

    public static final String ASC = "asc";
    public static final String SEARCH_RESULT = "search_result";
    public static final String KEYWORD = "keyword";
    public static final String ID_DIVER = "id_diver";
    public static final String ID_SERVICE = "id_service";
    public static final String SERVICE = "id_service";
    public static final String DIVER = "diver";
    public static final String DIVE_CENTER = "dive_center";
    public static final String SCHEDULE = "schedule";
    public static final String DIVE_SPOT = "dive_spot";
    public static final String DIVE_SPOTS = "dive_spots";
    public static final String EVENT = "event";
    public static final String DATE = "date";
    public static final String TYPE = "type";
    public static final String ECO_TRIP = "eco_trip";
    public static final String CERTIFICATE = "certificate";
    public static final String CONTACT = "contact";
    public static final String PARTICIPANT = "participant";
    public static final String CART_TOKEN = "cart_token";
    public static final String POSITION = "position";
    public static final String IS_NOT_NEW = "is_not_new";
    public static final String IS_MAIN_ACTIVITY = "is_main_activity";
    public static final String IS_ECO_TRIP = "is_eco_trip";
    public static final String DIVE_SPOT_ID = "dive_spot_id";
    public static final String CART_RETURN = "cart_return";
    public static final int LOGIN_REQ = 101;
    public static final String ID_ORDER = "id_order";
    public static final String SUMMARY = "summary";
    public static final String ORDER_RETURN = "order_return";
    public static final String BANK_TRANSFER = "bank_transfer";
    public static final String MIDTRANS = "midtrans";

    public static final String NY_ACCEPT_FRAUD_STATUS = "accept";
    public static final String NY_TRANSACTION_STATUS_CAPTURE = "capture";
    public static final String TRANSACTION_COMPLETED = "transaction_completed";
    public static final String TRANSACTION_PENDING = "pending";
    public static final String ORDER = "order";
    public static final String ORDER_HELPER = "order_id";
    public static final String CATEGORIES = "categories";
    public static final int REQ_LOGIN = 41;
    public static final int REQ_CART_EXPIRED = 1405;
    public static final String TRANSACTION_RESPONSE = "transaction_response";

    public static boolean isStringNotEmpty(String string) {
        return (string != null && !TextUtils.isEmpty(string));
    }

    public static String setMillisToDate(long millis) {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis*1000);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //String monthString = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String monthString = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        return (String.valueOf(day) + " " + monthString + " " + String.valueOf(year));
    }

    public static String setMillisToDateMonth(long millis) {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis*1000);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        //String monthString = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        String monthString = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());

        return (monthString + " " + String.valueOf(day) + ", " + String.valueOf(year));
    }

    public static String priceFormatter(double price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return "Rp "+String.valueOf(formatter.format(price));
    }

    public static String priceFormatter(String currency, double price) {
        if (isStringNotEmpty(currency) && currency.equals("USD")){

            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            format.setCurrency(Currency.getInstance("USD"));
            return "Rp "+format.format(price);
        } else {
            DecimalFormat formatter = new DecimalFormat("#,###");
            return "Rp "+String.valueOf(formatter.format(price));
        }

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

    public static boolean saveEmailUser(Context context, String email) {
        EmailLoginStorage storage = new EmailLoginStorage(context);
        storage.email = email;
        return storage.save();
    }

    public static boolean updateUserData(Context context, User user) {
        LoginStorage storage = new LoginStorage(context);
        storage.user = user;
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

        //NYLog.e("ERROR APA : "+e.getCause().getMessage());

        String message = context.getResources().getString(R.string.warn_no_connection);
        if (e.getCause() != null) {
            if (e.getCause().getMessage().contains("Internal Server Error")){
                message = context.getString(R.string.warn_server_error);
            } else {
                message = e.getCause().getMessage();
            }
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


    public static final void handleAPIException(final Context context, Exception e, boolean isCancelable, DialogInterface.OnClickListener listener) {

        //NYLog.e("ERROR APA : "+e.getCause().getMessage());

        String message = context.getResources().getString(R.string.warn_no_connection);
        if (e.getCause() != null) {
            if (e.getCause().getMessage().contains("Internal Server Error")){
                message = context.getString(R.string.warn_server_error);
            } else {
                message = e.getCause().getMessage();
            }
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
                    .setCancelable(isCancelable);
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
                        .setCancelable(isCancelable)
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

    public static final void handlePopupMessage(Context context, String message, boolean isCancelable , DialogInterface.OnClickListener listener, String okButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setNeutralButton(okButton,
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

    public static void setFacilities(boolean isTrue, ImageView icImageView) {
        if (isTrue){
            icImageView.setImageResource(R.drawable.ic_boat_active);
        } else {
            icImageView.setImageResource(R.drawable.ic_boat_disable);
        }
    }

    public static int integerToDP(Context myContext, Integer myInteger) {
        Resources r = myContext.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                myInteger,
                r.getDisplayMetrics()
        );

        return px;
    }

    public static boolean isValidEmaillId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    public static String capitalizeString(String str){
        String[] strArray = str.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String s : strArray) {
            String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
            builder.append(cap + " ");
        }
        return  builder.toString();
    }

    public static void logout(Activity activity) {
        LoginStorage storage = new LoginStorage(activity);
        storage.clear();
        Intent intent = new Intent(activity, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    /** CHECK WHETHER INTERNET CONNECTION IS AVAILABLE OR NOT */
    public static boolean checkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();

        if (activeNetworkInfo != null) { // connected to the internet
            //Toast.makeText(context, activeNetworkInfo.getTypeName(), Toast.LENGTH_SHORT).show();

            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;
            } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        }
        return false;
    }

    public static DisplayImageOptions getCompressedOption(Context context) {
//        final String httpAuthUsername = context.getResources().getString(R.string.http_auth_username);
//        final String httpAuthPassword = context.getResources().getString(R.string.http_auth_password);
        Map<String, String> headers = new HashMap<String, String>() {
//            {
//                String httpAuth = new StringBuffer("Basic ")
//                        .append(Base64.encodeToString(
//                                new StringBuffer(httpAuthUsername)
//                                        .append(":")
//                                        .append(httpAuthPassword)
//                                        .toString()
//                                        .getBytes(),
//                                Base64.DEFAULT)).toString();
//                put("Authorization", httpAuth);
//            }
        };
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false)  // default
                .delayBeforeLoading(1000)
                .extraForDownloader(headers)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .build();
        return options;
    }



    public static String bitmapToString(Bitmap realImage){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        realImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();

        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encodedImage;
    }


    public static Bitmap stringToBitmap(String previouslyEncodedImage){
        byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }

}
