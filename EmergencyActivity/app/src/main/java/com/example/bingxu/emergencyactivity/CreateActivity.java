package com.example.bingxu.emergencyactivity;

import android.content.Intent;
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
import android.widget.SimpleAdapter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateActivity extends ActionBarActivity {

    protected static EditText firstname;
    protected static  EditText lastname;
    protected static EditText patientno;
    private Button search;
    private Button create;
    private Button add;
    private Intent intent;
    private static ListView listview;
    public  SimpleAdapter simpleAdapter;
    public static List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    private static PatientAdapter patientAdapter;
    private static ArrayList<Patient> patientList;
    public static Map<Integer, Boolean> isSelected;
    public static Map<Integer, View> viewList;
    public static String selectedPatient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        firstname = (EditText)findViewById(R.id.edit_firstname);
        lastname = (EditText)findViewById(R.id.edit_lastname);
        patientno = (EditText)findViewById(R.id.edit_patientno);
        search = (Button)findViewById(R.id.search);
        create = (Button)findViewById(R.id.create);
        add = (Button)findViewById(R.id.add);

        listview = (ListView) findViewById(R.id.listview);

        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent = new Intent(CreateActivity.this,AddPatientActivity.class);
                startActivity(intent);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                list = new ArrayList<HashMap<String, Object>>();
                isSelected = new HashMap<Integer, Boolean>();
                viewList = new HashMap<Integer, View>();
                selectedPatient = "";
                searchPatient(Urls.urlPatientList);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(CreateActivity.this, CreateDetailedActivity.class);
                intent.putExtra("patientID", selectedPatient);
                startActivity(intent);
                finish();
            }
        });

    }


    /**
     * Search patient and show the first name and last anme
     * @param url
     */
    public void searchPatient(String url){

        patientList = new ArrayList<Patient>();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("firstName", getFirstName()));
        params.add(new BasicNameValuePair("lastName", getLastName()));
        params.add(new BasicNameValuePair("patientNO", getPatientNo()));

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
            String patientid = "";
            String patientno = "";
            String lastname = "";
            String firstname = "";

            for (int z = 0; z < item.length; z++) {
                if (z + 2 > item.length)
                    break;
                switch (item[z]) {
                    case "PatientID":
                        patientid = item[z+1];
                        break;
                    case "PatientNo":
                        if (item[z + 2].equals("PatientID")) {
                            patientno = "";
                        } else {
                            patientno = item[z + 2];
                        }
                        break;
                    case "FirstName":
                        if (item[z + 2].equals("LastName")) {
                            firstname = "";
                        } else {
                            firstname = item[z + 2];
                        }
                        break;
                    case "LastName":
                        if (item[z + 2].equals("PatientNo")) {
                            lastname = "";
                        } else {
                            lastname = item[z + 2];
                        }
                        break;
                }
            }

            String[] pis = patientid.split(":");
            patientid = pis[1];
            patientList.add(new Patient(patientno, patientid, firstname, lastname));
        }
        patientAdapter = new PatientAdapter(patientList,this);
        listview.setAdapter(patientAdapter);

    }

    public static String getFirstName(){
        return firstname.getText().toString();
    }
    public static String getLastName(){
        return lastname.getText().toString();
    }
    public static String getPatientNo(){
        return patientno.getText().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create, menu);
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
