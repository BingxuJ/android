package com.example.bingxu.emergencyactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Handler;


public class DoctorManagement extends Activity implements AbsListView.OnScrollListener {

    private String result = null;
    private String mStrContent = null;
    private byte[] imageAsBytes;

    public static List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    ListView listview;
    View moreView;
    private int count=0;
    public  SimpleAdapter simpleAdapter;
    private int times = 1;
    int lastItemIndex;
    private static final String TAG = null;

    private ImageView ptv;
    private Handler pic_hdl;
    private Bitmap bm;


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_management);
        list = new ArrayList<HashMap<String, Object>>();
        listview = (ListView) findViewById(R.id.listview);
        count = list.size();
        String[] items = {"img", "firstname", "lastname", "doctorLevel", "docNo"};
        int[] ids = { R.id.head_image, R.id.user_name, R.id.user_text, R.id.staff_level, R.id.staff_no };

        try {
            simpleAdapter = new SimpleAdapter(this, getData(times),
                    R.layout.list_item, items, ids);

        } catch (IOException e) {
            e.printStackTrace();
        }

        listview.setAdapter(simpleAdapter);

        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                if (view instanceof ImageView && data instanceof Drawable) {
                    ImageView iv = (ImageView) view;
                    iv.setImageDrawable((Drawable) data);
                    return true;
                } else
                    return false;
            }
        });

        listview.setOnScrollListener(this);

    }


    List<HashMap<String, Object>> getData(int num) throws IOException {
        times=times+1;
        List<NameValuePair> lists = new ArrayList<NameValuePair>();

        lists.add(new BasicNameValuePair("userID", num + ""));

        Thread httpThread = new Thread(new HttpUtil(Urls.urlDoctor, lists));
        httpThread.start();
        //start a new thread, and rest of the main thread will start until the thread finishes

        try {
            httpThread.join();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String result = HttpUtil.result;
        String[] results = result.split("\\}");

        for (int i = 0; i < results.length-1; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();

            String[] item = results[i].split("\"");
            String address = "";
            String email = "";
            String firstname = "";
            String lastname = "";
            String phone = "";
            String pt = "";
            String doctorLevel = "";
            String docNo = "";
            for(int z=0;z<item.length;z++){
                if(z+2>=item.length)
                    break;
                switch (item[z]){
                    case "address":
                        if(item[z+2].equals("department")){
                            address = "";
                        }else{
                            address = item[z+2];
                        }
                        break;
                    case "email":
                        if(item[z+2].equals("address")){
                            email = "";
                        }else{
                            email = item[z+2];
                        }
                        break;
                    case "firstName":
                        if(item[z+2].equals("photo")){
                            firstname = "";
                        }else{
                            firstname = item[z+2];
                        }
                        break;
                    case "lastName":
                        if(item[z+2].equals("userID")){
                            lastname = "";
                        }else{
                            lastname = item[z+2];
                        }
                        break;
                    case "phone":
                        if(item[z+2].equals("username")){
                            phone = "";
                        }else{
                            phone = item[z+2];
                        }
                        break;
                    case "photo":
                        pt = item[z+1];
                        break;
                    case "doctorLevel":
                        if(item[z+2].equals("email")){
                            doctorLevel = "";
                        }else{
                            doctorLevel = item[z+2];
                        }
                        break;
                    case "doctorNO":
                        if(item[z+2].equals("firstName")){
                            docNo = "";
                        }else{
                            docNo = item[z+2];
                        }

                }
            }
            String name = firstname;
            String price = lastname;
            String[] compare = pt.split("\\[");
            String pic = "";
            if(compare[0].equals(":null,")){
                pic = "";
            }else{
                String[] ims = compare[1].split("\\]");
                String[] cc = ims[0].split("\\,");
                byte[] t = new byte[cc.length];

                for(int j=0;j<t.length;j++){
                    t[j] = (byte) Integer.parseInt(cc[j]);
                }
                pic = Base64.encodeToString(t,Base64.DEFAULT);
            }
            String tag = "item";
            Log.v(tag, item[0]);
            FormatTools format = new FormatTools();
            byte[] imageAsBytes = Base64.decode(pic.getBytes(), Base64.DEFAULT);

            map.put("img", format.Bytes2Drawable(imageAsBytes));
            map.put("firstname", "First Name: "+name);
            map.put("lastname", "Last Name: "+price);
            map.put("doctorLevel", "DoctorLevel: " + doctorLevel);
            map.put("docNo", "DoctorNo.: " + docNo);
            list.add(map);
        }
        count=list.size();
        return list;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (lastItemIndex == simpleAdapter.getCount()-1 && scrollState == this.SCROLL_STATE_IDLE&&lastItemIndex<25) {
            Log.i(TAG, "Bottom line");
            moreView.setVisibility(view.VISIBLE);
            mHandler.sendEmptyMessage(0);
        }
        if(lastItemIndex>=25){
            Toast.makeText(getApplicationContext(), "No more", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        lastItemIndex = firstVisibleItem + visibleItemCount - 1 -1;
    }


    private android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    try {
                        getData(times);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    simpleAdapter.notifyDataSetChanged();
                    moreView.setVisibility(View.GONE);
                    break;
//                case 1:
//
//                    break;
                default:
                    break;
            }
        }
    };
}
