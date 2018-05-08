package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.Category;
import com.nyelam.android.data.Contact;
import com.nyelam.android.data.Price;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/24/2018.
 */

public class NYGetMinMaxPriceRequest extends NYBasicRequest<Price> {

    private static final String KEY_PRICE = "price";
    private static final String POST_TYPE = "type";
    private static final String POST_CATEGORIES = "dive_category_id[]";
    private static final String POST_DIVER = "diver";
    private static final String POST_CERTIFICATE = "certificate";
    private static final String POST_DATE = "date";
    private static final String POST_SORT_BY = "sort_by";
    private static final String POST_ECO_TRIP = "eco_trip";

    private static final String POST_DIVE_SPOT_ID = "dive_spot_id";
    private static final String POST_DIVE_CENTER_ID = "dive_center_id";
    private static final String POST_COUNTRY_ID = "country_id";
    private static final String POST_PROVINCE_ID = "province_id";
    private static final String POST_CITY_ID = "city_id";
    private static final String POST_CATEGORY_ID = "category_id";

    public NYGetMinMaxPriceRequest(Context context, String type) throws Exception {
        super(String.class, context, context.getResources().getString(R.string.api_path_get_min_max_price_list));
        if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }
    }

    public NYGetMinMaxPriceRequest(Context context, String type, String diverIdType, String diverId, List<Category> categories, String diver, String certificate, String date, String sortBy, boolean isEcoTrip) throws Exception {
        super(String.class, context, context.getResources().getString(R.string.api_path_get_min_max_price_list));
        if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }

        if(!TextUtils.isEmpty(diverId) && !TextUtils.isEmpty(diverIdType)) {
            if(diverIdType.equals("1")) {
                addQuery(POST_DIVE_SPOT_ID, diverId);
            } else if ( diverIdType.equals("2")){
                addQuery(POST_CATEGORY_ID, diverId);
            } else if (diverIdType.equals("3")){
                addQuery(POST_DIVE_CENTER_ID, diverId);
            } else if (diverIdType.equals("5")){
                addQuery(POST_PROVINCE_ID, diverId);
            } else if (diverIdType.equals("6")){
                addQuery(POST_CITY_ID, diverId);
            }
        }

        if(categories != null && !categories.isEmpty()) {
            for (Category cat : categories)
                addQuery(POST_CATEGORIES, cat.getId());
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        if(!TextUtils.isEmpty(certificate)) {
            addQuery(POST_CERTIFICATE, certificate);
        }

        if(!TextUtils.isEmpty(date)) {
            addQuery(POST_DATE, date);
        }

        if(!TextUtils.isEmpty(sortBy)) {
            addQuery(POST_SORT_BY, sortBy);
        }

        if(isEcoTrip) {
            addQuery(POST_ECO_TRIP, "1");
        }

    }

    @Override
    protected Price onProcessSuccessData(JSONObject obj) throws Exception {
        Price price = new Price();
        price.parse(obj.getJSONObject(KEY_PRICE));
        return price;
    }
}
