package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class LicenseTypeList extends NYAbstractList<LicenseType> {
    @Override
    public Class<LicenseType> getHandledClass() {
        return LicenseType.class;
    }
}