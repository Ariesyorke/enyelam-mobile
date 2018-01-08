package com.nyelam.android.data;

import com.nyelam.android.data.dao.DaoSession;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/8/2018.
 */

public interface DAODataBridge<RAW> {
    void copyFrom(RAW raw);

    void copyTo(RAW raw, DaoSession session, Object... args);
}