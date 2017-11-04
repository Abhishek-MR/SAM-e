package com.example.anuj.e_co;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;



public class HomeAccount extends AppCompatActivity {



    TextView name_1,name_2,homseed,seed_1,seed_2,lgt,fan;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String Seeds = "0";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_account);

        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();



        name_1= (TextView) findViewById(R.id.us1);
        name_2= (TextView) findViewById(R.id.us2);
        seed_1= (TextView) findViewById(R.id.sed1);
        seed_2= (TextView) findViewById(R.id.sed2);
        homseed= (TextView) findViewById(R.id.hseed);
        lgt= (TextView) findViewById(R.id.light1);
        fan= (TextView) findViewById(R.id.fan1);


        switch (preferences.getString(LIGHT,"dsv")){

            case "on": lgt.setText("on");
                        fan.setText("on");
                break;
            case "off": lgt.setText("off");
                fan.setText("off");
                break;
        }

        homseed.setText(""+preferences.getInt(HOMESEED,0));
        name_1.setText(preferences.getString(USER1N,"hjg"));
        seed_1.setText(preferences.getString(USER1S,"sh"));
        name_2.setText("Anuj");
        seed_2.setText(preferences.getString(USER2S,"sd"));


    }


}
