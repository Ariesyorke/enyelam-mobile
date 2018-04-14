package com.nyelam.android.http;

import com.danzoye.lib.http.DBaseRequest;

/**
 * Created by bobi on 4/14/18.
 */

public class NYPaypalNotificationRequest extends DBaseRequest<Boolean> {
    private static final String HOST_URL = "https://nyelam.dantech.id/notification/paypal";
//    private static final String HOST_URL = "https://e-nyelam.com/notification/veritrans";

    protected NYPaypalNotificationRequest(Class clazz, String url) {
        super(Boolean.class, HOST_URL);
    }

    @Override
    protected Boolean onProcessData(byte[] data) throws Exception {
        return true;
    }
}
