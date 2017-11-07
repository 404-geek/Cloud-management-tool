package com.yf.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Resources {
	static Logger LOGGER = Logger.getLogger(Resources.class.getName());
	static String CONTENT = "application/json";

	public static String getResources(String token) {
		String tok = "Bearer " + token;
		ArrayList<String> subs = Subscriptions.getId(token);
		JsonObject jo1 = new JsonObject();
		for (int j=1;j <= subs.size();j++) {
		    int k = j-1;
			String id = subs.get(k);
			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url("https://management.azure.com" + id + "/resources?api-version=2017-05-10")
					.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
			try {
				Response response = client.newCall(request).execute();
				JsonElement je = new JsonParser().parse(response.body().string());
				JsonObject jo = je.getAsJsonObject();
				JsonArray ja = jo.getAsJsonArray("value");
				jo1.add(""+j+"", ja);
		} catch (Exception e) {
			return null;
		}
		}
		return jo1.toString();
	}

	public static LinkedHashMap<String, String> getType(String token) {
		ArrayList<String> subs = Subscriptions.getId(token);
		LinkedHashMap<String, String> hm = new LinkedHashMap<String, String>();
		for (String id : subs) {
		String tok = "Bearer " + token;

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + id + "/resources?api-version=2017-05-10")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JSONObject jo = new JSONObject(response.body().string());
			JSONArray ja = jo.getJSONArray("value");
			int n = 0;
			for (n = 0; n < ja.length(); n++) {
				JSONObject je = ja.getJSONObject(n);
				String Type = je.getString("type");
				String uid = je.getString("id");
				hm.put(uid, Type);
			}
		} catch (Exception e) {
			return null;
		}
		}
		return hm;
	}

	public static String getResid(String token, int arr) {
		LinkedHashMap<String, String> type = getType(token);
		ArrayList<String> list = new ArrayList<String>();
		try {
			for (Map.Entry<String, String> entry : type.entrySet()) {
				list.add(entry.getKey());
			}
			return list.get(arr);
		} catch (Exception e) {
			return null;
		}
	}
}
