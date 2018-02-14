package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class ModuleList extends NYAbstractList<Module> {
    @Override
    public Class<Module> getHandledClass() {
        return Module.class;
    }
}