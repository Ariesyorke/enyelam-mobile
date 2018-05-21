
package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/5/2018.
 */

public class EquipmentRentList extends NYAbstractList<EquipmentRent> {
    @Override
    public Class<EquipmentRent> getHandledClass() {
        return EquipmentRent.class;
    }
}