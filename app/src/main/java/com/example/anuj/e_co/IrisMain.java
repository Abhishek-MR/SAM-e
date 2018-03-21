package com.example.anuj.e_co;

/**
 * Created by amogh on 19/3/18.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;
import android.net.Uri;
import android.os.Bundle;

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
import org.jetbrains.annotations.NotNull;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class IrisMain extends AppCompatActivity {

    String host = "tcp://m13.cloudmqtt.com:13476";
    // String clientId = "ExampleAndroidClient";
    String topic = "top";

    String username = "ptbelqhi";
    String password = "wdJXMPWzv6Q5";

    MqttAndroidClient client;
    IMqttToken token = null;
    MqttConnectOptions options;

    EditText addrField;
    Button btnConnect,btn2;
    VideoView streamView;
    MediaController mediaController;
    SeekBar seek2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iris_main);

        seek2 = (SeekBar) findViewById(R.id.seekBar2);

        //int flag=0;
        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String message = String.valueOf(progress);
                try {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                    client.publish(topic, message.getBytes(),0,false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        addrField = (EditText)findViewById(R.id.addr);
        //  btnConnect = (Button)findViewById(R.id.connect);
        streamView = (VideoView)findViewById(R.id.streamview);

        StickySwitch stickySwitch = (StickySwitch) findViewById(R.id.sticky_switch);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                Log.d("TAG", "Now Selected : " + direction.name() + ", Current Text : " + text);
            }
        });
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                if(direction== StickySwitch.Direction.LEFT)
                {
                    streamView.stopPlayback();

                }
                else {
                    String sa = addrField.getEditableText().toString();

                    playStream(sa);

                }
            }
        });

        //  btnConnect.setOnClickListener(new OnClickListener(){

        //    @Override
        //  public void onClick(View v) {


        //    String s = addrField.getEditableText().toString();

        //  playStream(s);
        // }});



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
                    Toast.makeText(getApplicationContext(),"    Connection successful",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(IrisMain.this, new String(message.getPayload()), Toast.LENGTH_LONG).show();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


    }

    private void playStream(String src){
        Uri UriSrc = Uri.parse(src);
        if(UriSrc == null){
            Toast.makeText(IrisMain.this,
                    "UriSrc == null", Toast.LENGTH_LONG).show();
        }else{
            streamView.setVideoURI(UriSrc);
            mediaController = new MediaController(this);
            streamView.setMediaController(mediaController);
            streamView.start();

            Toast.makeText(IrisMain.this,
                    "Connect: " + src,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        streamView.stopPlayback();
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
