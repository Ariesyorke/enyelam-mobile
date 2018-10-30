package com.nyelam.android.doshop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nyelam.android.BasicFragment;
import com.nyelam.android.R;

public class DoShopCartFragment extends BasicFragment {




    public DoShopCartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_do_shop_cart;
    }


}
