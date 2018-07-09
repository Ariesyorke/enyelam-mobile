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
    private static String KEY_ACCOMODATION = "accomodation";

    private Boolean diveGuide;
    private Boolean food;
    private Boolean towel;
    private Boolean diveEquipment;
    private Boolean license;
    private Boolean transportation;
    private Boolean accomodation;

    public Boolean getDiveGuide() {
        return diveGuide;
    }

    public void setDiveGuide(Boolean diveGuide) {
        this.diveGuide = diveGuide;
    }

    public Boolean getFood() {
        return food;
    }

    public void setFood(Boolean food) {
        this.food = food;
    }

    public Boolean getTowel() {
        return towel;
    }

    public void setTowel(Boolean towel) {
        this.towel = towel;
    }

    public Boolean getDiveEquipment() {
        return diveEquipment;
    }

    public void setDiveEquipment(Boolean diveEquipment) {
        this.diveEquipment = diveEquipment;
    }

    public Boolean getLicense() {
        return license;
    }

    public void setLicense(Boolean license) {
        this.license = license;
    }

    public Boolean getTransportation() {
        return transportation;
    }

    public void setTransportation(Boolean transportation) {
        this.transportation = transportation;
    }

    public Boolean getAccomodation() {
        return accomodation;
    }

    public void setAccomodation(Boolean accomodation) {
        this.accomodation = accomodation;
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

        try {
            if (!obj.isNull(KEY_ACCOMODATION)) {
                setAccomodation(obj.getBoolean(KEY_ACCOMODATION));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            obj.put(KEY_DIVE_GUIDE, getDiveGuide());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_FOOD, getFood());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_TOWEL, getTowel());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_DIVE_EQUIPMENT, getDiveEquipment());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_LICENSE, getLicense());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_TRANSPORTATION, getTransportation());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_ACCOMODATION, getAccomodation());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}