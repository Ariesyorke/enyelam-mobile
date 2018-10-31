package com.nyelam.android.doshop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoShopCheckoutFragment extends BasicFragment  {


    private CheckoutListener listener;

    @OnClick(R.id.tv_checkout) void choosePayment(){
        listener.proceedToChoosePayment();
    }

    public DoShopCheckoutFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener = (CheckoutListener) getActivity();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_do_shop_checkout;
    }

}
