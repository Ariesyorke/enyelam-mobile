package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxList implements Parseable {

    private static final String KEY_NEXT = "next";
    private static final String KEY_INBOXDATAS = "inbox_datas";

    private String next;
    private List<InboxData> inboxDatas;

    public InboxList(){

    }

    public InboxList(String next, List<InboxData> inboxDatas){
        this.next = next;
        this.inboxDatas = inboxDatas;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public List<InboxData> getInboxData() {
        return inboxDatas;
    }

    public void setInboxData(List<InboxData> inboxDatas) {
        this.inboxDatas = inboxDatas;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {

            if (!obj.isNull(KEY_NEXT)) {
                setNext(obj.getString(KEY_NEXT));
            }

            if (!obj.isNull(KEY_INBOXDATAS)) {
                JSONArray array = obj.getJSONArray(KEY_INBOXDATAS);
                if(array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        if(inboxDatas == null) {
                            inboxDatas = new ArrayList<>();
                        }
                        JSONObject o = array.getJSONObject(i);
                        InboxData inboxData = new InboxData();
                        inboxData.parse(o);
                        inboxDatas.add(inboxData);
                    }
                }
            }

        } catch (JSONException e) {e.printStackTrace();}
    }

}