package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveGuide;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYDiveGuideDetailRequest extends NYBasicRequest<DiveGuide> {

    private static String KEY_DIVE_GUIDE = "dive_guide";
    private static String POST_DIVE_GUIDE_ID = "dive_guide_id";

    public NYDiveGuideDetailRequest(Class clazz, Context context, String diveGuideId) {
        super(clazz, context, context.getResources().getString(R.string.api_path_dive_guide_detail));

        if (NYHelper.isStringNotEmpty(diveGuideId)){
            addQuery(POST_DIVE_GUIDE_ID, diveGuideId);
        }

    }

    @Override
    protected DiveGuide onProcessSuccessData(JSONObject obj) throws Exception {
        DiveGuide diveGuide = new DiveGuide();
        diveGuide.parse(obj.getJSONObject(KEY_DIVE_GUIDE));
        return diveGuide;
    }


}
