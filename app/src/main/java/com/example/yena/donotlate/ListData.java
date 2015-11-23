package com.example.yena.donotlate;

import java.io.Serializable;

/**
 * Created by yena on 2015-11-13.
 */
public class ListData implements Serializable{

    public static final String TABLE_NAME = "ListData", ID = "ID"
            , PLACE_ID = "PLACE_ID", PLACE_NAME = "PLACE_NAME", TITLE = "TITLE"
            , S_DAY = "S_DAY", D_DAY = "D_DAY", S_LATITUDE = "S_LATITUDE", S_LONGITUDE = "S_LONGITUDE"
            , D_LATITUDE = "D_LATITUDE", D_LONGITUDE = "D_LONGITUDE"
            ,IS_ACTIVATED = "IS_ACTIVATED", IS_COMPLETED = "IS_COMPLETED", IS_SUCCESS = "IS_SUCCESS";



    String title;
    String placeName;
    Day dDay;
    Day startDay;
    Day startToDday;

    Boolean isActivated = false; // 활성화는 완료되면 다시 false로 바꿔줘야햄
    Boolean isComplete = false;
    Boolean isSuccess = false;

    String getTitle(){
        return this.title;
    }

    ListData(String title, Day dDay){
        this.title = title;
        this.dDay = dDay;
    }
    ListData(String title){
        this.title = title;
    }
}
