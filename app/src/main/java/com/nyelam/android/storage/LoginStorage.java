package com.nyelam.android.storage;

import android.content.Context;
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

    public User user;
    public String nyelamToken;

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
        return obj;
    }
}

