package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class SummaryList extends NYAbstractList<Summary> {
    @Override
    public Class<Summary> getHandledClass() {
        return Summary.class;
    }
}