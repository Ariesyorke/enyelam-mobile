package com.nyelam.android.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danzoye.lib.auth.facebook.FBAuthHelper;
import com.danzoye.lib.auth.facebook.FBAuthResult;
import com.danzoye.lib.auth.google.GPlusAuthHelper;
import com.danzoye.lib.auth.google.GPlusAuthResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nyelam.android.NYApplication;
import com.nyelam.android.R;
import com.nyelam.android.backgroundservice.NYSpiceService;
import com.nyelam.android.data.AuthReturn;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYLoginRequest;
import com.nyelam.android.http.NYLoginSocmedRequest;
import com.nyelam.android.storage.EmailLoginStorage;
import com.nyelam.android.storage.LoginStorage;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.binary.InFileBigInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends AuthBaseFragment implements
        GoogleApiClient.OnConnectionFailedListener,
        AuthBaseFragment.OnFragmentInteractionListener {

    private static int RC_SIGN_IN = 201;
    private static final String KEY_FB_RESULT = "fb result";
    private static final String KEY_GOOGLE_RESULT = "google result";
    private static final int REQ_CODE_AUTH_FB = 0;
    private static final int REQ_CODE_AUTH_GOOGLE = 1;

    private ProgressDialog progressDialog, socemdProgressDialog;
    protected SpiceManager spcMgr = new SpiceManager(NYSpiceService.class);
    private OnFragmentInteractionListener mListener;
    private TextView loginTextView, registerTextView, forgotPasswordTextView;
    private EditText emailEditText, passwordEditText;
    private ImageView backgroundImageView;
    private LinearLayout googleLinearLayout, facebookLinearLayout;

    protected FBAuthResult fbResult;
    protected FBAuthHelper<FBAuthResult> fbAuthHelper;

    protected GPlusAuthResult googleResult;
    protected GPlusAuthHelper<GPlusAuthResult> googleAuthHelper;

    public LoginFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.background_blur);

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        initView(view);
        initControl();
    }

    private void initControl() {
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getActivity(), "Email can't be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(getActivity(), "Password can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    NYLoginRequest req = new NYLoginRequest(getContext(), email, password);
                    spcMgr.execute(req, onLoginRequest());
                }

            }
        });
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out);
                transaction.replace(R.id.fragment_container, RegisterFragment.newInstance());
                transaction.addToBackStack(LoginFragment.class.getName());
                transaction.commit();
            }
        });
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, ForgotPasswordFragment.newInstance());
                transaction.addToBackStack(LoginFragment.class.getName());
                transaction.commit();
            }
        });

        facebookLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbAuthHelper.auth();
            }
        });

        googleLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleAuthHelper.auth();
            }
        });
    }

    private RequestListener<AuthReturn> onLoginRequest() {
        return new RequestListener<AuthReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(AuthReturn authReturn) {
                if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                NYHelper.saveUserData(getActivity(), authReturn);
                if (NYHelper.isStringNotEmpty(emailEditText.getText().toString()))NYHelper.saveEmailUser(getActivity(), emailEditText.getText().toString());
                mListener.isLoginSuccess(true);
            }
        };
    }


    private void initView(View v) {
        emailEditText = (EditText) v.findViewById(R.id.email_editText);
        passwordEditText = (EditText) v.findViewById(R.id.password_editText);
        loginTextView = (TextView) v.findViewById(R.id.login_textView);
        googleLinearLayout = (LinearLayout) v.findViewById(R.id.google_linearLayout);
        facebookLinearLayout = (LinearLayout) v.findViewById(R.id.facebook_linearLayout);
        registerTextView = (TextView) v.findViewById(R.id.register_textView);
        forgotPasswordTextView = (TextView) v.findViewById(R.id.forgot_password_textView);
        backgroundImageView = (ImageView) v.findViewById(R.id.background_imageView);
        NYApplication application = (NYApplication)getActivity().getApplication();
        String imageUri = "drawable://"+R.drawable.background_blur;

        if(application.getCache(imageUri) != null) {
            Bitmap bitmap = application.getCache(imageUri);
            backgroundImageView.setImageBitmap(bitmap);
        } else {
            ImageLoader.getInstance().displayImage(imageUri, backgroundImageView, NYHelper.getCompressedOption(getActivity()));
        }

        EmailLoginStorage storage = new EmailLoginStorage(getActivity());
        if (storage != null && NYHelper.isStringNotEmpty(storage.email)) emailEditText.setText(storage.email);

    }






    /*private RequestListener<AuthReturn> onLoginRequest() {
        return new RequestListener<AuthReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                NYHelper.handleAPIException(getActivity(), spiceException, null);
            }

            @Override
            public void onRequestSuccess(AuthReturn authReturn) {
                *//*if(progressDialog != null && progressDialog.isShowing()){
                    progressDialog.dismiss();
                }*//*

                NYHelper.saveUserData(getActivity(), authReturn);
                //mListener.isLoginSuccess(true);
                //mListener.checkLocation();
            }
        };
    }*/


    @Override
    public void onSuccess(FBAuthHelper helper, FBAuthResult result) {
        super.onSuccess(helper, result);

        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();

        socemdProgressDialog = new ProgressDialog(getActivity());
        socemdProgressDialog.setMessage(getString(R.string.loading));
        socemdProgressDialog.setCancelable(false);
        socemdProgressDialog.show();

        fbResult = result;

        NYLog.e("CEK FB USER : "+result.toString());

        NYLoginSocmedRequest req = new NYLoginSocmedRequest(getActivity(), fbResult.email, NYHelper.GK_SOCMED_TYPE_FACEBOOK, fbResult.id, fbResult.accessToken);
        spcMgr.execute(req, onLoginSocmedRequest("fb"));
    }

    @Override
    public void onSuccess(GPlusAuthHelper helper, GPlusAuthResult result) {
        super.onSuccess(helper, result);
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();

        socemdProgressDialog = new ProgressDialog(getActivity());
        socemdProgressDialog.setMessage(getString(R.string.loading));
        socemdProgressDialog.setCancelable(false);
        socemdProgressDialog.show();

        googleResult = result;

        NYLog.e("CEK google USER : "+result.toString());

        NYLoginSocmedRequest req = new NYLoginSocmedRequest(getActivity(), googleResult.email, NYHelper.GK_SOCMED_TYPE_GOOGLE, googleResult.id, googleResult.accessToken);
        spcMgr.execute(req, onLoginSocmedRequest("google"));
    }

    private RequestListener<AuthReturn> onLoginSocmedRequest(final String type) {
        return new RequestListener<AuthReturn>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (spiceException != null) {
                    if (socemdProgressDialog != null && socemdProgressDialog.isShowing()) socemdProgressDialog.dismiss();
                    //progressDialog.cancel();
                    if (type.equals("fb")) {

                        //NYLog.e("CEK FB 1 ");
                        //NYLog.e("CEK FB 1 "+fbResult.toString());

                        //mListener.intentRegister(fbResult.email, fbResult.firstName, fbResult.lastName, GKHelper.GK_SOCMED_TYPE_FACEBOOK, fbResult.id, fbResult.accessToken, fbResult.profilePictureUrl);
                        //mListener.onRegisterRequest(progressDialog, fbResult.email, fbResult.firstName, fbResult.lastName, NYHelper.GK_SOCMED_TYPE_FACEBOOK, fbResult.id, fbResult.accessToken, fbResult.profilePictureUrl);
                        mListener.onRegisterRequest(progressDialog,  NYHelper.GK_SOCMED_TYPE_FACEBOOK, fbResult.toString());
                    } else if (type.equals("google")) {
                        //mListener.intentRegister(googleResult.email, googleResult.firstName, googleResult.lastName, GKHelper.GK_SOCMED_TYPE_GOOGLE, googleResult.id, googleResult.accessToken, googleResult.profilePictureUrl);
                        mListener.onRegisterRequest(progressDialog, NYHelper.GK_SOCMED_TYPE_GOOGLE, googleResult.toString());
                    }

                } else {
                    socemdProgressDialog.dismiss();
                    NYHelper.handleErrorMessage(getActivity(), getActivity().getResources().getString(R.string.warn_no_connection));
                }
            }

            @Override
            public void onRequestSuccess(AuthReturn authReturn) {
                if (socemdProgressDialog != null && socemdProgressDialog.isShowing()) socemdProgressDialog.dismiss();
                NYHelper.saveUserData(getActivity(), authReturn);

                NYLog.e("LOGIN SOCMED SUCCES");

                mListener.isLoginSuccess(true);
            }
        };
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == REQ_CODE_AUTH_FB){
            fbAuthHelper.onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == REQ_CODE_AUTH_GOOGLE) {
            googleAuthHelper.onActivityResult(requestCode, resultCode, data);
        } else  if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        NYLog.e("handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //Toast.makeText(getActivity(), acct.getId()+" "+acct.getDisplayName()+" "+acct.getEmail()+" "+acct.getIdToken(), Toast.LENGTH_SHORT).show();
            //Toast.makeText(getActivity(), acct.getFamilyName()+" - "+acct.getGivenName(), Toast.LENGTH_SHORT).show();
            mListener.intentRegister(acct.getEmail(), acct.getGivenName(), acct.getFamilyName(), NYHelper.GK_SOCMED_TYPE_GOOGLE, acct.getId(), acct.getIdToken(), acct.getPhotoUrl().toString());

        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
            GoogleSignInAccount acct = result.getSignInAccount();
            mListener.intentRegister(acct.getEmail(), acct.getGivenName(), acct.getFamilyName(), NYHelper.GK_SOCMED_TYPE_GOOGLE, acct.getId(), acct.getIdToken(), acct.getPhotoUrl().toString());
        }
    }

    public String validate(String emailAddress, String password) {
        if (TextUtils.isEmpty(emailAddress)) {
            return getActivity().getResources().getString(R.string.warn_field_email_cannot_be_empty);
        } else if (TextUtils.isEmpty(password)) {
            return getActivity().getResources().getString(R.string.warn_field_password_cannot_be_empty);
        } else if (!NYHelper.isValidEmaillId(emailAddress)) {
            return getActivity().getResources().getString(R.string.warn_email_not_valid);
        }
        return null;
    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        spcMgr.start(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (spcMgr.isStarted()){
            spcMgr.shouldStop();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public interface OnFragmentInteractionListener {
        void isLoginSuccess(boolean success);
        void intentRegister(String email, String firstName, String lastName, String socmedType, String id, String accessToken, String profilePictureUrl);
        void onRegisterRequest(ProgressDialog progressDialog, String email, String firstName, String lastName, String socmedType, String id, String accessToken, String profilePictureUrl);
        void onRegisterRequest(ProgressDialog progressDialog, String socmedType, String authResult);
        void intentForgotPassword();
    }



}
