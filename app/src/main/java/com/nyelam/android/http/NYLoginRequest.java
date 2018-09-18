package com.nyelam.android.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.danzoye.lib.util.StringHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.User;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYLoginRequest extends NYBasicRequest<AuthReturn> {

    private static final String POST_IMEI = "imei";
    private static final String POST_EMAIL = "email";
    private static final String POST_PASSWORD = "password";

    public NYLoginRequest(Context context, String email, String password) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_login));

        if(!TextUtils.isEmpty(email)) {
            addQuery(POST_EMAIL, email);
        }

        if(!TextUtils.isEmpty(password)) {
            //addQuery(POST_PASSWORD, StringHelper.md5(password.getBytes()));
            addQuery(POST_PASSWORD, password);
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
