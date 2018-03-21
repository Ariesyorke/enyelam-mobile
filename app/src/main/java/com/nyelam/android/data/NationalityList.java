package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class NationalityList extends NYAbstractList<Nationality> {
    @Override
    public Class<Nationality> getHandledClass() {
        return Nationality.class;
    }
}