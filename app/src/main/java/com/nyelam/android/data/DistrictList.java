package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class DistrictList extends NYAbstractList<District> {
    @Override
    public Class<District> getHandledClass() {
        return District.class;
    }
}