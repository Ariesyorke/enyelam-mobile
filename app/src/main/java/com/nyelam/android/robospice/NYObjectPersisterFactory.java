package com.nyelam.android.robospice;

import android.app.Application;

import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;
import com.octo.android.robospice.persistence.file.InFileObjectPersisterFactory;

import java.io.File;
import java.util.List;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYObjectPersisterFactory extends InFileObjectPersisterFactory {

    public NYObjectPersisterFactory(Application application)  throws CacheCreationException {
        super(application);
    }

    public NYObjectPersisterFactory(Application application, File cacheFolder)  throws CacheCreationException {
        super(application, cacheFolder);
    }

    public NYObjectPersisterFactory(Application application, List<Class<?>> listHandledClass) throws CacheCreationException {
        super(application, listHandledClass);
    }

    public NYObjectPersisterFactory(Application application, List<Class<?>> listHandledClasses, File cacheFolder)  throws CacheCreationException {
        super(application, listHandledClasses, cacheFolder);
    }

    @Override
    public <T> InFileObjectPersister<T> createInFileObjectPersister(Class<T> clazz, File cacheFolder) throws CacheCreationException {
        return new NYObjectPersister<T>(getApplication(), clazz, cacheFolder);
    }
}
