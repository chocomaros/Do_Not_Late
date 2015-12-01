package com.example.yena.donotlate;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by yena on 2015-12-01.
 */
public class NotificationHandling {

    Context context;
    NotificationManager notificationManager;
    PendingIntent pendingIntent;
    Intent intent;

    public NotificationHandling(Context context){
        this.context = context;
    }


    void successNotification(String title){
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        intent = new Intent(context, StartActivity.class);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_check)
                .setTicker(title + "에 도착을 완료하였습니다.")
                .setWhen(System.currentTimeMillis())
                .setContentTitle("성공하였습니다.")
                .setContentInfo(title + "에 도착을 완료하였습니다.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        notificationManager.notify(1,builder.build());
    }

    void failNotification(String title){
        notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        intent = new Intent(context, StartActivity.class);
        pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_x)
                .setTicker(title + "에 도착을 하지 못했습니다.")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent)
                .setContentTitle("실패하였습니다. 인터넷을 차단합니다.")
                .setContentInfo(title + "에 도착을 하지 못했습니다.")
                .setAutoCancel(true);
        notificationManager.notify(2,builder.build());
    }

}
