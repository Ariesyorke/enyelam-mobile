package com.nyelam.android.data;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;

public class InboxDetailDataItem extends InboxData {

    private static String KEY_ID = "id";
    private static String KEY_USER_ID = "user_id";
    private static String KEY_NAMA_DETAIL = "user_name";
    private static String KEY_SUBJECT_DETAIL = "subject_detail";
    private static String KEY_ATTACHMENT = "attachment";
    private static String KEY_DATE_DETAIL = "date";

    private int id;
    private String userId;
    private String userNameDetail;
    private String subjectDetail;
    private String attachment;
    private Date dateDetail;

    public InboxDetailDataItem(){

    }

    public InboxDetailDataItem(int id, String userId, String userNameDetail, String subjectDetail, String attachment
            , Date dateDetail){
        this.id = id;
        this.userId = userId;
        this.userNameDetail = userNameDetail;
        this.subjectDetail = subjectDetail;
        this.attachment = attachment;
        this.dateDetail = dateDetail;
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

    public String getUserNameDetail() {
        return userNameDetail;
    }

    public void setUserNameDetail(String userNameDetail) {
        this.userNameDetail = userNameDetail;
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

    public Date getDateDetail() {
        return dateDetail;
    }

    public void setDateDetail(Date dateDetail) {
        this.dateDetail = dateDetail;
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
            if (!obj.isNull(KEY_NAMA_DETAIL)) {
                setUserNameDetail(obj.getString(KEY_NAMA_DETAIL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SUBJECT_DETAIL)) {
                setSubjectDetail(obj.getString(KEY_SUBJECT_DETAIL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_ATTACHMENT)) {
                setAttachment(obj.getString(KEY_ATTACHMENT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        /*try {
            if(!obj.isNull(KEY_ATTACHMENT)) {
                String filePath = obj.getString(KEY_ATTACHMENT);
                attachment = new File(filePath);
                setAttachment(attachment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        try {
            if(!obj.isNull(KEY_DATE_DETAIL)) {
                long timestamp = Long.parseLong(obj.getString(KEY_DATE_DETAIL));
                dateDetail = new Date(timestamp * 1000);
                setDateDetail(dateDetail);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}