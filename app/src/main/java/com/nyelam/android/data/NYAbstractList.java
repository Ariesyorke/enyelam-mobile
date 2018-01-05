package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public abstract class NYAbstractList <T extends Parseable> {
    private List<T> list;

    public void setList(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public abstract Class<T> getHandledClass();

    public void parse(JSONArray array) throws JSONException {
        if (array == null) return;

        list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject obj = array.getJSONObject(i);
                T temp = getHandledClass().newInstance();
                temp.parse(obj);
                list.add(temp);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray toJSONArray() throws JSONException {
        JSONArray array = new JSONArray();
        if (list != null && !list.isEmpty()) {
            for (T item : list) {
                JSONObject o = new JSONObject(item.toString());
                array.put(o);
            }
        }
        return array;
    }
}