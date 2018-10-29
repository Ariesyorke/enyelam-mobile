package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class ReviewUser implements Parseable {

    private static String KEY_USER_ID = "user_id";
    private static String KEY_FULLNAME = "fullname";
    private static String KEY_PICTURE = "picture";

    private String userId;
    private String fullname;
    private String picture;

    public ReviewUser(){}

    public ReviewUser(String userId, String fullname, String picture){
        this.userId = userId;
        this.fullname = fullname;
        this.picture = picture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_USER_ID)) {
                setUserId(obj.getString(KEY_USER_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_FULLNAME)) {
                setFullname(obj.getString(KEY_FULLNAME));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_PICTURE)) {
                setPicture(obj.getString(KEY_PICTURE));
            }
        } catch (JSONException e) {e.printStackTrace();}
    }

}