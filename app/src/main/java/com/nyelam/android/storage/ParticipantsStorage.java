package com.nyelam.android.storage;

import android.content.Context;

import com.danzoye.lib.util.AbstractStorage;
import com.nyelam.android.data.ModuleList;
import com.nyelam.android.data.ParticipantList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Dantech on 8/11/2017.
 */

public class ParticipantsStorage extends AbstractStorage {

    private static final String FILENAME = "nyelam:storage:participants";
    private static final String KEY_PARTICIPANTS = "participants";

    private ParticipantList participantList;

    public ParticipantsStorage(Context context) {
        super(context);
    }

    public ParticipantList getParticipantList() {
        return participantList;
    }

    public void setParticipantList(ParticipantList participantList) {
        this.participantList = participantList;
    }

    @Override
    protected String getStorageKey() {
        return FILENAME;
    }

    @Override
    protected void onParseData(JSONObject obj) throws JSONException {

        try {
            if (!obj.isNull(KEY_PARTICIPANTS)) {
                JSONArray array = obj.getJSONArray(KEY_PARTICIPANTS);
                participantList = new ParticipantList();
                participantList.parse(array);
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    protected JSONObject onSaveData() throws JSONException {
        JSONObject obj = new JSONObject();
        try {
            if(participantList != null && participantList.getList() != null && !participantList.getList().isEmpty()) {
                obj.put(KEY_PARTICIPANTS, participantList.toJSONArray());
            }
        } catch (JSONException e){e.printStackTrace();}

        return obj;
    }
}