package com.example.yena.donotlate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.Calendar;

public class AddDataActivity extends AppCompatActivity {
    TextView tvTime,tvDate,tvPlace;
    EditText etTitle;
    ListData data = new ListData("",new Day());
    Boolean checkTitle = false;
    Boolean checkDate = false;
    Boolean checkTime = false;
    Boolean checkPlace = false;
    final int REQUEST_PLACE_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        etTitle = (EditText) findViewById(R.id.et_insert_title);
        tvTime = (TextView) findViewById(R.id.tv_insert_time);
        tvDate = (TextView) findViewById(R.id.tv_insert_date);
        tvPlace = (TextView) findViewById(R.id.tv_insert_place);

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

        tvPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent = intentBuilder.build(AddDataActivity.this);
                // Start the Intent by requesting a result, identified by a request code.
                startActivityForResult(intent, REQUEST_PLACE_PICKER);
                }catch (Exception e){

                }
//                Intent intent = new Intent(AddDataActivity.this, Main2Activity.class);
//                startActivity(intent);
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // BEGIN_INCLUDE(activity_result)
        if (requestCode == REQUEST_PLACE_PICKER) {
            // This result is from the PlacePicker dialog.


            if (resultCode == Activity.RESULT_OK) {
                /* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 */
                final Place place = PlacePicker.getPlace(data, AddDataActivity.this);

                /* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 */
                final CharSequence name = place.getName();
                final CharSequence address = place.getAddress();
                final CharSequence phone = place.getPhoneNumber();
                final String placeId = place.getId();
                String attribution = PlacePicker.getAttributions(data);
                if(attribution == null){
                    attribution = "";
                }
                tvPlace.setText(name);
            } else {
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            data.dDay.year = year;
            data.dDay.month = monthOfYear;
            data.dDay.day = dayOfMonth;
            tvDate.setText("날짜 => "+year+". "+(monthOfYear+1)+". "+dayOfMonth);
            tvDate.setTextColor(Color.BLACK);
            checkDate = true;
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            data.dDay.hour = hourOfDay;
            data.dDay.minute = minute;
            if(minute < 10){
                tvTime.setText("시간 => "+hourOfDay+" : 0"+minute);
            }
            else {
                tvTime.setText("시간 => " + hourOfDay + " : " + minute);
            }
            tvTime.setTextColor(Color.BLACK);
            checkTime = true;
        }
    };

    private void setTitle(){
        data.title = etTitle.getText().toString();
        if(data.title.length() != 0) checkTitle = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            setTitle();
            //TODO 디비에 저장하기, checkPlace도 추가
            if(checkTitle && checkDate && checkTime){
                if(!isTimePassed()){

                    finish();
                }
                else{
                    printAlertDialog("시간 확인","과거 시간을 선택하셨습니다.");
                }
            }
            else{
                printAlertDialog("선택 확인","선택하지 않은 항목이 있습니다.");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    boolean isTimePassed(){
        long currentTime, dDayTime;
        Calendar current = Calendar.getInstance();
        Calendar dDay = Calendar.getInstance();
        dDay.set(data.dDay.year, data.dDay.month, data.dDay.day, data.dDay.hour, data.dDay.minute);
        currentTime = current.getTimeInMillis();
        dDayTime = dDay.getTimeInMillis();

        if(currentTime >= dDayTime) return true;
        else return false;
    }

    void printAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomActionBarTheme));

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        dialog.cancel();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
