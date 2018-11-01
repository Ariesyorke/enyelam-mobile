package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class DoShopCategoryList extends NYAbstractList<DoShopCategory> {
    @Override
    public Class<DoShopCategory> getHandledClass() {
        return DoShopCategory.class;
    }
}