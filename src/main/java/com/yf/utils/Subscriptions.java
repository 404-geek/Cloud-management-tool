package com.yf.utils;

import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Subscriptions {
    

   static Logger LOGGER = Logger.getLogger(Subscriptions.class.getName());
	
	public static String subscriptions(String token) {
		final String CONTENT = "application/json";
		String tok = "Bearer "+token;
		OkHttpClient client = new OkHttpClient();
		
		Request request = new Request.Builder()
			    .url("https://management.azure.com/subscriptions?api-version=2015-01-01")
                .addHeader("Authorization", tok)
                .addHeader("Content-type", CONTENT)
                .build();
		try {
           Response response = client.newCall(request).execute();
			return response.body().string();
			}
		catch (Exception e){
			return "";
		}
       }
	public static String getId(String token) {
		
		final String CONTENT = "application/json";
		String tok = "Bearer "+token;
        
		OkHttpClient client = new OkHttpClient();
		
		Request request = new Request.Builder()
			    .url("https://management.azure.com/subscriptions?api-version=2015-01-01")
                .addHeader("Authorization", tok)
                .addHeader("Content-type", CONTENT)
                .build();
		try {
			Response response = client.newCall(request).execute();
			JSONObject jo = new JSONObject(response.body().string());
			JSONArray ja = jo.getJSONArray("value");
			JSONObject  je= ja.getJSONObject(0);
			
			String id = je.getString("id");
			return id;
			}
		catch (Exception e){
			return "";
		}
		
	}

}


