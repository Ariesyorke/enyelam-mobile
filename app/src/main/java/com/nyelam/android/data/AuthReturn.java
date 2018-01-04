package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class AuthReturn implements Parseable {

    private static String KEY_TOKEN = "token";
    private static String KEY_USER = "user";

    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_TOKEN)){
                setToken(obj.getString(KEY_TOKEN));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (!obj.isNull(KEY_USER)) {
                JSONObject objUser = obj.getJSONObject(KEY_USER);
                User user = new User();
                user.parse(objUser);
                setUser(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getToken())) {
                obj.put(KEY_TOKEN, getToken());
            } else {
                obj.put(KEY_TOKEN, JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if (getUser() != null) {
                JSONObject objUser = new JSONObject(getUser().toString());
                obj.put(KEY_USER, objUser);
            } else {
                obj.put(KEY_USER, JSONObject.NULL);
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