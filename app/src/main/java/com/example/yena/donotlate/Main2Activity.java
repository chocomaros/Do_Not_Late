package com.example.yena.donotlate;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.example.yena.donotlate.cardstream.CardStream;
import com.example.yena.donotlate.cardstream.CardStreamFragment;

public class Main2Activity extends FragmentActivity implements CardStream {

    public static final String FRAGTAG = "PlacePickerFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        FragmentManager fragmentManager = getSupportFragmentManager();
//        PlacePickerFragment fragment =
//                (PlacePickerFragment) fragmentManager.findFragmentByTag(FRAGTAG);
        PlacePickerFragment fragment = new PlacePickerFragment();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fragment_map,fragment);
        ft.commit();
    }

    @Override
    public CardStreamFragment getCardStream() {
        return null;
    }
}
