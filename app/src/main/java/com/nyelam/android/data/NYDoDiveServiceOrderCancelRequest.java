package com.nyelam.android.data;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.http.NYBasicAuthRequest;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveServiceOrderCancelRequest extends NYBasicAuthRequest<OrderReturn> {

    private static final String KEY_SUMMARY = "summary";

    private static final String POST_CART_TOKEN = "cart_token";
    private static final String POST_CONTACT = "contact";
    private static final String POST_PARTICIPANT = "diver";
    private static final String POST_TYPE = "type";
    private static final String POST_NOTE = "note";

    public NYDoDiveServiceOrderCancelRequest(Context context, String cartToken, String contact, String participant, String type, String note) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_service_order));

        if(!TextUtils.isEmpty(cartToken)) {
            addQuery(POST_CART_TOKEN, cartToken);
        }

        if(!TextUtils.isEmpty(contact)) {
            addQuery(POST_CONTACT, contact);
        }

        if(!TextUtils.isEmpty(participant)) {
            addQuery(POST_PARTICIPANT, participant);
        }

        if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }

        if(!TextUtils.isEmpty(note)) {
            addQuery(POST_NOTE, note);
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

