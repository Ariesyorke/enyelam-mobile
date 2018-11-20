package com.nyelam.android.doshoporder;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopCartReturn;
import com.nyelam.android.helper.NYHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoShopCheckoutActivity extends BasicActivity implements CheckoutListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.container)
    FrameLayout container;

    @BindView(R.id.ll_container_step_view)
    LinearLayout llContainerStepView;

    @BindView(R.id.step_view)
    HorizontalStepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_checkout);
        ButterKnife.bind(this);
        initToolbar();
        initFragment();
        initStepView(1);
    }

    private void initStepView(int pos) {
        List<StepBean> stepsBeanList = new ArrayList<>();

        if (pos == 0){
            StepBean stepBean0 = new StepBean("Step 1",-1);
            StepBean stepBean1 = new StepBean("Step 2",-1);
            StepBean stepBean2 = new StepBean("Step 3",-1);
            stepsBeanList.add(stepBean0);
            stepsBeanList.add(stepBean1);
            stepsBeanList.add(stepBean2);
        } else if (pos == 1){
            StepBean stepBean0 = new StepBean("Step 1",0);
            StepBean stepBean1 = new StepBean("Step 2",-1);
            StepBean stepBean2 = new StepBean("Step 3",-1);
            stepsBeanList.add(stepBean0);
            stepsBeanList.add(stepBean1);
            stepsBeanList.add(stepBean2);
        } else if (pos == 2) {
            StepBean stepBean0 = new StepBean("Step 1",0);
            StepBean stepBean1 = new StepBean("Step 2",0);
            StepBean stepBean2 = new StepBean("Step 3",-1);
            stepsBeanList.add(stepBean0);
            stepsBeanList.add(stepBean1);
            stepsBeanList.add(stepBean2);
        } else if (pos == 3) {
            StepBean stepBean0 = new StepBean("Step 1",0);
            StepBean stepBean1 = new StepBean("Step 2",0);
            StepBean stepBean2 = new StepBean("Step 3",0);
            stepsBeanList.add(stepBean0);
            stepsBeanList.add(stepBean1);
            stepsBeanList.add(stepBean2);
        }

        stepView
                .setStepViewTexts(stepsBeanList)//总步骤
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(this, android.R.color.white))
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))
                .setStepViewComplectedTextColor(ContextCompat.getColor(this, android.R.color.white))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this, R.color.uncompleted_text_color))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this, R.drawable.complted))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this, R.drawable.default_icon))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this, R.drawable.attention));
    }

    private void initFragment() {
        //FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DoShopCartFragment fragment = new DoShopCartFragment();
        //DoShopCheckoutFragment fragment = new DoShopCheckoutFragment();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void proceedToCheckOut(DoShopCartReturn cartReturn) {

        Bundle bundle = new Bundle();
        bundle.putString(NYHelper.CART_RETURN, cartReturn.toString());
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DoShopCheckoutFragment fragment = new DoShopCheckoutFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.container, fragment)
                .addToBackStack("itemDetail")
                .commit();
    }

    @Override
    public void proceedToChoosePayment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DoShopChoosePaymentFragment fragment = new DoShopChoosePaymentFragment();
        fragmentTransaction.replace(R.id.container, fragment)
                .addToBackStack("checkout")
                .commit();
    }

    @Override
    public void proceedPayment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DoShopOrderSuccessFragment fragment = new DoShopOrderSuccessFragment();
        fragmentTransaction.replace(R.id.container, fragment)
                .addToBackStack("payment")
                .commit();
    }

    @Override
    public void stepView(int pos) {
        if (pos == 0){
            llContainerStepView.setVisibility(View.GONE);
        } else {
            llContainerStepView.setVisibility(View.VISIBLE);
        }

        initStepView(pos);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        toolbar.setContentInsetsRelative(0, contentInsetStartWithNavigation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }


}
