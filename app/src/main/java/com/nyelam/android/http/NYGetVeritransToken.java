package com.nyelam.android.http;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.Contact;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/24/2018.
 */

public class NYGetVeritransToken  extends NYBasicRequest<HashMap<String, String>> {

    private static final String KEY_VERITRANS = "veritrans";
    public static final String KEY_TOKEN_ID = "token_id";
    public static final String KEY_ORDER_ID = "order_id";
    private static final String POST_CART_TOKEN = "cart_token";
    private static final String POST_CONTACT = "contact";
    private static final String POST_TICKET_SUMMARY = "ticket_summary";
    private static final String POST_VOUCHER_CODE = "voucher_code";

    protected NYGetVeritransToken(Class clazz, Context context, String apiPath) {
        super(clazz, context, apiPath);
    }

//    public NYGetVeritransToken(Context context, String cartToken, String voucherCode, Contact contact, TicketListDraft ticketDrafts, Location currentLocation) throws Exception {
//        super(String.class, context, context.getResources().getString(R.string.api_path_veritrans_token), currentLocation);
//
//        if(!TextUtils.isEmpty(cartToken)) {
//            addQuery(POST_CART_TOKEN, cartToken);
//        }
//
//        if(contact != null) {
//            addQuery(POST_CONTACT, contact.toString());
//        }
//
//        if(!TextUtils.isEmpty(voucherCode)) {
//            addQuery(POST_VOUCHER_CODE, voucherCode);
//        }
//
//        if(ticketDrafts != null && ticketDrafts.getList() != null && !ticketDrafts.getList().isEmpty()) {
//            addQuery(POST_TICKET_SUMMARY, ticketDrafts.toJSONArray().toString());
//        }
//
//    }

    @Override
    protected HashMap<String, String> onProcessSuccessData(JSONObject obj) throws Exception {
        HashMap<String, String> temp = new HashMap<>();
        JSONObject o = obj.getJSONObject(KEY_VERITRANS);
        temp.put(KEY_ORDER_ID, o.getString(KEY_ORDER_ID));
        temp.put(KEY_TOKEN_ID, o.getString(KEY_TOKEN_ID));
        return temp;
    }
}
