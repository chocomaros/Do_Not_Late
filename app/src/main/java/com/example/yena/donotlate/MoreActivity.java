package com.example.yena.donotlate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MoreActivity extends AppCompatActivity {
    TextView tvTitle, tvDate, tvTime, tvPlace;
    MapView mapView;
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        final ListData data = (ListData)getIntent().getSerializableExtra("data");
        tvTitle = (TextView)findViewById(R.id.tv_more_title);
        tvDate = (TextView)findViewById(R.id.tv_more_date);
        tvTime = (TextView)findViewById(R.id.tv_more_time);
        tvPlace = (TextView)findViewById(R.id.tv_more_place);
        mapView = (MapView)findViewById(R.id.more_map);

        tvTitle.setText(data.title);
        tvDate.setText("날짜 => "+data.dDay.year+". "+(data.dDay.month+1)+". "+data.dDay.day);
        if(data.dDay.minute < 10){
            tvTime.setText("시간 => "+data.dDay.hour+" : 0"+data.dDay.minute);
        }
        else {
            tvTime.setText("시간 => " + data.dDay.hour + " : " + data.dDay.minute);
        }
        tvPlace.setText("장소 => " + data.placeName);

        mapView.onCreate(new Bundle());
        mapView.onResume();

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

                MarkerOptions marker = new MarkerOptions().position(new LatLng(data.dLatitude, data.dLongitude)).title("Seoul");
                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(data.dLatitude, data.dLongitude)).zoom(16).build();
                googleMap.clear();
                googleMap.addMarker(marker);
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                googleMap.getUiSettings().setScrollGesturesEnabled(false);
                //mapView.setVisibility(View.VISIBLE);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            //TODO 삭제하는거 여기에 만들어
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
