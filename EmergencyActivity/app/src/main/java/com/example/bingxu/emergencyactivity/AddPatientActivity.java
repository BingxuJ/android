package com.example.bingxu.emergencyactivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddPatientActivity extends ActionBarActivity {

    String patientid;
    String patientno;
    String firstname;
    String lastname;
    String gender;
    String age;
    String email;
    String phone;
    String address;
    String genderID;

    EditText patientnoE;
    EditText firstnameE;
    EditText lastnameE;
    RadioGroup genderR;
    RadioButton maleR;
    RadioButton femaleR;
    EditText ageE;
    EditText emailE;
    EditText phoneE;
    EditText addressE;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        patientnoE = (EditText)findViewById(R.id.edit_patientno);
        firstnameE = (EditText)findViewById(R.id.edit_firstname);
        lastnameE = (EditText)findViewById(R.id.edit_lastname);
        genderR = (RadioGroup)findViewById(R.id.radio_gender);
        maleR = (RadioButton)findViewById(R.id.male);
        femaleR = (RadioButton)findViewById(R.id.female);
        ageE = (EditText)findViewById(R.id.edit_age);
        emailE = (EditText)findViewById(R.id.edit_email);
        phoneE = (EditText)findViewById(R.id.edit_phone);
        addressE = (EditText)findViewById(R.id.edit_address);
        save  =(Button)findViewById(R.id.save);

        genderR.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.male:
                        genderID = 0 + "";
                        break;
                    case R.id.female:
                        genderID = 1 + "";
                        break;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                patientno = patientnoE.getText().toString();
                firstname = firstnameE.getText().toString();
                lastname = lastnameE.getText().toString();
                gender = genderID;
                age = ageE.getText().toString();
                email = emailE.getText().toString();
                phone = phoneE.getText().toString();
                address = addressE.getText().toString();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("firstName", firstname));
                params.add(new BasicNameValuePair("lastName", lastname));
                params.add(new BasicNameValuePair("patientNO", patientno));
                params.add(new BasicNameValuePair("gender", genderID));
                params.add(new BasicNameValuePair("age", age));
                params.add(new BasicNameValuePair("address", address));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("email", email));

                Thread httpThread = new Thread(new HttpUtil(Urls.urlAddPatientInfo, params));
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

                for (int z = 0; z < item.length; z++) {
                    if (z + 2 > item.length)
                        break;
                    switch (item[z]) {
                        case "PatientID":
                                patientid = item[z+1];
                            break;
                    }
                }
                String[] pt = patientid.split(":");
                patientid = pt[1];
                Intent intent = new Intent(AddPatientActivity.this, CreateDetailedActivity.class);
                intent.putExtra("patientID", patientid);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_patient, menu);
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
