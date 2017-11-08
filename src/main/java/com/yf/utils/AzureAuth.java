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
				.addFormDataPart("client_id", "05cd59f9-6e20-4928-878b-a80b7033492a")
				.addFormDataPart("grant_type", "authorization_code").addFormDataPart("code", token)
				.addFormDataPart("redirect_uri", "https://tpconnect.yellowfin.bi/getToken.jsp")
				.addFormDataPart("client_secret", "P3oOt9z2XWj8cE2ZjwINTLwFYZYLjzo/+d45qfFjO4k=")
				.addFormDataPart("resource", "https://management.core.windows.net/").build();

		Request request = new Request.Builder().url("https://login.microsoftonline.com/common/OAuth2/token")
				.post(formBody).build();

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
				.addFormDataPart("client_id", "05cd59f9-6e20-4928-878b-a80b7033492a")
				.addFormDataPart("grant_type", "authorization_code").addFormDataPart("code", token)
				.addFormDataPart("redirect_uri", "https://tpconnect.yellowfin.bi/getToken.jsp")
				.addFormDataPart("client_secret", "P3oOt9z2XWj8cE2ZjwINTLwFYZYLjzo/+d45qfFjO4k=")
				.addFormDataPart("resource", "https://management.core.windows.net/").build();
		Request request = new Request.Builder().url("https://login.microsoftonline.com/common/OAuth2/token")
				.post(formBody).build();

		try {
			Response response = client.newCall(request).execute();
			return response.body().string();
		} catch (Exception e) {

			return "OK";
		}

	}
}
