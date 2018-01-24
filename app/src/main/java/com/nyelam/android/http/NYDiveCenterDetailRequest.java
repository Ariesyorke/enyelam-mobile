package com.nyelam.android.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYDiveCenterDetailRequest extends NYBasicRequest<DiveCenter> {

    private static String KEY_DIVE_CENTER = "dive_center";
    private static String POST_ID_DIVE_CENTER = "dive_center_id";

    public NYDiveCenterDetailRequest(Class clazz, Context context, String idDiveCenter) {
        super(clazz, context, context.getResources().getString(R.string.api_path_dodive_dive_center_detail));

        if (NYHelper.isStringNotEmpty(idDiveCenter)){
            addQuery(POST_ID_DIVE_CENTER, idDiveCenter);
        }

    }

    @Override
    protected DiveCenter onProcessSuccessData(JSONObject obj) throws Exception {
        DiveCenter dc = new DiveCenter();
        dc.parse(obj.getJSONObject(KEY_DIVE_CENTER));
        return dc;
    }


}
