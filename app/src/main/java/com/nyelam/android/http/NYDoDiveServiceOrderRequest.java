package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.SearchResultList;

import org.greenrobot.greendao.annotation.OrderBy;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveServiceOrderRequest extends NYBasicRequest<Boolean> {

    private static final String KEY_SUCCESS = "success";

    private static final String POST_CART_TOKEN = "cart_token";
    private static final String POST_CONTACT = "contact";
    private static final String POST_PARTICIPANT = "participant";

    public NYDoDiveServiceOrderRequest(Context context, String cartToken, String contact, String participant) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_master_dodive_search));

        if(!TextUtils.isEmpty(cartToken)) {
            addQuery(POST_CART_TOKEN, cartToken);
        }

        if(!TextUtils.isEmpty(contact)) {
            addQuery(POST_CONTACT, contact);
        }

        if(!TextUtils.isEmpty(participant)) {
            addQuery(POST_PARTICIPANT, participant);
        }

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected Boolean onProcessSuccessData(JSONObject obj) throws Exception {

        Boolean isSuccess = obj.getBoolean(KEY_SUCCESS);
        return isSuccess;
    }

}

