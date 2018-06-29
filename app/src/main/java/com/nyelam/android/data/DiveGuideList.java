package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DiveGuideList extends NYAbstractList<DiveGuide> {
    @Override
    public Class<DiveGuide> getHandledClass() {
        return DiveGuide.class;
    }
}