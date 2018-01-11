package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class Schedule implements Parseable {

    private static String KEY_START_DATE = "start_date";
    private static String KEY_END_DATE = "end_date";

    private long startDate;
    private long endDate;

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_START_DATE)) {
                setStartDate(obj.getLong(KEY_START_DATE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_END_DATE)) {
                setEndDate(obj.getLong(KEY_END_DATE));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            obj.put(KEY_START_DATE, startDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_END_DATE, endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}