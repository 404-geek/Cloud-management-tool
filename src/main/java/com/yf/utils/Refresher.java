package com.yf.utils;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Refresher {
   
	static Logger LOGGER = Logger.getLogger(Refresher.class.getName());

	public static String accessToken(String token) throws IOException {
      
		OkHttpClient client = new OkHttpClient();

		RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("client_id", "05cd59f9-6e20-4928-878b-a80b7033492a")
				.addFormDataPart("grant_type", "refresh_token")
				.addFormDataPart("refresh_token", token)
				.addFormDataPart("redirect_uri", "https://tpconnect.yellowfin.bi/getToken.jsp")
				.addFormDataPart("client_secret", "P3oOt9z2XWj8cE2ZjwINTLwFYZYLjzo/+d45qfFjO4k=")
				.addFormDataPart("resource", "https://management.core.windows.net/")
				.build();
		Request request = new Request.Builder().url("https://login.microsoftonline.com/common/OAuth2/token")
				.post(formBody).build();

		try{
	    Response response = client.newCall(request).execute();
		JsonElement je = new JsonParser().parse(response.body().string());
		JsonObject jo = je.getAsJsonObject();
		String accessToken = jo.get("access_token").getAsString();
		return accessToken;
		}
		
	    catch (Exception e) {

		return null;

		}
		}
	
	public static String refreshToken(String token) throws IOException {
	      
		OkHttpClient client = new OkHttpClient();

		RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("client_id", "05cd59f9-6e20-4928-878b-a80b7033492a")
				.addFormDataPart("grant_type", "refresh_token")
				.addFormDataPart("refresh_token", token)
				.addFormDataPart("redirect_uri", "https://tpconnect.yellowfin.bi/getToken.jsp")
				.addFormDataPart("client_secret", "P3oOt9z2XWj8cE2ZjwINTLwFYZYLjzo/+d45qfFjO4k=")
				.addFormDataPart("resource", "https://management.core.windows.net/")
				.build();
		Request request = new Request.Builder().url("https://login.microsoftonline.com/common/OAuth2/token")
				.post(formBody).build();

		try{
	    Response response = client.newCall(request).execute();
		JsonElement je = new JsonParser().parse(response.body().string());
		JsonObject jo = je.getAsJsonObject();
		String accessToken = jo.get("refresh_token").getAsString();
		return accessToken;
		}
		
	    catch (Exception e) {

		return null;

		}
		}
	}


