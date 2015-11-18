package com.example.yena.donotlate;

/**
 * Created by yena on 2015-11-13.
 */
public class ListData {

    String title;
    String destination;
    Day dDay;
    Day startDay;
    Day startToDday;

    Boolean isComplete = false;

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
