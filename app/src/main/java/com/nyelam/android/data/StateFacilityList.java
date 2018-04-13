package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class StateFacilityList extends NYAbstractList<StateFacility> {
    @Override
    public Class<StateFacility> getHandledClass() {
        return StateFacility.class;
    }
}