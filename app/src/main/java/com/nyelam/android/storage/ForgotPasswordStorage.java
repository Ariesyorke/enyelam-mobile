package com.nyelam.android.storage;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.util.AbstractStorage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dantech on 8/11/2017.
 */

public class ForgotPasswordStorage extends AbstractStorage {
    private static final String FILENAME = "kleen:storage:forgot-password";
    private static final String KEY_FORGOT = "forgot";
    private static final String KEY_EMAIL = "email";

    public boolean forgot;
    public String email;

    public boolean isForgot() {
        return forgot;
    }

    public void setForgot(boolean forgot) {
        this.forgot = forgot;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ForgotPasswordStorage(Context context) {
        super(context);
    }


    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {
        if (!obj.isNull(KEY_FORGOT)){
            setForgot(obj.getBoolean(KEY_FORGOT));
        }

        if (!obj.isNull(KEY_EMAIL)){
            setEmail(obj.getString(KEY_EMAIL));
        }
    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(KEY_FORGOT, isForgot());
        if (!TextUtils.isEmpty(getEmail())){
            obj.put(KEY_EMAIL, getEmail());
        }

        return obj;
    }
}