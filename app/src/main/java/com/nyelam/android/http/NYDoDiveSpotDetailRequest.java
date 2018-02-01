package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.DiveCenter;
import com.nyelam.android.data.DiveSpot;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYDoDiveSpotDetailRequest extends NYBasicRequest<DiveSpot> {

    private static String KEY_DIVE_SPOT = "divespot";

    private static String POST_ID_DIVE_SPOT = "dive_spot_id";

    public NYDoDiveSpotDetailRequest(Class clazz, Context context, String idDiveSpot) {
        super(clazz, context, context.getResources().getString(R.string.api_path_dodive_dive_spot_detail));

        if (NYHelper.isStringNotEmpty(idDiveSpot)){
            addQuery(POST_ID_DIVE_SPOT, idDiveSpot);
        }

    }

    @Override
    protected DiveSpot onProcessSuccessData(JSONObject obj) throws Exception {

        if (obj.has(KEY_DIVE_SPOT) && obj.get(KEY_DIVE_SPOT) instanceof JSONObject && obj.getJSONObject(KEY_DIVE_SPOT) != null){
            DiveSpot dc = new DiveSpot();
            dc.parse(obj.getJSONObject(KEY_DIVE_SPOT));
            return dc;
        } else {
            return null;
        }

    }


}
