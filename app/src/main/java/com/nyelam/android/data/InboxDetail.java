package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxDetail extends InboxData {

    private static String KEY_NEXT = "next";
    private static String KEY_DATA = "data";

    private String next;
    private List<InboxDetailData> data;

    public InboxDetail(){

    }

    public InboxDetail(String next, List<InboxDetailData> data){
        this.next = next;
        this.data = data;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<InboxDetailData> getData() {
        return data;
    }

    public void setData(List<InboxDetailData> data) {
        this.data = data;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_NEXT)) {
                setNext(obj.getString(KEY_NEXT));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_DATA)) {
                JSONArray array = obj.getJSONArray(KEY_DATA);
                if(array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        if(data == null) {
                            data = new ArrayList<>();
                        }
                        JSONObject o = array.getJSONObject(i);
                        InboxDetailData inboxDetailData = new InboxDetailData();
                        inboxDetailData.parse(o);
                        data.add(inboxDetailData);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}
    }
}