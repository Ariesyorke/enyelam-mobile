package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class AreaList extends NYAbstractList<Area> {
    @Override
    public Class<Area> getHandledClass() {
        return Area.class;
    }
}