package com.nyelam.android;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nyelam.android.home.HomeActivity;

public class StarterActivity extends AppCompatActivity {

    private final int SPLASH_TIME = 3000;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starter);
        startSplashTimer();
    }

    private void startSplashTimer() {
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(StarterActivity.this, HomeActivity.class));
                        finish();
                    }
                }, SPLASH_TIME);
    }


}
