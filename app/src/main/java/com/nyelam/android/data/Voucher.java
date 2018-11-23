package com.nyelam.android.data;


import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class Voucher implements Parseable {

    private static String KEY_TYPE = "type";
    private static String KEY_COUPON_TYPE = "coupon_type";
    private static String KEY_CODE = "code";
    private static String KEY_VALUE = "value";

    private String type;
    private String code;
    private double value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
            if (!obj.isNull(KEY_TYPE)) {
                setType(obj.getString(KEY_TYPE));
            } else if (!obj.isNull(KEY_COUPON_TYPE)) {
                setType(obj.getString(KEY_COUPON_TYPE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_CODE)) {
                setCode(obj.getString(KEY_CODE));
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
            if (!TextUtils.isEmpty(getType())) {
                obj.put(KEY_TYPE, getType());
            } else {
                obj.put(KEY_TYPE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getCode())) {
                obj.put(KEY_CODE, getCode());
            } else {
                obj.put(KEY_CODE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            obj.put(KEY_VALUE, getValue());
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}


        return super.toString();
    }

}
