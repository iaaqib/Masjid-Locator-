package com.kodesnippets.aaqib.locator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

/**
 * Created by silen on 11/1/2015.
 */
public class MenuActivity extends Activity {
    Button FindMasjid, Compass, PrayerTimings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        intializations();
        FindMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent findMasjid = new Intent(MenuActivity.this, MapsActivity.class);
                startActivity(findMasjid);
            }
        });
        PrayerTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timings = new Intent(MenuActivity.this, SalahTimings.class);
                startActivity(timings);
            }
        });
        Compass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timings = new Intent(MenuActivity.this, CompassActivity.class);
                startActivity(timings);
            }
        });

    }



    private void intializations() {
        FindMasjid = (Button) findViewById(R.id.findMasjid);
        Compass = (Button) findViewById(R.id.qiblahDirection);
        PrayerTimings = (Button) findViewById(R.id.salahtimings);
            }




}

