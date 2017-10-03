package com.yf.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {
	public static void main(String[] args) {

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA2OTUyNzQ5LCJuYmYiOjE1MDY5NTI3NDksImV4cCI6MTUwNjk1NjY0OSwiYWNyIjoiMSIsImFpbyI6IlkyVmdZSWgzODF3NjI5TmtkK0tEcnhGdmxGZHBYL2gvU2VKV1B2ZGE5UUtmVmZhaWJ0Y0EiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoiWG0wZTJwMlhNRXlBRUNQaWhfOFlBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.BUkoZ01-xHqJSjnizqhOLXhdAT84-NXebMbyWKsRhs0plDnuGYv2Gdx1Xsv0m081qWdKYN5KPcmKoj6e0DIWr6r__TjISktLVDQT1aBHnFTUhqRtrGvW-LLw3SiemX8saNzMEcBUDxyTCiFSuhAn65nXemW9p6pqiAjV2x2it5hOc4BAq2ZdS4BOI2NOyVB_5ZCaTzSuDIeOJsoKh7vmKljJgq0Y0596vBo3vVKhtxz_wjYAEXtl60A8Lywy7QxIv2gttqkYLlaeKMlYpUWnmGKOsFH2aPEvUTCq6mlp2316noYh1892jvUZdaI6uXAYTH8Oawl4X92ILg_4uGJxQA";
		String CONTENT = "application/json";
		String p = "1";
		String currency = "AUD";
		JsonArray ja = new JsonArray();
		String id = Subscriptions.getId(token);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		String cycle = Billing.getBillingCycle(token);
		JsonElement je1 = new JsonParser().parse(cycle);
		JsonArray jaa = je1.getAsJsonArray();
		for(int k=0;k< Integer.parseInt(p); k++){
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
		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/UsageAggregates?api-version=2015-06-01-preview&reportedStartTime="
						+ dstart + "&reportedEndTime=" + dend
						+ "&aggregationGranularity=Daily&showDetails=True")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("value");
			Map<String, BigDecimal> hm = new HashMap<String, BigDecimal>();

			String resp = Billing.getCard(token,currency);
			JsonElement match = new JsonParser().parse(resp);
			JsonArray ja2 = match.getAsJsonArray();
			for (int j = 0; j < ja1.size(); j++) {
				String i = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("meterId")
						.getAsString();
				BigDecimal MeterQ = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("quantity")
						.getAsBigDecimal();

				BigDecimal rates = (BigDecimal) (hm.containsKey(i) ? hm.get(i) : BigDecimal.ZERO);
				rates = rates.add(MeterQ);
				hm.put(i, rates);
			}
			BigDecimal re = BigDecimal.ZERO;
			ArrayList<BigDecimal> list = new ArrayList<BigDecimal>();
			for (Map.Entry<String, BigDecimal> entry : hm.entrySet()) {
				
				System.out.println(entry.getKey()+"             "+entry.getValue());
/*				for (int j = 0; j < ja2.size(); j++) {
					String ko = entry.getKey();
					String mo = ja2.get(j).getAsJsonObject().get("MeterId").getAsString();
					if (ko.equals(mo)) {
						BigDecimal po = ja2.get(j).getAsJsonObject().get("MeterRates").getAsBigDecimal();
						BigDecimal lo = entry.getValue();
						re = po.multiply(lo);
						list.add(re);
					}
					
				}*/
			}
/*			BigDecimal sum = new BigDecimal(0);
			for(BigDecimal d : list){
			  sum = sum.add(d);
			}
			JsonObject jo1 = new JsonObject();
			jo1.addProperty("ReportedStartedTime", dstart);
			jo1.addProperty("ReportedEndTime", dend);
			jo1.addProperty("Bill", sum);
			ja.add(jo1);*/
		} catch (Exception e) {
			System.out.println("gggg");
		}
		}
		//System.out.println(ja.toString());		
		}
}