package com.example.yena.donotlate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CompletedShowingListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_showing_list);

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.rv_completed);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        List<ListData> appointmentList=new ArrayList<>();


        Day current = new Day();
        current = current.currentTime();
        ListData appointment = new ListData("이건 끝난곳",current);
        appointmentList.add(appointment);

        recyclerView.setAdapter(new ListAdapter(getApplicationContext(), appointmentList, R.layout.current_appointment_list));
    }
}
