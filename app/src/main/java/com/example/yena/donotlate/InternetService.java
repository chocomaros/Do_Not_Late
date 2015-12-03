package com.example.yena.donotlate;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;

import com.google.android.gms.vision.barcode.Barcode;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class InternetService extends Service {

    public static final int INTERNET_TIMER_PERIOD = 10*1000; // 10초
    Timer timer;

    public InternetService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer = new Timer(true);
        timer.schedule(timerTask, 0, INTERNET_TIMER_PERIOD);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            setWifiOff();
        }
    };

    private void setWifiOff(){
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
