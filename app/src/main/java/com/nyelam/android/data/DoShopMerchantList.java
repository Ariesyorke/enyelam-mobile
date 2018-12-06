package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class DoShopMerchantList extends NYAbstractList<DoShopMerchant> {
    @Override
    public Class<DoShopMerchant> getHandledClass() {
        return DoShopMerchant.class;
    }
}