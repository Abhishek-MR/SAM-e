package com.example.anuj.e_co;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class Loader_act extends AppCompatActivity {

    String host = "tcp://m11.cloudmqtt.com:16201";
    // String clientId = "ExampleAndroidClient";
    String topic = "sensor/snd";

    String username = "rcduaeoh";
    String password = "hm3O7P_0KiXi";


    MqttAndroidClient client;
    IMqttToken token = null;
    MqttConnectOptions options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader_act);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                Intent i = new Intent(Loader_act.this,Ride_act.class);
                startActivity(i);
                finish();
            }
        }, 9000);

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
                   Toast.makeText(getApplicationContext(),"Connection successful in loader",Toast.LENGTH_SHORT).show();
                    String conreq="conreq";
                    try {
                        client.publish(topic, conreq.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }


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
                //textView.setText(new String(message.getPayload()));
                Toast.makeText(getApplicationContext(),new String(message.getPayload()),Toast.LENGTH_SHORT).show();
                String temp = new String(message.getPayload());
                if (temp.contains("conf")){
                    Intent i = new Intent(Loader_act.this,Ride_act.class);
                    startActivity(i);
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
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
