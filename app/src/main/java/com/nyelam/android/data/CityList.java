package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class CityList extends NYAbstractList<City> {
    @Override
    public Class<City> getHandledClass() {
        return City.class;
    }
}