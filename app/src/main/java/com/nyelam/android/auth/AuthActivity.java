package com.nyelam.android.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.home.HomeActivity;
import com.nyelam.android.storage.LoginStorage;

public class AuthActivity extends BasicActivity implements
    LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener,
    ForgotPasswordFragment.OnFragmentInteractionListener{

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
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

}
