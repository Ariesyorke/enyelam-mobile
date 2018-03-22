package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class Participant implements Parseable {

    private static String KEY_NAME = "name";
    private static String KEY_EMAIL_ADDRESS = "email_address";
    private static String KEY_EMAIL = "email";

    private String name;
    private String email;


    public Participant(){}

    public Participant(String name, String email){
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
            if (!obj.isNull(KEY_EMAIL)) {
                setEmail(obj.getString(KEY_EMAIL));
            } else if (!obj.isNull(KEY_EMAIL_ADDRESS)) {
                setEmail(obj.getString(KEY_EMAIL_ADDRESS));
            }
        } catch (JSONException e) {e.printStackTrace();}
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
            if (!TextUtils.isEmpty(getEmail())) {
                obj.put(KEY_EMAIL_ADDRESS, getEmail());
            } else {
                obj.put(KEY_EMAIL_ADDRESS, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}