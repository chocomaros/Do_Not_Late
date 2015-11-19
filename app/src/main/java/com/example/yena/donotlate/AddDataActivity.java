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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AddDataActivity extends Activity {
    TextView tvTime,tvDate;
    EditText etTitle;
    ListData data = new ListData("",new Day());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        tvTime = (TextView) findViewById(R.id.tv_insert_time);
        tvDate =(TextView) findViewById(R.id.tv_insert_date);

        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog((new ContextThemeWrapper((AddDataActivity.this),
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar)), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                dialog.show();
            }
        });

       tvTime.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Calendar calendar = Calendar.getInstance();
               TimePickerDialog timePicker = new TimePickerDialog((new ContextThemeWrapper(AddDataActivity.this,
                       android.R.style.Theme_Holo_Light_Dialog_NoActionBar)), myTimeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
               timePicker.show();
           }
       });

    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            data.dDay.year = year;
            data.dDay.month = monthOfYear;
            data.dDay.day = dayOfMonth;
            tvDate.setText(" "+year+". "+(monthOfYear+1)+". "+dayOfMonth);
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            data.dDay.hour = hourOfDay;
            data.dDay.minute = minute;
            if(minute < 10){
                tvTime.setText(" "+hourOfDay+": 0"+minute);
            }
            else {
                tvTime.setText(" " + hourOfDay + ": " + minute);
            }
        }
    };

}
