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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_account);

        SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String rkey= cd.getString("key", "");
        SharedPreferences ef = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String ret= ef.getString("key", "");
        SharedPreferences gh = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String usnm1 = gh.getString("key", "");
        SharedPreferences ij = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String useed1 = ij.getString("key", "");
        SharedPreferences kl = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String usnm2 = kl.getString("key", "");
        SharedPreferences mn = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String useed2 = mn.getString("key", "");


        name_1= (TextView) findViewById(R.id.us1);
        name_2= (TextView) findViewById(R.id.us2);
        seed_1= (TextView) findViewById(R.id.sed1);
        seed_2= (TextView) findViewById(R.id.sed2);
        homseed= (TextView) findViewById(R.id.hseed);
        lgt= (TextView) findViewById(R.id.light1);
        fan= (TextView) findViewById(R.id.fan1);

        SharedPreferences settings = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        final String name = settings.getString(Name,"name");
        final int seeds = settings.getInt(Seeds, 0);

        switch (rkey){

            case "on": lgt.setText("on");
                        fan.setText("on");
                break;
            case "off": lgt.setText("off");
                fan.setText("off");
                break;
        }

        homseed.setText(ret);
        name_1.setText(usnm1);
        seed_1.setText(useed1);
        name_2.setText(usnm2);
        seed_2.setText(useed2);


    }


}
