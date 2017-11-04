package com.example.anuj.e_co;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CarAct extends AppCompatActivity {


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String SEEDS = "seed";
    private static final String NAME = "name";
    private static final String LIGHT = "light";
    private static final String HOMESEED = "seed";
    private static final String USER1N = "name";
    private static final String USER2N = "name";
    private static final String USER1S = "s_name";
    private static final String USER2S = "s_name";
    private static final String DRIVE = "ame";

    TextView drivestat;
    TextView drivetxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();

        drivestat=(TextView) findViewById(R.id.drivestat);
        drivetxt=(TextView) findViewById(R.id.drivetxt);

        String txt=preferences.getString(DRIVE,"dsf");

        while(true)
        {
            drivestat.setText("Driving");
            if(txt=="c1")
            {
            drivetxt.setText(" Fuel waste");
            }
            if(txt=="c2")
            {
                drivetxt.setText("Half clutch used a lot");
            }if(txt=="c3")
        {
            drivetxt.setText("Power waste");
        }
            if(txt=="c4")
            {
                drivetxt.setText("Good driving");
            }

        }

    }
}
