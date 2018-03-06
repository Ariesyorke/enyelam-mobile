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
    private ModuleList moduleList;

    public ModulHomepageStorage(Context context) {
        super(context);
    }

    public void setModuleList(ModuleList moduleList) {
        this.moduleList = moduleList;
    }

    public ModuleList getModuleList() {
        return moduleList;
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        try {
            if (!obj.isNull(KEY_MODULES)) {
                JSONArray array = obj.getJSONArray(KEY_MODULES);
                moduleList = new ModuleList();
                moduleList.parse(array);
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();
        try {
            if(moduleList != null && moduleList.getList() != null && !moduleList.getList().isEmpty()) {
                obj.put(KEY_MODULES, moduleList.toJSONArray());
            }
        } catch (JSONException e){e.printStackTrace();}

        return obj;
    }
}