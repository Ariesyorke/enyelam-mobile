package com.nyelam.android.doshop;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.dodive.DoDiveFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoShopCheckoutActivity extends BasicActivity {

    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_checkout);
        ButterKnife.bind(this);
        initFragment();
    }

    private void initFragment() {
        //FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DoShopCartFragment fragment = new DoShopCartFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }




}
