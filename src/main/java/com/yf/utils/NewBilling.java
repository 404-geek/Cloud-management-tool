package com.yf.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewBilling {
	static Logger LOGGER = Logger.getLogger(NewBilling.class.getName());
	static String CONTENT = "application/json";
	public static String getBillingCycle(String token,String id) {
		ArrayList<String> idl = Subscriptions.getId(token);
		JsonArray ja = new JsonArray();
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Billing/invoices?api-version=2017-04-24-preview")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("value");
			for (int j = 0; j < ja1.size(); j++) {
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("Subscription Id", id);
				jo1.addProperty("Name", ja1.get(j).getAsJsonObject().get("name").getAsString());
				jo1.addProperty("InvoicePeriodEndDate", ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject()
						.get("invoicePeriodEndDate").getAsString());
				jo1.addProperty("InvoicePeriodStartDate", ja1.get(j).getAsJsonObject().get("properties")
						.getAsJsonObject().get("invoicePeriodStartDate").getAsString());
				ja.add(jo1);
			}

		} catch (Exception e) {
			return null;
		}
		return ja.toString();
		
	}
		public static String getBilling(String token, String p) {
			JsonArray ja = new JsonArray();
			String tok = "Bearer " + token;
			OkHttpClient client = new OkHttpClient();
			ArrayList<String> idl = Subscriptions.getId(token);
			for (String id : idl) {
				String cycle = getBillingCycle(token, id);
				JsonElement je1 = new JsonParser().parse(cycle);
				JsonArray jaa = je1.getAsJsonArray();
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
						return null;
					}
				}
			}
			return ja.toString();
		}
	}

