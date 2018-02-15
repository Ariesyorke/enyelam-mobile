package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class EventList extends NYAbstractList<Event> {
    @Override
    public Class<Event> getHandledClass() {
        return Event.class;
    }
}