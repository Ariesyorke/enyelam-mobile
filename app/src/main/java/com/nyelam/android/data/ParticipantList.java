package com.nyelam.android.data;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/11/2018.
 */

public class ParticipantList extends NYAbstractList<Participant> {
    @Override
    public Class<Participant> getHandledClass() {
        return Participant.class;
    }
}