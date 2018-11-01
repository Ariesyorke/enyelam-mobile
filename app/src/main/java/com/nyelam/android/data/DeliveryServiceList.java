package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class DeliveryServiceList extends NYAbstractList<DeliveryService> {
    @Override
    public Class<DeliveryService> getHandledClass() {
        return DeliveryService.class;
    }
}