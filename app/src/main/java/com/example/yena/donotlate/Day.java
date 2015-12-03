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

    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);

        return sdf.format(new Date(calendar.getTimeInMillis()));
    }

    public Calendar toCalendar(){
        Calendar returnCalendar = Calendar.getInstance();
        returnCalendar.set(year, month, day, hour, minute,0);
        return returnCalendar;
        }

        }
