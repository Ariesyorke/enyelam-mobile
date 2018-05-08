package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.DiveServiceList;
import com.nyelam.android.data.StateFacility;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.result.NYPaginationResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NYDoTripShowAllServiceRequest extends NYBasicRequest<NYPaginationResult<DiveServiceList>> {

    private static final String KEY_DIVE_SERVICE = "trips";

    private static final String POST_PAGE = "page";
    private static final String POST_DIVER = "diver";
    private static final String POST_CERTIFICATE = "certificate";
    private static final String POST_DATE = "date";
    private static final String POST_SORT_BY = "sort_by";
    private static final String POST_RATING = "rating";
    private static final String POST_DIVE_CATEGORY_ID = "dive_category_id[]";
    private static final String POST_PRICE_MIN = "price_min";
    private static final String POST_PRICE_MAX = "price_max";
    private static final String POST_TOTAL_DIVES = "total_dives[]";
    private static final String POST_FACILITIES = "facilities[]";
    private static final String POST_ECO_TRIP = "eco_trip";

    public NYDoTripShowAllServiceRequest(Context context, String page, String diver, String certificate, String date, String sortingBy, List<Category> categories, List<StateFacility> facilityList, List<String> totalDives, double priceMin, double priceMax,  String rating,  String eco_trip) {
        super(AuthReturn.class, context, context.getString(R.string.api_path_dotrip_show_all_service));

        if(!TextUtils.isEmpty(sortingBy)) {
            addQuery(POST_SORT_BY, sortingBy);
        }

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

        /*if(!TextUtils.isEmpty(certificate)) {
            addQuery(POST_CERTIFICATE, certificate);
        }*/

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        if(!TextUtils.isEmpty(date)) {
            addQuery(POST_DATE, date);
        }

        if(priceMin >= 0) {
            addQuery(POST_PRICE_MIN, String.valueOf(priceMin));
        }

        if(priceMax >= 0) {
            addQuery(POST_PRICE_MAX, String.valueOf(priceMax));
        }

        if (categories != null && categories.size() > 0){
            for (Category cat : categories){
                if (cat != null && NYHelper.isStringNotEmpty(cat.getId()))addQuery(POST_DIVE_CATEGORY_ID, cat.getId());
            }
        }

        if (facilityList != null && facilityList.size() > 0){
            for (StateFacility fac : facilityList){
                if (fac != null && NYHelper.isStringNotEmpty(fac.getTag()))addQuery(POST_FACILITIES, fac.getTag());
            }
        }

        if (totalDives != null && totalDives.size() > 0){
            for (String st : totalDives){
                if (NYHelper.isStringNotEmpty(st))addQuery(POST_TOTAL_DIVES, st);
            }
        }

        /*if(!TextUtils.isEmpty(rating)) {
            addQuery(POST_RATING, rating);
        }*/

        /*if(!TextUtils.isEmpty(eco_trip)) {
            addQuery(POST_ECO_TRIP, eco_trip);
        }*/

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
