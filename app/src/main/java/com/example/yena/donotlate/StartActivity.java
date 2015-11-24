package com.example.yena.donotlate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button showList = (Button)findViewById(R.id.bt_list);

        ListDataDBHelper mDBHelper = new ListDataDBHelper(getApplicationContext());
        mDBHelper.open().mDB.execSQL("insert into " + ListData.TABLE_NAME + " values (null, '존나 하기 시러','adf'," +
                    "'연구실임','2015-11-24 03:33','2015-11-24 05:00', 1.13, 2.13, 3.3, 4.1, 1, 1 , 0, 0)");

        showList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
