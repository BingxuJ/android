package com.example.bingxu.emergencyactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingxu on 3/10/15.
 */
class Activity{

    String activityID;
    String userID;
    String activityName;
    String activityDes;
    int accepted = 0;
    String patientID;

    public Activity(String activityID, String activityName, String activityDes, String userID , int accepted, String patientID){
        super();
        this.activityID = activityID;
        this.activityName = activityName;
        this.activityDes = activityDes;
        this.userID = userID;
        this.accepted = accepted;
        this.patientID = patientID;
    }

    public String getUserID(){
        return userID;
    }
    public String getActivityID(){
        return activityID;
    }
    public String getActivityName(){
        return activityName;
    }
    public String getActivityDes(){
        return activityDes;
    }
    public int getAccepted(){
        return accepted;
    }
    public String getPatientID(){
        return patientID;
    }
}


public class ActivityAdapter extends ArrayAdapter<Activity>{
    private List<Activity> activityList;
    private Context context;
    private Intent intent;
    private Activity JoinActivity;
    public static String patientid;


    private static class ActivityHolder {
        private TextView actname;
        private TextView actdes;
        private Button join;
        private Button info;
        private Button chat;
    }

    public ActivityAdapter(List<Activity> activityList, Context context) {
        super(context, R.layout.activity_item, activityList);
        this.activityList = activityList;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        View v = convertView;

        final ActivityHolder holder = new ActivityHolder();
        if(v == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.activity_item, null);

        } else {
            v = (View) v.getTag();
        }
        Activity p = activityList.get(position);
        holder.actname = (TextView)v.findViewById(R.id.activityname);
        holder.actdes = (TextView)v.findViewById(R.id.adescription);
        final Button join = (Button)v.findViewById(R.id.join);
        holder.info = (Button)v.findViewById(R.id.info);
        holder.chat = (Button)v.findViewById(R.id.chat);

        holder.actname.setText(p.getActivityName()== null ? "" : "Act Name:" + p.getActivityName());
        holder.actdes.setText(p.getActivityDes()== null ? "" : "Act Des:" + p.getActivityDes());
        v.setTag(v);

        if(p.accepted==1){
            join.setVisibility(View.INVISIBLE);
        }
        holder.info.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Activity p = activityList.get(position);
                patientid = p.getPatientID();
                intent = new Intent(context, ShowPatientActivity.class);
                intent.putExtra("patientID", patientid);
                context.startActivity(intent);

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Activity p = activityList.get(position);
                String id = p.getActivityID();
                int accepted = p.accepted;
                join.setVisibility(View.INVISIBLE);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userID", p.getUserID()));
                params.add(new BasicNameValuePair("activityID", p.getActivityID()));

                Thread httpThread = new Thread(new HttpUtil(Urls.urlJoinActivity, params));
                httpThread.start();
                try {
                    httpThread.join();
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            }
        });

       return v;
    }

}

