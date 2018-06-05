package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.nyelam.android.R;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.Price;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/24/2018.
 */

public class NYGetEcoTripDateRequest extends NYBasicRequest<List<Long>> {

    private static final String KEY_DATE = "date";
    private static final String POST_TYPE = "type";

    public NYGetEcoTripDateRequest(Context context) throws Exception {
        super(String.class, context, context.getResources().getString(R.string.api_path_get_eco_trip_date));
//        if(!TextUtils.isEmpty(type)) {
//            addQuery(POST_TYPE, type);
//        }
    }

    @Override
    protected List<Long> onProcessSuccessData(JSONObject obj) throws Exception {
        List<Long> result = new ArrayList<>();

        if (obj.has(KEY_DATE) && obj.getJSONArray(KEY_DATE) != null){

            JSONArray array = obj.getJSONArray(KEY_DATE);
            for (int i=0;i<array.length();i++){
                result.add(array.getLong(i));
            }

        }
        return result;
    }
}
