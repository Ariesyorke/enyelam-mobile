package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxData implements Parseable {

    private static String KEY_TICKET_ID = "ticket_id";
    private static String KEY_SUBJECT = "subject";
    private static String KEY_NAMA = "name";
    private static String KEY_REF_ID = "ref_id";
    private static String KEY_STATUS = "status";
    private static String KEY_DATE = "date";
    private static String KEY_INBOX_TYPE = "Inbox_type";

    private int ticketId;
    private String subject;
    private String nama;
    private String refId;
    private String status;
    private long date;
    private String inboxType;

    public InboxData(){

    }

    public InboxData(int ticketId, String subject, String nama, String refId, String status
            , long date, String inboxType){
        this.ticketId = ticketId;
        this.subject = subject;
        this.nama = nama;
        this.refId = refId;
        this.status = status;
        this.date = date;
        this.inboxType = inboxType;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getsubject() {
        return subject;
    }

    public void setsubject(String subject) {
        this.subject = subject;
    }

    public String getnama() {
        return nama;
    }

    public void setnama(String nama) {
        this.nama = nama;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getInboxType() {
        return inboxType;
    }

    public void setInboxType(String inboxType) {
        this.inboxType = inboxType;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_TICKET_ID)) {
                setTicketId(obj.getInt(KEY_TICKET_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_SUBJECT)) {
                setsubject(obj.getString(KEY_SUBJECT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_NAMA)) {
                setnama(obj.getString(KEY_NAMA));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_REF_ID)) {
                setRefId(obj.getString(KEY_REF_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_STATUS)) {
                setStatus(obj.getString(KEY_STATUS));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DATE)) {
                setDate(obj.getLong(KEY_DATE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_INBOX_TYPE)) {
                setInboxType(obj.getString(KEY_INBOX_TYPE));
            }
        } catch (JSONException e) {e.printStackTrace();}
    }
}