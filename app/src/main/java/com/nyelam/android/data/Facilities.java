package com.nyelam.android.data;

import android.text.TextUtils;

import com.nyelam.android.dev.NYLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class Facilities implements Parseable {

    private static String KEY_DIVE_GUIDE = "dive_guide";
    private static String KEY_FOOD = "food";
    private static String KEY_TOWEL = "towel";
    private static String KEY_DIVE_EQUIPMENT = "dive_equipment";
    private static String KEY_LICENSE = "license";
    private static String KEY_TRANSPORTATION = "transportation";

    private boolean diveGuide;
    private boolean food;
    private boolean towel;
    private boolean diveEquipment;
    private boolean license;
    private boolean transportation;

    public boolean isDiveGuide() {
        return diveGuide;
    }

    public void setDiveGuide(boolean diveGuide) {
        this.diveGuide = diveGuide;
    }

    public boolean isFood() {
        return food;
    }

    public void setFood(boolean food) {
        this.food = food;
    }

    public boolean isTowel() {
        return towel;
    }

    public void setTowel(boolean towel) {
        this.towel = towel;
    }

    public boolean isDiveEquipment() {
        return diveEquipment;
    }

    public void setDiveEquipment(boolean diveEquipment) {
        this.diveEquipment = diveEquipment;
    }

    public boolean isLicense() {
        return license;
    }

    public void setLicense(boolean license) {
        this.license = license;
    }

    public boolean isTransportation() {
        return transportation;
    }

    public void setTransportation(boolean transportation) {
        this.transportation = transportation;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_DIVE_GUIDE)) {
                setDiveGuide(obj.getBoolean(KEY_DIVE_GUIDE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_FOOD)) {
                setFood(obj.getBoolean(KEY_FOOD));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TOWEL)) {
                setTowel(obj.getBoolean(KEY_TOWEL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DIVE_EQUIPMENT)) {
                setDiveEquipment(obj.getBoolean(KEY_DIVE_EQUIPMENT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_LICENSE)) {
                setLicense(obj.getBoolean(KEY_LICENSE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_TRANSPORTATION)) {
                setTransportation(obj.getBoolean(KEY_TRANSPORTATION));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            obj.put(KEY_DIVE_GUIDE, isDiveGuide());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_FOOD, isFood());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_TOWEL, isTowel());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_DIVE_EQUIPMENT, isDiveEquipment());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_LICENSE, isLicense());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_TRANSPORTATION, isTransportation());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}