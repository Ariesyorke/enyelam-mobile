package com.nyelam.android.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nyelam.android.R;
import com.nyelam.android.data.InboxData;
import com.nyelam.android.dev.NYLog;
import com.nyelam.android.helper.NYHelper;
import com.nyelam.android.http.NYEquipmentRentListRequest;
import com.nyelam.android.http.NYSendFirebaseTokenRequest;
import com.nyelam.android.inbox.InboxActivity;
import com.nyelam.android.storage.LoginStorage;
import com.nyelam.android.view.OreoNotification;

import org.json.JSONException;
import org.json.JSONObject;

public class FirebaseInstanceIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        LoginStorage storage = new LoginStorage(getApplicationContext());
        if (storage.isUserLogin()){
            try {
                NYSendFirebaseTokenRequest req = new NYSendFirebaseTokenRequest(this, s);
                req.loadDataFromNetwork();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        LoginStorage storage = new LoginStorage(this);
        if (storage.isUserLogin()){
            String title = remoteMessage.getData().get(NYHelper.TITLE);
            String body = remoteMessage.getData().get(NYHelper.BODY);
            String inbox = remoteMessage.getData().get(NYHelper.INBOX);
            //TODO Action open inbox detail
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                sendOreoNotification(remoteMessage);
//            } else {
//                sendNotification(remoteMessage);
//            }
            SharedPreferences pref = getSharedPreferences("PREFS", MODE_PRIVATE);
            String currentTicket = pref.getString(NYHelper.TICKET_ID, "none");

            if(NYHelper.isAppInForeground) {
                InboxData inboxData = null;
                try {
                    JSONObject obj = new JSONObject(inbox);
                    inboxData = new InboxData();
                    inboxData.parse(obj);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if (inboxData != null) {
                    if (!currentTicket.equalsIgnoreCase(String.valueOf(inboxData.getTicketId()))) {
                        Intent i = new Intent();
                        i.setAction("com.nyelam.android.CHAT_SERVICE");
                        i.putExtra(NYHelper.TITLE, title);
                        i.putExtra(NYHelper.BODY, body);
                        i.putExtra(NYHelper.TICKET_ID, String.valueOf(inboxData.getTicketId()));
                        sendBroadcast(i);
                    }
                }
            }
        }
    }

    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get(NYHelper.TITLE);
        String body = remoteMessage.getData().get(NYHelper.BODY);
        String inbox = remoteMessage.getData().get(NYHelper.INBOX);

        Intent intent = new Intent(this, InboxActivity.class);
        InboxData inboxData = null;
        try {
            JSONObject obj = new JSONObject(inbox);
            inboxData = new InboxData();
            inboxData.parse(obj);
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (inboxData != null){
            intent.putExtra(NYHelper.TITLE, title);
            intent.putExtra(NYHelper.TICKET_ID, String.valueOf(inboxData.getTicketId()));
            intent.putExtra(NYHelper.STATUS, inboxData.getStatus());
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSoundUri);
        oreoNotification.getManager().notify(0, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get(NYHelper.TITLE);
        String body = remoteMessage.getData().get(NYHelper.BODY);
        String inbox = remoteMessage.getData().get(NYHelper.INBOX);

        Intent intent = new Intent(this, InboxActivity.class);
        InboxData inboxData = null;
        try {
            JSONObject obj = new JSONObject(inbox);
            inboxData = new InboxData();
            inboxData.parse(obj);
        }catch (JSONException e){
            e.printStackTrace();
        }
        if (inboxData != null){
            intent.putExtra(NYHelper.TITLE, title);
            intent.putExtra(NYHelper.TICKET_ID, String.valueOf(inboxData.getTicketId()));
            intent.putExtra(NYHelper.STATUS, inboxData.getStatus());
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(data.replace("[\\D]", ""));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_nyelam))
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        int i = 0;
//        if ( j > 0){
//            i = j;
//        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}
