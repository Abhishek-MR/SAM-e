package com.example.anuj.e_co;

/**
 * Created by anuj on 27/10/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Toast;

import com.example.anuj.e_co.EcoService.DialogAct;
import com.tapadoo.alerter.Alerter;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;


/**
 * Created by amogh on 26/10/17.
 */

public class MyService extends Service {

    int c1=0,c2=0,c3=0,c4=0;

    String host = "tcp://m10.cloudmqtt.com:16470";
    // String clientId = "ExampleAndroidClient";
    String topic = "pitoand";

    String username = "btnuybpp";
    String password = "lsuaT1jteJsV";
    MqttAndroidClient client;
    IMqttToken token = null;
    MqttConnectOptions options;





    @Override
    public void onCreate() {
        super.onCreate();



        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), host, clientId);

        options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());


        try {
            token = client.connect(options);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        try {
            token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    subscribtion();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                String msg = new String(message.getPayload());

                //Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();




                if (msg.equals("unknown")) {
                    //startNotification1();
                    Toast.makeText(getApplicationContext(), "Unknown Person", Toast.LENGTH_LONG).show();

                    sendBroadcast("unknown");

                    //vibrate
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                    v.vibrate(400);
                    long[] pattern = {0, 100, 1000, 300, 200, 100, 500, 200, 100};

// The '-1' here means to vibrate once, as '-1' is out of bounds in the pattern array
                    v.vibrate(pattern, -1);

                    // alert sound
                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


    }

    private void sendBroadcast (String msg){
        Intent intent = new Intent ("service_message"); //put the same message as in the filter you used in the activity when registering the receiver
        intent.putExtra("msg", msg);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        //Toast.makeText(this, "AAAA", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "ServiceChat Destroyed", Toast.LENGTH_LONG).show();
    }

    private void subscribtion(){
        try {
            client.subscribe(topic,0);
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}
