package com.danzoye.lib.auth.facebook;

import android.util.Log;

import com.danzoye.lib.auth.AuthResult;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class FBAuthResult extends AuthResult {
    private static final String KEY_LOCATION_ID = "facebook.result.key.LOCATION_ID";
    public String locationId;

    @Override
    public void parse(JSONObject obj) {
        super.parse(obj);
        if (obj == null) {
            return;
        }

        try {
            if (!obj.isNull(KEY_LOCATION_ID)) {
                locationId = obj.getString(KEY_LOCATION_ID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }
    }

    @Override
    public String toString() {
        JSONObject obj = null;
        try {
            obj = new JSONObject(super.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.e("danzoye", e.toString());
        }

        try {
            if (locationId != null) {
                obj.put(KEY_LOCATION_ID, locationId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("danzoye", e.toString());
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("danzoye", e.toString());
        }

        return super.toString();
    }
}
