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

public class ModuleDiveSpot extends Module implements Parseable {
    protected List<DiveSpot> diveSpots;


    public List<DiveSpot> getDiveSpots() {
        return diveSpots;
    }

    public void setDiveSpots(List<DiveSpot> diveSpots) {
        this.diveSpots = diveSpots;
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
            if(!obj.isNull(KEY_DIVE_SPOTS)) {
                diveSpots = new ArrayList<>();
                JSONArray array = obj.getJSONArray(KEY_DIVE_SPOTS);
                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    DiveSpot diveSpot = new DiveSpot();
                    diveSpot.parse(o);
                    diveSpots.add(diveSpot);
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
            if (getDiveSpots() != null && !getDiveSpots().isEmpty()) {
                JSONArray array = new JSONArray();
                for (DiveSpot a : getDiveSpots() ) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_DIVE_SPOTS, array);
            } else  {
                obj.put(KEY_DIVE_SPOTS, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();
        }

        return super.toString();
    }
}
