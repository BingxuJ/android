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
import java.util.List;


public class EditPatientInfoActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_edit_patient_info);

        Intent intent=this.getIntent();
        patientid = intent.getStringExtra("patientID");
        patientno = intent.getStringExtra("patientNO");
        firstname = intent.getStringExtra("patientFirstName");
        lastname = intent.getStringExtra("patientLastName");
        gender = intent.getStringExtra("patientGender");
        age = intent.getStringExtra("patientAge");
        email = intent.getStringExtra("patientEmail");
        phone = intent.getStringExtra("patientPhone");
        address = intent.getStringExtra("patientAddress");

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

        patientnoE.setText(patientno);
        firstnameE.setText(firstname);
        lastnameE.setText(lastname);
        ageE.setText(age);
        emailE.setText(email);
        phoneE.setText(phone);
        addressE.setText(address);
        if(gender.equals("Female")){
            femaleR.setChecked(true);
            genderID = "1";
        }else{
            maleR.setChecked(true);
            genderID = "0";
        }

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
                params.add(new BasicNameValuePair("patientID", patientid));
                params.add(new BasicNameValuePair("firstName", firstname));
                params.add(new BasicNameValuePair("lastName", lastname));
                params.add(new BasicNameValuePair("patientNO", patientno));
                params.add(new BasicNameValuePair("gender", genderID));
                params.add(new BasicNameValuePair("age", age));
                params.add(new BasicNameValuePair("address", address));
                params.add(new BasicNameValuePair("phone", phone));
                params.add(new BasicNameValuePair("email", email));

                Thread httpThread = new Thread(new HttpUtil(Urls.urlEditPatientInfo, params));
                httpThread.start();
                try {
                    httpThread.join();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                Intent intent = new Intent(EditPatientInfoActivity.this, ShowPatientActivity.class);
                intent.putExtra("patientID", patientid);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_patient_info, menu);
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
