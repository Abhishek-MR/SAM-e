package com.example.anuj.e_co.EcoService;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anuj.e_co.R;

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

import java.util.concurrent.TimeUnit;


public class DialogAct extends AppCompatActivity {

    String host = "tcp://m12.cloudmqtt.com:11871";
    // String clientId = "ExampleAndroidClient";
    String topic = "sensor/snd";

    String username = "zyekiwpb";
    String password = "z58Alb-SFL-_";

    MqttAndroidClient client;
    IMqttToken token = null;
    MqttConnectOptions options;


    private long timeCountInMilliSeconds = 3 * 60000;

    public MediaPlayer mp;


    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;

    final Context context = this;
    private Button button;


    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private static final String PREFS = "prefs";
    private static final String HOMESEED = "seed";

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);


        //ass
        preferences = getSharedPreferences(PREFS,0);
        editor = preferences.edit();


        //MQTT


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
                Toast.makeText(getApplicationContext(),new String(message.getPayload()),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });


        //MQTT




        button = (Button) findViewById(R.id.buttonShowCustomDialog);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(getApplicationContext(), notification);
        {
            mp.start();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms

                }
            }, timeCountInMilliSeconds);
        }

        // add button listener
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // custom dialog
                final android.app.Dialog dialog = new android.app.Dialog(context);
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Title...");

                progressBarCircle = (ProgressBar) dialog.findViewById(R.id.progressBarCircle);
                editTextMinute = (EditText) dialog.findViewById(R.id.editTextMinute);
                textViewTime = (TextView) dialog.findViewById(R.id.textViewTime);


                // set the custom dialog components - text, image and button

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mp.stop();
                        Toast.makeText(getApplicationContext(),"You have lost 3 seeds because of negligence",Toast.LENGTH_SHORT).show();
                        String message = "offy47tfj";

                        try {
                            client.publish(topic, message.getBytes(), 0, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        editor.putInt(HOMESEED,preferences.getInt(HOMESEED,0)-3).apply();
                        String message2 = "seedand "+preferences.getInt(HOMESEED,0);
                        try {
                            client.publish(topic, message2.getBytes(), 0, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }

                        finish();


                    }
                });

                dialog.show();
                startStop();

            }
        });
        button.callOnClick();
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


    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            setTimerValues();
            // call to initialize the progress bar values
            setProgressBarValues();

            editTextMinute.setEnabled(false);
            // changing the timer status to started
            timerStatus = TimerStatus.STARTED;
            // call to start the count down timer
            startCountDownTimer();

        } else {

            // hiding the reset icon
            // changing stop icon to start icon
            // making edit text editable
            editTextMinute.setEnabled(true);
            // changing the timer status to stopped
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();
            startCountDownTimer();
        }

    }

    /**
     * method to initialize the values for count down timer
     */
    private void setTimerValues() {
        int time = 3;

        // assigning values after converting to milliseconds
        timeCountInMilliSeconds = time * 60 * 1000;
    }

    /**
     * method to start count down timer
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));
                // call to initialize the progress bar values
                setProgressBarValues();
                // hiding the reset icon
                // changing stop icon to start icon

                Toast.makeText(getApplicationContext(),"You have lost 5 seeds because of negligence",Toast.LENGTH_SHORT).show();

                String message = "off5";
                try {
                    client.publish(topic, message.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                editor.putInt(HOMESEED,preferences.getInt(HOMESEED,0)-5).apply();
                String message2 = "seedand "+preferences.getInt(HOMESEED,0);
                try {
                    client.publish(topic, message2.getBytes(), 0, false);
                } catch (MqttException e) {
                    e.printStackTrace();
                }

                // making edit text editable
                editTextMinute.setEnabled(true);
                // changing the timer status to stopped
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    /**
     * method to stop count down timer
     */
    private void stopCountDownTimer() {
        countDownTimer.cancel();
    }

    /**
     * method to set circular progress bar values
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;


    }
}

