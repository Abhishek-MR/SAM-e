package com.example.anuj.e_co;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Space;

public class Seekbar_act extends AppCompatActivity implements View.OnClickListener  {

    private ViewGroup buttonsContainer;
    private Button activeButton = null;
    private final int MAX_BUTTONS = 3;

    SeekBar seek2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);

        this.buttonsContainer = (ViewGroup) findViewById(R.id.buttonsContainer);

        int buttonsSpacing = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        int buttonSize = (int) getResources().getDimension(R.dimen.button_size);

       /* for (int i = 0; i < MAX_BUTTONS; i++) {
            Button button = (Button) getLayoutInflater().inflate(R.layout.circular_button_layout, buttonsContainer, false);
            button.setText("Test " + i);
            button.setOnClickListener(this);
            buttonsContainer.addView(button);

            //Add margin between buttons manually
            if (i != MAX_BUTTONS - 1) {
                buttonsContainer.addView(new Space(this), new ViewGroup.LayoutParams(buttonsSpacing, buttonSize));
            }
        }
        selectButton((Button) buttonsContainer.getChildAt(0));
*/
        seek2 = (SeekBar) findViewById(R.id.seekBar2);





        seek2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"seekbar progress: "+progress, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });






    }

    @Override
    public void onClick(View v) {

    }
}
