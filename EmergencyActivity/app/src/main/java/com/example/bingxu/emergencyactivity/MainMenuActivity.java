package com.example.bingxu.emergencyactivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


public class MainMenuActivity extends Activity{
    private Intent intent;

    private String[] texts = {
            "Doctor Management",
            "Paramedic Management",
            "Roster",
            "Join Activity"
    };

    private int[] images = new int[] {
            R.drawable.doctor,
            R.drawable.paramedic,
            R.drawable.roster,
            R.drawable.chat
    };

//    private ArrayList mButtons = new ArrayList();

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        String userID = settings.getString("userID","").toString();
        String firstName = settings.getString("firstName","").toString();

        GridView gridView = (GridView)findViewById(R.id.image_gridview);
        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();

        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("itemImage", images[i]);
            map.put("itemText", texts[i]);
            lstImageItem.add(map);
        }

        SimpleAdapter saImageItems = new SimpleAdapter(this,
                lstImageItem,// data
                R.layout.grid_item_button,// present layout
                new String[] { "itemImage", "itemText" },
                new int[] { R.id.griditem_image, R.id.griditem_text });
        gridView.setAdapter(saImageItems);
        gridView.setOnItemClickListener(new ItemClickListener());
    }


 public class ItemClickListener implements AdapterView.OnItemClickListener {

     public void onItemClick(AdapterView<?> parent, View view, int position, long rowid) {
         HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
         String itemText = (String) item.get("itemText");
         Object object = item.get("itemImage");
         Toast.makeText(MainMenuActivity.this, itemText, Toast.LENGTH_LONG).show();

         //according to the position of the picture, start the new activity
         switch (images[position]) {
             case R.drawable.doctor:
                 startActivity(new Intent(MainMenuActivity.this, DoctorManagement.class));//start a new Activity
//                 finish();//finish activity and destroy the activity
                 break;
             case R.drawable.paramedic:
                 startActivity(new Intent(MainMenuActivity.this, ParamedicActivity.class));
                 break;
             case R.drawable.roster:
                 startActivity(new Intent(MainMenuActivity.this, RosterActivity.class));
                 break;
             case R.drawable.chat:
                 startActivity(new Intent(MainMenuActivity.this, JoinActivity.class));
                 break;
         }

     }
 }

}
