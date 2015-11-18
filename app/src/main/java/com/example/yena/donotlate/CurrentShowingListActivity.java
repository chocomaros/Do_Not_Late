package com.example.yena.donotlate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CurrentShowingListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_showing_list);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rv_current);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<ListData> appointmentList=new ArrayList<>();

       //TODO 지워야될거 test하는거
        Day current = new Day();
        current = current.currentTime();
        ListData appointment = new ListData("집가야지",current);
        ListData appointment1 = new ListData("으에에엥");
        ListData appointment2 = new ListData("으에에엥2");
        ListData appointment3 = new ListData("으에에엥3");
        ListData appointment4 = new ListData("으에에엥4");
        ListData appointment5 = new ListData("으에에엥5");
        ListData appointment6 = new ListData("으에에엥6");
        appointmentList.add(appointment);
        appointmentList.add(appointment1);
        appointmentList.add(appointment2);
        appointmentList.add(appointment3);
        appointmentList.add(appointment4);
        appointmentList.add(appointment5);
        appointmentList.add(appointment6);
        Log.d("현재 시간? ", current.year + "년 " + current.month + "월 " + current.day + "일 " + current.hour + "시 " + current.minute + "분");

        recyclerView.setAdapter(new ListAdapter(getApplicationContext(),appointmentList,R.layout.current_appointment_list));
    }


}
