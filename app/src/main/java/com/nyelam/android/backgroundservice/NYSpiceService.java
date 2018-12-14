package com.nyelam.android.backgroundservice;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.nyelam.android.R;
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


    @Override
    public Notification createDefaultNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = createNotificationChannel("com.nyelam.android.robospice", "ROBOSPICE", NotificationManager.IMPORTANCE_LOW);
            Notification notification = new Notification.Builder(this, channelId)
                    .setSmallIcon(getApplicationInfo().icon)
                    .build();

            return notification;
        } else {
            return super.createDefaultNotification();
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName) {

        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);

        channel.setLightColor(getResources().getColor(R.color.colorAccent));

        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

        return channelId;

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private String createNotificationChannel(String channelId, String channelName, int priority) {

        NotificationChannel channel = new NotificationChannel(channelId, channelName, priority);

        channel.setLightColor(getResources().getColor(R.color.colorAccent));

        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.createNotificationChannel(channel);

        return channelId;

    }

}
