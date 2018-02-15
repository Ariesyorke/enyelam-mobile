package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/14/2018.
 */

public class ModuleService extends Module implements Parseable {


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
            if(!obj.isNull(KEY_DIVE_SERVICES)) {
                diveServices = new ArrayList<>();
                JSONArray array = obj.getJSONArray(KEY_DIVE_SERVICES);
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
                obj.put(KEY_DIVE_SERVICES, array);
            } else  {
                obj.put(KEY_DIVE_SERVICES, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}


        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();
        }

        return super.toString();
    }
}
