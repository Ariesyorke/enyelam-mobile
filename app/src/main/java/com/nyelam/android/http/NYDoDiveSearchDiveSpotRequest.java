package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DiveCenterList;
import com.nyelam.android.data.DiveSpotList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NYDoDiveSearchDiveSpotRequest extends NYBasicRequest<DiveSpotList> {

    private static final String KEY_DIVE_SPOTS = "dive_spots";

    private static final String POST_PAGE = "page";
    private static final String POST_CITY_ID = "city_id";
    private static final String POST_PROVINCE_ID = "province_id";

    private static final String POST_CERTIFICATE = "certificate";
    private static final String POST_DIVER = "diver";
    private static final String POST_DATE = "date";
    private static final String POST_DIVE_SPOT_ID = "dive_spot_id";
    private static final String POST_DIVE_CATEGORY_ID = "dive_category_id";

    public NYDoDiveSearchDiveSpotRequest(Context context, String apiPath, String page, String diverId, String type) {
        super(AuthReturn.class, context, apiPath);

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

        if(!TextUtils.isEmpty(type) && type.equals("5")) {
            addQuery(POST_PROVINCE_ID, diverId);
        } else {
            addQuery(POST_CITY_ID, diverId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DiveSpotList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.has(KEY_DIVE_SPOTS) && obj.get(KEY_DIVE_SPOTS) instanceof JSONArray) {
            DiveSpotList diveSpotList = new DiveSpotList();
            diveSpotList.parse(obj.getJSONArray(KEY_DIVE_SPOTS));
            return diveSpotList;
        }

        return null;
    }


}
