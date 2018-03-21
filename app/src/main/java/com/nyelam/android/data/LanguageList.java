package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class LanguageList extends NYAbstractList<Language> {
    @Override
    public Class<Language> getHandledClass() {
        return Language.class;
    }
}