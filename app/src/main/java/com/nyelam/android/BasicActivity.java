package com.nyelam.android;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/3/2018.
 */

public abstract class BasicActivity extends AppCompatActivity {

    private ImageView backImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initToolbar(boolean isHomeBackEnable) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_button_white);
        setSupportActionBar(toolbar);
        if (isHomeBackEnable){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
            toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
        }
    }

    protected void backEnable(){
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


}
