package com.danzoye.lib.auth.facebook;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.danzoye.lib.util.ImageHelper;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.nyelam.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class DFacebookActivity extends Activity {
    public static final String RESULT_KEY_ERROR = "facebook.result.key.ERROR";
    public static final String RESULT_KEY_DATA = "facebook.result.key.DATA";
    public static final String EXTRA_ADDITIONAL_PERMISSION = "com.danzoye.lib.auth.facebook.EXTRA_ADDITIONAL_PERMISSION";
    private static final String KEY_JSON_RESULT = "json result";
    private CommonFacebookProcessor fbProcessor;
    private CallbackManager callbackManager;

    protected FacebookProcessor getFacebookProcessor() {
        if (fbProcessor == null) {
            fbProcessor = new CommonFacebookProcessor();
        }
        return fbProcessor;
    }

    protected List<String> getPermissions() {
        List<String> list = Collections.synchronizedList(new ArrayList<String>());
        list.addAll(Arrays.asList("public_profile",
                "email","user_birthday","user_location"));
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dfacebook);
        if (savedInstanceState == null) {
            tryingOpenFacebookSession();
        } else {
            try {
                JSONObject resultObj = new JSONObject(
                        savedInstanceState.getString(KEY_JSON_RESULT));

                getFacebookProcessor().getResult().parse(resultObj);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("danzoye", e.toString());
            }
        }
    }

    protected void onDone(Intent intent) {
        setResult(RESULT_OK, intent);
        finish();
    }

    protected void onFBSessionOpened(final LoginResult loginResult) {
        if(loginResult != null && loginResult.getAccessToken() != null) {
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try {
                        Log.d("danzoye", "on complete facebook me request! " + object.toString(3));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(response != null && response.getJSONObject() != null) {
                        JSONObject map = response.getJSONObject();
                        try {
                            map.put(CommonFacebookProcessor.RESULT_KEY_FACEBOOK_ACCESS_TOKEN, loginResult.getAccessToken().getToken());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getFacebookProcessor().onProcess(CommonFacebookProcessor.TYPE_ME, map);
                        onGetProfilePictureURL(loginResult.getAccessToken());
                    } else if (response != null && response.getError() != null) {
                        FacebookRequestError error = response.getError();
                        Intent intent = new Intent();
                        intent.putExtra(RESULT_KEY_ERROR, error.toString());
                        onDone(intent);
                    } else {
                        onDone(null);
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,first_name, last_name, email,birthday,location");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            onDone(null);
        }

        //udah deprecated
//        Request.newMeRequest(session, new Request.GraphUserCallback() {
//
//            @Override
//            public void onCompleted(GraphUser user, Response response) {
//                Log.d("danzoye", "on complete facebook me request!");
//
//                if (response != null
//                        && response.getGraphObject() != null
//                        && response.getGraphObject().getInnerJSONObject() != null) {
//                    JSONObject map = response.getGraphObject()
//                            .getInnerJSONObject();
//
//                    Log.d("danzoye", map.toString());
//
//                    getFacebookProcessor().onProcess(
//                            CommonFacebookProcessor.TYPE_ME, map);
//
//                    onGetProfilePictureURL(Session.getActiveSession());
//                } else if (response != null && response.getError() != null) {
//                    Log.d("danzoye",
//                            "facebook me request error = "
//                                    + response.getError());
//
//                    FacebookRequestError error = response.getError();
//
//                    Intent intent = new Intent();
//                    intent.putExtra(RESULT_KEY_ERROR, error.toString());
//
//                    onDone(intent);
//                } else {
//                    Log.d("danzoye",
//                            "facebook me request error = no json inner object found!");
//                    onDone(null);
//                }
//            }
//        }).executeAsync();

    }

    protected void onGetProfilePictureURL(AccessToken accessToken) {
        Log.d("danzoye", "facebook get profile picture url");

        Bundle bundle = new Bundle();
        bundle.putBoolean("redirect", false);
        bundle.putString("width", "750");
        bundle.putString("height", "750");
        bundle.putString("type", "normal");
        new GraphRequest(accessToken, "/me/picture", bundle, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                if(response != null && response.getJSONObject() != null) {
                    JSONObject obj = response.getJSONObject();
                    getFacebookProcessor().onProcess(CommonFacebookProcessor.TYPE_PROFILE_PICTURE, obj);
                    Intent data = new Intent();
                    data.putExtra(RESULT_KEY_DATA, getFacebookProcessor().getResult().toString());
                    onDone(data);
                } else {
                    Intent data = new Intent();
                    data.putExtra(RESULT_KEY_DATA, getFacebookProcessor().getResult().toString());
                    onDone(data);
                }
            }
        }).executeAsync();


        //udah deprecated
//        new Request(session, "/me/picture", bundle, HttpMethod.GET,
//                new Request.Callback() {
//
//                    @Override
//                    public void onCompleted(Response response) {
//                        Log.d("danzoye",
//                                "facebook get profile picture url on complete");
//
//                        if (response != null
//                                && response.getGraphObject() != null) {
//                            JSONObject obj = response.getGraphObject()
//                                    .getInnerJSONObject();
//
//                            getFacebookProcessor()
//                                    .onProcess(
//                                            CommonFacebookProcessor.TYPE_PROFILE_PICTURE,
//                                            obj);
////                            --
////                            doDownloadProfilePicture(obj);
////                            Kalo mau download profile picture nya, uncomment syntax di atas.
////                            dan comment syntax di bawah
//                            Intent data = new Intent();
//                            data.putExtra(RESULT_KEY_DATA,
//                                    getFacebookProcessor().getResult().toString());
//                            onDone(data);
////                            --
//                        } else {
//                            Intent data = new Intent();
//                            data.putExtra(RESULT_KEY_DATA, getFacebookProcessor().getResult().toString());
//
//                            onDone(data);
//                        }
//                    }
//                }).executeAsync();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_JSON_RESULT, getFacebookProcessor().getResult().toString());
    }

    private void doDownloadProfilePicture(JSONObject obj) {
        try {
            if (obj.getJSONObject("data").getBoolean(
                    "is_silhouette")) {
                Intent data = new Intent();
                data.putExtra(RESULT_KEY_DATA,
                        getFacebookProcessor().getResult().toString());

                onDone(data);
                return;
            }
        } catch (JSONException e1) {
            Intent data = new Intent();
            data.putExtra(RESULT_KEY_DATA,
                    getFacebookProcessor().getResult().toString());
            e1.printStackTrace();
            onDone(data);
            return;
        }

        String url = null;
        try {
            url = obj.getJSONObject("data")
                    .getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
            Intent data = new Intent();
            data.putExtra(RESULT_KEY_DATA,
                    getFacebookProcessor().getResult().toString());
            e.printStackTrace();
            onDone(data);
            return;
        }

        if (url != null) {
            // Log.d("danzoye", "facebook get profile picture url on download and save file");

            AsyncTask<String, Void, File> task = new AsyncTask<String, Void, File>() {

                @Override
                protected File doInBackground(
                        String... params) {
                    File saveTo = null;
                    File root;
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        root = DFacebookActivity.this.getExternalCacheDir();
                    } else {
                        Log.w("danzoye", "unable to write external storage, cache saved to internal storage directory.");
                        root = DFacebookActivity.this.getCacheDir();
                    }
                    File media = new File(new StringBuffer(root.getAbsolutePath())
                            .append("/media/").toString());
                    if (!media.exists() && !media.mkdirs()) {
                        // folder ga ada dan ga berhasil bikin, artinya jangan di
                        // save
                        // di
                        // local sama skali
                        // Log.d("danzoye", "folder not found and can't be created : " + media.getAbsolutePath());
                    } else {
                        saveTo = new File(media, ImageHelper.generateLocalFilenameFromURLImage(params[0]));
                    }
                    ImageHelper.loadRemoteImage(DFacebookActivity.this, params[0], null, saveTo);
                    return saveTo;
                }

                protected void onPostExecute(File result) {
                    getFacebookProcessor().getResult().profilePictureLocalPath =
                            result.getAbsolutePath();

                    Intent data = new Intent();
                    data.putExtra(RESULT_KEY_DATA,
                            getFacebookProcessor().getResult().toString());

                    onDone(data);
                }
            };
            task.execute(new String[]{url});
        } else {
            Intent data = new Intent();
            data.putExtra(RESULT_KEY_DATA,
                    getFacebookProcessor().getResult().toString());

            onDone(data);
        }
    }

    private void tryingOpenFacebookSession() {
        List<String> permissions = getPermissions();

        List<String> addPermission = getIntent().getStringArrayListExtra(EXTRA_ADDITIONAL_PERMISSION);
        if (addPermission != null && !addPermission.isEmpty()) {
            permissions.addAll(addPermission);
        }
        if (callbackManager == null) {
            callbackManager = CallbackManager.Factory.create();
        }
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                onFBSessionOpened(loginResult);
            }

            @Override
            public void onCancel() {
                onDone(null);
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        LoginManager.getInstance().logInWithReadPermissions(this, permissions);
        // udah deprecated jadi gak kepake
//        Session.openActiveSession(this, true, permissions,
//                new Session.StatusCallback() {
//
//                    @Override
//                    public void call(Session session, SessionState state,
//                                     Exception exception) {
//                        // Log.d("danzoye", "open facebook session is opened? " + session.isOpened());
//                        // Log.d("danzoye", "open facebook session is closed? " + session.isClosed());
//
//                        if (session.isOpened()) {
//                            onFBSessionOpened(session);
//                        } else if (session.isClosed()) {
//                            onDone(null);
//                        }
//                        if (exception != null) {
//                            // Log.e("danzoye", exception.toString());
//                        }
//                    }
//                });
    }
}
