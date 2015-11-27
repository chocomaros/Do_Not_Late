package com.example.yena.donotlate;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MoreActivity extends AppCompatActivity {

    private static final String MESSAGE ="저는 못난이 입니다.ㅠㅠ 행복한 하루 되십시오~";

    TextView tvTitle, tvDate, tvTime, tvPlace;
    MapView mapView;
    GoogleMap googleMap;
    ListData data;
    String name, number; /// 문자보낼거


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);


        tvTitle = (TextView)findViewById(R.id.tv_more_title);
        tvDate = (TextView)findViewById(R.id.tv_more_date);
        tvTime = (TextView)findViewById(R.id.tv_more_time);
        tvPlace = (TextView)findViewById(R.id.tv_more_place);
        mapView = (MapView)findViewById(R.id.more_map);
        data = (ListData)getIntent().getSerializableExtra("data");

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

                MarkerOptions marker = new MarkerOptions().position(new LatLng(data.dLatitude, data.dLongitude)).title(data.placeName);
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
            if(data.isComplete){
                completedPrintAlertDialog("삭제", "정말로 삭제하시겠습니까?");

            } else{
                currentPrintAlertDialog("삭제", "다른 사람에게 문자를 보내야 가능합니다.\n보내시겠습니까?");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void currentPrintAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomActionBarTheme));

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        showPhoneNumber();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void completedPrintAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomActionBarTheme));

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        YenaDAO.deleteData(getApplicationContext(), data);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void sendingPrintAlertDialog(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.CustomActionBarTheme));

        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        sendMessage();
                        YenaDAO.deleteData(getApplicationContext(),data);
                        finish();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void sendMessage(){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, MESSAGE, null, null);
    }

    void showPhoneNumber(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK)
        {
            Cursor cursor = getContentResolver().query(data.getData(),
                    new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                            ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
            cursor.moveToFirst();
            name = cursor.getString(0);        //이름 얻어오기
            number = cursor.getString(1);     //번호 얻어오기
            cursor.close();
        }
        super.onActivityResult(requestCode, resultCode, data);

        if(name != null || number != null){
            sendingPrintAlertDialog("문자", name + "님에게 문자를 보내시겠습니까?");
            Log.d("문자보내기","이름 : "+name+" 번호 : "+number);
        } else{
            Toast.makeText(getBaseContext(),"이름이나 번호 널값",Toast.LENGTH_LONG);
            Log.d("이름이나 번호 널값","");
        }
    }
}
