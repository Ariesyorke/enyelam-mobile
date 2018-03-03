package com.nyelam.android.http;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import com.danzoye.lib.http.DBaseRequest;
import com.nyelam.android.R;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public abstract class NYBasicRequest <DATA> extends DBaseRequest<DATA> {


    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILED = 2;
    public static final int STATUS_INVALID_TOKEN = 3;
    public static final String CODE_CART_EXPIRED = "1014";

    public static final String KEY_STATUS = "status";
    public static final String KEY_DATA = "data";
    public static final String KEY_CODE = "code";
    public static final String KEY_ERROR = "error";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_APP_VER = "app_ver";
    public static final String KEY_OS_VER = "os_ver";
    public static final String KEY_API_VER = "api_ver";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_DEVICE = "device";

    private Context context;
    private long gaStartTiming;
    private String apiPath;

    protected NYBasicRequest(Class clazz, Context context, String apiPath) {

        super(clazz, new StringBuffer(context.getResources().getString(R.string.host_url))
                .append(apiPath).toString());

        this.context = context;
        this.apiPath = apiPath;

        addQuery(KEY_APP_VER, getAppVersion());
        addQuery(KEY_OS_VER, getOSVersion());
        addQuery(KEY_API_VER, getApiVersion());
        addQuery(KEY_TIMESTAMP, String.valueOf(getTimestamp()));
        addQuery(KEY_DEVICE, NYHelper.getDevice());
        gaStartTiming = System.currentTimeMillis();

    }

    protected boolean isAPITesting() {
        return true;
    }

    public Context getContext() {
        return context;
    }

    protected String getApiVersion() {
        return String.valueOf(getContext().getResources().getInteger(R.integer.api_ver));
    }

    protected String getAppVersion() {
        try {
            return String.valueOf(getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown app version";
        }
    }


    protected String getGATimingLabel() {
        return apiPath;
    }

    protected String getOSVersion() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }

    protected long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    @Override
    public DATA loadDataFromNetwork() throws Exception {
        NYLog.d("will fetch remote debug getUrl() = " + getUrl());
        if (getQueries() != null && !getQueries().isEmpty()) {
            NYLog.d("will fetch remote  debug POST = ");
            for (Object[] i : getQueries()) {
                NYLog.d("--- " + i[0] + " = " + i[1]);
            }
        } else {
            NYLog.d("will fetch remote  debug POST = null");
        }

        if (getHeaders() != null && !getHeaders().isEmpty()) {
            NYLog.d("will fetch remote  debug HEADERS = ");
            for (Header i : getHeaders()) {
                NYLog.d("--- " + i.getName() + " = " + i.getValue());
            }
        } else {
            NYLog.d("will fetch remote  debug HEADERS = null");
        }
        return super.loadDataFromNetwork();
    }

    @Override
    protected DATA onProcessData(byte[] data) throws Exception {
        String json = new String(data);

        NYLog.d("return debug getUrl() = " + getUrl());
        if (getQueries() != null && !getQueries().isEmpty()) {
            NYLog.d("return debug POST = ");
            for (Object[] i : getQueries()) {
                NYLog.d("--- " + i[0] + " = " + i[1]);
            }
        } else {
            NYLog.d("return debug POST = null");
        }

        if (getHeaders() != null && !getHeaders().isEmpty()) {
            NYLog.d("return debug HEADERS = ");
            for (Header i : getHeaders()) {
                NYLog.d("--- " + i.getName() + " = " + i.getValue());
            }
        } else {
            NYLog.d("return debug HEADERS = null");
        }


        JSONObject obj = new JSONObject(json);

        if (obj.isNull(KEY_STATUS)) {
            if(isAPITesting()) {
                return onProcessSuccessData(obj);
            } else {
                throw new NYInvalidReturnValueException("no json with key \"status\"");
            }
        } else if (obj.getInt(KEY_STATUS) == STATUS_SUCCESS) {
            if (obj.has(KEY_DATA)) {
                JSONObject objData = null;
                if (!obj.isNull(KEY_DATA)) {
                    objData = obj.getJSONObject(KEY_DATA);
                }
                DATA result = onProcessSuccessData(objData);

                long doneTime = System.currentTimeMillis();
                long processTiming = doneTime - gaStartTiming;

//                EasyTracker.getInstance(getContext()).send(MapBuilder.createTiming(GAHelper.GA_CATEGORY_LOADING_REMOTE,
//                        processTiming, GAHelper.GA_NAME_HTTP_SUCCESS, getGATimingLabel()).build());

                return result;
            } else {
                long doneTime = System.currentTimeMillis();
                long processTiming = doneTime - gaStartTiming;
//                EasyTracker.getInstance(getContext()).send(MapBuilder.createTiming(GAHelper.GA_CATEGORY_LOADING_REMOTE,
//                        processTiming, GAHelper.GA_NAME_HTTP_FAILED, getGATimingLabel()).build());

                throw new NYInvalidReturnValueException("no json with key \"data\"");
            }
        } else if (obj.getInt(KEY_STATUS) == STATUS_INVALID_TOKEN) {
            String errorMessage = null;
            if (!obj.isNull(KEY_MESSAGE)) {
                errorMessage = obj.getString(KEY_MESSAGE);
            } else {
                errorMessage = "unknown error";
            }

            String code = "unknown code";
            String error = "unknown error";

            if (!obj.isNull(KEY_CODE)) {
                code = obj.getString(KEY_CODE);
            }

            if (!obj.isNull(KEY_ERROR)) {
                error = obj.getString(KEY_ERROR);
            }

            long doneTime = System.currentTimeMillis();
            long processTiming = doneTime - gaStartTiming;
            //EasyTracker.getInstance(getContext()).send(MapBuilder.createTiming(GAHelper.GA_CATEGORY_LOADING_REMOTE,
            //processTiming, GAHelper.GA_NAME_HTTP_FAILED, getGATimingLabel()).build());

            throw new NYStatusInvalidTokenException("request status invalid token");
        } else {
            String errorMessage = null;
            if (!obj.isNull(KEY_MESSAGE)) {
                errorMessage = obj.getString(KEY_MESSAGE);
            } else {
                errorMessage = "unknown error";
            }

            String code = "unknown code";
            String error = "unknown error";

            if (!obj.isNull(KEY_CODE)) {
                code = obj.getString(KEY_CODE);
            }

            if (!obj.isNull(KEY_ERROR)) {
                error = obj.getString(KEY_ERROR);
            }

            long doneTime = System.currentTimeMillis();
            long processTiming = doneTime - gaStartTiming;

            NYStatusFailedException e = new NYStatusFailedException("request status failed with code " + code + " - " + error);
            e.setCode(code);
            e.setError(error);
            e.setMessage(errorMessage);
            throw e;

            //long doneTime = System.currentTimeMillis();
            //long processTiming = doneTime - gaStartTiming;
            //EasyTracker.getInstance(getContext()).send(MapBuilder.createTiming(GAHelper.GA_CATEGORY_LOADING_REMOTE,
            //processTiming, GAHelper.GA_NAME_HTTP_FAILED, getGATimingLabel()).build());

            //throw e;
        }
    }

    protected abstract DATA onProcessSuccessData(JSONObject obj) throws Exception;
}