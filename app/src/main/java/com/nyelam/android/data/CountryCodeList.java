package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class CountryCodeList extends NYAbstractList<CountryCode> {
    @Override
    public Class<CountryCode> getHandledClass() {
        return CountryCode.class;
    }
}