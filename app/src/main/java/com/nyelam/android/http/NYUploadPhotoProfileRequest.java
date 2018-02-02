package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.Summary;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYUploadPhotoProfileRequest extends NYBasicAuthRequest<Boolean> {

    private static String KEY_SUCCESS = "success";

    private static String POST_FILE = "file";

    public NYUploadPhotoProfileRequest(Context context, File file) throws Exception {
        super(Summary.class, context, context.getResources().getString(R.string.api_path_upload_profile));

        if (file != null){
            addMultiPartFile(POST_FILE, file);
        }

    }


    @Override
    protected Boolean onProcessSuccessData(JSONObject obj) throws Exception {
        if (obj.has(KEY_SUCCESS) && obj.get(KEY_SUCCESS) instanceof Boolean){
            return obj.getBoolean(KEY_SUCCESS);
        } else{
            return false;
        }
    }

}
