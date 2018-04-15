package com.nyelam.android.data;

import com.nyelam.android.helper.NYHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class Price implements Parseable {

    private static String KEY_LOWEST_PRICE = "lowest_price";
    private static String KEY_HIGHEST_PRICE = "highest_price";

    private Double lowestPrice;
    private Double highestPrice;

    public double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(double highestPrice) {
        this.highestPrice = highestPrice;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_LOWEST_PRICE)) {
                setLowestPrice(obj.getDouble(KEY_LOWEST_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_HIGHEST_PRICE)) {
                setHighestPrice(obj.getDouble(KEY_HIGHEST_PRICE));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (lowestPrice != null) {
                obj.put(KEY_LOWEST_PRICE, getLowestPrice());
            } else {
                obj.put(KEY_LOWEST_PRICE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (highestPrice != null) {
                obj.put(KEY_HIGHEST_PRICE, getHighestPrice());
            } else {
                obj.put(KEY_HIGHEST_PRICE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
