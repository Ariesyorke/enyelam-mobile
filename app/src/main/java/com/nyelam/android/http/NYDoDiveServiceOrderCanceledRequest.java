package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.OrderReturn;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveServiceOrderCanceledRequest extends NYBasicAuthRequest<Boolean> {

    private static final String KEY_SUCCESS = "success";
    private static final String POST_ORDER_ID = "order_id";

    public NYDoDiveServiceOrderCanceledRequest(Context context, String orderId) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_service_order_cancel));

        if(!TextUtils.isEmpty(orderId)) {
            addQuery(POST_ORDER_ID, orderId);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected Boolean onProcessSuccessData(JSONObject obj) throws Exception {
        if (obj.has(KEY_SUCCESS) && obj.get(KEY_SUCCESS) instanceof Boolean){
            return obj.getBoolean(KEY_SUCCESS);
        }
        return false;
    }

}

