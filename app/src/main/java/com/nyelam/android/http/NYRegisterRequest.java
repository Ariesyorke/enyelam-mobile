package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.danzoye.lib.util.StringHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.dev.NYLog;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class NYRegisterRequest extends NYBasicRequest<AuthReturn> {

    private static final String POST_USERNAME = "username";
    private static final String POST_EMAIL = "email";
    private static final String POST_PHONE = "phone";
    private static final String POST_PASSWORD = "password";
    private static final String POST_CONFIRM_PASSWORD = "confirm_password";
    private static final String POST_GENDER = "gender";
    private static final String POST_SOCMED_TYPE = "socmed_type";
    private static final String POST_SOCMED_ID = "socmed_id";
    private static final String POST_SOCMED_ACCESS_TOKEN = "socmed_access_token";
    private static final String POST_PICTURE = "picture";
    private static final String POST_COUNTRY_CODE_ID = "country_code_id";

    public NYRegisterRequest(Context context,
                             String username,
                             String email,
                             String phoneNumber,
                             String countryCodeId,
                             String password,
                             String confirmPassword,
                             String gender,
                             String socmedType,
                             String socmedId,
                             String socmedAccessToken,
                             String picture) {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_register));

        if(!TextUtils.isEmpty(username)) {
            addQuery(POST_USERNAME, username);
        }

        if(!TextUtils.isEmpty(email)) {
            addQuery(POST_EMAIL, email);
        }

        if(!TextUtils.isEmpty(phoneNumber)) {
            addQuery(POST_PHONE, phoneNumber);
        }

        if(!TextUtils.isEmpty(countryCodeId)) {
            addQuery(POST_COUNTRY_CODE_ID, countryCodeId);
        }

        if(!TextUtils.isEmpty(password)) {
            addQuery(POST_PASSWORD, StringHelper.md5(password.getBytes()));
        }

        if(!TextUtils.isEmpty(confirmPassword)) {
            addQuery(POST_CONFIRM_PASSWORD, StringHelper.md5(confirmPassword.getBytes()));
        }

        if(!TextUtils.isEmpty(gender) && !gender.equals("Select Gender")) {
            addQuery(POST_GENDER, gender.toLowerCase());
        }


        if(!TextUtils.isEmpty(socmedType)) {
            addQuery(POST_SOCMED_TYPE, socmedType);
        }
        if(!TextUtils.isEmpty(socmedId)) {
            addQuery(POST_SOCMED_ID, socmedId);
        }
        if(!TextUtils.isEmpty(socmedAccessToken)) {
            addQuery(POST_SOCMED_ACCESS_TOKEN, socmedAccessToken);
        }
        if(!TextUtils.isEmpty(picture)) {
            addQuery(POST_PICTURE, picture);
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
