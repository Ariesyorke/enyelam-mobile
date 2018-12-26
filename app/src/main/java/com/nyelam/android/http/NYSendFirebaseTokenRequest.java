package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

public class NYSendFirebaseTokenRequest extends NYBasicAuthRequest<Boolean> {
    private static final String POST_FIREBASE_TOKEN = "firebase_token";
    private static final String POST_PLATFORM = "platform";

    public NYSendFirebaseTokenRequest(Context context, String firebaseToken) throws Exception {
        super(Boolean.class, context, context.getResources().getString(R.string.api_path_send_firebase_token));
        if (NYHelper.isStringNotEmpty(firebaseToken)){
            addQuery(POST_FIREBASE_TOKEN, firebaseToken);
        }
        addQuery(POST_PLATFORM, "1");
    }

    @Override
    protected Boolean onProcessSuccessData(JSONObject obj) throws Exception {
        return true;
    }
}
