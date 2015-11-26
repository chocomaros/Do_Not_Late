package com.example.yena.donotlate;

import android.content.Context;
import android.database.Cursor;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yena on 2015-11-26.
 */
public class YenaDAO {

    public static ArrayList<ListData> getCurrentList(Context context) {
        ArrayList<ListData> appointmentList = new ArrayList<>();

        ListDataDBHelper mDBHelper = new ListDataDBHelper(context);

        appointmentList.clear();
        Cursor cursor = mDBHelper.open().mDB.rawQuery("select * from " + ListData.TABLE_NAME + " order by " + ListData.D_DAY + ";", null);
        while (cursor.moveToNext())

        {
            ListData temp = new ListData(cursor);
//                Log.d("메인액티비티",temp.dDay+"");
            if (!temp.isComplete) {
                appointmentList.add(temp);
            }
        }

        cursor.close();
        mDBHelper.close();
        return appointmentList;
    }

    public static ArrayList<ListData> getCompletedList(Context context) {
        ArrayList<ListData> appointmentList = new ArrayList<>();

        ListDataDBHelper mDBHelper = new ListDataDBHelper(context);

        appointmentList.clear();
        Cursor cursor = mDBHelper.open().mDB.rawQuery("select * from " + ListData.TABLE_NAME + " order by " + ListData.D_DAY + ";", null);
        while (cursor.moveToNext())

        {
            ListData temp = new ListData(cursor);
//                Log.d("메인액티비티",temp.dDay+"");
            if (temp.isComplete) {
                appointmentList.add(temp);
            }
        }

        cursor.close();
        mDBHelper.close();
        return appointmentList;
    }

    public static void updateState(Context context, ListData listData){
        ListDataDBHelper mDBHelper = new ListDataDBHelper(context);
        int complete, success, start;
        if(listData.isComplete) complete = 1;
        else complete = 0;
        if(listData.isSuccess) success = 1;
        else success = 0;
        if(listData.isStarted) start = 1;
        else start = 0;
        mDBHelper.open().mDB.execSQL("update " + ListData.TABLE_NAME + " set " + ListData.IS_COMPLETED + " = " + complete + ", "
                + ListData.IS_SUCCESS + " = " + success + ", " + ListData.IS_STARTED + " = " + start + " where " + ListData.ID + " = " + listData.id + ";");
        mDBHelper.close();
    }

    public static void insertData(Context context, ListData listData){
        ListDataDBHelper mDBHelper = new ListDataDBHelper(context);
        mDBHelper.open().mDB.execSQL("insert into " + ListData.TABLE_NAME + " values (null,'"+listData.title+"','"+listData.placeID
                +"','"+listData.placeName+"',null,'"+listData.dDay.toString()+"', "+listData.dLatitude+", "+listData.dLongitude+", 0, 0, 0, 0 , 0)");
        mDBHelper.close();
    }
}

