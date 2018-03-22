package com.nyelam.android.http;

import android.content.Context;
import android.text.TextUtils;

import com.danzoye.lib.http.DHTTPConnectionHelper;
import com.nyelam.android.R;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.data.ParticipantList;
import com.nyelam.android.data.SummaryList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/15/2018.
 */

public class NYDoDiveGetParticipantsRequest extends NYBasicAuthRequest<ParticipantList> {

    private static final String KEY_PARTICIPANTS = "participants";

    public NYDoDiveGetParticipantsRequest(Context context) throws Exception {
        super(AuthReturn.class, context, context.getResources().getString(R.string.api_path_dodive_book_get_participants));

    }

    @Override
    public String getHTTPType() {
        return DHTTPConnectionHelper.HTTP_POST;
    }

    @Override
    protected ParticipantList onProcessSuccessData(JSONObject obj) throws Exception {

        if(obj.get(KEY_PARTICIPANTS) instanceof JSONArray) {
            ParticipantList participantList = new ParticipantList();
            participantList.parse(obj.getJSONArray(KEY_PARTICIPANTS));
            if (participantList != null && participantList.getList() != null && participantList.getList().size() > 0) return participantList;
        }

        return null;
    }

}

