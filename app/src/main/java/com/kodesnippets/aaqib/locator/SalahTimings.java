package com.kodesnippets.aaqib.locator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.model.LatLng;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by silen on 10/2/2015.
 */public class SalahTimings extends AppCompatActivity {

    TextView fajr, sunrise, dhuhr,asr,sunset,maghrib,isha;
    Spinner calculationMethod;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salah_timings);
        variableInitializations();
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        checkIfGPSIsOn(manager,this);
        Location lastLoc = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        final double lat = lastLoc.getLatitude();
        final double lng = lastLoc.getLongitude();
        final double timeZone = (TimeZone.getTimeZone(Time.getCurrentTimezone()).getOffset(System.currentTimeMillis())) / (1000.0 * 60.0 * 60.0);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        final SharedPreferences getData = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        getData.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
               Log.d("TestPref", key);
                String test = sharedPreferences.getString(key,"Karachi");
                if (test.contentEquals("Jafari")){
                    Toast.makeText(getApplication(),"Jafari",Toast.LENGTH_LONG).show();
                    Log.d("JafariToast","Jafari");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Jafari,PrayTime.Hanafi,lat,lng,timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                } else if (test.contentEquals("Karachi")){
                    Toast.makeText(getApplication(),"Karachi",Toast.LENGTH_LONG).show();
                    Log.d("JafariToast","Karachi");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Karachi,PrayTime.Hanafi,lat,lng,timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                }
               else if (test.contentEquals("ISNA")){
                    Toast.makeText(getApplication(),"ISNA",Toast.LENGTH_LONG).show();
                    Log.d("JafariToast","ISNA");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.ISNA,PrayTime.Hanafi,lat,lng,timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                }
                else if (test.contentEquals("MWL")){
                    Toast.makeText(getApplication(),"MWL",Toast.LENGTH_LONG).show();
                    Log.d("JafariToast","MWL");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.MWL,PrayTime.Hanafi,lat,lng,timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                }
               else if (test.contentEquals("Makkah")){
                    Toast.makeText(getApplication(),"Makkah",Toast.LENGTH_LONG).show();
                    Log.d("JafariToast","Makkah");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Makkah,PrayTime.Hanafi,lat,lng,timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                }
                else if (test.contentEquals("Egypt")){
                    Toast.makeText(getApplication(),"Egypt",Toast.LENGTH_LONG).show();
                    Log.d("JafariToast","Egypt");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Egypt,PrayTime.Hanafi,lat,lng,timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                }
                else {
                    Toast.makeText(getApplication(),"Tehran",Toast.LENGTH_LONG).show();
                    Log.d("JafariToast","tehran");
                    ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Tehran,PrayTime.Hanafi,lat,lng,timeZone);
                    clearFields();
                    fajr.setText(prayerTimes.get(0));
                    sunrise.setText(prayerTimes.get(1));
                    dhuhr.setText(prayerTimes.get(2));
                    asr.setText(prayerTimes.get(3));
                    sunset.setText(prayerTimes.get(4));
                    maghrib.setText(prayerTimes.get(5));
                    isha.setText(prayerTimes.get(6));

                }
            }
        });
        String getCalMethod = getData.getString("calMethod", "Karachi");
        if(getCalMethod.contentEquals("Karachi")){

        }


        Log.d("TimeZone",""+timeZone);
        //create LatLng
        LatLng lastLatLng = new LatLng(lat, lng);
     ArrayList<String> prayerTimes = extractPrayerTimings(PrayTime.Jafari,PrayTime.Shafii,lat,lng,timeZone);

        fajr.setText(prayerTimes.get(0));
        sunrise.setText(prayerTimes.get(1));
        dhuhr.setText(prayerTimes.get(2));
        asr.setText(prayerTimes.get(3));
        sunset.setText(prayerTimes.get(4));
        maghrib.setText(prayerTimes.get(5));
        isha.setText(prayerTimes.get(6));

        Log.d("LatLng in Salah", lastLatLng.toString());
        String lats = String.valueOf(lat);
        String lngs = String.valueOf(lng);
        Toast.makeText(this, lats + lngs + lastLatLng.toString(), Toast.LENGTH_LONG).show();
        // txt.setText(lastLoc.toString());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    private void clearFields(){
        fajr.setText("");
        sunrise.setText("");
        dhuhr.setText("");
        asr.setText("");
        sunset.setText("");
        maghrib.setText("");
        isha.setText("");



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent prefs = new Intent(SalahTimings.this, Prefs.class);
            startActivity(prefs);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void variableInitializations(){
        fajr = (TextView) findViewById(R.id.textView1);
        sunrise = (TextView) findViewById(R.id.textView2);
        dhuhr = (TextView) findViewById(R.id.textView3);
        asr = (TextView) findViewById(R.id.textView4);
        sunset = (TextView) findViewById(R.id.textView5);
        maghrib = (TextView) findViewById(R.id.textView6);
        isha = (TextView) findViewById(R.id.textView7);

    }
    private ArrayList<String> extractPrayerTimings(int calMethod, int juristic, double lat, double lng, double timeZone){
        PrayTime prayers = new PrayTime();


        prayers.setTimeFormat(prayers.Time12);

        prayers.setCalcMethod(calMethod);
        prayers.setAsrJuristic(juristic);
        prayers.setAdjustHighLats(prayers.AngleBased);
        int[] offsets = {0, 0, 0, 0, 0, 0, 0}; // {Fajr,Sunrise,Dhuhr,Asr,Sunset,Maghrib,Isha}
        prayers.tune(offsets);

        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        ArrayList<String> prayerTimes = prayers.getPrayerTimes(cal,
                lat, lng, timeZone);
        ArrayList<String> prayerNames = prayers.getTimeNames();

        for (int i = 0; i < prayerTimes.size(); i++) {
            //   System.out.println(prayerNames.get(i) + " - " + prayerTimes.get(i));
            Log.d("Namaz",prayerTimes.get(i));


        }


        return prayerTimes;
    }

    protected void checkIfGPSIsOn(LocationManager manager, Context context) {
        if (!manager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Location Manager");
            builder.setMessage("Masjid Locator requires a device with GPS to work");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.create().show();
        } else {

            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Ask the user to enable GPS
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Location Manager");
                builder.setMessage("Masjid Locator would like to enable your device's GPS?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Launch settings, allowing user to make a change

                        try {
                          //  Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            Intent i = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //No location service, no Activity
                        finish();
                    }
                });
                builder.create().show();
            }
        }
    }
}


