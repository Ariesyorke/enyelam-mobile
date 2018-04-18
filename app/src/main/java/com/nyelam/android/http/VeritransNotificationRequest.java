package com.nyelam.android.http;

import android.content.Context;

import com.danzoye.lib.http.DJSONRequest;
import com.nyelam.android.R;
import com.nyelam.android.dev.NYLog;

import org.json.JSONObject;

/**
 * Created by bobi on 3/5/18.
 */

public class VeritransNotificationRequest extends DJSONRequest<Boolean> {
    /*public static final String HOST_URL = "https://nyelam-adam.dantech.id/notification/veritrans";*/
    /*public static final String HOST_URL = "https://nyelam.dantech.id/notification/veritrans";*/
    public static final String HOST_URL = "https://e-nyelam.com/notification/veritrans";

    public VeritransNotificationRequest(Context context, JSONObject transactionResponse) {
        super(Boolean.class, HOST_URL);
        setJSONObject(transactionResponse);
    }

    @Override
    protected Boolean onProcessData(byte[] data) throws Exception {
        String response = new String(data);
        return true;
    }

}
