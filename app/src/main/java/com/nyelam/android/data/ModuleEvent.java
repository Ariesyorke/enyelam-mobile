package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/14/2018.
 */

public class ModuleEvent extends Module implements Parseable {


    @Override
    public void parse(JSONObject obj) {
        super.parse(obj);

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_MODULES_NAME)) {
                setName(obj.getString(KEY_MODULES_NAME));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if(!obj.isNull(KEY_EVENTS)) {
                diveServices = new ArrayList<>();
                JSONArray array = obj.getJSONArray(KEY_EVENTS);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    Event event = new Event();
                    event.parse(o);
                    events.add(event);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getName())) {
                obj.put(KEY_MODULES_NAME, getName());
            } else  {
                obj.put(KEY_MODULES_NAME, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        //NYLog.e("MY NAME IS : "+getList().getClass().getName());

        try {
            if (getEvents() != null && !getEvents().isEmpty()) {
                JSONArray array = new JSONArray();
                for (Event a : getEvents() ) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_EVENTS, array);
            } else  {
                obj.put(KEY_EVENTS, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();
        }

        return super.toString();
    }
}
