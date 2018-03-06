package com.nyelam.android.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.danzoye.lib.util.*;
import com.nyelam.android.data.User;
import com.nyelam.android.dev.NYLog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class LoginStorage extends com.danzoye.lib.util.AbstractStorage {

    private static final String FILENAME = "nyelam:storage:login-user";
    private static final String KEY_USER = "user";
    private static final String KEY_NYELAM_TOKEN = "nyelam_token";
    private static final String KEY_PHOTO_PROFILE = "photo";
    private static final String KEY_COVER = "cover";

    public User user;
    public String nyelamToken;
    public String photo;
    public String cover;

    public boolean isUserLogin() {
        return user != null && !TextUtils.isEmpty(user.getUserId()) &&
                !TextUtils.isEmpty(nyelamToken);
    }

    public LoginStorage(Context context) {
        super(context);
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        if (!obj.isNull(KEY_USER)) {
            JSONObject cObj = obj.getJSONObject(KEY_USER);
            user = new User();
            user.parse(cObj);
        }

        if (!obj.isNull(KEY_NYELAM_TOKEN)) {
            nyelamToken = obj.getString(KEY_NYELAM_TOKEN);
        }

        if (!obj.isNull(KEY_PHOTO_PROFILE)) {
            photo = obj.getString(KEY_PHOTO_PROFILE);
        }

        if (!obj.isNull(KEY_COVER)) {
            cover = obj.getString(KEY_COVER);
        }
    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();
        if (user != null) {
            obj.put(KEY_USER, new JSONObject(user.toString()));
        }

        if (nyelamToken != null) {
            obj.put(KEY_NYELAM_TOKEN, nyelamToken);
        }

        if (photo != null) {
            obj.put(KEY_PHOTO_PROFILE, photo);
        }

        if (cover != null) {
            obj.put(KEY_COVER, cover);
        }

        return obj;
    }
}

