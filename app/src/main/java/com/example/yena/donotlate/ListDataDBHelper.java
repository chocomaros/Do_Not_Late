package com.example.yena.donotlate;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

/**
 * Created by yena on 2015-11-24.
 */
public class ListDataDBHelper {
    private static final String DATABASE_NAME = "addressbook.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;

    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table "+ListData.TABLE_NAME+"("
                    +ListData.ID+" integer primary key autoincrement, " //0
                    +ListData.TITLE+" text not null , "                 //1
                    +ListData.PLACE_ID+" text not null , "              //2
                    +ListData.PLACE_NAME+" text  , "                    //3
                    +ListData.S_DAY+" text, "                           //4
                    +ListData.D_DAY+" text not null , "                 //5
                    +ListData.D_LATITUDE+"double , "                    //6
                    +ListData.D_LONGITUDE+"double , "                   //7
                    +ListData.S_LATITUDE+"double , "                    //8
                    +ListData.S_LONGITUDE+"double , "                   //9
                    +ListData.IS_ACTIVATED+" int , "                //10
                    +ListData.IS_COMPLETED+" int , "                //11
                    +ListData.IS_SUCCESS+" int );");                //12

        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ ListData.TABLE_NAME);
            onCreate(db);
        }
    }

    public ListDataDBHelper(Context context){
        this.mCtx = context;
    }

    public ListDataDBHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }
}
