package com.example.hasib.foodapplication.Helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.hasib.foodapplication.R;

import java.net.URI;

/**
 * Created by HASIB on 6/11/2018.
 */

public class NotificationHelper extends ContextWrapper {

    private static  final String FOOD_APPLICATION_CHANEL_ID="com.example.hasib.foodapplication";
    private static  final String FOOD_APPLICATION_CHANEL_NAME="Food Application User";

    public NotificationManager notificationManager;


    public NotificationHelper(Context base) {
        super(base);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            createChanel();

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChanel() {



        NotificationChannel notificationChannel=new NotificationChannel(FOOD_APPLICATION_CHANEL_ID,FOOD_APPLICATION_CHANEL_NAME,NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.enableLights(false);
        notificationChannel.enableVibration(true);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        
        getManager().createNotificationChannel(notificationChannel);
    }

    public NotificationManager getManager() {
        if (notificationManager==null ){
            notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

   @RequiresApi(api = Build.VERSION_CODES.O)
   public  android.app.Notification.Builder getFoodAoolicationNotification(String title, String body,
                                                                           PendingIntent pendingIntent,
                                                                           Uri notificationSound){

        return  new android.app.Notification.Builder(getApplicationContext(),FOOD_APPLICATION_CHANEL_ID)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setContentTitle(title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(body)
                .setSound(notificationSound)
                ;

   }




}
