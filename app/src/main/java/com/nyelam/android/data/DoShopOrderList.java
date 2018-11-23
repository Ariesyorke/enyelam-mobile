package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoShopOrderList extends NYAbstractList<DoShopOrder> {
    @Override
    public Class<DoShopOrder> getHandledClass() {
        return DoShopOrder.class;
    }
}