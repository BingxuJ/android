package com.example.bingxu.emergencyactivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class RosterActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    private EditText showDate = null;
    private Button pickDate = null;
    private EditText showTime = null;
    private Button pickTime = null;
    private TextView endTime = null;
    private Button editButton = null;
    private Button searchButton = null;
    private ListView listview;
    public  SimpleAdapter simpleAdapter;

    private Intent intent;

    private static final int SHOW_DATAPICK = 0;
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_TIMEPICK = 2;
    private static final int TIME_DIALOG_ID = 3;

    public static int mYear;
    public static int mMonth;
    public static int mDay;
    public static String hour = "";
    public static String ehour = "";

    private int sHour;
    private int mMinute;
    private int cYear;
    private int cMonth;
    private int cDay;


    private int count=0;

    private RadioGroup role;
    private String roleID = 1+"";

    public static List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roster);
        initializeViews();

        list = new ArrayList<HashMap<String, Object>>();

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        cYear = c.get(Calendar.YEAR);
        cMonth = c.get(Calendar.MONTH);
        cDay = c.get(Calendar.DAY_OF_MONTH);

        setDateTime();
        Spinner spinner = (Spinner) findViewById(R.id.timespinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.times_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        endTime = (TextView)findViewById(R.id.showendtime);
        editButton = (Button)findViewById(R.id.edit);
        searchButton = (Button)findViewById(R.id.search_button);

        listview = (ListView) findViewById(R.id.listview);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    list = new ArrayList<HashMap<String, Object>>();
                    searchRoster(Urls.urlRoster);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        //edit button starts the edit roster activity
        editButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent = new Intent(RosterActivity.this,EditRosterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    public void searchRoster(String url) throws UnsupportedEncodingException{


        String[] items = {"img", "firstname", "lastname", "staffRole"};
        int[] ids = { R.id.head_image, R.id.user_name, R.id.user_text, R.id.staff_level};

        try {
            simpleAdapter = new SimpleAdapter(this, getData(url),
                    R.layout.list_item, items, ids);
        } catch (IOException e) {
            e.printStackTrace();
        }

        listview.setAdapter(simpleAdapter);

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else
                    return false;
            }
        });

        //initial the edit button
        View b = findViewById(R.id.edit);
        if(mYear>=cYear && mMonth>=cMonth && mDay>cDay){
            b.setVisibility(View.VISIBLE);
        }else{
            b.setVisibility(View.INVISIBLE);
        }

    }

    List<HashMap<String, Object>> getData(String url) throws IOException {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("schedule.year", mYear+""));
        params.add(new BasicNameValuePair("schedule.month", mMonth + 1 + ""));
        params.add(new BasicNameValuePair("schedule.day", mDay + ""));
        params.add(new BasicNameValuePair("schedule.startTime", hour+""));
        params.add(new BasicNameValuePair("schedule.endTime", ehour + ""));


        Thread httpThread = new Thread(new HttpUtil(url, params));
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
            String phone = "";
            String pt = "";
            String paramedicLevel = "";
            String paraNo = "";
            String staffRole = "";

            for (int z = 0; z < item.length; z++) {
                if (z + 2 > item.length)
                    break;
                switch (item[z]) {
                    case "FirstName":
                        if (item[z + 2].equals("Photo")) {
                            firstname = "";
                        } else {
                            firstname = item[z + 2];
                        }
                        break;

                    case "LastName":
                        if (item[z + 2].equals("RoleID")) {
                            lastname = "";
                        } else {
                            lastname = item[z + 2];
                        }
                        break;
                    case "Photo":
                        System.out.print("test");
                        pt = item[z + 1];
                        break;

                    case "RoleID":
                        if (item[z + 1].equals(":2,")) {
                            staffRole = "Paramedic";
                        }else{
                            staffRole = "Doctor";
                        }
                        break;
                }
            }



            String[] compare = pt.split("\\[");
            String pic = "";

            if(compare[0].equals(":null")){
                pic = "";
            }else{
                String[] ims = compare[1].split("\\]");
                String[] cc = ims[0].split("\\,");
                byte[] t = new byte[cc.length];

                for(int j=0;j<t.length;j++){
                    t[j] = (byte) Integer.parseInt(cc[j]);
                }
                pic = Base64.encodeToString(t, Base64.DEFAULT);
            }
            String tag = "item";
            Log.v(tag, item[0]);
            FormatTools format = new FormatTools();
            byte[] imageAsBytes = Base64.decode(pic.getBytes(), Base64.DEFAULT);

            map.put("img", format.Bytes2Drawable(imageAsBytes));
            map.put("firstname", "First Name: "+firstname);
            map.put("lastname", "Last Name: "+lastname);
            map.put("staffRole", "Staff Role: " + staffRole);
            list.add(map);
        }
        count=list.size();
        return list;

    }

    /**
    initialise UI
     */
    private void initializeViews(){
        showDate = (EditText) findViewById(R.id.showdate);
        pickDate = (Button) findViewById(R.id.pickdate);
//        showTime = (EditText)findViewById(R.id.showtime);
//        pickTime = (Button)findViewById(R.id.picktime);

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Message msg = new Message();
                if (pickDate.equals((Button) v)) {
                    msg.what = RosterActivity.SHOW_DATAPICK;
                }
                RosterActivity.this.dateandtimeHandler.sendMessage(msg);
            }
        });
    }

    /**
     * set data
     */
    private void setDateTime(){
        final Calendar c = Calendar.getInstance();

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        updateDateDisplay();

    }

    /**
     * update date shown
     */
    private void updateDateDisplay(){


        showDate.setText(new StringBuilder().append(mYear).append("-")
                .append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-")
                .append((mDay < 10) ? "0" + mDay : mDay));
    }

    /**
     * date component listener
     */
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;

            updateDateDisplay();
        }
    };

    /**
     * set time
     */
    private void setTimeOfDay(){
        final Calendar c = Calendar.getInstance();
        sHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        updateTimeDisplay();
    }

    /**
     * update time and shown
     */
    private void updateTimeDisplay(){
        showTime.setText(new StringBuilder().append(sHour).append(":")
                .append((mMinute < 10) ? "0" + mMinute : mMinute));
    }

    /**
     * time component listener
     */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            sHour = hourOfDay;
            mMinute = minute;

            updateTimeDisplay();
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
                        mDay);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, sHour, mMinute, true);
        }

        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            case TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(sHour, mMinute);
                break;
        }
    }

    /**
     * date and time component Handler
     */
    Handler dateandtimeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RosterActivity.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case RosterActivity.SHOW_TIMEPICK:
                    showDialog(TIME_DIALOG_ID);
                    break;
            }
        }

    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_roster, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        hour = parent.getItemAtPosition(position).toString();
        String[] temp = hour.split(":");
        String h = temp[0];
        hour = h;
        ehour = ((Integer.parseInt(h)+8)%24)+"";
        endTime.setText((Integer.parseInt(h)+8)%24+":00");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public int getYear(){
        return mYear;
    }
    public int getMonth(){
        return mMonth+1;
    }
    public int getDay(){
        return mDay;
    }
    public String getStartTime(){
        return hour;
    }
    public String getEndTime(){
        return ehour;
    }

}
