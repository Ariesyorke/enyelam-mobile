package com.nyelam.android.storage;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.data.Event;
import com.nyelam.android.data.Module;
import com.nyelam.android.data.ModuleDiveSpot;
import com.nyelam.android.data.ModuleEvent;
import com.nyelam.android.data.ModuleList;
import com.nyelam.android.data.ModuleService;
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

    public ModuleDiveSpot moduleDiveSpots;
    public ModuleEvent moduleEvents;
    public ModuleService moduleServices;

    public ModuleDiveSpot getModuleDiveSpots() {
        return moduleDiveSpots;
    }

    public void setModuleDiveSpots(ModuleDiveSpot moduleDiveSpots) {
        this.moduleDiveSpots = moduleDiveSpots;
    }

    public ModuleEvent getModuleEvents() {
        return moduleEvents;
    }

    public void setModuleEvents(ModuleEvent moduleEvents) {
        this.moduleEvents = moduleEvents;
    }

    public ModuleService getModuleServices() {
        return moduleServices;
    }

    public void setModuleServices(ModuleService moduleServices) {
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

        if (obj.has(KEY_MODULES) &&  obj.get(KEY_MODULES) instanceof JSONArray){

            JSONArray array = obj.getJSONArray(KEY_MODULES);

            ModuleList list = new ModuleList();
            list.parse(array);

            if (array.length() > 0){

                for(int i = 0; i < array.length(); i++) {
                    JSONObject o = array.getJSONObject(i);
                    String type = o.getString("module_name");
                    //Module result = null;

                    NYLog.e("CEK MODULE : "+i+" , "+o.toString());

                    if (type.equals("Event")) {

                        NYLog.e("CEK MODULE : event");

                        moduleEvents = new ModuleEvent();
                        moduleEvents.parse(o);
                    } else if (type.equals("Hot Offer")){

                        NYLog.e("CEK MODULE : hot offer");

                        moduleServices = new ModuleService();
                        moduleServices.parse(o);
                    } else if (type.equals("Popular Dive Spots")){

                        NYLog.e("CEK MODULE : dive spot");

                        moduleDiveSpots = new ModuleDiveSpot();
                        moduleDiveSpots.parse(o);
                    }

                }

            }

        }

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();

        try {
            if (moduleEvents != null) {
                JSONArray array = new JSONArray();
                for (Event a : getModuleEvents().getEvents()) {
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
                for (DiveService a : getModuleServices().getDiveServices()) {
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
                for (DiveSpot a : getModuleDiveSpots().getDiveSpots()) {
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