package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class Variation implements Parseable {

    private static String KEY_ID = "id";
    private static String KEY_NAME = "name";
    private static String KEY_QTY = "qty";
    private static String KEY_NORMAL_PRICE = "normal_price";
    private static String KEY_SPECIAL_PRICE = "special_price";

    private String id;
    private String name;
    private int qty;
    private double normalPrice;
    private double specialPrice;

    public Variation(){}

    public Variation(String id, String name){
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(double normalPrice) {
        this.normalPrice = normalPrice;
    }

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
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
            if (!obj.isNull(KEY_QTY)) {
                setQty(obj.getInt(KEY_QTY));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NORMAL_PRICE)) {
                setNormalPrice(obj.getDouble(KEY_NORMAL_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SPECIAL_PRICE)) {
                setSpecialPrice(obj.getDouble(KEY_SPECIAL_PRICE));
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
            obj.put(KEY_QTY, getQty());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_NORMAL_PRICE, getNormalPrice());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_SPECIAL_PRICE, getSpecialPrice());
        } catch (JSONException e) {e.printStackTrace();}


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }


}