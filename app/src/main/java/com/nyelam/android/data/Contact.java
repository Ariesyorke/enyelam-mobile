package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/24/2018.
 */

public class Contact implements Parseable {

    private static String KEY_EMAIL_ADDRESS = "email_address";
    private static String KEY_PHONE_NUMBER = "phone_number";
    private static String KEY_COUNTRY_CODE = "country_code";
    private static String KEY_LOCATION = "location";

    private String emailAddress;
    private String phoneNumber;
    private String countryCode;
    private Location location;

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_EMAIL_ADDRESS)) {
                setEmailAddress(obj.getString(KEY_EMAIL_ADDRESS));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_PHONE_NUMBER)) {
                setPhoneNumber(obj.getString(KEY_PHONE_NUMBER));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_COUNTRY_CODE)) {
                setCountryCode(obj.getString(KEY_COUNTRY_CODE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_LOCATION)) {
                Location location = new Location();
                location.parse(obj.getJSONObject(KEY_LOCATION));
                setLocation(location);
            }
        } catch (JSONException e) {e.printStackTrace();}
    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getEmailAddress())) {
                obj.put(KEY_EMAIL_ADDRESS, getEmailAddress());
            } else {
                obj.put(KEY_EMAIL_ADDRESS, JSONObject.NULL);
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
            if (!TextUtils.isEmpty(getCountryCode())) {
                obj.put(KEY_COUNTRY_CODE, getCountryCode());
            } else {
                obj.put(KEY_COUNTRY_CODE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try{
            if(getLocation()!=null){
                obj.put(KEY_LOCATION, getLocation());
            } else {
                obj.put(KEY_LOCATION, JSONObject.NULL);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}