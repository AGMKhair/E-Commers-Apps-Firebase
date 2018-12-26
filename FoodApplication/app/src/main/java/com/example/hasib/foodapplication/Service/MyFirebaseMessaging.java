package com.example.hasib.foodapplication.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Helper.NotificationHelper;
import com.example.hasib.foodapplication.MainActivity;
import com.example.hasib.foodapplication.Model.Notification;
import com.example.hasib.foodapplication.OrderStatus;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by HASIB on 12/15/2017.
 */

public class MyFirebaseMessaging extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){

            sendNotificationAPI26(remoteMessage);
        }else {

            sendNotification(remoteMessage);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotificationAPI26(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification=remoteMessage.getNotification();

        String title=notification.getTitle();
        String contentbody=notification.getBody();

        Intent in=new Intent(this, OrderStatus.class);

        in.putExtra(Common.PHONE_TEXT,Common.currentUser.getPhone());
        in.setFlags(in.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,in,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationHelper helper=new NotificationHelper(this);

        android.app.Notification.Builder builder=helper.getFoodAoolicationNotification(title,contentbody,pendingIntent,defaultSound);
        helper.getManager().notify(new Random().nextInt(),builder.build());




    }

    private void sendNotification(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification=remoteMessage.getNotification();
        Intent in=new Intent(this, OrderStatus.class);
        in.setFlags(in.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,in,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)

                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
              .setAutoCancel(true)
              .setContentIntent(pendingIntent)
              .setContentText(notification.getBody())
              .setContentTitle(notification.getTitle())
              .setSound(defaultSound);
        NotificationManager manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());


    }
}
