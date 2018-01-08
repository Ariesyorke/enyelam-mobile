package com.nyelam.android.http.result;

import com.nyelam.android.data.NYAbstractList;
import com.nyelam.android.dev.NYLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public abstract class NYPaginationResult<T extends NYAbstractList> {

    private static final String KEY_NEXT = "next";

    public int next;
    public T item;
    private Class<T> clazz;

    public NYPaginationResult(Class clazz) {
        this.clazz = clazz;
    }

    public void parse(JSONObject obj) throws Exception {
        if (obj == null) {
            return;
        }

        if (!obj.isNull(KEY_NEXT)) {
            next = obj.getInt(KEY_NEXT);
        }

        NYLog.d("KEY : " + getListKey());
        if (!obj.isNull(getListKey())) {
            JSONArray array = obj.getJSONArray(getListKey());
            item = clazz.newInstance();
            item.parse(array);
        }
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            obj.put(KEY_NEXT, next);
        } catch (JSONException e) {
            e.printStackTrace();
            NYLog.e(e);
        }

        try {
            if (item != null && item.getList() != null && !item.getList().isEmpty()) {
                obj.put(getListKey(), item.toJSONArray());
            } else {
                obj.put(getListKey(), JSONObject.NULL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            NYLog.e(e);
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
            NYLog.e(e);
        }
        return super.toString();
    }

    protected abstract String getListKey();
}
