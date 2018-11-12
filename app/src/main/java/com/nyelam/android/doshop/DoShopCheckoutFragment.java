package com.nyelam.android.doshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Helper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.view.NYDialogChooseAddress;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoShopCheckoutFragment extends BasicFragment implements NYDialogChooseAddress.Listener  {


    private CheckoutListener listener;

    @BindView(R.id.ll_container_input_personal_information)
    LinearLayout llContainerInputPersonal;
    @BindView(R.id.ll_container_personal_information)
    LinearLayout llContainerPersonal;

    @OnClick(R.id.tv_next_step_personal_information) void nextStepPersonalInformation(){
        llContainerInputPersonal.setVisibility(View.GONE);
        llContainerPersonal.setVisibility(View.VISIBLE);
        listener.stepView(2);
    }

    @OnClick(R.id.tv_checkout) void choosePayment(){
        listener.proceedToChoosePayment();
    }

    @OnClick(R.id.tv_choose_address) void chooseAddress(){
        //listener.proceedToChoosePayment();
//        NYDialogChooseAddress dialog = new NYDialogChooseAddress();
//        dialog.showChooseAddressDialog(getActivity(), null);
        Intent intent = new Intent(getActivity(), DoShopChooseAddressActivity.class);
        startActivity(intent);
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

    @Override
    public void onShopAgainListener() {

    }

    @Override
    public void onChoosedAddress(DoShopAddress address) {

    }
}
