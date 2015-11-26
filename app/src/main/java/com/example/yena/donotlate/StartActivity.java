package com.example.yena.donotlate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isStarted()) {
            setContentView(R.layout.activity_start);

            Button showList = (Button) findViewById(R.id.bt_list);

            showList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StartActivity.this, GoingGraphicActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            Intent intent = new Intent(StartActivity.this, GoingGraphicActivity.class);
            startActivity(intent);
            finish();
        }
    }

    Boolean isStarted(){
        ArrayList<ListData> appointmentList = YenaDAO.getCurrentList(getApplicationContext());
        if(appointmentList.get(0).isStarted){
            return true;
        }else return false;
    }
}
