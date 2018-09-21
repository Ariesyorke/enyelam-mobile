package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.nyelam.android.R;
import com.nyelam.android.data.InboxList;
import com.nyelam.android.dev.NYLog;

import org.json.JSONObject;

import java.io.File;

public class NYInboxAddRequest extends NYBasicAuthRequest<Boolean> {

    private static final String POST_TICKET_ID = "ticket_id";
    private static final String POST_SUBJECT_DETAIL = "subject_detail";
    private static final String POST_FILE = "file";

    public NYInboxAddRequest(Context context, String ticketId, String subjectDetail, File file) throws Exception{
        super(Boolean.class, context, context.getResources().getString(R.string.api_inbox_add));

        if(!TextUtils.isEmpty(ticketId)) {
            addQuery(POST_TICKET_ID, ticketId);
        }

        if(!TextUtils.isEmpty(subjectDetail)) {
            addQuery(POST_SUBJECT_DETAIL, subjectDetail);
        }
        if (file != null){
            addMultiPartFile(POST_FILE, file);
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
    protected Boolean onProcessSuccessData(JSONObject obj) throws Exception {
        NYLog.e("JSONOBJCET " + obj);
        Boolean success = obj.getBoolean("success");
        return success;
    }

}