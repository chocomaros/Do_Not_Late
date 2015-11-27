package com.example.yena.donotlate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StartActivity extends Activity {

    GpsService gpsService;
    ImageView ivLate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isStarted()) {
            setContentView(R.layout.activity_start);

            ivLate = (ImageView)findViewById(R.id.late_image);
             Button showList = (Button) findViewById(R.id.bt_list);
//
//            gpsService = new GpsService(getApplicationContext());
//            Intent intent = new Intent("gps_start_service");
//            gpsService.startService(intent);
//
//            Log.d("StartActivity", "위도 : " + gpsService.latitude + "경도 : " +gpsService.longitude);


            showList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStarted()) {
            Intent intent = new Intent(StartActivity.this, GoingGraphicActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        ivLate.setBackgroundResource(R.drawable.late_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) ivLate.getBackground();
        frameAnimation.start();
        super.onWindowFocusChanged(hasFocus);
    }

    Boolean isStarted(){
        ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
        if(appointmentList.size() == 0){
            return false;
        }else {
            if (appointmentList.get(0).isStarted) {
                return true;
            } else return false;
        }
    }
}
