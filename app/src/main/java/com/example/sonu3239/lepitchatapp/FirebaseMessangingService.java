package com.example.sonu3239.lepitchatapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessangingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String notificationtitle=remoteMessage.getNotification().getTitle();
        String body=remoteMessage.getNotification().getBody();
        String clickaction=remoteMessage.getNotification().getClickAction();
        String uid=remoteMessage.getData().get("userid");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notificationtitle)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent resultIntent=new Intent(clickaction);
        resultIntent.putExtra("uid",uid);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
        int notificationId=(int)System.currentTimeMillis();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());
    }
}
