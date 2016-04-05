package com.example.bingxu.emergencyactivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bingxu on 4/10/2015.
 */
class Patient{
    String patientno;
    String patientid;
    String firstname;
    String lastname;
    boolean selected;

    public Patient(String patientno, String patientid, String firstname, String lastname){
        super();
        this.patientno = patientno;
        this.patientid = patientid;
        this.firstname = firstname;
        this.lastname = lastname;
        selected = false;
    }
    public String getPatientNo(){
        return patientno;
    }
    public String getPatientId(){
        return patientid;
    }
    public String getFirstName(){
        return firstname;
    }
    public String getLastName(){
        return lastname;
    }
    public boolean selected(){
        return selected;
    }
    public void setSelected(boolean selected){this.selected=selected;}
}

public class PatientAdapter extends ArrayAdapter<Patient> {
    private List<View> views;
    private List<Patient> patientList;
    private Context context;
    private Intent intent;
    public static ArrayList<String> patients = new ArrayList<String>();
    public static String patientid;
//    private Map<Integer, Boolean> isSelected;
//    public View[] holder;
//    public int[] ids = new int[] {R.id.firstname, R.id.lastname, R.id.patientno,R.id.patient_selected,R.id.info};

    private static class PatientHolder {
        private TextView firstname;
        private TextView lastname;
        private TextView patientno;
        public CheckBox chkBox;
        private Button info;

    }

    public PatientAdapter(List<Patient>patientList, Context context) {
        super(context, R.layout.patientlist_item,patientList);
        this.patientList = patientList;
        this.context = context;
        views = new ArrayList<View>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;
        PatientHolder holder = new PatientHolder();

//        if(v == null) {
//
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = inflater.inflate(R.layout.patientlist_item, null);
//            holder = new View[ids.length];
//            for(int i=0; i<ids.length;i++) {
//                holder[i] = v.findViewById(ids[i]);
//            }
//            v.setTag(holder);
//        } else {
//            holder = (View[]) v.getTag();
//        }
        if(v == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.patientlist_item, null);

        } else {
            v = (View) v.getTag();
        }
        if(!views.contains(v)) {
            views.add(v);
        }

//        Map map = (Map)getItem(position);
//        v.setOnClickListener(new ButtonClickListener(map));
        Patient p = patientList.get(position);

        //Set map
//        if(!CreateActivity.isSelected.containsKey(position)){
//            CreateActivity.isSelected.put(position, p.selected());
//        }
//        if(!CreateActivity.viewList.containsKey(position)){
//            CreateActivity.viewList.put(position, v);
//        }

        holder.firstname = (TextView)v.findViewById(R.id.firstname);
        holder.lastname = (TextView)v.findViewById(R.id.lastname);
        holder.patientno = (TextView)v.findViewById(R.id.patientno);
        holder.chkBox = (CheckBox)v.findViewById(R.id.patient_selected);
        holder.info = (Button)v.findViewById(R.id.info);

//        holder.chkBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);

        holder.firstname.setText(p.getFirstName()== null ? "" : "First Name:"+p.getFirstName());
        holder.lastname.setText(p.getLastName()== null ? "" : "Last Name:"+p.getLastName());
        holder.patientno.setText(p.getPatientNo()== null ? "" : "Patient NO.:"+p.getPatientNo());

//        if(position==selected_position)
//        {
//            holder.chkBox.setChecked(true);
//        }
//        else
//        {
//            holder.chkBox.setChecked(false);
//        }
        holder.chkBox.setChecked(p.selected());
        v.setTag(v);

        holder.info.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Patient p = patientList.get(position);
                patientid = p.getPatientId();
                intent = new Intent(context, ShowPatientActivity.class);
                intent.putExtra("patientID", patientid);
                context.startActivity(intent);

            }
        });


        holder.chkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                int t = position;
                Patient p = patientList.get(position);
                String id = p.getPatientId();

                patients.clear();
                CreateActivity.selectedPatient = "";


                if (((CheckBox) v).isChecked()) {
                    for(Patient patient : patientList) {
                        patient.setSelected(false);
                    }
                    patientList.get(position).setSelected(true);
                    CreateActivity.selectedPatient = id;
//                    patients.add(id);
                } else {
                    patientList.get(position).setSelected(false);
                    CreateActivity.selectedPatient = "";
                }
                PatientAdapter.this.notifyDataSetChanged();

            }
        });

        return v;
    }

}
