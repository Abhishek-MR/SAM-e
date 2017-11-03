package com.example.anuj.e_co.EcoService;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by abhi on 22/10/17.
 */

public class BatteryService extends Service {



    int k=0;
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        //When Event is published, onReceive method is called
        public void onReceive(Context c, Intent i) {
            //Get BatteryService %
            int level = i.getIntExtra("level", 0);
            //Find the progressbar creating in main.xml

            if(level <= 30)
            {
                k=0;
            }
            if (level==100)
                if(k==0)
                {
                   k=1;
                   startActivity(new Intent(BatteryService.this,DialogAct.class));
                }
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        registerReceiver(mBatInfoReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        //Toast.makeText(this, "ServiceChat Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "ServiceChat Destroyed", Toast.LENGTH_LONG).show();
    }
}