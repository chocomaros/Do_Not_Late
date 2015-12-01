package com.example.yena.donotlate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class GoingGraphicActivity extends AppCompatActivity {

    public static final long INTERVAL = 1000;

    ImageView ivBird;
    Button btList;
    TextView tvName,tvRemainTime,tvRemainDistance;
    ProgressBar progressBar;
    int percent = 15;
    float displayWidth, density;
    ListData data;
    Timer countDownTimer;
    SharedPreferences  pref;
    Location currentLocation, destinationLocation, startLocation;
    FrameLayout weightFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("고잉", "온크리에이트");
        setContentView(R.layout.activity_going_graphic);
        ivBird = (ImageView) findViewById(R.id.bird_image);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        weightFrame = (FrameLayout)findViewById(R.id.weight_frame);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        density  = getResources().getDisplayMetrics().density;
        displayWidth = outMetrics.widthPixels / density;
        pref = getSharedPreferences("pref",MODE_PRIVATE);
        if(!isStarted()){
            Log.d("고잉","스타트로 보내기");
            Intent intent = new Intent(GoingGraphicActivity.this, StartActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
            data = appointmentList.get(0);

            tvName = (TextView)findViewById(R.id.tv_name);
            tvRemainTime = (TextView)findViewById(R.id.tv_remain_time);
            tvRemainDistance = (TextView)findViewById(R.id.tv_remain_distance);

            tvRemainTime.setText("시간 얼마나 남았지");
            tvRemainDistance.setText("거리 얼마나 남았지");
            btList = (Button)findViewById(R.id.bt_list_gg);

            currentLocation = new Location("");
            destinationLocation = new Location("");
            startLocation = new Location("");
            destinationLocation.setLatitude(data.dLatitude);
            destinationLocation.setLongitude(data.dLongitude);
            startLocation.setLatitude(data.startLatitude);
            startLocation.setLongitude(data.startLongitude);

            btList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GoingGraphicActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("고잉 온리줌", "서비스 시작 전");
        Log.d("고잉 온리쥼", "여긴 왔니?");
        if(!isStarted()){
            Intent intent = new Intent(GoingGraphicActivity.this, StartActivity.class);
            startActivity(intent);
            Log.d("고잉", "여긴 들어오니?");
            finish();
        }else{
            Log.d("destinationLocation", "La  "+destinationLocation.getLatitude()+" Long  " + destinationLocation.getLongitude());
            Log.d("startLocation", "La  "+startLocation.getLatitude()+" Long  " + startLocation.getLongitude());
            tvName.setText(data.title);
            countDownTimer = new Timer( data.dDay.toCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis(), INTERVAL);
            Log.d("타임인 밀리스 차이", "" + (-Calendar.getInstance().getTimeInMillis())+"    "+ data.dDay.toCalendar().getTimeInMillis()+"   "+Calendar.getInstance().getTimeInMillis());
            countDownTimer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        ivBird.setBackgroundResource(R.drawable.bird_animation);
        AnimationDrawable frameAnimation = (AnimationDrawable) ivBird.getBackground();
        frameAnimation.start();
        super.onWindowFocusChanged(hasFocus);
    }

//    void setImagePosition(){
//        int[] location = new int[2]; /// left = location[0], top = location[1]
//        progressBar.getLocationOnScreen(location);
//
//        int right = location[0] + progressBar.getWidth();
//        int bottom = location[1] + progressBar.getHeight();
//
//        imageLocation[0] = ((right + location[0]) / 100)* percent;
//        imageLocation[1] = location[1] + 20;
//    }
//
//    void setImagePosition(){
//        matrixImage.postTranslate(progressBar.getScrollX(),100);
//    }

    void setImagePosition(int percent) {
        weightFrame.setLayoutParams(new LinearLayout.LayoutParams(0,1,percent));
    }

    Boolean isStarted(){
        ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
        Log.d("isStarted","여긴 오니?");
        if(appointmentList.size() == 0){
            return false;
        }else {
            if (appointmentList.get(0).isStarted) {
                return true;
            } else return false;
        }
    }

    public class Timer extends CountDownTimer
    {

        public Timer(long startTime, long interval)
        {
            super(startTime, interval);
            Log.d("Timer",""+startTime);
        }

        @Override
        public void onFinish()
        {
            tvRemainTime.setText("끝!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            currentLocation.setLatitude(Double.parseDouble(pref.getString("LastLatitude", "37.5740339")));
            currentLocation.setLongitude(Double.parseDouble(pref.getString("LastLongitude", "126.97677499999998")));
            Log.d("currentLocation", "La  " + currentLocation.getLatitude() + " Long  " + currentLocation.getLongitude());
            try {
                tvRemainTime.setText(" 완료까지\n"+ String.format("%02d:%02d:%02d", millisUntilFinished / 3600000, (millisUntilFinished % 3600000) / 60000, (millisUntilFinished % 60000) / 1000));
                tvRemainDistance.setText("남은 거리: " + (int) (currentLocation.distanceTo(destinationLocation)) + "m");
                Log.d("총 거리"," "+startLocation.distanceTo(destinationLocation));
                Log.d("남은 거리",""+currentLocation.distanceTo(destinationLocation));
                percent = (int)((startLocation.distanceTo(destinationLocation)/startLocation.distanceTo(currentLocation))*100);
                if(percent <= 0){
                    percent = 0;
                }
                Log.d("percent",""+percent);
                progressBar.setProgress(percent);
                setImagePosition(percent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
