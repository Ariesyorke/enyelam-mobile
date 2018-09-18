package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxDetailData extends InboxData {

    private static String KEY_DATA = "data";

    private List<InboxDetailDataItem> dataInboxDetailItem;

    public InboxDetailData(){

    }

    public InboxDetailData(List<InboxDetailDataItem> dataInboxDetailItem){
        this.dataInboxDetailItem = dataInboxDetailItem;
    }

    public List<InboxDetailDataItem> getDataInboxDetailItem() {
        return dataInboxDetailItem;
    }

    public void setDataInboxDetailItem(List<InboxDetailDataItem> dataInboxDetailItem) {
        this.dataInboxDetailItem = dataInboxDetailItem;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_DATA)) {
                JSONArray array = obj.getJSONArray(KEY_DATA);
                if(array.length() > 0) {
                    for(int i = 0; i < array.length(); i++) {
                        if(dataInboxDetailItem == null) {
                            dataInboxDetailItem = new ArrayList<>();
                        }
                        JSONObject o = array.getJSONObject(i);
                        InboxDetailDataItem inboxDetailDataItem = new InboxDetailDataItem();
                        inboxDetailDataItem.parse(o);
                        dataInboxDetailItem.add(inboxDetailDataItem);
                    }
                }
            }
        } catch (JSONException e) {e.printStackTrace();}

    }
}