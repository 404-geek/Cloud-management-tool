package com.yf.utils;

import java.util.logging.Logger;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AzureAuth {

	static Logger LOGGER = Logger.getLogger(AzureAuth.class.getName());

	public static int authCheck(String token) {

		int responseCode = 999;

		OkHttpClient client = new OkHttpClient();

		RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("client_id", "b3144fe1-6338-43a0-8918-c05291f51170")
				.addFormDataPart("grant_type", "authorization_code")
				.addFormDataPart("code", token)
				.addFormDataPart("redirect_uri", "https://tpconnect.yellowfin.bi/getToken.jsp")
				.addFormDataPart("client_secret", "L7mq5+HjRZo3NCnJ1vjR4E0FWubFkiC+KHe2+hd2xd0=")
				.addFormDataPart("resource", "https://management.core.windows.net/")
				.build();

		Request request = new Request.Builder()
				.url("https://login.microsoftonline.com/common/OAuth2/token")
				.post(formBody)
				.build();

		try {
			Response response = client.newCall(request).execute();
			return response.code();
		} catch (Exception e) {

			return responseCode;
		}

	}

	public static String getResponse(String token) {
  
	

		OkHttpClient client = new OkHttpClient();

		RequestBody formBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("client_id", "b3144fe1-6338-43a0-8918-c05291f51170")
				.addFormDataPart("grant_type", "authorization_code")
				.addFormDataPart("code", token)
				.addFormDataPart("redirect_uri", "https://tpconnect.yellowfin.bi/getToken.jsp")
				.addFormDataPart("client_secret", "L7mq5+HjRZo3NCnJ1vjR4E0FWubFkiC+KHe2+hd2xd0=")
				.addFormDataPart("resource", "https://management.core.windows.net/").build();
		Request request = new Request.Builder().url("https://login.microsoftonline.com/common/OAuth2/token")
				.post(formBody).build();

		try {
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (Exception e) {

			return "";
		}

	}
}
