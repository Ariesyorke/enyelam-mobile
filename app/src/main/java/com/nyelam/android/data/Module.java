package com.nyelam.android.data;

import com.nyelam.android.dev.NYLog;

import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/14/2018.
 */

public abstract class Module implements Parseable {

    protected static String KEY_MODULES_NAME = "module_name";
    protected static String KEY_EVENTS = "events";
    protected static String KEY_DIVE_SERVICES = "dive_services";
    protected static String KEY_DIVE_SPOTS = "dive_spots";

    protected String name;
    protected List<Event> events;
    protected List<DiveService> diveServices;
    protected List<DiveSpot> diveSpots;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<DiveService> getDiveServices() {
        return diveServices;
    }

    public void setDiveServices(List<DiveService> diveServices) {
        this.diveServices = diveServices;
    }

    public List<DiveSpot> getDiveSpots() {
        return diveSpots;
    }

    public void setDiveSpots(List<DiveSpot> diveSpots) {
        this.diveSpots = diveSpots;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_MODULES_NAME)) {
                setName(obj.getString(KEY_MODULES_NAME));
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if(!obj.isNull(KEY_EVENTS)) {
                events = new ArrayList<>();
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
            if (getEvents() != null && !getEvents().isEmpty()) {
                JSONArray array = new JSONArray();
                for (Event e : getEvents() ) {
                    JSONObject o = new JSONObject(e.toString());
                    array.put(o);
                }
                obj.put(KEY_EVENTS, array);
            } else  {
                obj.put(KEY_EVENTS, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}


        //NYLog.e("MY NAME IS : "+getList().getClass().getName());

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
