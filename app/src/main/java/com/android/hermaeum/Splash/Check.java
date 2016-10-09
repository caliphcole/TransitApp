package com.android.hermaeum.Splash;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.hermaeum.Data.DBHelper;
import com.android.hermaeum.MainActivity;

/**
 * Created by Caliph Cole on 06/16/2015.
 */
public class Check extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", false);
        DBHelper db = new DBHelper(this);
        if (firstRun == false )//if running for first time
        //Splash will load for first time
        {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", true);
            editor.commit();
            Intent i = new Intent(this, SplashScreen.class);
            startActivity(i);
            finish();
        } else if(!db.checkIfTableExist("weekday_1")&& !db.checkIfTableExist("tripplannerfeed")) {

            Intent a = new Intent(this, MainActivity.class);
            startActivity(a);
            finish();
        }else{
            Intent i = new Intent(this, SplashScreen.class);
            startActivity(i);
            finish();
        }
    }
}
