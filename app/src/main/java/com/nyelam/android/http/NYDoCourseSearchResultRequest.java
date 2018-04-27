package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.StateFacility;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.result.NYPaginationResult;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NYDoCourseSearchResultRequest extends NYBasicRequest<NYPaginationResult<DiveServiceList>> {

    private static final String KEY_DIVE_SERVICE = "dive_services";

    private static final String POST_PAGE = "page";
    private static final String POST_DIVER = "diver";
    private static final String POST_ORGANIZATION_ID = "organization_id";
    private static final String POST_LICENSE_TYPE_ID = "license_type_id";
    private static final String POST_DATE = "date";
    private static final String POST_SORT_BY = "sort_by";
    private static final String POST_RATING = "rating";
    private static final String POST_OPEN_WATER = "open_water";
    private static final String POST_PRICE_MIN = "price_min";
    private static final String POST_PRICE_MAX = "price_max";
    private static final String POST_FACILITIES = "facilities[]";

    private static final String POST_DIVE_SPOT_ID = "dive_spot_id";
    private static final String POST_DIVE_CENTER_ID = "dive_center_id";
    private static final String POST_PROVINCE_ID = "province_id";
    private static final String POST_CITY_ID = "city_id";
    private static final String POST_CATEGORY_ID = "category_id";


    public NYDoCourseSearchResultRequest(Context context, String apiPath, String page, String diverId, String type, String diver, String certificate, String date, String sortingBy, List<Category> categories, List<StateFacility> facilityList, double priceMin, double priceMax, String organizationId, String licenseTypeId, String rating, String openWater ) {
        super(AuthReturn.class, context, apiPath);

        if(!TextUtils.isEmpty(diverId) && !TextUtils.isEmpty(type)) {
            if(type.equals("1")) {
                addQuery(POST_DIVE_SPOT_ID, diverId);
            } else if (!TextUtils.isEmpty(type) && type.equals("2")){
                addQuery(POST_CATEGORY_ID, diverId);
            } else if (!TextUtils.isEmpty(type) && type.equals("3")){
                addQuery(POST_DIVE_CENTER_ID, diverId);
            } else if (!TextUtils.isEmpty(type) && type.equals("5")){
                addQuery(POST_PROVINCE_ID, diverId);
            } else if (!TextUtils.isEmpty(type) && type.equals("6")){
                addQuery(POST_CITY_ID, diverId);
            }
        }

        if(!TextUtils.isEmpty(sortingBy)) {
            addQuery(POST_SORT_BY, sortingBy);
        }

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

        if(!TextUtils.isEmpty(organizationId)) {
            addQuery(POST_ORGANIZATION_ID, organizationId);
        }

        if(!TextUtils.isEmpty(licenseTypeId)) {
            addQuery(POST_LICENSE_TYPE_ID, licenseTypeId);
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        if(!TextUtils.isEmpty(date)) {
            addQuery(POST_DATE, date);
        }

        if(priceMin >= 0) {
            addQuery(POST_PRICE_MIN, String.valueOf(priceMin));
        }

        if(priceMin >= 0) {
            addQuery(POST_PRICE_MAX, String.valueOf(priceMax));
        }

        if (facilityList != null && facilityList.size() > 0){
            for (StateFacility fac : facilityList){
                if (fac != null && NYHelper.isStringNotEmpty(fac.getTag()))addQuery(POST_FACILITIES, fac.getTag());
            }
        }

        if(!TextUtils.isEmpty(rating)) {
            addQuery(POST_RATING, rating);
        }

        if(!TextUtils.isEmpty(openWater)) {
            addQuery(POST_OPEN_WATER, openWater);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected NYPaginationResult<DiveServiceList> onProcessSuccessData(JSONObject obj) throws Exception {
        NYPaginationResult<DiveServiceList> temp = new NYPaginationResult<DiveServiceList>(DiveServiceList.class) {
            @Override
            protected String getListKey() {
                return KEY_DIVE_SERVICE;
            }
        };
        temp.parse(obj);
        return temp;
    }


}
