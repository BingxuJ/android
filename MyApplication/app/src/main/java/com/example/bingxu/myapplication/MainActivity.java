package com.example.bingxu.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity {



    private TextView textView;
    private Button stopButton;
    private Button startButton;
    private Button optionButton;
    private Button resumeButton;
    private long remainSecond;
    private MyCount mCount;
    private SharedPreferences sharedPreferences;
    private int seconds;
    private long se;
    private long ho;
    private long mi;
    private long sec;

    private boolean flag = false;
    private boolean fround = true;

    private ClockView cv;
    private int SEC = 5000;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cv = (ClockView)findViewById(R.id.clock_view);

        seconds = SEC;

        sharedPreferences = getSharedPreferences("times", 0);

        final String m = sharedPreferences.getString("minute","");
            if(m!=null&&!m.equals("")){
                seconds = Integer.parseInt(m);
            }

        se = seconds/1000;
        ho = se/3600;
        mi = (se-ho*3600)/60;
        sec = se-ho*3600-mi*60;

        textView = (TextView)findViewById(R.id.hour);
        textView.setText("Hours: "+ ho + ", Minutes: "+ mi + ", Seconds: "+sec);
        mCount = new MyCount((seconds+1),1000);

        cv.setTime(sec, mi, ho);

        startButton = (Button)findViewById(R.id.start);
        resumeButton = (Button)findViewById(R.id.resume);
        stopButton = (Button)findViewById(R.id.stop);

        startButton.setVisibility(View.VISIBLE);
        resumeButton.setVisibility(View.INVISIBLE);

        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cv.start();
                cv.invalidate();

                startButton.setVisibility(View.INVISIBLE);
                resumeButton.setVisibility(View.VISIBLE);

                if (flag) {
                    flag = false;
                    mCount = new MyCount(remainSecond, 1000);
                }

//                if(fround){
//
//                    try{
//                        Thread.sleep(1000);
//                    }catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                    fround = false;
//                }
                mCount.start();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCount.cancel();
                cv.stop();
                textView.setText("Hours: " + ho + ", Minutes: " + mi + ", Seconds: " + sec);
                remainSecond = SEC;
                startButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
                fround = true;
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                cv.resume();
                flag = true;
                long s = remainSecond / 1000;
                long h = s / 3600;
                long min = (s - h * 3600) / 60;
                long second = s - h * 3600 - min * 60;

                textView.setText("Hours: " + h + ", Minutes: " + min + ", Seconds: " + second);
                mCount.cancel();

                startButton.setVisibility(View.VISIBLE);
                resumeButton.setVisibility(View.INVISIBLE);
            }
        });

        optionButton = (Button)findViewById(R.id.option);
        optionButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, OptionActivity.class));
                finish();
            }
        });

    }


    class MyCount extends CountDownTimer {
        public MyCount(long mfuture, long count){
            super(mfuture, count);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            remainSecond = millisUntilFinished;
            long s = millisUntilFinished/1000;
            long h = s/3600;
            long min = (s-h*3600)/60;
            long second = s-h*3600-min*60;

            Log.v(TAG,"mili: "+millisUntilFinished);
            Log.v(TAG,"second: "+second);

            textView.setText("Hours: " + h + ", Minutes: " + min + ", Seconds: " + second);
        }

        @Override
        public void onFinish() {
            textView.setText("Hours: 0, Minutes: 0, Seconds: 0");
            startButton.setVisibility(View.VISIBLE);
            resumeButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
