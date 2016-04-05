package com.example.bingxu.emergencyactivity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AddDescriptionActivity extends ActionBarActivity {

    EditText descriptionE;
    Button add;
    String patientid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_description);

        descriptionE = (EditText)findViewById(R.id.edit_des);
        add = (Button)findViewById(R.id.add);

        Intent intent=this.getIntent();
        patientid = intent.getStringExtra("patientID");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("patientID", patientid));
                params.add(new BasicNameValuePair("medicalDescription", descriptionE.getText().toString()));
                params.add(new BasicNameValuePair("createTime", currentDateandTime));

                Thread httpThread = new Thread(new HttpUtil(Urls.urlAddPatientDes, params));
                httpThread.start();
                try {
                    httpThread.join();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                Intent intent = new Intent(AddDescriptionActivity.this, ShowPatientActivity.class);
                intent.putExtra("patientID", patientid);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_description, menu);
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
