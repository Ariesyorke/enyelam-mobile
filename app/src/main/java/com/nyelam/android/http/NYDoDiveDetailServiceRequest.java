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
    private static final String POST_DO_COURSE_ID = "do_course_id";
    private static final String POST_DIVER = "diver";
    private static final String POST_CERTIFICATE = "certificate";
    private static final String POST_DATE = "date";

    public NYDoDiveDetailServiceRequest(Context context, String serviceId, String diver, String certificate, String date) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_detail_service));

        if(!TextUtils.isEmpty(serviceId)) {
            addQuery(POST_SERVICE_ID, serviceId);
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        if(!TextUtils.isEmpty(certificate)) {
            addQuery(POST_CERTIFICATE, certificate);
        }

        if(!TextUtils.isEmpty(date)) {
            addQuery(POST_DATE, date);
        }
    }



    public NYDoDiveDetailServiceRequest(Context context, String apiPath, String serviceId, String diver, String certificate, String date) {
        super(AuthReturn.class, context, apiPath);

        if(!TextUtils.isEmpty(serviceId)) {
            addQuery(POST_DO_COURSE_ID, serviceId);
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        if(!TextUtils.isEmpty(certificate)) {
            addQuery(POST_CERTIFICATE, certificate);
        }

        if(!TextUtils.isEmpty(date)) {
            addQuery(POST_DATE, date);
        }
    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DiveService onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.get(KEY_SERVICE) instanceof JSONObject && obj.getJSONObject(KEY_SERVICE) != null){
            DiveService diveService = new DiveService();
            diveService.parse(obj.getJSONObject(KEY_SERVICE));
            return diveService;
        } else {
            return null;
        }

    }

}
