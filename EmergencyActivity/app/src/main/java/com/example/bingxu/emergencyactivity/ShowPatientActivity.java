package com.example.bingxu.emergencyactivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShowPatientActivity extends ActionBarActivity {

    private String patientid;
    private TextView patientV;
    private TextView firstnameV;
    private TextView lastnameV;
    private TextView genderV;
    private TextView addressV;
    private TextView emailV;
    private TextView phoneV;
    private String patientID;
    private Button edit;
    private Button search;
    private Button add;
    private Intent intent;



    String patientno;
    String firstname;
    String lastname;
    String gender;
    String age;
    String email;
    String phone;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_patient);

        patientV = (TextView)findViewById(R.id.patientno);
        firstnameV = (TextView)findViewById(R.id.firstname);
        lastnameV = (TextView)findViewById(R.id.lastname);
        genderV = (TextView)findViewById(R.id.gender);
        addressV = (TextView)findViewById(R.id.address);
        emailV = (TextView)findViewById(R.id.email);
        phoneV = (TextView)findViewById(R.id.phone);
        edit = (Button)findViewById(R.id.edit);
        search = (Button)findViewById(R.id.searchdescription);
        add = (Button)findViewById(R.id.adddescription);

        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Activity p = activityList.get(position);
//                patientid = p.getPatientID();
                intent = new Intent(ShowPatientActivity.this, EditPatientInfoActivity.class);
                intent.putExtra("patientID", patientid);
                intent.putExtra("patientNO", patientno);
                intent.putExtra("patientFirstName", firstname);
                intent.putExtra("patientLastName", lastname);
                intent.putExtra("patientGender", gender);
                intent.putExtra("patientAge", age);
                intent.putExtra("patientEmail", email);
                intent.putExtra("patientPhone", phone);
                intent.putExtra("patientAddress", address);
                startActivity(intent);
                finish();

            }
        });

        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                intent = new Intent(ShowPatientActivity.this, AddDescriptionActivity.class);
                intent.putExtra("patientID", patientid);
                startActivity(intent);
                finish();
            }
        });

        Intent intent=this.getIntent();
        patientid = intent.getStringExtra("patientID");

        displayActivity();
    }

    private void displayActivity() {
        final List<NameValuePair> params = new ArrayList<NameValuePair>();

        String URL;
        params.add(new BasicNameValuePair("patientID", patientid));
        URL = Urls.urlPatientID;

        Thread httpThread = new Thread(new HttpUtil(URL, params));
        httpThread.start();
        try {
            httpThread.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String result = HttpUtil.result;

        String[] results = result.split("\\}");


        HashMap<String, Object> map = new HashMap<String, Object>();

        String[] item = results[0].split("\"");
        patientno = "";
        firstname = "";
        lastname = "";
        gender = "";
        age = "";
        email = "";
        phone = "";
        address = "";

        for (int z = 0; z < item.length; z++) {
            if (z + 2 > item.length)
                break;
            switch (item[z]) {
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
                case "Phone":
                    if(item[z+2].equals("Age")) {
                        phone = "";
                    }else{
                        phone = item[z+2];
                    }
                    break;
                case "Age":
                    if(item[z+1].equals("Email")){
                        age = "";
                    }else{
                        age = item[z + 1];
                    }
                    break;
                case "Email":
                    if(item[z+2].equals("Address")){
                        email = "";
                    }else{
                        email = item[z + 2];
                    }
                    break;
                case "Gender":
                    if(item[z+1].equals("FirstName")){
                        gender = "";
                    }else{
                        gender = item[z + 1];
                    }
                    break;
                case "Address":
                    if(item[z+2].equals("Gender")){
                        address = "";
                    }else{
                        address = item[z + 2];
                    }
                    break;
            }
        }
        String[] ag = age.split(":");
        String[] ags = ag[1].split(",");
        age = ags[0];

        if(gender.equals(":1,")){
            gender = "Female";
        }else{
            gender = "Male";
        }
        patientV.setText("Patient NO.: " + patientno);
        firstnameV.setText("First Name: " + firstname);
        lastnameV.setText("Last Name: " + lastname);
        genderV.setText("Gender: " + gender);
        addressV.setText("Address: " + address);
        emailV.setText("Email: " + email);
        phoneV.setText("Phone: " + phone);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_patient, menu);
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
