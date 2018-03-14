package com.nyelam.android.http;

import android.content.Context;

import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.Summary;
import com.nyelam.android.data.Update;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;

import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/23/2018.
 */

public class NYUpdateVersionRequest extends NYBasicRequest<Update> {

    private static String KEY_UPDATE = "update";

    private static String POST_PLATFORM = "platform";
    private static String POST_VERSION = "version";

    public NYUpdateVersionRequest(Context context, String version) throws Exception {
        super(Summary.class, context, context.getResources().getString(R.string.api_path_update_version));

        addQuery(POST_PLATFORM, "android");

        if (NYHelper.isStringNotEmpty(version)){
            addQuery(POST_VERSION, version);
        }

    }

    @Override
    protected Update onProcessSuccessData(JSONObject obj) throws Exception {

        if (!obj.isNull(KEY_UPDATE)){
            Update update = new Update();
            update.parse(obj.getJSONObject(KEY_UPDATE));
            return update;
        } else{
            return null;
        }
    }


}
