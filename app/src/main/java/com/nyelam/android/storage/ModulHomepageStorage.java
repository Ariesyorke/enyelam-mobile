package com.nyelam.android.storage;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.Additional;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.Module;
import com.nyelam.android.data.ModuleDiveSpot;
import com.nyelam.android.data.ModuleEvent;
import com.nyelam.android.data.ModuleList;
import com.nyelam.android.data.ModuleService;
import com.nyelam.android.data.User;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dantech on 8/11/2017.
 */

public class ModulHomepageStorage extends AbstractStorage {
    private static final String FILENAME = "nyelam:storage:modul-homepage";
    private static final String KEY_MODULES = "modules";
    private static final String KEY_MODULE_HOMEPAGE = "module_homepage";
    private static final String KEY_MODULE_DIVE_SPOT = "Popular Dive Spot";
    private static final String KEY_MODULE_EVENT = "Events";
    private static final String KEY_MODULE_SERVICE = "Hot Offer";

    public List<DiveSpot> moduleDiveSpots;
    public List<Event> moduleEvents;
    public List<DiveService> moduleServices;

    public List<DiveSpot> getModuleDiveSpots() {
        return moduleDiveSpots;
    }

    public void setModuleDiveSpots(List<DiveSpot> moduleDiveSpots) {
        this.moduleDiveSpots = moduleDiveSpots;
    }

    public List<Event> getModuleEvents() {
        return moduleEvents;
    }

    public void setModuleEvents(List<Event> moduleEvents) {
        this.moduleEvents = moduleEvents;
    }

    public List<DiveService> getModuleServices() {
        return moduleServices;
    }

    public void setModuleServices(List<DiveService> moduleServices) {
        this.moduleServices = moduleServices;
    }

    public ModulHomepageStorage(Context context) {
        super(context);
    }


    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        try {
            if (!obj.isNull(KEY_MODULE_EVENT)) {
                JSONArray array = obj.getJSONArray(KEY_MODULE_EVENT);
                if (array != null && array.length() > 0) {
                    moduleEvents = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        Event event = new Event();
                        event.parse(o);
                        moduleEvents.add(event);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_MODULE_SERVICE)) {
                JSONArray array = obj.getJSONArray(KEY_MODULE_SERVICE);
                if (array != null && array.length() > 0) {
                    moduleServices = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        DiveService service = new DiveService();
                        service.parse(o);
                        moduleServices.add(service);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_MODULE_DIVE_SPOT)) {
                JSONArray array = obj.getJSONArray(KEY_MODULE_DIVE_SPOT);
                if (array != null && array.length() > 0) {
                    moduleDiveSpots = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject o = array.getJSONObject(i);
                        DiveSpot diveSpot = new DiveSpot();
                        diveSpot.parse(o);
                        moduleDiveSpots.add(diveSpot);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();

        try {
            if (moduleEvents != null) {
                JSONArray array = new JSONArray();
                for (Event a : getModuleEvents()) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_MODULE_EVENT, array);
            } else  {
                obj.put(KEY_MODULE_EVENT, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}

        try {
            if (moduleServices != null) {
                JSONArray array = new JSONArray();
                for (DiveService a : getModuleServices()) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_MODULE_SERVICE, array);
            } else  {
                obj.put(KEY_MODULE_SERVICE, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}


        try {
            if (moduleDiveSpots != null) {
                JSONArray array = new JSONArray();
                for (DiveSpot a : getModuleDiveSpots()) {
                    JSONObject o = new JSONObject(a.toString());
                    array.put(o);
                }
                obj.put(KEY_MODULE_DIVE_SPOT, array);
            } else  {
                obj.put(KEY_MODULE_DIVE_SPOT, JSONObject.NULL);
            }
        } catch (JSONException e){e.printStackTrace();}


        return obj;
    }
}