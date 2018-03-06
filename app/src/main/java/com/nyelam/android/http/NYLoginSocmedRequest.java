package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur on 4/11/2017.
 */

public class NYLoginSocmedRequest extends NYBasicRequest<AuthReturn> {

private static final String POST_TYPE = "type";
private static final String POST_ID = "id";
private static final String POST_ACCESS_TOKEN = "access_token";

    public NYLoginSocmedRequest(Context context, String type, String id, String accessToken) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_login));

        if(!TextUtils.isEmpty(type)) {
            addQuery(POST_TYPE, type);
        }

        if(!TextUtils.isEmpty(id)) {
            addQuery(POST_ID, id);
        }
        if(!TextUtils.isEmpty(accessToken)) {
            addQuery(POST_ACCESS_TOKEN, accessToken);
        }
    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected AuthReturn onProcessSuccessData(JSONObject obj) throws Exception {
        AuthReturn temp = new AuthReturn();
        temp.parse(obj);
        return temp;
    }
}