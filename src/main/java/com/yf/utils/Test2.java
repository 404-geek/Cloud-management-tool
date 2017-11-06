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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyIsImtpZCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA5OTcwNTk5LCJuYmYiOjE1MDk5NzA1OTksImV4cCI6MTUwOTk3NDQ5OSwiYWNyIjoiMSIsImFpbyI6IlkyTmdZTWk3dHE0ZzB1OWpCNnR0VWRLQmVaZTZMNVNFL3I5NDRVUHdUZjFrKzBzK1Z5SUEiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoiTHo4QU9TNDdSMGF2bHhrRVNXb2xBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.UapR4ouP2jlJUhfwB5vQQWB7Dma8suqrgP3ewhrSDghmmsBRHLlmVkNOaDdYKeVQIedYk7UPjqmgrIxJI8IkYtXWyjL9qYzU1V1A-TfPjLWWidKz0XgTvffQdKhdQKglNzyFaIHVfEFFwMBxU9a_h4GHQKZ--FwmfbBmqNtW7Sja-2aSy8EudFr-Q0U38p4IdSeKc7VywiaMcU2xB6t021nTGdYl10a7EHh99j4IFdRRBs8HZF3n4d5CrURQIiG3W1Ad5Zh0jrWsFo69uJ3kvocKTtc6vORkQ8ZZwexX0shNnrVn2A0MBAtdsVB05tOEixFc2Z2uimNKu0i8SECiYw";
		String CONTENT = "application/json";
		String p = "3";
		String currency = "AUD";
		String Loc = "en-AU";
		String reg = "AU";
		String offer = "0003P";
		JsonArray ja = new JsonArray();
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		ArrayList<String> idl = Subscriptions.getId(token);
		for (String id : idl) {
			String cycle = Billing.getBillingCycle(token, id);
			JsonElement je1 = new JsonParser().parse(cycle);
			JsonArray jaa = je1.getAsJsonArray();
			for (int k = 0; k < Integer.parseInt(p); k++) {
				try{
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
				Request request = new Request.Builder().url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/UsageAggregates?api-version=2015-06-01-preview&reportedStartTime="
						+ dstart + "&reportedEndTime=" + dend + "&aggregationGranularity=Daily&showDetails=True")
						.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
				try {
					Response response = client.newCall(request).execute();
					JsonElement je = new JsonParser().parse(response.body().string());
					JsonObject jo = je.getAsJsonObject();
					JsonArray ja1 = jo.getAsJsonArray("value");
					Map<String, BigDecimal> hm = new HashMap<String, BigDecimal>();

					String resp = Billing.getCard(token, currency, Loc, reg, offer);
					JsonElement match = new JsonParser().parse(resp);
					JsonArray ja2 = match.getAsJsonArray();
					for (int j = 0; j < ja1.size(); j++) {
						String i = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("meterId")
								.getAsString();
						BigDecimal MeterQ = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject()
								.get("quantity").getAsBigDecimal();

						BigDecimal rates = (BigDecimal) (hm.containsKey(i) ? hm.get(i) : BigDecimal.ZERO);
						rates = rates.add(MeterQ);
						hm.put(i, rates);
					}
					BigDecimal re = BigDecimal.ZERO;
					ArrayList<BigDecimal> list = new ArrayList<BigDecimal>();
					for (Map.Entry<String, BigDecimal> entry : hm.entrySet()) {
						for (int j = 0; j < ja2.size(); j++) {
							String ko = entry.getKey();
							String mo = ja2.get(j).getAsJsonObject().get("MeterId").getAsString();
							if (ko.equals(mo)) {
								BigDecimal po = ja2.get(j).getAsJsonObject().get("MeterRates").getAsBigDecimal();
								BigDecimal lo = entry.getValue().setScale(2, BigDecimal.ROUND_HALF_EVEN);
								re = po.multiply(lo).setScale(2, BigDecimal.ROUND_HALF_EVEN);
								list.add(re);
							}
						}
					}
					BigDecimal sum = new BigDecimal(0);
					for (BigDecimal d : list) {
						sum = sum.add(d);
					}
					JsonObject jo1 = new JsonObject();
					jo1.addProperty("Subscription Id", id);
					jo1.addProperty("ReportedStartedTime", dstart);
					jo1.addProperty("ReportedEndTime", dend);
					jo1.addProperty("Bill", sum);
					ja.add(jo1);
				} catch (Exception e) {
					return;
				}
				}catch (Exception e) {
					BigDecimal re = BigDecimal.ZERO;
					JsonObject jo1 = new JsonObject();
					jo1.addProperty("Subscription Id", id);
					jo1.addProperty("ReportedStartedTime", "0");
					jo1.addProperty("ReportedEndTime", "0");
					jo1.addProperty("Bill", re);
					ja.add(jo1);
				}
			}
		}
		System.out.println(ja.toString());
	}
}