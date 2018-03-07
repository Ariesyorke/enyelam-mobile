package com.danzoye.lib.auth.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.nyelam.android.dev.NYLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class FBAuthHelper<RESULT extends FBAuthResult> {
    private int reqCode;
    private Activity activity;
    private Fragment fragment;
    private Callback callback;
    private Class<RESULT> clazz;
    private ArrayList<String> addPermissions;

    public FBAuthHelper(Activity activity, Callback callback, Class<RESULT> clazz, int requestCode) {
        this.reqCode = requestCode;
        this.activity = activity;
        this.callback = callback;
        this.clazz = clazz;
    }

    public FBAuthHelper(Fragment fragment, Callback callback, Class<RESULT> clazz, int requestCode) {
        this.reqCode = requestCode;
        this.fragment = fragment;
        this.callback = callback;
        this.clazz = clazz;
    }

    public void auth() {
        Context context = activity;
        if (context == null) {
            context = fragment.getActivity();
        }
        Intent intent = new Intent(context, onCreateFacebookActivity());
        if (addPermissions != null && !addPermissions.isEmpty()) {
            intent.putStringArrayListExtra(DFacebookActivity.EXTRA_ADDITIONAL_PERMISSION, addPermissions);
        }
        if (activity != null) {
            activity.startActivityForResult(intent, reqCode);
        } else {
            fragment.startActivityForResult(intent, reqCode);
        }
    }

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == reqCode) {
            if (resultCode == Activity.RESULT_OK || resultCode == Activity.RESULT_FIRST_USER) {
                if (data == null) {
                    getCallback()
                            .onFailed(this, new Exception("general error"));
                    return;
                }

                if (data.getStringExtra(DFacebookActivity.RESULT_KEY_ERROR) != null) {
                    getCallback()
                            .onFailed(
                                    this,
                                    new Exception(
                                            data.getStringExtra(DFacebookActivity.RESULT_KEY_ERROR)));
                    return;
                }

                String json = data
                        .getStringExtra(DFacebookActivity.RESULT_KEY_DATA);

                NYLog.e("isi JSON "+json);
                try {
                    JSONObject obj = new JSONObject(json);
                    FBAuthResult result = clazz.newInstance();
                    result.parse(obj);
                    getCallback().onSuccess(this, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getCallback().onFailed(this, e);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                    getCallback().onFailed(this, e);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    getCallback().onFailed(this, e);
                }
            }
        }
    }

    public void setAdditionalPermissions(List<String> permissions) {
        addPermissions = new ArrayList<>(permissions);
    }

    protected Class<DFacebookActivity> onCreateFacebookActivity() {
        return DFacebookActivity.class;
    }

    public static interface Callback<RESULT extends FBAuthResult> {
        public void onFailed(FBAuthHelper helper, Exception e);

        public void onSuccess(FBAuthHelper helper, RESULT result);
    }
}
