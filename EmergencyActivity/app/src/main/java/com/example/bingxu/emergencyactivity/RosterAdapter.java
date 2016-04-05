package com.example.bingxu.emergencyactivity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bingxu on 18/09/15.
 */
class Roster {

    String firstname;
    String lastname;
    String role;
    Drawable image;
    String userid;
    boolean selected = false;

    public Roster(String ui, Drawable image, String firstname, String lastname, String role, boolean selected) {
        super();
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.selected = selected;
        userid = ui;
        this.image = image;
    }

    public String getID(){
        return userid;
    }
    public void setID(String userid){
        this.userid = userid;
    }

    public Drawable getImage(){
        return image;
    }
    public void setImage(Drawable image){
        this.image = image;
    }

    public String getFirstName() {
        return firstname;
    }

    public void setFirstName(String firstname) {
        this.firstname = firstname;
    }

    public String getLastName(){
        return lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public String getRole(){
        return role;
    }
    public void setRole(String role){
        this.role = role;
    }
}

public class RosterAdapter extends ArrayAdapter<Roster>{

    private List<Roster> rosterList;
    private Context context;

    public static ArrayList<String> users = new ArrayList<String>();

    public RosterAdapter(List<Roster> rosterList, Context context) {
        super(context, R.layout.rosterlist_item, rosterList);
        this.rosterList = rosterList;
        this.context = context;
    }

    private static class RosterHolder {
        public ImageView img;
        public TextView firstName;
        public TextView lastName;
        public TextView role;
        public CheckBox chkBox;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        RosterHolder holder = new RosterHolder();

        if(v == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.rosterlist_item, null);

        } else {
            v = (View) v.getTag();
        }

        Roster p = rosterList.get(position);
        holder.img = (ImageView)v.findViewById(R.id.head_image);
        holder.firstName = (TextView) v.findViewById(R.id.user_name);
        holder.lastName = (TextView) v.findViewById(R.id.user_text);
        holder.role= (TextView)v.findViewById(R.id.staff_role);
        holder.chkBox = (CheckBox) v.findViewById(R.id.staff_selected);

        holder.chkBox.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) context);

        holder.img.setImageDrawable(p.getImage() == null ? null : p.getImage());
        holder.firstName.setText(p.getFirstName() == null ? "" : p.getFirstName());
        holder.lastName.setText(p.getLastName() == null ? "" : p.getLastName());
        holder.role.setText(p.getRole() == null ? "" : p.getRole());
        holder.chkBox.setChecked(p.isSelected());
        if(p.isSelected()==true && !users.contains(p.getID())){
            users.add(p.getID());
        }
        //holder.chkBox.setTag(p);
        v.setTag(v);

        holder.chkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Roster p = rosterList.get(position);
                String id = p.getID();
                if (((CheckBox) v).isChecked()) {
                    users.add(id);
                    p.selected = true;

                } else {
                    p.selected = false;
                    users.remove(id);

                }
            }
        });
        return v;
    }
}
