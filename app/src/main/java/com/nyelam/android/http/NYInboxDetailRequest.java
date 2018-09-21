package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.InboxDetail;
import com.nyelam.android.data.InboxList;
import com.nyelam.android.dev.NYLog;

import org.json.JSONObject;

public class NYInboxDetailRequest extends NYBasicAuthRequest<InboxDetail> {

    private static final String POST_TICKET_ID = "ticket_id";

    public NYInboxDetailRequest(Context context, int page) throws Exception{
        super(InboxList.class, context, context.getResources().getString(R.string.api_inbox_detail));

        if(!TextUtils.isEmpty(String.valueOf(page))) {
            addQuery(POST_TICKET_ID, String.valueOf(page));
        }
    }

    /*@Override
    public InboxList loadDataFromNetwork() throws Exception {
        String json = null;
        try {
            InputStream is = getContext().getAssets().open("inbox_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONObject obj = new JSONObject(json);
            return  onProcessSuccessData(obj);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }*/

    @Override
    protected InboxDetail onProcessSuccessData(JSONObject obj) throws Exception {
        InboxDetail inboxDetail = new InboxDetail();
        inboxDetail.parse(obj);
        return inboxDetail;
    }

}