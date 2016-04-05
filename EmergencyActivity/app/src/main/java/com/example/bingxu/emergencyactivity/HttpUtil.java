package com.example.bingxu.emergencyactivity;

import android.graphics.Bitmap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;

public class HttpUtil implements Runnable {
	public static String result = "";
	public static Bitmap bitmap = null;
	public String url = null;
	List<NameValuePair> list;

	public HttpUtil(String url, List<NameValuePair> list) {
		this.list = list;
		this.url = url;
	}

	@Override
	public void run() {
		try {
			// http post connection object url
			HttpPost httppost = new HttpPost(url);
			// set the entity
			HttpEntity httpentity = new UrlEncodedFormEntity(list, "UTF-8");
			// set the query parameters
			httppost.setEntity(httpentity);
			// set the http connection
			HttpClient httpclient = new DefaultHttpClient();
			// get the query result
			HttpResponse response = httpclient.execute(httppost);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				// get the string format result
				result = EntityUtils.toString(response.getEntity(), "UTF-8");

			/*
				HttpEntity httpEntity = response.getEntity();
				InputStream is =  httpEntity.getContent();
				bitmap = BitmapFactory.decodeStream(is);
				is.close();*/
			} else {
				result = "Error request!";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

}
