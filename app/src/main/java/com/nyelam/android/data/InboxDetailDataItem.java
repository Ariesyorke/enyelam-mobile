package com.nyelam.android.data;

import org.json.JSONException;
import org.json.JSONObject;

public class InboxDetailDataItem extends InboxData {

    private static String KEY_ID = "id";
    private static String KEY_USER_ID = "user_id";
    private static String KEY_NAMA = "user_name";
    private static String KEY_SUBJECT_DETAIL = "subject_detail";
    private static String KEY_ATTACHMENT = "attachment";
    private static String KEY_DATE = "date";

    private int id;
    private String userId;
    private String userName;
    private String subjectDetail;
    private String attachment;
    private String date;

    public InboxDetailDataItem(){

    }

    public InboxDetailDataItem(int id, String userId, String userName, String subjectDetail, String attachment
            , String date){
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.subjectDetail = subjectDetail;
        this.attachment = attachment;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSubjectDetail() {
        return subjectDetail;
    }

    public void setSubjectDetail(String subjectDetail) {
        this.subjectDetail = subjectDetail;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Override
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_ID)) {
                setId(obj.getInt(KEY_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_USER_ID)) {
                setUserId(obj.getString(KEY_USER_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NAMA)) {
                setUserName(obj.getString(KEY_NAMA));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SUBJECT_DETAIL)) {
                setSubjectDetail(obj.getString(KEY_SUBJECT_DETAIL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_ATTACHMENT)) {
                setStatus(obj.getString(KEY_ATTACHMENT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DATE)) {
                setDate(obj.getString(KEY_DATE));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }
}