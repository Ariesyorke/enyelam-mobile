package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/14/2018.
 */

public class ModuleService extends Module implements Parseable {
    private static final String KEY_TRIPS = "trips";
    protected List<DiveService> diveServices;


    public List<DiveService> getDiveServices() {
        return diveServices;
    }

    public void setDiveServices(List<DiveService> diveServices) {
        this.diveServices = diveServices;
    }

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
            if(!obj.isNull(KEY_TRIPS)) {
                diveServices = new ArrayList<>();
                JSONArray array = obj.getJSONArray(KEY_TRIPS);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    DiveService diveService = new DiveService();
                    diveService.parse(o);
                    diveServices.add(diveService);
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

        try {
            if (getDiveServices() != null && !getDiveServices().isEmpty()) {
                JSONArray array = new JSONArray();
                for (DiveService a : getDiveServices() ) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_TRIPS, array);
            } else  {
                obj.put(KEY_TRIPS, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();
        }

        return super.toString();
    }
}
