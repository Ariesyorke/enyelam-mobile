package com.nyelam.android.data;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/24/2018.
 */

public class Membership implements Parseable {

    private static String KEY_MEMBERSHIP_TYPE = "membership_type";
    private static String KEY_MEMBERSHIP_EXPIRED = "membership_expired";

    private String membershipType;
    private String membershipExpired;

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getMembershipExpired() {
        return membershipExpired;
    }

    public void setMembershipExpired(String membershipExpired) {
        this.membershipExpired = membershipExpired;
    }

    @Override
    public void parse(JSONObject obj) {

        if (obj == null) return;

        try {
            if (!obj.isNull(KEY_MEMBERSHIP_TYPE)) {
                setMembershipType(obj.getString(KEY_MEMBERSHIP_TYPE));
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!obj.isNull(KEY_MEMBERSHIP_EXPIRED)) {
                setMembershipExpired(obj.getString(KEY_MEMBERSHIP_EXPIRED));
            }
        } catch (JSONException e) {e.printStackTrace();}

    }

    @Override
    public String toString() {

        JSONObject obj = new JSONObject();

        try {
            if (!TextUtils.isEmpty(getMembershipType())) {
                obj.put(KEY_MEMBERSHIP_TYPE, getMembershipType());
            } else {
                obj.put(KEY_MEMBERSHIP_TYPE, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            if (!TextUtils.isEmpty(getMembershipExpired())) {
                obj.put(KEY_MEMBERSHIP_EXPIRED, getMembershipExpired());
            } else {
                obj.put(KEY_MEMBERSHIP_EXPIRED, JSONObject.NULL);
            }
        } catch (JSONException e) {e.printStackTrace();}

        try {
            return obj.toString(3);
        } catch (JSONException e) {e.printStackTrace();}

        return super.toString();
    }

}