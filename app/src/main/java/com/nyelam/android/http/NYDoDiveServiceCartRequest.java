package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.CartReturn;
import com.nyelam.android.data.SearchResultList;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveServiceCartRequest extends NYBasicAuthRequest<CartReturn> {

    private static final String KEY_CART = "cart";

    private static final String POST_DIVE_SERVICE_ID = "dive_service_id";
    private static final String POST_DIVE_CENTER_ID = "dive_center_id";
    private static final String POST_DIVER = "diver";
    private static final String POST_TYPE = "type";
    private static final String POST_SCHEDULE = "schedule";

    private static final String POST_LICENSE_TYPE = "license_type";
    private static final String POST_ORGANIZATION_ID= "organization_id";

    public NYDoDiveServiceCartRequest(Context context, String diveServiceId, String diver, String schedule) throws Exception {
        super(CartReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_service_cart));

        if(!TextUtils.isEmpty(diveServiceId)) {
            addQuery(POST_DIVE_SERVICE_ID, diveServiceId);
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        /*if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }*/

        addQuery(POST_TYPE, "1");

        if(!TextUtils.isEmpty(schedule)) {
            addQuery(POST_SCHEDULE, schedule);
        }

    }

    public NYDoDiveServiceCartRequest(Context context, String diveServiceId, String diver, String schedule, String diveCenterId) throws Exception {
        super(CartReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_service_cart));

        if(!TextUtils.isEmpty(diveServiceId)) {
            addQuery(POST_DIVE_SERVICE_ID, diveServiceId);
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        if(!TextUtils.isEmpty(diveCenterId)) {
            addQuery(POST_DIVE_CENTER_ID, diveCenterId);
        }

        /*if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }*/

        addQuery(POST_TYPE, "1");

        if(!TextUtils.isEmpty(schedule)) {
            addQuery(POST_SCHEDULE, schedule);
        }

    }


    public NYDoDiveServiceCartRequest(Context context, String diveServiceId, String diver, String schedule, String diveCenterId, String organizationId, String licenseType) throws Exception {
        super(CartReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_service_cart));

        if(!TextUtils.isEmpty(diveServiceId)) {
            addQuery(POST_DIVE_SERVICE_ID, diveServiceId);
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        if(!TextUtils.isEmpty(diveCenterId)) {
            addQuery(POST_DIVE_CENTER_ID, diveCenterId);
        }

        addQuery(POST_TYPE, "1");

        if(!TextUtils.isEmpty(schedule)) {
            addQuery(POST_SCHEDULE, schedule);
        }

        if(!TextUtils.isEmpty(organizationId)) {
            addQuery(POST_ORGANIZATION_ID, organizationId);
        }

        if(!TextUtils.isEmpty(licenseType)) {
            addQuery(POST_LICENSE_TYPE, licenseType);
        }

    }


    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected CartReturn onProcessSuccessData(JSONObject obj) throws Exception {
        CartReturn cartReturn = new CartReturn();
        cartReturn.parse(obj);
        //cartReturn.parse(obj.getJSONObject(KEY_CART));
        return cartReturn;
    }

}

