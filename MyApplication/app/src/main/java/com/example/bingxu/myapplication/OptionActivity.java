package com.example.bingxu.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class OptionActivity extends AppCompatActivity {

    private EditText minute;
    private Button submitButton;
    private TimePicker timePicker;
    private AnalogClock analogClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        minute = (EditText)findViewById(R.id.input);
        analogClock = (AnalogClock)findViewById(R.id.analogclock);

//        timePicker = (TimePicker)findViewById(R.id.timepicker);

        submitButton = (Button)findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String min = minute.getText().toString();
                min = Integer.parseInt(min)*1000+"";
                SharedPreferences sharedPreferences = getSharedPreferences("times", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("minute",min);
                editor.commit();
                startActivity(new Intent(OptionActivity.this, MainActivity.class));
                finish();
            }
        });
    }

}
