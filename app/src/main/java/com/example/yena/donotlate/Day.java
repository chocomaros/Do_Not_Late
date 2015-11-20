package com.example.yena.donotlate;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yena on 2015-11-13.
 */
public class Day implements Serializable{

    public static int KOREA_TIME_DIFFERENCE = 9;

    int year;
    int month; ///// 1월은 0
    int day;
    int hour; //// 24시 표기법
    int minute;

    public Day(int year, int month, int day, int hour, int minute){
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }
    public Day(Day day){
        this.year = day.year;
        this.month = day.month;
        this.day = day.day;
        this.hour = day.hour;
        this.minute = day.minute;
    }
    public Day() {
    }

    public Day currentTime(){
        Calendar calendar = Calendar.getInstance();
        Day currentDay = new Day(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        return currentDay;
    }

    Day timeDifference(Day day){

        int resultDay;
        int resultHour;
        int resultMinute;

        Calendar thisCalendar = Calendar.getInstance();
        thisCalendar.set(this.year, this.month, this.day, this.hour, this.minute);
        Calendar calendar = Calendar.getInstance();
        calendar.set(day.year, day.month, day.day, day.hour, day.minute);

        long thisTime = thisCalendar.getTimeInMillis();
        long time =  calendar.getTimeInMillis();

        //TODO month가 0이 1월이므로 오차생기는지 확인해야함
        long result = ((thisTime - time) > 0) ? (thisTime - time) : (time - thisTime);
        result = result / (1000 * 60); //// 분 단위

        if((result / (60 * 24)) > 0) {
            resultDay = (int) (result / (60 * 24));
            result -= resultDay * 60 * 24; /// 몇일인지
        }
        else
            resultDay = 0;

        if(((result / 60) > 0) && ((result / 60) < 24)){
            resultHour = (int)(result / 60);
            result -= resultHour * 60;
        }
        else
            resultHour = 0;

        resultMinute = (int)result;

        Day difference = new Day(); ///// 시간 차이 표현해줄땐, 일 / 시간 / 분 만
        difference.year = 0;
        difference.month = 0;
        difference.day = resultDay;
        difference.hour = resultHour;
        difference.minute = resultMinute;
        return difference;
    }

    void printTimeDifference(){
       // Day difference = timeDifference();
        //TODO 디데이와 현재 시간 출력 어떻게 해줄지?
    }
}
