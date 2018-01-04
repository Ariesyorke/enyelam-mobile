package com.nyelam.android.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;

public class AuthActivity extends BasicActivity implements
    LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener {

    public static final String REQ_INVALID_TOKEN = "invalid_token";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        initFragment();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, LoginFragment.newInstance()).commit();
    }



}
