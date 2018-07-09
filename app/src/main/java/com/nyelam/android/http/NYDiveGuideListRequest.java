package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DiveGuideList;
import com.nyelam.android.data.ReviewList;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDiveGuideListRequest extends NYBasicAuthRequest<DiveGuideList> {

    private static final String KEY_DIVE_GUIDES = "dive_guides";

    private static final String POST_DIVE_CENTER_ID = "dive_center_id";


    public NYDiveGuideListRequest(Context context, String diveCenterId) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dive_guide_list));

        if(!TextUtils.isEmpty(diveCenterId)) {
            addQuery(POST_DIVE_CENTER_ID, diveCenterId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DiveGuideList onProcessSuccessData(JSONObject obj) throws Exception {

        DiveGuideList diveGuideList = new DiveGuideList();
        diveGuideList.parse(obj.getJSONArray(KEY_DIVE_GUIDES));
        return diveGuideList;
    }

}

