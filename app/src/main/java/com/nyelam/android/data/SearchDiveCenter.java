package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/9/2018.
 */

public class SearchDiveCenter extends SearchResult {

    private static String KEY_PROVINCE = "province";
    private static String KEY_COUNT = "count";

    private String province;
    private String count;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
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
            if (!obj.isNull(KEY_NAME)) {
                setName(obj.getString(KEY_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_RATING)) {
                setRating(obj.getString(KEY_RATING));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TYPE)) {
                setType(obj.getInt(KEY_TYPE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_PROVINCE)) {
                setProvince(obj.getString(KEY_PROVINCE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_COUNT)) {
                setCount(obj.getString(KEY_COUNT));
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
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_NAME, getName());
            } else {
                obj.put(KEY_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getRating())) {
                obj.put(KEY_RATING, getRating());
            } else {
                obj.put(KEY_RATING, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (getType() != null) {
                obj.put(KEY_TYPE, getType());
            } else {
                obj.put(KEY_TYPE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getProvince())) {
                obj.put(KEY_PROVINCE, getProvince());
            } else {
                obj.put(KEY_PROVINCE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getCount())) {
                obj.put(KEY_COUNT, getCount());
            } else {
                obj.put(KEY_COUNT, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}


        return super.toString();
    }


}
