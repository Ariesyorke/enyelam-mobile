package com.danzoye.lib.auth.google;

import android.text.TextUtils;

import com.danzoye.lib.auth.AuthResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class GPlusAuthResult extends AuthResult {
    private static final String KEY_AUTH_CODE = "auth.result.key.GOOGLE_AUTH_CODE";
    public String authCode;

    @Override
    public void parse(JSONObject obj) {
        super.parse(obj);
        if(!obj.isNull(KEY_AUTH_CODE)) {
            try {
                authCode = obj.getString(KEY_AUTH_CODE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String toString() {
        try {
            JSONObject obj = new JSONObject(super.toString());

            if(!TextUtils.isEmpty(authCode)) {
                obj.put(KEY_AUTH_CODE, authCode);
            }
            return obj.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }

}
