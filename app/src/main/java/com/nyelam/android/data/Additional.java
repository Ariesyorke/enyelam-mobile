package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class Additional implements Parseable {

    private static String KEY_TITLE = "title";
    private static String KEY_VALUE = "value";

    private String title;
    private double value;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_TITLE)) {
                setTitle(obj.getString(KEY_TITLE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_VALUE)) {
                setValue(obj.getDouble(KEY_VALUE));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            obj.put(KEY_VALUE, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (!TextUtils.isEmpty(getTitle())) {
                obj.put(KEY_TITLE, getTitle());
            } else {
                obj.put(KEY_TITLE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
