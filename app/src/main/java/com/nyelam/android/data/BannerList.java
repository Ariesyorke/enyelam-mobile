package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class BannerList extends NYAbstractList<Banner> {
    @Override
    public Class<Banner> getHandledClass() {
        return Banner.class;
    }
}