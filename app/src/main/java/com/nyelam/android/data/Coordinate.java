package com.nyelam.android.data;

import com.nyelam.android.dev.NYLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class Coordinate implements Parseable {

    private static String KEY_LAT = "lon";
    private static String KEY_LON = "lat";

    private double lat;
    private double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_LAT)) {
                setLat(obj.getDouble(KEY_LAT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_LON)) {
                setLon(obj.getDouble(KEY_LON));
            }
        } catch (JSONException e) {e.printStackTrace();}


    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            obj.put(KEY_LAT, lat);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            obj.put(KEY_LON, lon);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}