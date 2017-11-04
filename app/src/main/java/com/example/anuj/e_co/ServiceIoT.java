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

    String host = "tcp://m12.cloudmqtt.com:11871";
    // String clientId = "ExampleAndroidClient";
    String topic = "sensor/snd";

    String username = "zyekiwpb";
    String password = "z58Alb-SFL-_";
    MqttAndroidClient client;
    IMqttToken token = null;
    MqttConnectOptions options;

    int val = 0,rkey;


    @Override
    public void onCreate() {
        super.onCreate();

/*        SharedPreferences xy = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        rkey= xy.getInt("key", 0);
        SharedPreferences ab = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        val= ab.getInt("key", 0);
*/
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
                    Toast.makeText(getApplicationContext(),"Connection successful",Toast.LENGTH_SHORT).show();
                    subscribtion();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(getApplicationContext(),"Connection failed",Toast.LENGTH_SHORT).show();

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

                    if (msg.contains("rhint")) {
                      //  if (rkey!=1)
                            startNotification();
                    }
                if (msg.equals("open")) {
                    Intent i = new Intent(ServiceIoT.this,Ride_act.class);
                    startActivity(i);
                }
                if (msg.equals("on"))
                {
                    SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    cd.edit().putString("key", "on").apply();
                }
                if (msg.equals("off"))
                {
                    SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    cd.edit().putString("key", "off").apply();
                }
                if (msg.contains("seedpi"))
                {

                    String[] msgs=msg.split(":");
                    //msgs[1];
                    SharedPreferences ef = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    ef.edit().putString("key", msgs[1]).apply();
                }

                if (msg.contains("user"))
                {

                    if (msg.contains("1"))
                    {

                        String[] msgs=msg.split(":");
                        //msgs[1];
                        SharedPreferences gh = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        gh.edit().putString("key", msgs[2]).apply();
                        SharedPreferences ij = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        ij.edit().putString("key", msgs[4]).apply();
                    }
                    if (msg.contains("2"))
                    {

                        String[] msgs=msg.split(":");
                        //msgs[1];
                        SharedPreferences kl = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        kl.edit().putString("key", msgs[2]).apply();
                        SharedPreferences mn = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        mn.edit().putString("key", msgs[4]).apply();
                    }
                }
                if (msg.equals("0"))
                {
                   //start car
                    SharedPreferences ef = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    ef.edit().putString("key", "on").apply();

                }

                if (msg.equals("1"))
                {
                    //full acc
                    c1++;
                    SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    cd.edit().putString("key", "on").apply();
                }
                if (msg.equals("2"))
                {
                    // half clutch
                    c2++;
                    SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    cd.edit().putString("key", "on").apply();
                }
                if (msg.equals("0"))
                {
                    //full clutch
                    c3++;
                    SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    cd.edit().putString("key", "on").apply();
                }
                if (msg.equals("0"))
                {

                    c4++;
                    //good
                    SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    cd.edit().putString("key", "on").apply();
                }

                if (msg.equals("car"))
                {
                    SharedPreferences cd = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                    cd.edit().putString("key", "Driving").apply();
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


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
        Intent intent = new Intent(this, Connecting_act.class);

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
                .setContentTitle("Bla blah wants to carpool.")
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
