package com.example.yena.donotlate;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ListData data;
        ArrayList<ListData> arrayList;
        arrayList = YenaDAO.getCurrentList(context);
        if(arrayList.size() == 0){

        }
        else{
            data = arrayList.get(0);
            if(data.isStarted){
            }
            else{
                YenaDAO.startDataUpdate(context,data);
                context.startService(new Intent(context, GpsService.class));
            }
        }
    }
}