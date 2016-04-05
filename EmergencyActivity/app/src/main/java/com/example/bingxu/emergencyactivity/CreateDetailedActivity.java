package com.example.bingxu.emergencyactivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class CreateDetailedActivity extends ActionBarActivity implements
        android.widget.CompoundButton.OnCheckedChangeListener{

    String patientid;
    EditText activityName;
    EditText activityDes;
    ListView lv;
    Button invite;
    Button create;
    Intent intent;
    private ArrayList<Roster> rosterList;
    private RosterAdapter rsAdapter;
    private String userids = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_detailed);

        activityName = (EditText)findViewById(R.id.edit_name);
        activityDes = (EditText)findViewById(R.id.edit_des);
        lv = (ListView)findViewById(R.id.listview);
        invite = (Button)findViewById(R.id.invite);
        create = (Button)findViewById(R.id.create);

        Intent intent=this.getIntent();
        patientid = intent.getStringExtra("patientID");

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayRoster();
            }
        });

        create.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                addActivity();
            }
        });

    }

    private void addActivity(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("activityName", activityName.getText().toString()));
        params.add(new BasicNameValuePair("activityDescription", activityDes.getText().toString()));
        params.add(new BasicNameValuePair("patientID", patientid));
        params.add(new BasicNameValuePair("status", "0"));
        params.add(new BasicNameValuePair("createTime", currentDateandTime));
        params.add(new BasicNameValuePair("updateTime", currentDateandTime));

        List<String> idlist = RosterAdapter.users;
        String uis= "";
        for(int i=0;i<idlist.size();i++){
            if(i==0){
                uis+=idlist.get(i);
            }else{
                uis+= "," + idlist.get(i);
            }
        }
        params.add(new BasicNameValuePair("userIDs", uis));
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String userID = settings.getString("userID","").toString();
        params.add(new BasicNameValuePair("creatorID", userID));

        Thread httpThread = new Thread(new HttpUtil(Urls.urlAddActivity, params));
        httpThread.start();
        try {
            httpThread.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String result = HttpUtil.result;

        intent = new Intent(CreateDetailedActivity.this,JoinActivity.class);
        startActivity(intent);
        finish();
    }

    private void displayRoster() {
        rosterList = new ArrayList<Roster>();
        RosterAdapter.users = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("datetime", currentDateandTime));

        Thread httpThread = new Thread(new HttpUtil(Urls.urlSearchStaff, params));
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
            String firstname = "";
            String lastname = "";
            String pt = "";
            String paraNo = "";
            String staffRole = "";
            boolean select = false;
            for (int z = 0; z < item.length; z++) {
                if (z + 2 > item.length)
                    break;
                switch (item[z]) {
                    case "Selected":
                        if (item[z + 2].equals("-1")) {
                            select = false;
                        } else {
                            select = true;
                        }
                        break;
                    case "Firstname":
                        if (item[z + 2].equals("UserID")) {
                            firstname = "";
                        } else {
                            firstname = item[z + 2];
                        }
                        break;
                    case "Lastname":
                        if (item[z + 2].equals("Firstname")) {
                            lastname = "";
                        } else {
                            lastname = item[z + 2];
                        }
                        break;

                    case "Photo":
                        pt = item[z + 1];
                        break;
                    case "RoleID":
                        if (item[z + 1].equals(":2,")) {
                            staffRole = "Paramedic";
                        } else {
                            staffRole = "Doctor";
                        }
                    case "UserID":
                        userids = item[z+1];
                        break;
                }
            }


            String pic = "";
            FormatTools format = new FormatTools();
            byte[] imageAsBytes = Base64.decode(pic.getBytes(), Base64.DEFAULT);
            String ui = userids.substring(1, userids.length());


            rosterList.add(new Roster(ui, format.Bytes2Drawable(imageAsBytes), "First Name: "+firstname, "Last Name: " + lastname, "Role: " + staffRole, select));
        }

        rsAdapter = new RosterAdapter(rosterList,this);
        lv.setAdapter(rsAdapter);

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_detailed, menu);
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
