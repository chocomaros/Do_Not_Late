package com.example.yena.donotlate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by yena on 2015-12-04.
 */
public class StartAlarm {
    public static final int AUTO_START = 60*60*1000; // 한시간

    AlarmManager alarmManager;
    Intent intent;
    PendingIntent pendingIntent;

    StartAlarm(){
    }

     void setAlarm(Context context, ListData data){
         Log.d("setAlarm", "알람 셋했엉!");
        long millisecond;
        Calendar currentCalendar = Calendar.getInstance();

        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        millisecond = (data.dDay.toCalendar().getTimeInMillis() - AUTO_START) - currentCalendar.getTimeInMillis();
        if(millisecond <= 0){
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent); /// 즉시 시작
        }else{
            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + millisecond, pendingIntent); // millisecond 뒤에 시작
        }
    }

     void releaseAlarm(Context context){
        alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}


