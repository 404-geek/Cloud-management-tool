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
		
		String token = "AQABAAIAAABHh4kmS_aKT5XrjzxRAtHzZwEf0sbfRs71MIYsI7HFKWM7PS-PzSEfdy0hqcVEPxECE0qxpJx4R9jLMdfLp_DwYS__ixqucatn1G_7S9SA5m-VYQhn_kITKD3yFgKGbSEvcWkvSnOfH1zSgE3lJQavGT6INCoOQ63gkM9fVFuXLTmmsk_IWE3iF84vkR9X3GeoT4s5o6LPK8-b1bknBHhmBmO4jIV8QXuaauQpLkxQgTt0dEmrEaYZo7_7E1Iwu1mma7KeI7_NCnqJESEiON817SErmgmJWi4Z7gRLLA_iOH9KBH3IsVAIiZ9gDIiUKbLxWkFIt4s021y46p_41EVmkmp7-F7L7f28fmNI5XZ0xIMjE3DaRgnbYD7wvb0z_iV39F9vMT6ia0akZFero2TGY_codaLJL9pHVlCTfrcqA94iRrq0AIPZymAuz90hmNphtTWFsv7dMl0B_EW9211A0HbbZKkzPBG7MNC0ivrVEXGWjeW6KUn6yp0SKYZ5NFvTgKbXGgprW8IZFWhuP3d9f9EhRg-TgWxEOkvOxj4BZTP_gHg5yZvCNipIYHYCBjsUXswcNByeOT7ylDx3ao7wwL8zO0Kh0HQMuCJSX1y39iAA	";
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
