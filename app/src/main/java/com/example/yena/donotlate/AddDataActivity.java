package com.example.yena.donotlate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddDataActivity extends Activity {

    ListData data = new ListData("",new Day());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        Log.d("ㅁㄴㅇㄹ","안되나?");
        Log.d("날짜", " " + data.dDay.year + "년" + data.dDay.month + "월" + data.dDay.day + "일");

        findViewById(R.id.tv_insert_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog((new ContextThemeWrapper((AddDataActivity.this),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar)), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                Log.d("날짜"," "+data.dDay.year+"년"+data.dDay.month+"월"+data.dDay.day+"일");
                dialog.show();
                Log.d("날짜"," "+data.dDay.year+"년"+data.dDay.month+"월"+data.dDay.day+"일");
            }
        });

        findViewById(R.id.tv_insert_time).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog timePicker = new TimePickerDialog((new ContextThemeWrapper(AddDataActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar)),myTimeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
                timePicker.show();
            }
        });


    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            AddDataActivity.this.data.dDay.year = year;
            AddDataActivity.this.data.dDay.month = monthOfYear;
            AddDataActivity.this.data.dDay.day = dayOfMonth;
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
            Log.d("날짜", " " + data.dDay.year + "년" + data.dDay.month + "월" + data.dDay.day + "일");
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String time = "Hour: " + String.valueOf(hourOfDay) + "\n"
                    + "Minute: " + String.valueOf(minute);
            Toast.makeText(getApplicationContext(), time, Toast.LENGTH_LONG).show();
        }
    };

}
