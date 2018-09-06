package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DiveGuide;
import com.nyelam.android.data.DiveGuideList;
import com.nyelam.android.data.ReviewList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDiveGuideListRequest extends NYBasicRequest<DiveGuideList> {

    private static final String KEY_DIVE_GUIDES = "dive_guides";

    private static final String POST_DIVE_CENTER_ID = "dive_center_id";


    public NYDiveGuideListRequest(Context context, String diveCenterId){
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dive_guide_list));

        if(!TextUtils.isEmpty(diveCenterId)) {
            addQuery(POST_DIVE_CENTER_ID, diveCenterId);
        }

    }

    @Override
    protected DiveGuideList onProcessSuccessData(JSONObject obj) throws Exception {
        DiveGuideList diveGuideList = new DiveGuideList();
        diveGuideList.parse(obj.getJSONArray(KEY_DIVE_GUIDES));
        return diveGuideList;
    }

}

