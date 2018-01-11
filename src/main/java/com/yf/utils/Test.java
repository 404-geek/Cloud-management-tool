package com.yf.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Test {
	public static void main(String[] args) {
		/*
		 * DateFormat utcFormat = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		 * utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		 * 
		 * Date date = null; try { date =
		 * utcFormat.parse("2016-03-01T00:00:00Z"); } catch (ParseException e) {
		 * e.printStackTrace(); } DateFormat pstFormat = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * pstFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		 * Timestamp ts = Timestamp.valueOf(pstFormat.format(date));
		 * 
		 * System.out.println(ts);
		 */
		/*
		 * String monthBegin = LocalDate.now().withDayOfMonth(1).toString();
		 * 
		 * SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		 * SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy"); String
		 * ds2 = null; try { ds2 = sdf2.format(sdf1.parse(monthBegin)); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } System.out.println(ds2);
		 */
		
		String token = "AQABAAIAAABHh4kmS_aKT5XrjzxRAtHzkTBeeW0PcUH4_twbRRw6UkWZz-WFJfWBTwOBLzpTc9yhWGvpkC5Rhm1XkZcszTr-CuQUrKy50AwmU_6hOiiC2VW1o0vOQcg7Eatofr1QQ8x5OC8cdBQYCwBtblCWqvG5f6KijIcaeMv4QoSed6BVh45bG5uYowZWVIj7nK5zNdaO1Gi776F-s71gs8Yp34lsVFREXwmNBeNIu3THmt1pGlhitC2XANRBelIk-LASgbKJ9nnIuAxaRY6WegdpFsi_ZtKJzC3qAasybG9JyNu0v-Gk0-0Y0nRjhNQk3zzmRuRm2a7jQmFdEZaqHaU2J3T4FhMh5Xcpo975yi6r1GJiOx3iTXyc8nADx00-pvAESTk0KQURQMaGhlx8CKz6DCk_tK7EDvZ3BJh3jq2CMChE9d2YN9f2zsy7GnHEYGU_LlVK3d8am-MFkhCTzfPt_K69saRK_zhaQCWdgGtQ39iBvlpZQge4i684BOgRq8yFELsBKFxX2XsJwyKamgKSGyR2fi21ynoxoL4INTCF5m7YByfnhawRutS7jS-SuBh_3dcV8Qm6t-vSQFg5BasHBJtw_nPXH2KoESMM8K44I-tCgiAA";
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
			System.out.println(response.body().string());
		} catch (Exception e) {
			
		}
	}
}
