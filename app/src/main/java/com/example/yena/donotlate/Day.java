package com.example.yena.donotlate;

import android.util.Log;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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

    public Day(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }

    public Day(Calendar calendar){
        year = calendar.YEAR;
        month = calendar.MONTH;
        day = calendar.DAY_OF_MONTH;
        hour = calendar.HOUR_OF_DAY;
        minute = calendar.MINUTE;
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

    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        return sdf.format(new Date(calendar.getTimeInMillis()));
    }

    public Calendar toCalendar(){
        Calendar returnCalendar = Calendar.getInstance();
        returnCalendar.set(year, month, day, hour, minute,0);
       Log.d("toCalendar", returnCalendar.YEAR+"년"+ returnCalendar.MONTH+"월"+ returnCalendar.DAY_OF_MONTH+"일"+ returnCalendar.HOUR_OF_DAY+"시"+ returnCalendar.MINUTE+"분");
        return returnCalendar;
        }

        }
