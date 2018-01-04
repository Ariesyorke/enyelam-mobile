package com.nyelam.android.backgroundservice;

import android.app.Application;

import com.nyelam.android.robospice.NYObjectPersisterFactory;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYSpiceService extends SpiceService {

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new NYObjectPersisterFactory(application));
        return cacheManager;
    }

    @Override
    public int getThreadCount() {
        return 3;
    }

}
