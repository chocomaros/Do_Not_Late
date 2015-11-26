package com.example.yena.donotlate;

import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

public class GoingGraphicActivity extends AppCompatActivity {

    ImageView ivBird;
    ProgressBar progressBar;
    int percent = 40;
    ListData data;
    Matrix matrixImage = new Matrix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going_graphic);
        ivBird = (ImageView) findViewById(R.id.bird_image);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setProgress(percent);
        ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
        data = appointmentList.get(0);
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

    void setImagePosition(){
        matrixImage.postTranslate(progressBar.getScrollX(),100);
    }

}
