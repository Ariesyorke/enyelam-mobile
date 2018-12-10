package com.nyelam.android.view;

import android.view.View;

import com.nyelam.android.data.DoShopMerchant;

public abstract class ProductCheckoutItemListener implements View.OnClickListener {

    public DoShopMerchant merchant;
    public int index;

    public ProductCheckoutItemListener(DoShopMerchant merchant, int index) {
        this.merchant = merchant;
        this.index = index;
    }

    @Override
    public void onClick(View view) {
        onItemClicked(view, merchant, index);
    }

    public abstract void onItemClicked(View view, DoShopMerchant merchant, int index);
}
