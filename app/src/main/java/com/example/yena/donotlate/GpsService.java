package com.example.yena.donotlate;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.renderscript.Double2;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ServiceCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GpsService extends Service implements LocationListener{
    public static final float DEFAULT_DISTANCE = 50;
    public static final int TIMER_PERIOD = 60*1000;

    private SharedPreferences pref;
    private Context context = this;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;
    private Location currentLocation;
    private NotificationHandling notificationHandling;

    Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("서비스 온크리에이트", "들어옴?");
        Toast.makeText(context, "서비스 시작", Toast.LENGTH_LONG).show();
        ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
        if(!haveData(appointmentList)) {
            stopSelf();
        }
        else {
            pref = getSharedPreferences("pref",MODE_PRIVATE);
            Log.d("서비스", "현재 데이터 있대");
            ListData data = appointmentList.get(0);
            currentLocation = getLocation();
            if(currentLocation == null) {
                data.startLatitude = Double.parseDouble(pref.getString("LastLatitude", "37.5740339"));
                data.startLongitude = Double.parseDouble(pref.getString("LastLongitude", "126.97677499999998"));
            }else{
                data.startLatitude = getLocation().getLatitude();
                data.startLongitude = getLocation().getLongitude();
            }
            data.startDay = new Day(Calendar.getInstance());
            YenaDAO.gpsStartUpdate(context,data);
            timer = new Timer(true);
            timer.schedule(timerTask, TIMER_PERIOD, TIMER_PERIOD);
            notificationHandling = new NotificationHandling(context);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("온스타트커맨드", "들어옴?");
        currentLocation = getLocation();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null){
            timer.cancel();
        }
    }

    @TargetApi(23)
    public Location getLocation(){
        if(Build.VERSION.SDK_INT>=23) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

//                ActivityCompat.requestPMISSION_ACCESS_COARSE_LOCATION);permissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
//                        LocationService.MY_PER
            }
        }
        Location location = null;
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(context,"위치를 가져올 수 없습니다.",Toast.LENGTH_LONG).show();
            } else {
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            Log.d("네트워크에서 정보 "," location은 널 아님");
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                Log.d("gps에서 정보 "," location은 널 아님");
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            ListData data;
            ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
            Log.d("타이머","나 일하고있긩~");
            if(haveData(appointmentList)) {
                data = appointmentList.get(0);
                if(timePassed(data)){
                    Log.d("timePassed","시간 지났지롱");
                    data.isComplete = true;
                    data.isSuccess = false;
                    data.isStarted = false;
                    YenaDAO.updateState(context,data);
                    stopSelf();
                }
            }
        }
    };

    public Calendar getTime(){
        Calendar calendar = Calendar.getInstance();
        return calendar;
    }

    Boolean checkIn(ListData data, Location currentLocation){
        Location destination = new Location("");
        destination.setLatitude(data.dLatitude);
        destination.setLongitude(data.dLongitude);

        if(destination.distanceTo(currentLocation) < DEFAULT_DISTANCE){
            return true;
        } else return false;
    }

    Boolean timePassed(ListData data){
        Calendar current = Calendar.getInstance();
        Calendar dataCal = Calendar.getInstance();
        dataCal.set(data.dDay.year, data.dDay.month, data.dDay.day, data.dDay.hour, data.dDay.minute);

        if(current.getTimeInMillis() < dataCal.getTimeInMillis()){
            return false;
        } else return true;
    }

    Boolean haveData(ArrayList<ListData> arrayList){
        if(arrayList.size() == 0){
            return false;
        }else
            return true;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(context, "onLocationChanged 중!", Toast.LENGTH_LONG).show();
        currentLocation = location;
        ListData data;
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("LastLatitude", Double.toString(currentLocation.getLatitude()));
        editor.putString("LastLongitude", Double.toString(currentLocation.getLongitude()));
        editor.commit();

        ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
        if(haveData(appointmentList)){
            data = appointmentList.get(0);
            if(data.isStarted){
                Log.d("서비스","시작된거 데이터있네");
                if(checkIn(data,currentLocation)){  ///// 제대로 도착해서 완료된거
                    Log.d("서비스","체크인 했대");
                    data.isComplete = true;
                    data.isSuccess = true;
                    data.isStarted = false;
                    YenaDAO.updateState(context,data);
                    notificationHandling.successNotification(data.placeName);
                    stopSelf();
                }
                else{
                    if(timePassed(data)){       /// 도착 못하고 끝난거
                        Toast.makeText(context,"시간지났지롱",Toast.LENGTH_LONG).show();
                        notificationHandling.failNotification(data.placeName);
                        data.isComplete = true;
                        data.isSuccess = false;
                        data.isStarted = false;
                        YenaDAO.updateState(context,data);
                        stopSelf();
                    }
                }
            }
        }
    }
}
