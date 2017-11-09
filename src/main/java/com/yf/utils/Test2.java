package com.yf.utils;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {
	public static void main(String[] args) {

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyIsImtpZCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTEwMjA1MzAwLCJuYmYiOjE1MTAyMDUzMDAsImV4cCI6MTUxMDIwOTIwMCwiYWNyIjoiMSIsImFpbyI6IlkyTmdZUGo2N20vWVJkZHRxOWMzYzNyZnVNaXUweTVwRnM4ZXBibTViczE5RzlXZEorc0EiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDEuNDYuMTQ2IiwibmFtZSI6Ikp1c3RpbiBIZXdpdHQiLCJvaWQiOiI1Zjg5MDZkMS1lZTJkLTQyMWQtYWE3MS0wNmE4MTA5YzIxMzAiLCJwdWlkIjoiMTAwMzNGRkY4RDI3MjRBQyIsInNjcCI6InVzZXJfaW1wZXJzb25hdGlvbiIsInN1YiI6InByWURpX2wtR0dsT3pqX2lMNk9renBBS3RBb0NkVGVsRzBWdVJzOU5SbVkiLCJ0aWQiOiIyN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTciLCJ1bmlxdWVfbmFtZSI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXBuIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1dGkiOiJPamU1UXdIdWgwS2o2U1RYY3NjYUFBIiwidmVyIjoiMS4wIiwid2lkcyI6WyI2MmU5MDM5NC02OWY1LTQyMzctOTE5MC0wMTIxNzcxNDVlMTAiXX0.EljMdyPZTUC4ulleLM9awHRBt0BrxHFJziRF2DYu6WDIzDai1IgptOY6zIQ-TCAY_4aEoS2UZ-_9xxML75tToQebXZ9vNvFyTgbezAyk8N0EQSD83e8Pnhs9ST33yKTn-pSCoV7ad0QxInzxpkAwnJRQYuRfDQ659mBzO2Mt4mWBOblDwyH9kEVIb8oRnGMm0_esVCDp8JBQuQ_Pw3JUQtcTQCy_wM_PdnNkwXaWruS_r8xqeb3542xj-6BjscNinz5gr7L554Ok3pr1LcM7v6TOu6ULTD9mDjUU7GwvwolaeM8NT0EqNLjuMfGRzIoMu9NO07PaopBff2rcJsQkIw";
		String CONTENT = "application/json";
		String p = "22";
		//String currency = "AUD";
		String Loc = "en-AU";
		String reg = "AU";
		String offer = "0003P";
		//String tok = "Bearer " + token;
		JsonArray ja = new JsonArray();
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		ArrayList<String> idl = Subscriptions.getId(token);
		for (String id : idl) {
			String cycle = NewBilling.getBillingCycle(token, id);
			JsonElement je1 = new JsonParser().parse(cycle);
			JsonArray jaa = je1.getAsJsonArray();
			System.out.println(jaa.size());
			int limit = Integer.parseInt(p);
			if(limit>jaa.size()){
				limit = jaa.size();
			}
			for (int k = 0; k < limit; k++) {
				String ds1 = jaa.get(k).getAsJsonObject().get("InvoicePeriodStartDate").getAsString();
				String ds2 = jaa.get(k).getAsJsonObject().get("InvoicePeriodEndDate").getAsString();
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
				String dstart = null;
				String dend = null;
				try {
					dstart = sdf2.format(sdf1.parse(ds1));
					dend = sdf2.format(sdf1.parse(ds2));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String name = jaa.get(k).getAsJsonObject().get("Name").getAsString();
				Request request = new Request.Builder()
						.url("https://management.azure.com" + id
								+ "/providers/Microsoft.Billing/invoices/"+name+"?api-version=2017-04-24-preview")
						.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
				try {
					Response response = client.newCall(request).execute();
					JsonElement je = new JsonParser().parse(response.body().string());
					String url = je.getAsJsonObject().get("properties").getAsJsonObject().get("downloadUrl").getAsJsonObject().get("url").getAsString();
				    PdfReader reader = new PdfReader(url);
				    String str=PdfTextExtractor.getTextFromPage(reader,1 );
				    reader.close();
				    String str1=str.replaceAll("[\r\n]+", " ");
					final String regex = "(?:\\S+\\s+\\S+\\s)?\\S*Total Amount";
					final Pattern pattern = Pattern.compile(regex);
					final Matcher matcher = pattern.matcher(str1);
			        String match ="";
					while (matcher.find()) {
						match = matcher.group(0);
					}
					
					final String regex1 = "[1-9]\\d*(\\.\\d+)";
					final String string = match;
					final Pattern pattern1 = Pattern.compile(regex1);
					final Matcher matcher1 = pattern1.matcher(string);
					String sum = "0.00";

					while (matcher1.find()) {
					   sum = matcher1.group(0);}
					final String regex2 = "^\\w+";
					final String string2 = match;

					final Pattern pattern2 = Pattern.compile(regex2);
					final Matcher matcher2 = pattern2.matcher(string2);
					String currency = "";

					while (matcher2.find()) {
					    currency = matcher2.group(0);}
				
					JsonObject jo1 = new JsonObject();
					jo1.addProperty("Subscription Id", id);
					jo1.addProperty("ReportedStartedTime", dstart);
					jo1.addProperty("ReportedEndTime", dend);
					jo1.addProperty("Bill", sum);
					jo1.addProperty("Currency", currency);
					
					ja.add(jo1);
				} catch (Exception e) {
					return;
				}
			}
		}
		System.out.println(ja.toString());
		
}
}