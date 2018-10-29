package com.nyelam.android.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxDetail implements Parseable {

    private static String KEY_DETAIL = "detail";

    private InboxDetailData dataInboxDetail;

    public InboxDetail(){

    }

    public InboxDetail(InboxDetailData dataInboxDetail){
        this.dataInboxDetail = dataInboxDetail;
    }

    public InboxDetailData getDataInboxDetail() {
        return dataInboxDetail;
    }

    public void setDataInboxDetail(InboxDetailData dataInboxDetail) {
        this.dataInboxDetail = dataInboxDetail;
    }

    @Override
    public void parse(JSONObject obj) {
        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_DETAIL)) {
                dataInboxDetail = new InboxDetailData();
                dataInboxDetail.parse(obj.getJSONObject(KEY_DETAIL));
            }
        } catch (JSONException e) {e.printStackTrace();}

        /*try {
            if (!obj.isNull(KEY_NEXT)) {
                setNext(obj.getString(KEY_NEXT));
            }
        } catch (JSONException e) {e.printStackTrace();}*/

        /*try {
            if (!obj.isNull(KEY_DETAIL)) {
                JSONArray array = obj.getJSONArray(KEY_DETAIL);
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
        } catch (JSONException e) {e.printStackTrace();}*/
    }
}