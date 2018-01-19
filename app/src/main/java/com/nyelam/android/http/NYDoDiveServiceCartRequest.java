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

    private static final String POST_DIVE_SERVICE_CART = "dive_service_cart";
    private static final String POST_DIVE_SERVICE_ID = "dive_service_id";
    private static final String POST_DIVE_SPOT_ID = "dive_spot_id";
    private static final String POST_DIVER = "diver";
    private static final String POST_TYPE = "type";
    private static final String POST_SCHEDULE = "schedule";

    public NYDoDiveServiceCartRequest(Context context, String diveServiceId, String diveSpotId, String diver, String type, String schedule) throws Exception {
        super(CartReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_service_cart));

        if(!TextUtils.isEmpty(diveServiceId)) {
            addQuery(POST_DIVE_SERVICE_ID, diveServiceId);
        }

        if(!TextUtils.isEmpty(diveSpotId)) {
            addQuery(POST_DIVE_SPOT_ID, diveSpotId);
        }

        if(!TextUtils.isEmpty(diver)) {
            addQuery(POST_DIVER, diver);
        }

        /*if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }*/

        addQuery(POST_TYPE, "3");

        if(!TextUtils.isEmpty(schedule)) {
            addQuery(POST_SCHEDULE, schedule);
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

