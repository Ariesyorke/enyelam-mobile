package com.nyelam.android.doshop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nyelam.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoShopCheckoutFragment extends Fragment {

    public DoShopCheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_do_shop_checkout, container, false);
    }

}
