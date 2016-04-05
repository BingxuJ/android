package com.example.bingxu.emergencyactivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EditRosterActivity extends ActionBarActivity implements
        android.widget.CompoundButton.OnCheckedChangeListener{


    private int cYear;
    private int cMonth;
    private int cDay;
    private String hour;
    private String ehour;
    private TextView currentYear = null;
    private TextView currentMonth = null;
    private TextView currentDay = null;
    private Button saveButton = null;
    private CheckBox selectedCheckbox = null;

    public  SimpleAdapter simpleAdapter;
    public static List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    private ListView listview;
    private ListView lv;
    private ArrayList<Roster> rosterList;
    private RosterAdapter rsAdapter;

    private int count=0;
    private String userids = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_roster);
        cYear = RosterActivity.mYear;
        cMonth = RosterActivity.mMonth;
        cDay = RosterActivity.mDay;
        hour = RosterActivity.hour;
        ehour = RosterActivity.ehour;

        currentYear = (TextView)findViewById(R.id.yeartext);
        currentMonth = (TextView)findViewById(R.id.monthtext);
        currentDay = (TextView)findViewById(R.id.daytext);
        currentYear.setText("Year: " + cYear);
        currentMonth.setText("Month: " + (cMonth+1));
        currentDay.setText("Day: " + cDay);
        saveButton = (Button)findViewById(R.id.save_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRoster(Urls.urlEditRoster);
            }
        });

        lv = (ListView)findViewById(R.id.listview);
        displayRoster();

    }

    public void displayRoster(){
        rosterList = new ArrayList<Roster>();
        RosterAdapter.users = new ArrayList<String>();

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("schedule.year", cYear+""));
        params.add(new BasicNameValuePair("schedule.month", cMonth+1+""));
        params.add(new BasicNameValuePair("schedule.day", cDay+""));
        params.add(new BasicNameValuePair("schedule.startTime", hour+""));
        params.add(new BasicNameValuePair("schedule.endTime", ehour+""));

        Thread httpThread = new Thread(new HttpUtil(Urls.urlFetchRoster, params));
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
                    case "FirstName":
                        if (item[z + 2].equals("UserID")) {
                            firstname = "";
                        } else {
                            firstname = item[z + 2];
                        }
                        break;
                    case "LastName":
                        if (item[z + 2].equals("Photo")) {
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

            String[] compare = pt.split("\\[");
            String pic = "";
            if (compare[0].equals(":null")) {
                pic = "";
            } else {
                String[] ims = compare[1].split("\\]");
                String[] cc = ims[0].split("\\,");
                byte[] t = new byte[cc.length];

                for (int j = 0; j < t.length; j++) {
                    t[j] = (byte) Integer.parseInt(cc[j]);
                }
                pic = Base64.encodeToString(t, Base64.DEFAULT);
            }
            String tag = "item";
            Log.v(tag, item[0]);
            FormatTools format = new FormatTools();
            byte[] imageAsBytes = Base64.decode(pic.getBytes(), Base64.DEFAULT);
            String ui = userids.substring(1, userids.length() - 1);


            rosterList.add(new Roster(ui, format.Bytes2Drawable(imageAsBytes), "First Name: "+firstname, "Last Name: " + lastname, "Role: " + staffRole, select));
        }

        rsAdapter = new RosterAdapter(rosterList,this);
        lv.setAdapter(rsAdapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    public void saveRoster(String url){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("schedule.year", cYear+""));
        params.add(new BasicNameValuePair("schedule.month", cMonth+1+""));
        params.add(new BasicNameValuePair("schedule.day", cDay+""));
        params.add(new BasicNameValuePair("schedule.startTime", hour+""));
        params.add(new BasicNameValuePair("schedule.endTime", ehour+""));
        List<String> idlist = RosterAdapter.users;
        userids= "";
        for(int i=0;i<idlist.size();i++){
            if(i==0){
                userids+=idlist.get(i);
            }else{
                userids+= "," + idlist.get(i);
            }
        }
        params.add(new BasicNameValuePair("userIDs", userids));

        Thread httpThread = new Thread(new HttpUtil(url, params));
        httpThread.start();
        try {
            httpThread.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String result = HttpUtil.result;
        if(result!=null){
            intent = new Intent(EditRosterActivity.this,RosterActivity.class);
            startActivity(intent);
            finish();
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_roster, menu);
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
