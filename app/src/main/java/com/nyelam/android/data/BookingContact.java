package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class BookingContact implements Parseable {

    private static String KEY_NAME = "name";
    private static String KEY_PHONE_NUMBER = "phone_number";
    private static String KEY_EMAIL = "email";
    private static String KEY_COUNTRY_CODE = "country_code";

    private String name;
    private String phoneNumber;
    private String email;
    private CountryCode countryCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(CountryCode countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_NAME)) {
                setName(obj.getString(KEY_NAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_PHONE_NUMBER)) {
                setPhoneNumber(obj.getString(KEY_PHONE_NUMBER));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_EMAIL)) {
                setEmail(obj.getString(KEY_EMAIL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_COUNTRY_CODE)) {
                JSONObject objUser = obj.getJSONObject(KEY_COUNTRY_CODE);
                CountryCode countryCode = new CountryCode();
                countryCode.parse(objUser);
                setCountryCode(countryCode);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String toServer() {
        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_NAME, getName());
            } else {
                obj.put(KEY_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getPhoneNumber())) {
                obj.put(KEY_PHONE_NUMBER, "+" + getCountryCode().getCountryNumber() + getPhoneNumber());
            } else {
                obj.put(KEY_PHONE_NUMBER, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getEmail())) {
                obj.put(KEY_EMAIL, getEmail());
            } else {
                obj.put(KEY_EMAIL, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}
        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}
        return super.toString();
    }
    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_NAME, getName());
            } else {
                obj.put(KEY_NAME, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getPhoneNumber())) {
                obj.put(KEY_PHONE_NUMBER, getPhoneNumber());
            } else {
                obj.put(KEY_PHONE_NUMBER, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getEmail())) {
                obj.put(KEY_EMAIL, getEmail());
            } else {
                obj.put(KEY_EMAIL, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (getCountryCode() != null) {
                JSONObject objCountryCode = new JSONObject(getCountryCode().toString());
                obj.put(KEY_COUNTRY_CODE, objCountryCode);
            } else {
                obj.put(KEY_COUNTRY_CODE, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }
}