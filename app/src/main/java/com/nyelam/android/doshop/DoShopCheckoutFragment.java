package com.nyelam.android.doshop;


import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Helper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;
import com.nyelam.android.data.DoShopAddress;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.view.NYDialogChooseAddress;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoShopCheckoutFragment extends BasicFragment implements NYDialogChooseAddress.Listener  {


    private CheckoutListener listener;
    private DoShopAddress address;

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
        //startActivity(intent);
        startActivityForResult(intent, 100);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you want to pass your result to the activity.
//        Toast.makeText(getActivity(), "rquest code : "+String.valueOf(requestCode), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "result code : "+String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), "is address Exist : "+String.valueOf(data.hasExtra(NYHelper.ADDRESS)), Toast.LENGTH_SHORT).show();
        if (data != null && data.hasExtra(NYHelper.ADDRESS)){
            try {
                JSONObject obj = new JSONObject(data.getStringExtra(NYHelper.ADDRESS));
                address.parse(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
