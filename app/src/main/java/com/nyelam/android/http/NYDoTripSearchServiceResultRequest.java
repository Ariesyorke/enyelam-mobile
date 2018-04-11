package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.DiveServiceList;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class NYDoTripSearchServiceResultRequest extends NYBasicRequest<DiveServiceList> {

    private static final String KEY_DIVE_SERVICE = "dive_services";

    private static final String POST_PAGE = "page";

    private static final String POST_DIVER_CATEGORY_ID = "dive_category_id[]";

    private static final String POST_DIVER = "diver";
    private static final String POST_CERTIFICATE = "certificate";
    private static final String POST_DATE = "date";
    private static final String POST_SORT_BY = "sort_by";
    private static final String POST_PRICE_MIN = "price_min";
    private static final String POST_PRICE_MAX = "price_max";
    private static final String POST_CATEGORY = "dive_category_id[]";

    private static final String POST_DIVE_SPOT_ID = "dive_spot_id";
    private static final String POST_DIVE_CENTER_ID = "dive_center_id";
    private static final String POST_COUNTRY_ID = "country_id";
    private static final String POST_PROVINCE_ID = "province_id";
    private static final String POST_CITY_ID = "city_id";
    private static final String POST_CATEGORY_ID = "category_id";
    private static final String POST_ECO_TRIP = "eco_trip";

    public NYDoTripSearchServiceResultRequest(Context context, String apiPath, String page, String diverId, String type, String diver, String certificate, String date, String sortingBy, List<String> categories, String priceMin, String priceMax, String isEcoTrip) {
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

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
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

        if(!TextUtils.isEmpty(priceMin)) {
            addQuery(POST_PRICE_MIN, priceMin);
        }

        if(!TextUtils.isEmpty(priceMax)) {
            addQuery(POST_PRICE_MAX, priceMax);
        }

        if (categories != null && categories.size() > 0){
            for (String st : categories){
                addQuery(POST_CATEGORY, st);
            }
        }

        addQuery(POST_ECO_TRIP, isEcoTrip);

    }

    /*public NYDoDiveSearchServiceResultRequest(Context context, String apiPath, String page, String diverCenterId, String certificate, String diver, String date, String type, String diverId, String ecoTrip) {
        super(AuthReturn.class, context, apiPath);

        if(!TextUtils.isEmpty(diverCenterId)) {
            addQuery(POST_DIVE_CENTER_ID, diverCenterId);
        }

        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, page);
        }

        if (!TextUtils.isEmpty(type) && type.equals("1")){
            addQuery(POST_SPOT_ID, diverId);
        } else if (!TextUtils.isEmpty(type) && type.equals("2")){
            addQuery(POST_CATEGORY_ID, diverId);
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

        if(!TextUtils.isEmpty(ecoTrip)) {
            addQuery(POST_ECO_TRIP, ecoTrip);
        }

    }*/

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected DiveServiceList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_DIVE_SERVICE) instanceof JSONArray) {
            DiveServiceList serviceList = new DiveServiceList();
            serviceList.parse(obj.getJSONArray(KEY_DIVE_SERVICE));
            if (serviceList != null && serviceList.getList() != null && serviceList.getList().size() > 0) return serviceList;
        }

        return null;
    }


}
