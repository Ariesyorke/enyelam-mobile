package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ListView;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DiveCenterList;
import com.nyelam.android.data.DiveServiceList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NYDoDiveSearchDiveCenterRequest extends NYBasicRequest<DiveCenterList> {

    private static final String KEY_DIVE_CENTERS = "dive_centers";

    private static final String POST_PAGE = "page";
    private static final String POST_CERTIFICATE = "certificate";
    private static final String POST_DIVER = "diver";
    private static final String POST_DATE = "date";

    private static final String POST_DIVE_SPOT_ID = "dive_spot_id";
    private static final String POST_DIVE_CATEGORY_ID = "dive_category_id";
    //private static final String POST_DIVE_CENTER_ID = "dive_center_id";
    //private static final String POST_COUNTRY_ID = "country_id";
    private static final String POST_PROVINCE_ID = "province_id";
    private static final String POST_CITY_ID = "city_id";
    private static final String POST_SORT_BY = "sort_by";
    private static final String POST_CATEGORIES = "dive_category_id[]";

    public NYDoDiveSearchDiveCenterRequest(Context context, String apiPath, String page, String diverId, String type, String certificate, String diver, String date, String sortBy, List<String> categories) {
        super(AuthReturn.class, context, apiPath);

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

        if (categories != null && categories.size() > 0){
            for (String st : categories){
                addQuery(POST_CATEGORIES, st);
            }
        }

        if(!TextUtils.isEmpty(type) && !TextUtils.isEmpty(diverId)) {
            if(type.equals("1")) {
                addQuery(POST_DIVE_SPOT_ID, diverId);
            } else if(type.equals("2")) {
                addQuery(POST_DIVE_CATEGORY_ID, diverId);
            } else if(type.equals("5")) {
                addQuery(POST_PROVINCE_ID, diverId);
            } else if(type.equals("6")) {
                addQuery(POST_CITY_ID, diverId);
            }
        }

        if(!TextUtils.isEmpty(certificate)) {
            addQuery(POST_CERTIFICATE, certificate);
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        if(!TextUtils.isEmpty(date)) {
            addQuery(POST_DATE, date);
        }

        if(!TextUtils.isEmpty(sortBy)) {
            addQuery(POST_SORT_BY, sortBy);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DiveCenterList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.has(KEY_DIVE_CENTERS) && obj.get(KEY_DIVE_CENTERS) instanceof JSONArray) {
            DiveCenterList diveCenterList = new DiveCenterList();
            diveCenterList.parse(obj.getJSONArray(KEY_DIVE_CENTERS));
            return diveCenterList;
        }

        return null;
    }


}
