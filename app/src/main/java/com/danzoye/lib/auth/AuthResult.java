package com.danzoye.lib.auth;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class AuthResult {
    public static final String MALE = "male";
    public static final String FEMALE = "female";
    private static final String KEY_ID = "auth.result.key.ID";
    private static final String KEY_NAME = "auth.result.key.NAME";
    private static final String KEY_FIRST_NAME = "auth.result.key.FIRST_NAME";
    private static final String KEY_LAST_NAME = "auth.result.key.LAST_NAME";
    private static final String KEY_GENDER = "auth.result.key.GENDER";
    private static final String KEY_EMAIL = "auth.result.key.EMAIL";
    private static final String KEY_LOCATION_NAME = "auth.result.key.LOCATION_NAME";
    private static final String KEY_BIRTHDAY = "auth.result.key.BIRTHDAY";
    private static final String KEY_PROFILE_PICTURE_URL = "auth.result.key.PROFILE_PICTURE_URL";
    private static final String KEY_PROFILE_PICTURE_LOCAL_PATH = "auth.result.key.PROFILE_PICTURE_LOCAL";
    private static final String KEY_ACCESS_TOKEN = "auth.result.key.ACCESS_TOKEN";
    private static final String KEY_ACCESS_TOKEN_FB = "auth.result.key.FACEBOOK_ACCESS_TOKEN";
    private static final String KEY_ACCESS_TOKEN_GOOGLE = "auth.result.key.GOOGLE_ID_TOKEN";
    public String id;
    public String name;
    public String firstName;
    public String lastName;
    public String gender;
    public String email;
    public String locationName;
    public Date birthday;
    public String profilePictureUrl;
    public String profilePictureLocalPath;
    public String accessToken;

    public void parse(JSONObject obj) {
        if (obj == null) {
            return;
        }

        try {
            if (!obj.isNull(KEY_ID)) {
                id = obj.getString(KEY_ID);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_NAME)) {
                name = obj.getString(KEY_NAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_FIRST_NAME)) {
                firstName = obj.getString(KEY_FIRST_NAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_LAST_NAME)) {
                lastName = obj.getString(KEY_LAST_NAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_GENDER)) {
                gender = obj.getString(KEY_GENDER);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_EMAIL)) {
                email = obj.getString(KEY_EMAIL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_LOCATION_NAME)) {
                locationName = obj.getString(KEY_LOCATION_NAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_BIRTHDAY)) {
                String s = obj.getString(KEY_BIRTHDAY);
                birthday = new SimpleDateFormat("MM/dd/yyyy").parse(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_PROFILE_PICTURE_URL)) {
                profilePictureUrl = obj.getString(KEY_PROFILE_PICTURE_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_PROFILE_PICTURE_LOCAL_PATH)) {
                profilePictureLocalPath = obj.getString(KEY_PROFILE_PICTURE_LOCAL_PATH);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (!obj.isNull(KEY_ACCESS_TOKEN)) {
                accessToken = obj.getString(KEY_ACCESS_TOKEN);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();

        try {
            if (id != null) {
                obj.put(KEY_ID, id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (name != null) {
                obj.put(KEY_NAME, name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (firstName != null) {
                obj.put(KEY_FIRST_NAME, firstName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (lastName != null) {
                obj.put(KEY_LAST_NAME, lastName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (gender != null) {
                obj.put(KEY_GENDER, gender);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (email != null) {
                obj.put(KEY_EMAIL, email);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (locationName != null) {
                obj.put(KEY_LOCATION_NAME, locationName);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (profilePictureUrl != null) {
                obj.put(KEY_PROFILE_PICTURE_URL, profilePictureUrl);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (profilePictureLocalPath != null) {
                obj.put(KEY_PROFILE_PICTURE_LOCAL_PATH, profilePictureLocalPath);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (accessToken != null) {
                obj.put(KEY_ACCESS_TOKEN, accessToken);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            if (birthday != null) {
                obj.put(KEY_BIRTHDAY, new SimpleDateFormat("MM/dd/yyyy").format(birthday));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        try {
            return obj.toString(3);
        } catch (JSONException e) {
            e.printStackTrace();
            // Log.d("danzoye", e.toString());
        }

        return super.toString();
    }
}
