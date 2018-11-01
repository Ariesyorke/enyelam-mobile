package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class DoShopAddressList extends NYAbstractList<DoShopAddress> {
    @Override
    public Class<DoShopAddress> getHandledClass() {
        return DoShopAddress.class;
    }
}