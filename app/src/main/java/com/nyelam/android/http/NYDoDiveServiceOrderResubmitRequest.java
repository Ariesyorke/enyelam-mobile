package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.OrderReturn;
import com.nyelam.android.http.NYBasicAuthRequest;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveServiceOrderResubmitRequest extends NYBasicAuthRequest<OrderReturn> {

    private static final String KEY_SUMMARY = "summary";
    private static final String POST_ORDER_ID = "order_id";
    private static final String POST_PAYMENT_TYPE = "payment_type";

    public NYDoDiveServiceOrderResubmitRequest(Context context, String orderId, String paymentType) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_service_order_resubmit));

        if(!TextUtils.isEmpty(orderId)) {
            addQuery(POST_ORDER_ID, orderId);
        }

        if(!TextUtils.isEmpty(paymentType)) {
            addQuery(POST_PAYMENT_TYPE, paymentType);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected OrderReturn onProcessSuccessData(JSONObject obj) throws Exception {
        OrderReturn orderReturn = new OrderReturn();
        orderReturn.parse(obj);
        return  orderReturn;
    }

}

