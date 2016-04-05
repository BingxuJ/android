package com.example.bingxu.emergencyactivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends Activity{

    private Button registerButton;
    private Button tPhoto;
    private EditText username;
    private EditText password;
    private EditText firstname;
    private EditText lastname;
    private EditText email;
    private EditText address;
    private EditText phone;
    private EditText department;
    private EditText departmentAddress;
    private EditText staffNo;
    private EditText staffLevel;
    private RadioGroup role;
    private Bitmap bm;
    private String roleID = 1+"";
    private String rp;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register);
        registerButton = (Button)findViewById(R.id.user_register_button);
        tPhoto = (Button)findViewById(R.id.select_photo);
        username = (EditText)findViewById(R.id.register_username);
        password = (EditText)findViewById(R.id.register_password);
        firstname = (EditText)findViewById(R.id.register_firstname);
        lastname = (EditText)findViewById(R.id.register_lastname);
        email = (EditText)findViewById(R.id.register_email);
        address = (EditText)findViewById(R.id.register_address);
        phone = (EditText)findViewById(R.id.register_phone);
        department = (EditText)findViewById(R.id.register_department);
        departmentAddress = (EditText)findViewById(R.id.register_department_address);
        role = (RadioGroup)findViewById(R.id.radio_role);
        staffNo = (EditText)findViewById(R.id.register_staffid);
        staffLevel = (EditText)findViewById(R.id.register_stafflevel);

        //register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    registerUser(Urls.urlCreateUser);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });

        //taking photos
        tPhoto.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,SelectPicActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        //setting roles
        role.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_doctor:
                        roleID = 1 + "";
                        break;
                    case R.id.radio_para:
                        roleID = 2 + "";
                        break;
                }
            }
        });

    }


    /**
     * setting image*/
    public void setImage(Bitmap bt){
        bm = bt;
    }

    protected void onActivityResult( int requestCode, int resultCode, Intent data )
    {
        switch ( resultCode ) {
            case -1 :
                //role paramedic
                System.out.println(data.getExtras().getString( "rp" ));
                rp = data.getExtras().getString( "rp" );
                break;
            default :
                break;
        }

    }

    public void registerUser(String url) throws UnsupportedEncodingException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();

        params.add(new BasicNameValuePair("user.username", username.getText().toString()));
        params.add(new BasicNameValuePair("user.password", password.getText().toString()));
        params.add(new BasicNameValuePair("user.phone", phone.getText().toString()));
        params.add(new BasicNameValuePair("user.email", email.getText().toString()));
        params.add(new BasicNameValuePair("user.address", address.getText().toString()));
        params.add(new BasicNameValuePair("user.firstName", firstname.getText().toString()));
        params.add(new BasicNameValuePair("user.lastName", lastname.getText().toString()));
        params.add(new BasicNameValuePair("staff.department", department.getText().toString()));
        params.add(new BasicNameValuePair("staff.departmentAddress", departmentAddress.getText().toString()));
        params.add(new BasicNameValuePair("user.roleID", roleID));
        params.add(new BasicNameValuePair("staff.no", staffNo.getText().toString()));
        params.add(new BasicNameValuePair("staff.level", staffLevel.getText().toString()));
        params.add(new BasicNameValuePair("headphoto", rp));
//        params.add(new BasicNameValuePair("headphoto", imageAsString));

        Thread httpThread = new Thread(new HttpUtil(url, params));
        httpThread.start();

        try {
            httpThread.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        String result = HttpUtil.result;

        if(!result.equals("")||!result.equals(null)){
            Intent intent = new Intent(RegisterActivity.this,MainMenuActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
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
