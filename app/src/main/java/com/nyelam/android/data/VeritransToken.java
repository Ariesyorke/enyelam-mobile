package com.nyelam.android.data;

import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 2/28/2018.
 */

public class VeritransToken implements Parseable {

    private static String KEY_TOKEN_ID = "token_id";

    private String tokenId;

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public void parse(JSONObject obj){

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_TOKEN_ID)) {
                setTokenId(obj.getString(KEY_TOKEN_ID));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getTokenId())) {
                obj.put(KEY_TOKEN_ID, getTokenId());
            } else {
                obj.put(KEY_TOKEN_ID, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}
