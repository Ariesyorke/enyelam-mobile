
package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class CategoryList extends NYAbstractList<Category> {
    @Override
    public Class<Category> getHandledClass() {
        return Category.class;
    }
}