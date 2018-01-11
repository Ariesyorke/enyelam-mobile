package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DiveServiceList extends NYAbstractList<DiveService> {
    @Override
    public Class<DiveService> getHandledClass() {
        return DiveService.class;
    }
}