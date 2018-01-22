package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/22/2018.
 */

public class NYForgotPasswordRequest extends NYBasicRequest<Boolean> {

    private static String KEY_SUCCESS = "exist";

    private static String POST_EMAIL = "email";

    public NYForgotPasswordRequest(Class clazz, Context context, String email) {
        super(clazz, context, context.getString(R.string.api_path_forgot_password));

        if (NYHelper.isStringNotEmpty(email)){
            addQuery(POST_EMAIL, email);
        }

    }

    @Override
    protected Boolean onProcessSuccessData(JSONObject obj) throws Exception {
        Boolean ret = obj.getBoolean(KEY_SUCCESS);
        return ret;
    }

}
