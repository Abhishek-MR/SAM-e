package com.example.anuj.e_co;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CarAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);
        SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String ret= cd.getString("key", "");

        TextView driv = (TextView) findViewById(R.id.textdrive);
        driv.setText(ret);
    }
}
