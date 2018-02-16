package com.nyelam.android.dodive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;

public class FilterListDiveCenterActivity extends BasicActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list_dive_center);
        initView();
        initToolbar(true);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
