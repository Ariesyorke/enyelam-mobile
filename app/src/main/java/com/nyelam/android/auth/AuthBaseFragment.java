package com.nyelam.android.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.danzoye.lib.auth.facebook.FBAuthHelper;
import com.danzoye.lib.auth.facebook.FBAuthResult;
import com.danzoye.lib.auth.google.GPlusAuthHelper;
import com.danzoye.lib.auth.google.GPlusAuthResult;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.octo.android.robospice.SpiceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by dantech on 9/14/16.
 */
public abstract class AuthBaseFragment extends Fragment implements
        FBAuthHelper.Callback<FBAuthResult>,
        GPlusAuthHelper.Callback<GPlusAuthResult>{

    private static final String KEY_FB_RESULT = "fb result";
    private static final String KEY_GOOGLE_RESULT = "google result";
    private static final int REQ_CODE_AUTH_FB = 0;
    private static final int REQ_CODE_AUTH_GOOGLE = 1;

    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    protected ProgressDialog dialog;
    protected Callback callback;

    protected FBAuthResult fbResult;
    protected FBAuthHelper<FBAuthResult> fbAuthHelper;

    protected GPlusAuthResult googleResult;
    protected GPlusAuthHelper<GPlusAuthResult> googleAuthHelper;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_AUTH_FB){
            fbAuthHelper.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQ_CODE_AUTH_GOOGLE) {
            googleAuthHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbAuthHelper = new FBAuthHelper<>(this, this, FBAuthResult.class, REQ_CODE_AUTH_FB);
        googleAuthHelper = new GPlusAuthHelper<>(this, this, GPlusAuthResult.class, REQ_CODE_AUTH_GOOGLE);

        if (savedInstanceState == null) {

        } else {

            try {
                fbResult = new FBAuthResult();
                fbResult.parse(new JSONObject(savedInstanceState.getString(KEY_FB_RESULT)));
            } catch (JSONException e) {
                e.printStackTrace();
                NYLog.e(e);
                fbResult = null;
            }

            NYLog.e("Google 1");

            try {
                googleResult = new GPlusAuthResult();
                googleResult.parse(new JSONObject(savedInstanceState.getString(KEY_GOOGLE_RESULT)));
                NYLog.e("Google Auth : "+googleResult.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                NYLog.e(e);
                googleResult = null;

                NYLog.e("Google Auth : null");

            }

        }
    }

    @Override
    public void onFailed(FBAuthHelper helper, Exception e) {

        NYLog.e(e);
        NYHelper.handleErrorMessage(getActivity(), getActivity().getResources().getString(R.string.fb_auth_failed));
        NYLog.e("Facebook Auth fail");
    }

    @Override
    public void onSuccess(FBAuthHelper helper, FBAuthResult result) {
        fbResult = result;
        NYLog.e("Facebook Auth - onSuccess : "+fbResult.toString());
    }

    @Override
    public void onFailed(GPlusAuthHelper helper, Exception e) {
        NYLog.e(e);
        NYHelper.handleErrorMessage(getActivity(), getActivity().getResources().getString(R.string.gp_auth_failed));

        NYLog.e("Google Auth fail");
    }

    @Override
    public void onSuccess(GPlusAuthHelper helper, GPlusAuthResult result) {
        googleResult = result;
        NYLog.e("Google Auth - onSuccess : "+googleResult.toString());
    }


    protected void onSocialMediaLogin(final String socMed, final String fbId, final String accessToken) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fbResult != null) {
            outState.putString(KEY_FB_RESULT, fbResult.toString());
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        spcMgr.start(getActivity());
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }


    public interface Callback {
        void onSignUpFacebook(FBAuthResult result);
        void onLoginSuccess(String email);
        void onSignUpSuccess();
        void onForgot();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(spcMgr.isStarted()) {
            spcMgr.shouldStop();
        }
    }
}

