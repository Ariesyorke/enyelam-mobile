package com.nyelam.android.http;

import android.content.Context;

import com.danzoye.lib.http.DBaseRequest;
import com.nyelam.android.dev.NYLog;

/**
 * Created by bobi on 4/14/18.
 */

public class NYPaypalNotificationRequest extends DBaseRequest<Boolean> {
//    private static final String HOST_URL = "https://nyelam-adam.dantech.id/api/notification/paypal";
//    private static final String HOST_URL = "https://nyelam.dantech.id/api/notification/paypal";
    private static final String HOST_URL = "https://api.e-nyelam.com/notification/paypal";
    private static final String POST_PAYPAL_ID = "paypal_id";

    public NYPaypalNotificationRequest(Context context, String paypalId) {
        super(Boolean.class, HOST_URL);
        addQuery(POST_PAYPAL_ID, paypalId);
    }

    @Override
    protected Boolean onProcessData(byte[] data) throws Exception {
        return true;
    }

}
