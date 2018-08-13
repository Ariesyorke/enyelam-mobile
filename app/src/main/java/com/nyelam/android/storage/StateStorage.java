package com.nyelam.android.storage;

import android.content.Context;

import com.danzoye.lib.util.AbstractStorage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class StateStorage extends AbstractStorage {

    private static final String FILENAME = "nyelam:storage:state";
    private static final String KEY_IS_FIRST = "is_first";

    public boolean isNotFirst;

    public StateStorage(Context context) {
        super(context);
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        if (!obj.isNull(KEY_IS_FIRST)) {
            isNotFirst = obj.getBoolean(KEY_IS_FIRST);
        }

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(KEY_IS_FIRST, isNotFirst);

        return obj;
    }
}

