package com.example.bingxu.emergencyactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JoinActivity extends ActionBarActivity {

    private ListView lv;
    private ArrayList<Activity> activityList;
    private String userID;
    private ActivityAdapter actAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        userID = settings.getString("userID","").toString();

        setContentView(R.layout.activity_join);
        lv = (ListView)findViewById(R.id.listview);
        displayActivity();
    }

    private void displayActivity() {
        activityList = new ArrayList<Activity>();

        final List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("userID", userID));

        Thread httpThread = new Thread(new HttpUtil(Urls.urlActivityList, params));
        httpThread.start();
        try {
            httpThread.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String result = HttpUtil.result;

        String[] results = result.split("\\}");

        for (int i = 0; i < results.length-1; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            String[] item = results[i].split("\"");
            String actid = "";
            String actname = "";
            String actdes = "";
            String acc = "";
            String patientID = "";

            for (int z = 0; z < item.length; z++) {
                if (z + 2 > item.length)
                    break;
                switch (item[z]) {
                    case "ActivityID":
                        if (item[z + 1].equals("PatientID")) {
                            actid = "";
                        } else {
                            actid = item[z+1];
                        }
                        break;
                    case "ActivityName":
                        if (item[z + 2].equals("ActivityDescription")) {
                            actname = "";
                        } else {
                            actname = item[z + 2];
                        }
                        break;
                    case "ActivityDescription":
                        if (item[z + 2].equals("ActivityID")) {
                            actdes = "";
                        } else {
                            actdes = item[z + 2];
                        }
                        break;

                    case "Accepted":
                        acc = item[z+1];
                        break;
                    case "PatientID":
                        patientID = item[z+1];
                        break;

                    }
            }
            String[] aci = actid.split(":");
            String[] ai = aci[1].split(",");
            String acid = ai[0];
            String[] ap = acc.split(":");
            String[] c = ap[1].split(",");
            int accepted = Integer.parseInt(c[0]);
            String[] pi = patientID.split(":");
            patientID = pi[1];

            activityList.add(new Activity(acid, actname, actdes, userID, accepted, patientID));
        }

        actAdapter = new ActivityAdapter(activityList,this);
        lv.setAdapter(actAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.create:
                //create a new activity
                startActivity(new Intent(JoinActivity.this, CreateActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
