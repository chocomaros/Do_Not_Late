package com.example.yena.donotlate;

import android.database.Cursor;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yena on 2015-11-13.
 */
public class ListData implements Serializable{

    public static final String TABLE_NAME = "ListData", ID = "ID"
            , PLACE_ID = "PLACE_ID", PLACE_NAME = "PLACE_NAME", TITLE = "TITLE"
            , S_DAY = "S_DAY", D_DAY = "D_DAY", S_LATITUDE = "S_LATITUDE", S_LONGITUDE = "S_LONGITUDE"
            , D_LATITUDE = "D_LATITUDE", D_LONGITUDE = "D_LONGITUDE"
            ,IS_ACTIVATED = "IS_ACTIVATED", IS_COMPLETED = "IS_COMPLETED", IS_SUCCESS = "IS_SUCCESS"
            ,IS_STARTED = "IS_STARTED";



    String title;
    String placeName, placeID;
    Day dDay;
    Day startDay;
    Day startToDday;
    double dLatitude, dLongitude, startLatitude, startLongitude;

    Boolean isActivated = false; // 활성화는 완료되면 다시 false로 바꿔줘야햄
    Boolean isComplete = false;
    Boolean isSuccess = false;
    Boolean isStarted = false;

    ListData(String title, Day dDay){
        this.title = title;
        this.dDay = dDay;
    }
    ListData(String title){
        this.title = title;
    }

    ListData(Cursor cursor){
        try{
            title = cursor.getString(1);
            placeID = cursor.getString(2);
            placeName = cursor.getString(3);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.KOREA);
            startDay = new Day(sdf.parse(cursor.getString(4)));
            dDay = new Day(sdf.parse(cursor.getString(5)));
            dLatitude = cursor.getDouble(6);
            dLongitude = cursor.getDouble(7);
            startLatitude = cursor.getDouble(8);
            startLongitude = cursor.getDouble(9);
            isActivated = (cursor.getInt(10) == 1);
            isComplete = (cursor.getInt(11) == 1);
            isSuccess = (cursor.getInt(12) == 1);
            isStarted = (cursor.getInt(13) == 1);

        }catch (Exception e){

        }

    }
}
