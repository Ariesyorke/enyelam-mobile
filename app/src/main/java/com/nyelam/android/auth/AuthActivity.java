package com.nyelam.android.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.danzoye.lib.auth.facebook.FBAuthResult;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.storage.LoginStorage;

public class AuthActivity extends BasicActivity implements
    LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener,
    ForgotPasswordFragment.OnFragmentInteractionListener,
        AuthBaseFragment.OnFragmentInteractionListener,
        AuthBaseFragment.Callback{

    public static final String REQ_INVALID_TOKEN = "invalid_token";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        LoginStorage storage = new LoginStorage(this);
        if (storage.isUserLogin()){
            isLoginSuccess(true);
        }

        initFragment();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, LoginFragment.newInstance()).commit();
    }


    @Override
    public void isLoginSuccess(boolean success) {

        NYLog.e("LOGIN SOCMED SUCCES INTENT");

        if(progressDialog != null) progressDialog.dismiss();
        if (success) {
//            if(getIntent().hasExtra(NYHelper.IS_MAIN_ACTIVITY)) {
//                Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            } else {
                setResult(RESULT_OK);
                finish();
//            }
//            //startService(new Intent(this, GKFirebaseInstanceIdService.class));
//            Intent intent = new Intent(AuthActivity.this, HomeActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            finish();
        }
    }

    @Override
    public void intentRegister(String email, String firstName, String lastName, String socmedType, String id, String accessToken, String profilePictureUrl) {

    }

    @Override
    public void onRegisterRequest(String email, String firstName, String lastName, String socmedType, String id, String accessToken, String profilePictureUrl) {

        /*if (progressDialog != null)progressDialog.dismiss();

        NYLog.e("CEK FB 2");

        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out);

        Bundle args = new Bundle();
        args.putString(NYHelper.EMAIL, email);
        args.putString(NYHelper.FIRST_NAME, firstName);
        args.putString(NYHelper.LAST_NAME, lastName);
        args.putString(NYHelper.SOCMED_TYPE, socmedType);
        args.putString(NYHelper.ID, id);
        args.putString(NYHelper.ACCESS_TOKEN, accessToken);
        args.putString(NYHelper.PROFILE_PICTURE, profilePictureUrl);

        transaction.replace(R.id.fragment_container, RegisterFragment.newInstance(args));
        transaction.addToBackStack(LoginFragment.class.getName());
        transaction.commit();*/
    }


    @Override
    public void onRegisterRequest(String socmedType, String authResult) {

        if (progressDialog != null)progressDialog.dismiss();

        NYLog.e("CEK FB 2");

        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in, android.R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out);

        Bundle args = new Bundle();

        args.putString(NYHelper.RESULT, authResult);
        args.putString(NYHelper.SOCMED_TYPE, socmedType);

        transaction.replace(R.id.fragment_container, RegisterFragment.newInstance(args));
        transaction.addToBackStack(LoginFragment.class.getName());
        transaction.commit();
    }

    @Override
    public void intentForgotPassword() {

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    @Override
    public void onSignUpFacebook(FBAuthResult result) {

    }

    @Override
    public void onLoginSuccess(String email) {

    }

    @Override
    public void onSignUpSuccess() {

    }

    @Override
    public void onForgot() {

    }
}
