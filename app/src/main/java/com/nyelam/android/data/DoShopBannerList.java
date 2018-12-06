package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class DoShopBannerList extends NYAbstractList<DoShopBanner> {
    @Override
    public Class<DoShopBanner> getHandledClass() {
        return DoShopBanner.class;
    }
}