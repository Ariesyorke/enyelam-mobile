package com.danzoye.lib.http;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Ramdhany Dwi Nugroho on Feb 2015.
 */
public abstract class DJSONRequest<RESULT> extends DBaseRequest<RESULT> {
    private JSONObject obj;

    public DJSONRequest(final Class clazz, String url) {
        super(clazz, url);
    }

    public DJSONRequest(final Class clazz, String url, JSONObject obj) {
        super(clazz, url);
        this.obj = obj;
    }

    @Override
    public void addMultiPartFile(String param, File file) {
        Log.w("danzoye", "DJSONRequest not supporting multipart post!");
    }

    public void setJSONObject(JSONObject obj) {
        this.obj = obj;
    }

    JSONObject getJSON() {
        return obj;
    }
}
