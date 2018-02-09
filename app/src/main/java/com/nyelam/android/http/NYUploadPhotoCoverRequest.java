package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Summary;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYUploadPhotoCoverRequest extends NYBasicAuthRequest<AuthReturn> {

    private static String KEY_USER = "user";

    private static String POST_FILE = "file";

    public NYUploadPhotoCoverRequest(Context context, File file) throws Exception {
        super(Summary.class, context, context.getResources().getString(R.string.api_path_upload_cover));

        if (file != null){
            addMultiPartFile(POST_FILE, file);
        }

    }


    @Override
    protected AuthReturn onProcessSuccessData(JSONObject obj) throws Exception {
        if (obj.has(KEY_USER) && obj.getJSONObject(KEY_USER) != null){

            AuthReturn authReturn = new AuthReturn();
            authReturn.parse(obj);
            return authReturn;
        } else{
            return null;
        }
    }


}
