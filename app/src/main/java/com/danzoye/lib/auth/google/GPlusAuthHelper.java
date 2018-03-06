package com.danzoye.lib.auth.google;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import id.kleen.android.dev.GKLog;


/**
 * Created by Ramdhany Dwi Nugroho on Mar 2015.
 */
public class GPlusAuthHelper<RESULT extends GPlusAuthResult> {
    private int reqCode;
    private Activity activity;
    private Fragment fragment;
    private Callback callback;
    private Class<RESULT> clazz;
    private ArrayList<String> addPermissions;

    public GPlusAuthHelper(Activity activity, Callback callback, Class<RESULT> clazz, int requestCode) {
        this.reqCode = requestCode;
        this.activity = activity;
        this.callback = callback;
        this.clazz = clazz;
    }

    public GPlusAuthHelper(Fragment fragment, Callback callback, Class<RESULT> clazz, int requestCode) {
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

        GKLog.e("Google auth");

        final int gms = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (gms != ConnectionResult.SUCCESS) {
            if (activity != null) {
                GooglePlayServicesUtil.showErrorDialogFragment(
                        gms,
                        activity,
                        reqCode, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                getCallback()
                                        .onFailed(
                                                GPlusAuthHelper.this,
                                                new Exception(GooglePlayServicesUtil.getErrorString(gms)));
                            }
                        });
            } else {
                GooglePlayServicesUtil.showErrorDialogFragment(
                        gms,
                        fragment.getActivity(),
                        fragment,
                        reqCode,
                        new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                getCallback()
                                        .onFailed(
                                                GPlusAuthHelper.this,
                                                new Exception(GooglePlayServicesUtil.getErrorString(gms)));
                            }
                        });
            }
            return;
        }

        Intent intent = new Intent(context, onCreateGoogleActivity());

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

                if (data.getStringExtra(DGoogleActivity.RESULT_EXTRA_ERROR) != null) {
                    getCallback()
                            .onFailed(
                                    this,
                                    new Exception(
                                            data.getStringExtra(DGoogleActivity.RESULT_EXTRA_ERROR)));
                    return;
                }

                String json = data
                        .getStringExtra(DGoogleActivity.RESULT_EXTRA_DATA);
                GKLog.e("isi data "+json);

                try {
                    JSONObject obj = new JSONObject(json);
                    GPlusAuthResult result = clazz.newInstance();
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

    protected Class<DGoogleActivity> onCreateGoogleActivity() {
        return DGoogleActivity.class;
    }


    public static interface Callback<RESULT extends GPlusAuthResult> {
        public void onFailed(GPlusAuthHelper helper, Exception e);

        public void onSuccess(GPlusAuthHelper helper, RESULT result);
    }
}
