package com.example.yena.donotlate;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GoingGraphicActivity extends AppCompatActivity {

    ImageView ivBird;
    Button btList;
    TextView tvName, tvRemainTime, tvRemainDistance;
    ProgressBar progressBar;
    int percent = 40;
    ListData data;
    Matrix matrixImage = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("고잉","온크리에이트");
        setContentView(R.layout.activity_going_graphic);
        ivBird = (ImageView) findViewById(R.id.bird_image);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(percent);

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

}
