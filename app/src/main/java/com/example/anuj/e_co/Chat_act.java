package com.example.anuj.e_co;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

import java.util.ArrayList;
import java.util.List;


public class Chat_act extends AppCompatActivity {
    int i=0;

    TextView textmsg;
    TextView textname;

    String host = "tcp://m11.cloudmqtt.com:16201";
    // String clientId = "ExampleAndroidClient";
    String topic = "sensor/snd";

    String username = "rcduaeoh";
    String password = "hm3O7P_0KiXi";

    MqttAndroidClient client;
    IMqttToken token = null;
    MqttConnectOptions options;
    EditText edit;
    String pub="",sub="";
    CardView cardView;

    String nameg = "nameKey";

    SharedPreferences sharedpreferences;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";

    List<RowItem> rowItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_act);


        SharedPreferences settings = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        nameg = settings.getString(Name,"name");



        // Get reference of widgets from XML layout
        final ListView lv = (ListView) findViewById(R.id.lv);
        final Button btn = (Button) findViewById(R.id.sendButton);

        // Initializing a new String Array
        final String name_disp = nameg;
        final String msg = "Some message";
        final int imgid = R.drawable.ic_home_black_24dp;

        // Create a List from String Array elements


        rowItems = new ArrayList<RowItem>();


        // Create an ArrayAdapter from List
        final CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                android.R.layout.simple_list_item_1, rowItems);

        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(adapter);

        textname = (TextView) findViewById(R.id.nameTextView);

        edit = (EditText)findViewById(R.id.messageEditText);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = edit.getText().toString();
                if(!message.isEmpty()) {
                    pub = message;
                    RowItem item = new RowItem(imgid, pub, name_disp);
                    rowItems.add(item);
                    adapter.notifyDataSetChanged();
                    edit.setText("");

                    try {
                        client.publish(topic, message.getBytes(), 0, false);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

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
                //textView.setText(new String(message.getPayload()));
                String temp = new String(message.getPayload());
                if(!temp.equals(sub))
                    if(!temp.equals(pub)){
                        sub = temp ;
                        RowItem item = new RowItem(imgid, sub, nameg);
                        rowItems.add(item);
                        adapter.notifyDataSetChanged();
                        edit.setText("");
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

    public void connect(View view){

        try {
            Toast.makeText(getApplicationContext(),"Hi",Toast.LENGTH_SHORT).show();
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

    }
    public void disconnect(View view){

        try {
            token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Toast.makeText(getApplicationContext(),"Disconnected",Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(getApplicationContext(),"Disconnection failed",Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
