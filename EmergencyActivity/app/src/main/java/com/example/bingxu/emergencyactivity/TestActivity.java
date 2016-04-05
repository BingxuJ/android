package com.example.bingxu.emergencyactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class TestActivity extends ActionBarActivity {

    List<String> records = new ArrayList<String>();
    private ListView listview;
    public  SimpleAdapter simpleAdapter;
    private int count=0;
    private int times = 1;
    public static List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    private Button sendButton;
    private EditText edittext;
    public static String usid = "";
    public static String aid = "";
    public static String firstName = "";
    String[] items;
    int[] ids;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listview = (ListView)findViewById(R.id.listview);
        sendButton = (Button)findViewById(R.id.send);
        edittext = (EditText)findViewById(R.id.input);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);

        usid = settings.getString("userID","").toString();
        firstName = settings.getString("firstName","").toString();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<NameValuePair> lists = new ArrayList<NameValuePair>();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                lists.add(new BasicNameValuePair("userID", ""));


                final List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("activityID", "1"));
                params.add(new BasicNameValuePair("userID", usid));
                params.add(new BasicNameValuePair("text", edittext.getText().toString()));
                params.add(new BasicNameValuePair("createTime", currentDateandTime));

                final ConnectTo ct = new ConnectTo(Urls.urlSendActivity);
                Thread httpThread = new Thread(){
                    @Override
                    public void run(){
                        ct.requestConnect(params);
                    }
                };
                httpThread.start();
//                Thread httpThread = new Thread(new HttpUtil(Urls.urlSendActivity, params));
//                httpThread.start();
            }
        });

        items = new String[]{"firstname"};
        ids = new int[]{ R.id.name};
        context = TestActivity.this;
        messageThread.start();
    }

    String [] text = new String[] {"111","222","333"};

    Thread messageThread = new Thread() {
        @Override
        public void run() {

        while(true){
            try {
                Thread.sleep(500);
                list.clear();
                getData();
                Message msg = handler.obtainMessage(1);
                msg.sendToTarget();

            } catch (InterruptedException e) {

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1){

                simpleAdapter = new SimpleAdapter(context, list,
                            R.layout.chat_item, items, ids);
                listview.setAdapter(simpleAdapter);
            }

        }
    };

    public void getRecords(int index) {
        records.add(text[index]);
    }

    public void getData() throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("activityID", "1"));
        ConnectTo ct = new ConnectTo(Urls.urlActivity);
        String result = ct.requestConnect(params);
        String[] results = result.split("\\}");

        for (int i = 0; i < results.length-1; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            String[] item = results[i].split("\"");

            String firstname = "";
            String time = "";
            String text = "";
            String userID = "";
            String activityID = "";

            for(int z=0;z<item.length;z++){
                if(z+2>=item.length)
                    break;
                switch (item[z]){
                    case "Firstname":
                        if(item[z+2].equals("UserID")){
                            firstname = "";
                        }else{
                            firstname = item[z+2];
                        }
                        break;
                    case "CreateTime":
                        if(item[z+2].equals("Lastname")){
                            time = "";
                        }else{
                            time = item[z+2];
                        }
                        break;
                    case "Text":
                        if(item[z+2].equals("Audio")){
                            text = "";
                        }else{
                            text = item[z+2];
                        }
                        break;
                    case "UserID":
                        userID = item[z+1];
                        break;
                    case "ActivityID":
                        activityID = item[z+1];
                        break;

                }
            }
            String[] ai = activityID.split(":");
            String[] ais = ai[1].split(",");
            aid = ais[0];
            String[] ui = userID.split(":");
            String[] uis = ui[1].split(",");


            map.put("firstname", firstname + " " + time + ": "+ text);

            list.add(map);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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
