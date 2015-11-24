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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class AddDataActivity extends AppCompatActivity {
    TextView tvTime,tvDate,tvPlace;
    EditText etTitle;
    MapView mapView;
    ListData listData = new ListData("",new Day());
    Boolean checkTitle = false;
    Boolean checkDate = false;
    Boolean checkTime = false;
    Boolean checkPlace = false;
    GoogleMap googleMap;
    final int REQUEST_PLACE_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        etTitle = (EditText) findViewById(R.id.et_insert_title);
        tvTime = (TextView) findViewById(R.id.tv_insert_time);
        tvDate = (TextView) findViewById(R.id.tv_insert_date);
        tvPlace = (TextView) findViewById(R.id.tv_insert_place);
        mapView = (MapView) findViewById(R.id.map);


        mapView.onCreate(new Bundle());
        mapView.onResume();
        mapView.setVisibility(View.INVISIBLE);

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
                    startActivityForResult(intent, REQUEST_PLACE_PICKER);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PLACE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                final Place place = PlacePicker.getPlace(data, AddDataActivity.this);
                listData.placeName = (String)place.getName();
                //final CharSequence address = place.getAddress();
                //final CharSequence phone = place.getPhoneNumber();
                listData.placeID = place.getId();
                listData.dLatitude = place.getLatLng().latitude;
                listData.dLongitude =place.getLatLng().longitude;
//                String attribution = PlacePicker.getAttributions(data);
//                if(attribution == null){
//                    attribution = "";
//                }
                int latitudeToInt = (int)listData.dLatitude;

                if(listData.placeName.toString().startsWith("(" + Integer.toString(latitudeToInt))){
                    editTextAlertDialog("장소 이름","선택하신 장소의 이름을 써주세요.");
                } else tvPlace.setText("장소 => "+listData.placeName);

                tvPlace.setTextColor(Color.BLACK);
                checkPlace = true;
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap returnedMap) {
                        Log.d("mapview", "ready");
                        googleMap = returnedMap;
                        try {
                            MapsInitializer.initialize(getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(listData.dLatitude, listData.dLongitude)).title(listData.placeName);
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(listData.dLatitude, listData.dLongitude)).zoom(16).build();
                        googleMap.clear();
                        googleMap.addMarker(marker);
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        googleMap.getUiSettings().setScrollGesturesEnabled(false);
                        mapView.setVisibility(View.VISIBLE);
                    }

                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            listData.dDay.year = year;
            listData.dDay.month = monthOfYear;
            listData.dDay.day = dayOfMonth;
            tvDate.setText("날짜 => "+year+". "+(monthOfYear+1)+". "+dayOfMonth);
            tvDate.setTextColor(Color.BLACK);
            checkDate = true;
        }
    };

    private TimePickerDialog.OnTimeSetListener myTimeSetListener
            = new TimePickerDialog.OnTimeSetListener() {

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            listData.dDay.hour = hourOfDay;
            listData.dDay.minute = minute;
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
        listData.title = etTitle.getText().toString();
        if(listData.title.length() != 0) checkTitle = true;
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
            // 디비에 저장하기
            if(checkTitle && checkDate && checkTime && checkPlace){
                if(!isTimePassed()){
                    ListDataDBHelper mDBHelper = new ListDataDBHelper(getApplicationContext());
                    mDBHelper.open().mDB.execSQL("insert into " + ListData.TABLE_NAME + " values (null,'"+listData.title+"','"+listData.placeID
                            +"','"+listData.placeName+"',null,'"+listData.dDay.toString()+"', "+listData.dLatitude+", "+listData.dLongitude+", 0, 0, 0, 0 , 0, 0)");
                    mDBHelper.close();
//                    Log.d("AdapterActivity",listData.dDay+"");
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
        dDay.set(listData.dDay.year, listData.dDay.month, listData.dDay.day, listData.dDay.hour, listData.dDay.minute);
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

    void editTextAlertDialog(String title, String message){
        final EditText input = new EditText(this);
        input.setSingleLine();

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomActionBarTheme));
        builder.setView(input);

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        tvPlace.findViewById(R.id.tv_insert_place);
                        tvPlace.setText("장소 => " +input.getText());
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
