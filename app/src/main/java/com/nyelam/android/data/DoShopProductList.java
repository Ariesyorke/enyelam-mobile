package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DoShopProductList extends NYAbstractList<DoShopProduct> {
    @Override
    public Class<DoShopProduct> getHandledClass() {
        return DoShopProduct.class;
    }
}