package com.example.anuj.e_co;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

public class PayAct extends AppCompatActivity {

    private IntentIntegrator qrScan;
    public String name, amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        qrScan = new IntentIntegrator(this);

        findViewById(R.id.repay2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());
                    //setting values to textviews
                    name=obj.getString("name");
                    amt=obj.getString("amount");

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(PayAct.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Payment Confirmation");

                    // Setting Dialog Message
                    alertDialog.setMessage("Confirm your payment with Abhishek of "+ amt);

                    // Setting Icon to Dialog
                    // alertDialog.setIcon(R.drawable.delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            Toast.makeText(getApplicationContext(), "Payment Successful to "+ name + "of Rs "+ amt, Toast.LENGTH_SHORT).show();

                            SharedPreferences nm = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            nm.edit().putString("name", name).apply();
                            SharedPreferences am = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            am.edit().putString("amt",amt).apply();
                        }
                    });

                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();



                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}
