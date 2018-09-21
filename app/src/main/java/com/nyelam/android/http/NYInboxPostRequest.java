package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.nyelam.android.R;
import com.nyelam.android.data.InboxList;
import com.nyelam.android.dev.NYLog;

import org.json.JSONObject;

import java.io.File;

public class NYInboxPostRequest extends NYBasicAuthRequest<Boolean> {

    private static final String POST_SUBJECT = "subject_name";
    private static final String POST_SUBJECT_DETAIL = "subject_detail";
    private static final String POST_FILE = "file";
    private static final String POST_INBOX_TYPE = "inbox_type";
    private static final String POST_REF_ID = "ref_id";

    public NYInboxPostRequest(Context context, String subject, String subjectDetail, File file, String inboxType, String refId) throws Exception{
        super(Boolean.class, context, context.getResources().getString(R.string.api_inbox_post));

        if(!TextUtils.isEmpty(subject)) {
            addQuery(POST_SUBJECT, subject);
        }

        if(!TextUtils.isEmpty(subjectDetail)) {
            addQuery(POST_SUBJECT_DETAIL, subjectDetail);
        }

        if (file != null){
            addMultiPartFile(POST_FILE, file);
        }

        if(!TextUtils.isEmpty(inboxType)) {
            addQuery(POST_INBOX_TYPE, String.valueOf(inboxType));
        }

        if(!TextUtils.isEmpty(refId)) {
            addQuery(POST_REF_ID, String.valueOf(refId));
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
        Boolean success = false;
        if(obj.getInt("status") == 1){
            success = true;
        }
        return success;
    }

}