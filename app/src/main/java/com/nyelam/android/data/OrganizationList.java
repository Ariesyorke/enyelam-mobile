package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class OrganizationList extends NYAbstractList<Organization> {
    @Override
    public Class<Organization> getHandledClass() {
        return Organization.class;
    }
}