package com.nyelam.android.robospice;

import android.app.Application;

import com.nyelam.android.dev.NYLog;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Aprilian Nur Wakhid Daini on 1/4/2018.
 */

public class NYObjectPersister  <T> extends InFileObjectPersister<T> {

    public NYObjectPersister(Application application, Class<T> clazz) throws CacheCreationException {
        super(application, clazz);
    }

    public NYObjectPersister(Application application, Class<T> clazz, File cacheFolder) throws CacheCreationException {
        super(application, clazz, cacheFolder);
    }


    @Override
    protected T readCacheDataFromFile(File file) throws CacheLoadingException {
        String stringData = null;
        try {
            FileUtils.readFileToString(file, CharEncoding.UTF_8);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            NYLog.w("file " + file.getAbsolutePath() + " does not exist");
        } catch (IOException e) {
            e.printStackTrace();
            throw new CacheLoadingException(e);
        }
        if(stringData!=null){
            try{
                T temp = getHandledClass().newInstance();
                return temp;
            } catch (InstantiationException e) {
                e.printStackTrace();
                NYLog.e(e);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                NYLog.e(e);
            }
        }
        return null;
    }

    @Override
    public T saveDataToCacheAndReturnData(T data, Object cacheKey) throws CacheSavingException {
        String toSaveString = data.toString();
        try{
            if(isAsyncSaveEnabled()){
                AsyncSaveRunnable t = new AsyncSaveRunnable(toSaveString, cacheKey){
                    @Override
                    public void run() {
                        try{
                            FileUtils.writeStringToFile(getCacheFile(cacheKey),this.data, CharEncoding.UTF_8);
                        } catch (IOException e){
                            e.printStackTrace();
                            NYLog.e("An error occured on saving request " + cacheKey + " data asynchronously");
                        }
                    }
                };
                t.start();
            } else {
                FileUtils.writeStringToFile(getCacheFile(cacheKey), toSaveString, CharEncoding.UTF_8);
            }
        } catch (Exception e){
            throw new CacheSavingException(e);
        }
        return data;
    }

    private static class AsyncSaveRunnable extends Thread {
        String data;
        Object cacheKey;

        private AsyncSaveRunnable(String data, Object cacheKey){
            this.data = data;
            this.cacheKey = cacheKey;
        }
    }
}
