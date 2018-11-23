package com.nyelam.android.http;

import android.content.Context;
import android.location.Location;

import com.nyelam.android.storage.LoginStorage;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/19/2018.
 */

public abstract class NYBasicAuthRequest<DATA> extends NYBasicRequest<DATA> {
    private static final String POST_USER_ID = "user_id";
    private static final String POST_NYELAM_TOKEN = "nyelam_token";
    private LoginStorage loginStorage;

    protected NYBasicAuthRequest(Class clazz, Context context, String apiPath) throws Exception {
        super(clazz, context, apiPath);

        loginStorage = new LoginStorage(context);

        if(loginStorage.isUserLogin()) {
            addQuery(POST_USER_ID, loginStorage.user.getUserId());
            //addQuery(POST_USER_ID, "126");
            addQuery(POST_NYELAM_TOKEN, loginStorage.nyelamToken);
            //addQuery(POST_NYELAM_TOKEN, "56bb43de6cde0feef01b");

        } else {
            throw new NYUserNotFoundException("User is either not login or token is expired");
        }
    }
}
