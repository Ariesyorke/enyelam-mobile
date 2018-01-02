package com.danzoye.lib.auth.facebook;

import com.facebook.AccessToken;
import com.oxoneindonesia.android.oxone.dev.OXLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;


public class CommonFacebookProcessor extends FacebookProcessor<FBAuthResult> {
    public static final String RESULT_KEY_FACEBOOK_ID = "facebook.result.key.FACEBOOK_ID";
    public static final String RESULT_KEY_NAME = "facebook.result.key.NAME";
    public static final String RESULT_KEY_FIRST_NAME = "facebook.result.key.FIRST_NAME";
    public static final String RESULT_KEY_LAST_NAME = "facebook.result.key.LAST_NAME";
    public static final String RESULT_KEY_GENDER = "facebook.result.key.GENDER";
    public static final String RESULT_KEY_EMAIL = "facebook.result.key.EMAIL";
    public static final String RESULT_KEY_LOCATION = "facebook.result.key.LOCATION";
    public static final String RESULT_KEY_BIRTHDAY = "facebook.result.key.BIRTHDAY";
    public static final String RESULT_KEY_PROFILE_PICTURE_URL = "facebook.result.key.PROFILE_PICTURE_URL";
    public static final String RESULT_KEY_PROFILE_PICTURE_LOCAL_PATH = "facebook.result.key.PROFILE_PICTURE_LOCAL";
    public static final String RESULT_KEY_FACEBOOK_ACCESS_TOKEN = "facebook.result.key.FACEBOOK_ACCESS_TOKEN";
    public static final int TYPE_ERROR = 0;
    public static final int TYPE_ME = 1;
    public static final int TYPE_PROFILE_PICTURE = 3;

    public CommonFacebookProcessor() {
        super(FBAuthResult.class);
    }

    @Override
    public void onProcess(int type, JSONObject value) {



        if (type == TYPE_ME) {
            // Log.d("danzoye", "CommonFacebookProcessor.onProcess type = TYPE_ME");

            result.accessToken = AccessToken.getCurrentAccessToken().getToken();

            OXLog.e("FBAuthResult accessToken : "+result.accessToken);

            try {
                if(!value.isNull("id")) {
                    result.id = value.getString("id");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            }

            try {
                if(!value.isNull("name")) {
                    result.name = value.getString("name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            }

            try {
                if(!value.isNull("first_name")) {
                    result.firstName = value.getString("first_name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            }

            try {
                if(!value.isNull("last_name")) {
                    result.lastName = value.getString("last_name");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            }

            try {
                if(!value.isNull("gender")) {
                    result.gender = value.getString("gender");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            }

            try {
                if(!value.isNull("birthday")) {
                    result.birthday = new SimpleDateFormat("MM/dd/yyyy").parse(value.getString("birthday"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                if(!value.isNull("email")) {
                    result.email = value.getString("email");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            }

            try {
                if(!value.isNull("location")) {
                    JSONObject locationJson = value.getJSONObject("location");
                    if(locationJson != null && !locationJson.isNull("name")) {
                        result.locationName = locationJson.getString("name");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            }

            try {
                if(!value.isNull("location")) {
                    JSONObject locationJson = value.getJSONObject("location");
                    if(locationJson != null && !locationJson.isNull("id")) {
                        result.locationId = locationJson.getString("id");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // Log.d("danzoye", e.toString());
            }

        } else if (type == TYPE_PROFILE_PICTURE) {
            // Log.d("danzoye", "CommonFacebookProcessor.onProcess type = TYPE_PROFILE_PICTURE");
            try {
                if (!value.getJSONObject("data").getBoolean("is_silhouette")) {
                    result.profilePictureUrl = value
                            .getJSONObject("data").getString("url");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
