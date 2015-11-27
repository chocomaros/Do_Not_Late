package com.example.yena.donotlate;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
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

public class GpsService extends Service implements LocationListener{
    public static final float DEFAULT_DISTANCE = 50;
    private static final int REQUEST_CODE_LOCATION = 2;

    private Context context;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location;
    double latitude, longitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    public GpsService(){

    }
    public GpsService(Context context) {
        this.context = context;
        getLocation();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "서비스 시작", Toast.LENGTH_LONG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        ListData data;
        Location currentLocation;
        currentLocation = getLocation();

        ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
        data = appointmentList.get(0);

        if(checkIn(data,currentLocation)){  ///// 제대로 도착해서 완료된거
            appointmentList.get(0).isComplete = true;
            appointmentList.get(0).isSuccess = true;
            appointmentList.get(0).isStarted = false;
            YenaDAO.updateState(getApplicationContext(),data);
            onDestroy();
        }
        else{
            if(timePassed(data)){       /// 도착 못하고 끝난거
                appointmentList.get(0).isComplete = true;
                appointmentList.get(0).isSuccess = false;
                appointmentList.get(0).isStarted = false;
                YenaDAO.updateState(getApplicationContext(),data);
                onDestroy();
            }
        }

        return START_STICKY;
    }

    @TargetApi(23)
    public Location getLocation(){
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

//            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
//                    LocationService.MY_PERMISSION_ACCESS_COARSE_LOCATION );
        }

        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(),"위치를 가져올 수 없습니다.",Toast.LENGTH_LONG);
            } else {
                this.canGetLocation = true;
                // 네트워크 정보로 부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // 위도 경도 저장
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
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
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
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

//    private void checkPermission() {
//        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                // Explain to the user why we need to write the permission.
//                Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
//            }
//
//            // MY_PERMISSION_REQUEST_STORAGE is an
//            // app-defined int constant
//
//        } else {
//            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
//        }
//    }

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

    }
}
