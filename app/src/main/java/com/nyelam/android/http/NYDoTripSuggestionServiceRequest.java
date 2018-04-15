package com.nyelam.android.http;

import android.content.Context;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DiveServiceList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NYDoTripSuggestionServiceRequest extends NYBasicRequest<DiveServiceList> {

    private static final String KEY_DIVE_SERVICE = "dive_services";
    private static final String POST_PAGE = "page";

    public NYDoTripSuggestionServiceRequest(Context context) {
        super(AuthReturn.class, context, context.getString(R.string.api_path_dodive_suggestion_service));

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DiveServiceList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_DIVE_SERVICE) instanceof JSONArray) {
            DiveServiceList serviceList = new DiveServiceList();
            serviceList.parse(obj.getJSONArray(KEY_DIVE_SERVICE));
            if (serviceList != null && serviceList.getList() != null && serviceList.getList().size() > 0) return serviceList;
        }

        return null;
    }


}
