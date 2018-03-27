package com.midtrans.sdk.uikit.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ramdhany Dwi Nugroho on Feb 2015.
 */
public abstract class AbstractStorage {
    private Context context;

    public AbstractStorage(Context context) {
        this.context = context;
        int mode = Context.MODE_PRIVATE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mode = Context.MODE_MULTI_PROCESS;
        }
        SharedPreferences sp = context.getSharedPreferences(getStorageKey(),
                mode);
        String json = sp.getString("data", null);

        if (json != null) {
            // Log.d("danzoye", "storage on load = " + json);
        } else {
            // Log.d("danzoye", "storage on load = null");
        }

        if (json != null) {
            try {
                onParseData(new JSONObject(json));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean clear() {
        int mode = Context.MODE_PRIVATE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mode = Context.MODE_MULTI_PROCESS;
        }
        return context.getSharedPreferences(getStorageKey(), mode).edit().clear().commit();
    }

    public Context getContext() {
        return context;
    }

    public boolean save() {
        int mode = Context.MODE_PRIVATE;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mode = Context.MODE_MULTI_PROCESS;
        }
        SharedPreferences.Editor edit = context.getSharedPreferences(
                getStorageKey(), mode).edit();
        try {
            String json = onSaveData().toString();
            // Log.d("danzoye", "storage on save = " + json);
            edit.putString("data", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return edit.commit();
    }

    protected abstract String getStorageKey();

    protected abstract void onParseData(JSONObject obj) throws JSONException;

    protected abstract JSONObject onSaveData() throws JSONException;
}
