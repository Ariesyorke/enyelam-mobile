package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DiveCenterList extends NYAbstractList<DiveCenter> {
    @Override
    public Class<DiveCenter> getHandledClass() {
        return DiveCenter.class;
    }
}