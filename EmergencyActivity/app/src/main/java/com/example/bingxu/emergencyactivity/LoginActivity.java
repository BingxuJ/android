package com.example.bingxu.emergencyactivity;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    private EditText username;
    private EditText password;
    private Button loginButton;
    private Button registerButton;
    private Intent intent;
    private String mStrContent;
    private ImageView background;
    private String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        background = (ImageView)findViewById(R.id.background);
//        background.setImageResource(R.drawable.emergency);
        loginButton = (Button)findViewById(R.id.login_button);
        registerButton = (Button)findViewById(R.id.register_button);

        
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = (EditText)findViewById(R.id.username);
                password = (EditText)findViewById(R.id.password);
                Thread thread = new Thread(new MyThread());
                thread.start();

            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                userLogin(username, password);
                Message msg = handler.obtainMessage(1);
                msg.sendToTarget();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1) {
                toLogin();
            }
        }
    };


    public void userLogin(EditText username, EditText password) throws UnsupportedEncodingException {

//        ConnectTo ct = new ConnectTo(Urls.urlLogin);

        List<NameValuePair> params = new ArrayList<NameValuePair>();


        params.add(new BasicNameValuePair("username", username.getText().toString()));
        params.add(new BasicNameValuePair("password", password.getText().toString()));

        ConnectTo ct = new ConnectTo(Urls.urlLogin);
        result = ct.requestConnect(params);

        String[] results = result.split("\\}");
        HashMap<String, Object> map = new HashMap<String, Object>();

        String[] item = results[0].split("\"");

        String firstname = "";
        String lastname = "";
        String userID = "";
        String activityID = "";
        for (int z = 0; z < item.length; z++) {
            if (z + 2 >= item.length)
                break;
            switch (item[z]) {
                case "firstName":
                    if (item[z + 2].equals("lastName")) {
                        firstname = "";
                    } else {
                        firstname = item[z + 2];
                    }
                    break;
                case "lastName":
                    if(item[z+2].equals("password")){
                        lastname = "";
                    }else{
                        lastname = item[z + 2];
                    }

                case "userID":
                    if (item[z + 2].equals("username")) {
                        userID = "";
                    } else {
                        userID = item[z + 2];
                    }
                    break;
            }
        }

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("firstName", firstname);
        editor.putString("lastName", lastname);
        editor.putString("userID", userID);
        editor.commit();
    }

    public void toLogin() {
        if(result!=null && !result.equals("null")){
            intent = new Intent(LoginActivity.this,MainMenuActivity.class);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
