package com.kodesnippets.aaqib.locator;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.kodesnippets.aaqib.locator.utils.Permissions;

/**
 * Created by silen on 11/1/2015.
 */
public class MenuActivity extends Activity {
    Button FindMasjid, Compass, PrayerTimings;
    Context mContext;
    int All_PERMISSIONS = 1;
    boolean check_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_activity);
        intializations();

        CheckingPermissions();

        FindMasjid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (check_permission == false) {

                    CheckingPermissions();
                } else {

                    Intent findMasjid = new Intent(MenuActivity.this, MapsActivity.class);
                    startActivity(findMasjid);
                }
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

    private void CheckingPermissions() {

        String PERMISSIONS[] = {Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION};
//,Manifest.permission.WRITE_EXTERNAL_STORAGE
        Permissions permissions = new Permissions(mContext);

        if (!permissions.haspermissions(mContext,PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, All_PERMISSIONS);
        }
        else{

            check_permission = true;

            //  return false;
        }


    }


    private void intializations() {
        mContext = MenuActivity.this;

        FindMasjid = (Button) findViewById(R.id.findMasjid);
        Compass = (Button) findViewById(R.id.qiblahDirection);
        PrayerTimings = (Button) findViewById(R.id.salahtimings);
            }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (All_PERMISSIONS) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED ) {

                    check_permission = true;

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    if ( !(grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    {

                        check_permission = false;
                        //    finish();
                        //	Intent intent = new Intent(CallingActivity.this,NewLoginActivity.class);
                        //	startActivity(intent);
                        Toast.makeText(MenuActivity.this, "Permission denied to access your Location", Toast.LENGTH_SHORT).show();
                    }
                    else if (!(grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                        //	finish();
                        //	Intent intent = new Intent(NewRegistrationActivity.this,NewLoginActivity.class);
                        //	startActivity(intent);
                        check_permission = false;
                        Toast.makeText(MenuActivity.this, "Permission denied to access your Location", Toast.LENGTH_SHORT).show();

                    }

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //   finish();
                    //  Toast.makeText(NewRegistrationActivity.this, "Permission denied to access your Gallery", Toast.LENGTH_SHORT).show();
                }
                return;
            }

        }
    }





}

