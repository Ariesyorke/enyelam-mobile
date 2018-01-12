package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DiveService;
import com.nyelam.android.data.DiveServiceList;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NYDoDiveDetailServiceRequest extends NYBasicRequest<DiveService> {

    private static final String KEY_SERVICE = "service";
    private static final String POST_SERVICE_ID = "service_id";

    public NYDoDiveDetailServiceRequest(Context context, String serviceId) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_detail_service));

        if(!TextUtils.isEmpty(serviceId)) {
            addQuery(POST_SERVICE_ID, serviceId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DiveService onProcessSuccessData(JSONObject obj) throws Exception {
        DiveService diveService = new DiveService();
        diveService.parse(obj.getJSONObject(KEY_SERVICE));
        return diveService;
    }


}
