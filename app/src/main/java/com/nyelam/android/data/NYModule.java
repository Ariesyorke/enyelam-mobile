package com.nyelam.android.data;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 3/4/2018.
 */

public abstract class NYModule implements Parseable {
    private static final String KEY_MODULE_NAME = "module_name";
    private static final String KEY_MODULE_TYPE = "module_type";

    private String moduleName;
    private String moduleType;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    @Override
    public void parse(JSONObject obj) {
        try {
            if (!obj.isNull(KEY_MODULE_NAME)) {
                setModuleName(obj.getString(KEY_MODULE_NAME));
            }
            if (!obj.isNull(KEY_MODULE_TYPE)) {
                setModuleType(obj.getString(KEY_MODULE_TYPE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}