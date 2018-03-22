package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class CountryList extends NYAbstractList<Country> {
    @Override
    public Class<Country> getHandledClass() {
        return Country.class;
    }
}