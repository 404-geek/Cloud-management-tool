package com.yf.utils;

import java.util.logging.Logger;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONObject;

public class Subscriptions {
	static Logger LOGGER = Logger.getLogger(Subscriptions.class.getName());

	public static String subscriptions(String token) {
		String CONTENT = "application/json";
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url("https://management.azure.com/subscriptions?api-version=2015-01-01")
				.addHeader("Authorization", tok).addHeader("Content-type", "application/json").build();
		try {
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (Exception e) {
		}
		return "";
	}

	public static String getId(String token) {
		String CONTENT = "application/json";
		String tok = "Bearer " + token;

		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder().url("https://management.azure.com/subscriptions?api-version=2015-01-01")
				.addHeader("Authorization", tok).addHeader("Content-type", "application/json").build();
		try {
			Response response = client.newCall(request).execute();
			JSONObject jo = new JSONObject(response.body().string());
			JSONArray ja = jo.getJSONArray("value");
			JSONObject je = ja.getJSONObject(0);

			return je.getString("id");
		} catch (Exception e) {
		}
		return "";
	}
}
