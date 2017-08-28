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

	public static String refreshToken(String token) throws IOException {
      
		OkHttpClient client = new OkHttpClient();

		RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("client_id", "b3144fe1-6338-43a0-8918-c05291f51170")
				.addFormDataPart("grant_type", "refresh_token")
				.addFormDataPart("refresh_token", token)
				.addFormDataPart("redirect_uri", "https://tpconnect.yellowfin.bi/getToken.jsp")
				.addFormDataPart("client_secret", "L7mq5+HjRZo3NCnJ1vjR4E0FWubFkiC+KHe2+hd2xd0=")
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

		return "";

		}
		}
	}


