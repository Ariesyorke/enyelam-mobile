package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DiveSpotList;
import com.nyelam.android.data.Module;
import com.nyelam.android.data.ModuleList;
import com.nyelam.android.data.SearchResultList;
import com.nyelam.android.dev.NYLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NYHomepageModuleRequest extends NYBasicRequest<ModuleList> {

    private static final String KEY_MODULES= "modules";

    public NYHomepageModuleRequest(Context context) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_homepage_modules));
    }


    @Override
    protected ModuleList onProcessSuccessData(JSONObject obj) throws Exception {

        if  (obj.get(KEY_MODULES) != null && obj.get(KEY_MODULES) instanceof JSONArray && obj.getJSONArray(KEY_MODULES).length() > 0){
            ModuleList moduleList = new ModuleList();
            moduleList.parse(obj.getJSONArray(KEY_MODULES));
            return moduleList;
        }

        return null;
    }
}
