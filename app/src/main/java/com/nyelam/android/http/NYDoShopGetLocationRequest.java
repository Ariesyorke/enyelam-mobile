package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AreaList;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.ParticipantList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoShopGetLocationRequest extends NYBasicAuthRequest<AreaList> {

    private static final String KEY_AREAS = "areas";

    private final static String POST_PROVINCE_ID = "province_id";
    private final static String POST_CITY_ID = "city_id";

    public NYDoShopGetLocationRequest(Context context, String provinceId, String cityId) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_doshop_get_location));

        if(!TextUtils.isEmpty(provinceId)) {
            addQuery(POST_PROVINCE_ID, provinceId);
        }

        if(!TextUtils.isEmpty(cityId)) {
            addQuery(POST_CITY_ID, cityId);
        }
    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected AreaList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_AREAS) instanceof JSONArray) {
            AreaList areaList = new AreaList();
            areaList.parse(obj.getJSONArray(KEY_AREAS));
            if (areaList != null && areaList.getList() != null && areaList.getList().size() > 0) return areaList;
        }

        return null;
    }

}

