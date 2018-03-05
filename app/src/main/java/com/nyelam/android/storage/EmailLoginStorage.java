package com.nyelam.android.storage;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class EmailLoginStorage extends AbstractStorage {

    private static final String FILENAME = "nyelam:storage:login-email";
    private static final String KEY_EMAIL = "email";

    public String email;

    public EmailLoginStorage(Context context) {
        super(context);
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        if (!obj.isNull(KEY_EMAIL)) {
            email = obj.getString(KEY_EMAIL);
        }

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();

        if (email != null) {
            obj.put(KEY_EMAIL, email);
        }
        return obj;
    }
}

