package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class ProvinceList extends NYAbstractList<Province> {
    @Override
    public Class<Province> getHandledClass() {
        return Province.class;
    }
}