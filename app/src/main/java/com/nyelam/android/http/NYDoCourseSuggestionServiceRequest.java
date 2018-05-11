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

public class NYDoCourseSuggestionServiceRequest extends NYBasicRequest<DiveServiceList> {

    private static final String KEY_COURSE = "course";
    private static final String POST_PAGE = "page";

    public NYDoCourseSuggestionServiceRequest(Context context) {
        super(AuthReturn.class, context, context.getString(R.string.api_path_docourse_suggestion_service));

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DiveServiceList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_COURSE) instanceof JSONArray) {
            DiveServiceList serviceList = new DiveServiceList();
            serviceList.parse(obj.getJSONArray(KEY_COURSE));
            if (serviceList != null && serviceList.getList() != null && serviceList.getList().size() > 0) return serviceList;
        }

        return null;
    }

}
