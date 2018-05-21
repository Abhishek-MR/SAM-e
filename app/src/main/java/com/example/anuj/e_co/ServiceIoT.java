package com.example.anuj.e_co;

/**
 * Created by anuj on 27/10/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.anuj.e_co.EcoService.DialogAct;

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

public class ServiceIoT extends Service {

    int c1=0,c2=0,c3=0,c4=0;

    String host = "tcp://m10.cloudmqtt.com:16470";
    // String clientId = "ExampleAndroidClient";
    String topic = "pitoand";

    String username = "btnuybpp";
    String password = "lsuaT1jteJsV";
    MqttAndroidClient client;
    IMqttToken token = null;
    MqttConnectOptions options;

    int c=0;


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
    private static final String KEY = "key";

    public String lastmsg="";


    @Override
    public void onCreate() {
        super.onCreate();


        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();

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

                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();


                    if (msg.equals("waste")) {
                        startActivity(new Intent(getApplicationContext(), DialogAct.class));
                    }

                    if (msg.contains("rhint")) {
                        if (preferences.getInt(KEY, 0) == 1) ;
                        startNotification1();
                    }
                    if (msg.equals("open")) {
                        Intent i = new Intent(ServiceIoT.this, Ride_act.class);
                        startActivity(i);
                    }
                    if (msg.equals("on")) {
                        editor.putString(LIGHT, "on").apply();
                    }
                    if (msg.equals("off")) {
                        editor.putString(LIGHT, "off").apply();
                    }
                    if (msg.contains("seedpi")) {

                        String[] msgs = msg.split(" ");
                        //msgs[1];
                        editor.putInt(HOMESEED, Integer.parseInt(msgs[1])).apply();


                    }

                    if (msg.contains("userpi")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        if (msg.contains("Abhishek")) {

                            String[] msgs = msg.split(" ");
                            //msgs[1];
                            editor.putString(USER1N, msgs[2]).apply();
                            editor.putString(USER1S, msgs[4]).apply();


                        }
                        if (msg.contains("Anuj")) {

                            String[] msgs = msg.split(" ");
                            //msgs[1];
                            editor.putString(USER2N, msgs[2]).apply();
                            editor.putString(USER2S, msgs[4]).apply();
                        }
                    }
                    if (msg.equals("0")) {
                        //start car
                        startNotification();

                    }

                    if (msg.equals("1")) {
                        //full acc
                        c1++;
                        max();


                    }
                    if (msg.equals("2")) {
                        // half clutch
                        c2++;
                        max();


                    }
                    if (msg.equals("0")) {
                        //full clutch
                        c3++;
                        max();


                    }
                    if (msg.equals("0")) {

                        c4++;
                        max();

                        //good
                    }
                    lastmsg=msg;


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


    }

    void max()
    {
        int max=c1;
        editor.putString(DRIVE,"c1");
        if(c2>max) editor.putString(DRIVE,"c2");
        if(c3>max) editor.putString(DRIVE,"c3");
        if(c4>max) editor.putString(DRIVE,"c4");

        editor.putString(DRIVE,"c1");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        Toast.makeText(this, "ServiceChat Started", Toast.LENGTH_LONG).show();
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

    private void startNotification(){

        // Set Notification Title
        String strtitle = getString(R.string.notificationtitle);
        // Set Notification Text
        String strtext = getString(R.string.notificationtext);

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, CarAct.class);

        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);


        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logo)
                // Set Ticker Message
                .setTicker(getString(R.string.notificationticker))
                // Set Title
                .setContentTitle("Drive Mode Started")
                // Set Text
                .setContentText(getString(R.string.notificationtext))
                .addAction(R.drawable.cast_ic_notification_0, "End Drive", pIntent)
                // Add an Action Button below Notification

                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);


        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());


    }

    private void startNotification1(){

        // Set Notification Title
        String strtitle = getString(R.string.notificationtitle);
        // Set Notification Text
        String strtext = getString(R.string.notificationtext);

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, Ride_act.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent2 = new Intent(this, Carpool_act.class);
        // Send data to NotificationView Class
        intent2.putExtra("title", strtitle);
        intent2.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent2 = PendingIntent.getActivity(this, 0, intent2,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Intent intent3 = new Intent(this, Chat_act.class);
        // Send data to NotificationView Class
        intent3.putExtra("title", strtitle);
        intent3.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent3 = PendingIntent.getActivity(this, 0, intent3,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Create Notification using NotificationCompat.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.logo)
                // Set Ticker Message
                .setTicker(getString(R.string.notificationticker))
                // Set Title
                .setContentTitle("Anuj wants to carpool.")
                // Set Text
                .setContentText(getString(R.string.notificationtext))
                .addAction(R.drawable.cast_ic_notification_0, "Accept", pIntent)
                // Add an Action Button below Notification
                .addAction(R.drawable.cast_ic_notification_0, "Reject", pIntent2)

                .addAction(R.drawable.cast_ic_notification_0, "Message", pIntent3)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                // Dismiss Notification
                .setAutoCancel(true);


        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());


    }

}
