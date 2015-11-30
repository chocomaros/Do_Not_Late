package com.example.yena.donotlate;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    Calendar remainTime;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("고잉","온크리에이트");
        setContentView(R.layout.activity_going_graphic);
        ivBird = (ImageView) findViewById(R.id.bird_image);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(percent);
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        density  = getResources().getDisplayMetrics().density;
        displayWidth = outMetrics.widthPixels / density;

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

            countDownTimer = new Timer(data.startDay.toCalendar().getTimeInMillis(),INTERVAL);
            countDownTimer.onTick(data.startDay.toCalendar().getTimeInMillis() - Calendar.getInstance().getTimeInMillis());

            tvName.setText(data.title);
            tvRemainTime.setText("시간 얼마나 남았지");
            tvRemainDistance.setText("거리 얼마나 남았지");
            btList = (Button)findViewById(R.id.bt_list_gg);


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
        startService(new Intent(this, GpsService.class));
        Log.d("고잉 온리쥼", "여긴 왔니?");
        if(!isStarted()){
            Intent intent = new Intent(GoingGraphicActivity.this, StartActivity.class);
            startActivity(intent);
            Log.d("고잉", "여긴 들어오니?");
            finish();
        }else{
            setImagePosition();
        }
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

    void setImagePosition() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) (ivBird.getLayoutParams());
        int marginLeft = (int)(3*displayWidth * percent / 100) ;
         if(percent <15){
            marginLeft = 0;
         }
         else if(percent > 50){
            marginLeft += 50;
         }
        lp.setMargins(marginLeft,0, 0, 0);
        ivBird.setLayoutParams(lp);
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
        }

        @Override
        public void onFinish()
        {
            tvRemainTime.setText("끝!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            remainTime.setTimeInMillis(millisUntilFinished);
            tvRemainTime.setText("남은 시간:" + formatter.format(remainTime));
        }
    }

}
