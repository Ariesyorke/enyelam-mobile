package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class CourierList extends NYAbstractList<Courier> {
    @Override
    public Class<Courier> getHandledClass() {
        return Courier.class;
    }
}