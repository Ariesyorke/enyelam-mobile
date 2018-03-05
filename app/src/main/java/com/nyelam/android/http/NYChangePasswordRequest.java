package com.nyelam.android.http;

import android.content.Context;

import com.danzoye.lib.util.StringHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Summary;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.profile.EditProfileActivity;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYChangePasswordRequest extends NYBasicAuthRequest<JSONObject> {

    private static String KEY_CHANGED = "changed";
    private static String KEY_REASON = "reason";

    private static String POST_CURRENT_PASSWORD = "current_password";
    private static String POST_NEW_PASSWORD = "new_password";
    private static String POST_CONFIRM_NEW_PASSWORD = "confirm_new_password";

    public NYChangePasswordRequest(Context context, String currentPassword, String newPassword, String confirmNewPassword) throws Exception {
        super(Summary.class, context, context.getResources().getString(R.string.api_path_change_password));

        if (NYHelper.isStringNotEmpty(currentPassword)){
            addQuery(POST_CURRENT_PASSWORD, StringHelper.md5(currentPassword.getBytes()) );
        }

        if (NYHelper.isStringNotEmpty(newPassword)){
            addQuery(POST_NEW_PASSWORD, StringHelper.md5(newPassword.getBytes()) );
        }

        if (NYHelper.isStringNotEmpty(confirmNewPassword)){
            addQuery(POST_CONFIRM_NEW_PASSWORD, StringHelper.md5(confirmNewPassword.getBytes()) );
        }

    }

    @Override
    protected JSONObject onProcessSuccessData(JSONObject obj) throws Exception {
        return obj;
    }

}
