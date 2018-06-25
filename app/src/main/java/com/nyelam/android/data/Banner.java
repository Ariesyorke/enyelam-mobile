package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class Banner implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_IMAGE_URL = "image_url";
    private static String KEY_CAPTION = "caption";
    private static String KEY_URL = "url";
    private static String KEY_SERVICE_ID = "service_id";
    private static String KEY_SERVICE_NAME = "service_name";
    private static String KEY_LICENSE = "license";
    private static String KEY_DATE = "date";
    private static String KEY_TYPE = "type";
    private static String KEY_ECO_TRIP = "eco_trip";
    private static String KEY_DO_TRIP = "do_trip";
    private static String KEY_DO_COURSE = "do_course";

    private String id;
    private String imageUrl;
    private String caption;
    private String url;
    private String serviceId;
    private String serviceName;
    private boolean license;
    private long date;
    private int type;
    private boolean ecoTrip;
    private boolean doTrip;
    private boolean doCourse;

    public Banner(){}

    //2 - URL
    public Banner(String id, String imageUrl, String caption, String url){
        this.id = id;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.url = url;
        this.type = 2;
    }

    //1 - SERVICE DETAIL
    public Banner(String id, String imageUrl, String serviceId, String serviceName, boolean isLicense, long date, boolean isEcoTrip, boolean isDoTrip){
        this.id = id;
        this.imageUrl = imageUrl;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        setLicense(isLicense);
        this.date = date;
        this.type = 1;
    }

    //3 - SEARCH SERVICE
    public Banner(String id, String imageUrl, String serviceId, String serviceName, boolean isLicense){
        this.id = id;
        this.imageUrl = imageUrl;
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        setLicense(isLicense);
        this.type = 3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public boolean isLicense() {
        return license;
    }

    public void setLicense(boolean license) {
        this.license = license;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEcoTrip() {
        return ecoTrip;
    }

    public void setEcoTrip(boolean ecoTrip) {
        this.ecoTrip = ecoTrip;
    }

    public boolean isDoTrip() {
        return doTrip;
    }

    public void setDoTrip(boolean doTrip) {
        this.doTrip = doTrip;
    }

    public boolean isDoCourse() {
        return doCourse;
    }

    public void setDoCourse(boolean doCourse) {
        this.doCourse = doCourse;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_ID)) {
                setId(obj.getString(KEY_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_IMAGE_URL)) {
                setImageUrl(obj.getString(KEY_IMAGE_URL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CAPTION)) {
                setCaption(obj.getString(KEY_CAPTION));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_URL)) {
                setUrl(obj.getString(KEY_URL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SERVICE_ID)) {
                setServiceId(obj.getString(KEY_SERVICE_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SERVICE_NAME)) {
                setServiceName(obj.getString(KEY_SERVICE_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_LICENSE)) {
                setLicense(obj.getBoolean(KEY_LICENSE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DATE)) {
                setDate(obj.getLong(KEY_DATE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TYPE)) {
                setType(obj.getInt(KEY_TYPE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_ECO_TRIP)) {
                setEcoTrip(obj.getBoolean(KEY_ECO_TRIP));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DO_COURSE)) {
                setDoCourse(obj.getBoolean(KEY_DO_COURSE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DO_TRIP)) {
                setDoTrip(obj.getBoolean(KEY_DO_TRIP));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }


    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getId())) {
                obj.put(KEY_ID, getId());
            } else {
                obj.put(KEY_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getImageUrl())) {
                obj.put(KEY_IMAGE_URL, getImageUrl());
            } else {
                obj.put(KEY_IMAGE_URL, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getCaption())) {
                obj.put(KEY_CAPTION, getCaption());
            } else {
                obj.put(KEY_CAPTION, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getUrl())) {
                obj.put(KEY_URL, getUrl());
            } else {
                obj.put(KEY_URL, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getServiceId())) {
                obj.put(KEY_SERVICE_ID, getServiceId());
            } else {
                obj.put(KEY_SERVICE_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getServiceName())) {
                obj.put(KEY_SERVICE_NAME, getServiceName());
            } else {
                obj.put(KEY_SERVICE_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_LICENSE, isLicense());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_DATE, getDate());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_TYPE, getType());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_ECO_TRIP, isEcoTrip());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_DO_TRIP, isDoTrip());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_DO_COURSE, isDoCourse());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}