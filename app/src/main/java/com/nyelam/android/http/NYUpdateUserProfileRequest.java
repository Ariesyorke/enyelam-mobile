package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Summary;
import com.nyelam.android.data.User;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYUpdateUserProfileRequest extends NYBasicAuthRequest<AuthReturn> {

    private static String KEY_USER = "user";

    private static String POST_FULLNAME = "fullname";
    private static String POST_USERNAME = "username";
    private static String POST_COUNTRY_CODE = "country_code";
    private static String POST_PHONE_NUMBER = "phone_number";

    public NYUpdateUserProfileRequest(Context context, String fullname, String username, String countryCode, String phoneNumber) throws Exception {
        super(Summary.class, context, context.getResources().getString(R.string.api_path_update_profile));

        if (NYHelper.isStringNotEmpty(fullname)){
            addQuery(POST_FULLNAME, fullname);
        }

        if (NYHelper.isStringNotEmpty(username)){
            addQuery(POST_USERNAME, username);
        }

        if (NYHelper.isStringNotEmpty(countryCode)){
            addQuery(POST_COUNTRY_CODE, countryCode);
        }

        if (NYHelper.isStringNotEmpty(phoneNumber)){
            addQuery(POST_PHONE_NUMBER, phoneNumber);
        }

    }

    @Override
    protected AuthReturn onProcessSuccessData(JSONObject obj) throws Exception {
        if (obj.has(KEY_USER) && obj.get(KEY_USER) != null){
            AuthReturn authReturn = new AuthReturn();
            authReturn.parse(obj);
            return authReturn;
        } else{
            return null;
        }
    }

}
