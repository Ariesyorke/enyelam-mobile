package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.DoShopList;
import com.nyelam.android.data.InboxList;
import com.nyelam.android.dev.NYLog;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class NYInboxRequest extends NYBasicAuthRequest<InboxList> {

    private static final String POST_PAGE = "page";

    public NYInboxRequest(Context context, int page) throws Exception{
        super(InboxList.class, context, context.getResources().getString(R.string.api_inbox_list));

//        if(!TextUtils.isEmpty(page)) {
            addQuery(POST_PAGE, String.valueOf(page));
//        }
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
    protected InboxList onProcessSuccessData(JSONObject obj) throws Exception {
        NYLog.e("JSONOBJCET " + obj);
        InboxList inboxList = new InboxList();
        inboxList.parse(obj);
        return inboxList;
    }

}