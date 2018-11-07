package com.nyelam.android.doshop;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;

import com.baoyachi.stepview.HorizontalStepView;
import com.baoyachi.stepview.bean.StepBean;
import com.nyelam.android.BasicActivity;
import com.nyelam.android.R;
import com.nyelam.android.dodive.DoDiveFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoShopCheckoutActivity extends BasicActivity implements CheckoutListener {

    @BindView(R.id.container)
    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_do_shop_checkout);
        ButterKnife.bind(this);
        initFragment();
        initStepView();
    }

    private void initStepView() {
        HorizontalStepView setpview5 = (HorizontalStepView) findViewById(R.id.step_view);
        List<StepBean> stepsBeanList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("Step 1",1);
        StepBean stepBean1 = new StepBean("Step 2",0);
        StepBean stepBean2 = new StepBean("Step 3",-1);
        stepsBeanList.add(stepBean0);
        stepsBeanList.add(stepBean1);
        stepsBeanList.add(stepBean2);

        setpview5
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
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void proceedToCheckOut() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DoShopCheckoutFragment fragment = new DoShopCheckoutFragment();
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


}
