package com.example.anuj.e_co;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileInputStream;

public class Details extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


    }

    public void  load(View view)
    {
        try {
            FileInputStream fileInputStream =  openFileInput("Code.txt");
            int read = -1;
            StringBuffer buffer = new StringBuffer();
            String mes;
            while((read =fileInputStream.read())!= -1){
                buffer.append((char)read);
            }
            Log.d("Code", buffer.toString());

            mes=buffer.toString();
            String values[]=mes.split(" ");
            int n=Integer.parseInt(values[1]);

            Toast.makeText(this,buffer+""+n, Toast.LENGTH_SHORT).show();
            Toast.makeText(this,values[0], Toast.LENGTH_SHORT).show();
            Toast.makeText(this,values[1], Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this,"cant do", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        Toast.makeText(this,"Loaded", Toast.LENGTH_SHORT).show();
    }
}
