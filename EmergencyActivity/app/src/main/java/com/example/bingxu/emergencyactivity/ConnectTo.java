package com.example.bingxu.emergencyactivity;

import android.os.Message;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import java.util.List;


public class ConnectTo {

    private String httpUrl;
    private HttpPost httpPost;
    public static String result = "";
    private HttpResponse httpResponse = null;

    public ConnectTo(String httpUrl){
        this.httpUrl = httpUrl;
        httpPost = new HttpPost(httpUrl);
    }

    public String getInfo() throws IOException {

        try{
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(httpUrl);
            HttpResponse httpResponse = httpClient.execute(httppost);

            int code = httpResponse.getStatusLine().getStatusCode();
            if (code == 200) {
                result = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

            }else{
                System.out.print("Failed");
            }

        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String requestConnect(List<NameValuePair> params){

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpResponse = new DefaultHttpClient().execute(httpPost);

            if (httpResponse.getStatusLine().getStatusCode() == 200) {

                result = EntityUtils.toString(httpResponse.getEntity());

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }
}
