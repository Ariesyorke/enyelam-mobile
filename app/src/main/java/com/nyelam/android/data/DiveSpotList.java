package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DiveSpotList extends NYAbstractList<DiveSpot> {
    @Override
    public Class<DiveSpot> getHandledClass() {
        return DiveSpot.class;
    }
}