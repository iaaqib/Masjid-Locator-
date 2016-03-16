package com.kodesnippets.aaqib.locator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by aaqib on 11/1/2015.
 */
public class SplashScreen extends Activity {
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    setContentView(R.layout.splashscreen_activity);

    Thread timer = new Thread(){
        public void run(){
            try{
                sleep(2000);


            }
            catch(InterruptedException e)
            {
                e.printStackTrace();


            }
            finally{

                Intent startAct = new Intent(SplashScreen.this, MenuActivity.class);
                startActivity(startAct);

            }

        }

    };
    timer.start();



}

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }



}
