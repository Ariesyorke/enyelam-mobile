package com.nyelam.android.doshop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoShopFilterActivity extends BasicActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_filter);
        ButterKnife.bind(this);
        tvTitle.setText("Filters");
    }


}
