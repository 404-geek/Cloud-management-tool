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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA3MjI2NzA0LCJuYmYiOjE1MDcyMjY3MDQsImV4cCI6MTUwNzIzMDYwNCwiYWNyIjoiMSIsImFpbyI6IlkyVmdZR0F2WEJxMmpOc3c2dCtsbUVKdTJTK0Jta0hKdm10U0p5VUlKcXVmRDdYNnZCMEEiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoiTG8yOW94cFh1a3V2dTlRcHQ4TTFBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.Luj9sgvEj9i3x4oXFu2nndpApGBU1F1Hz8XJ8jP__3IJ4QhMY8BTc4bHNp-AuYbIByBoUJn0thq9v4fbqwX8BbwlHJ410wb_cXE1RsiMjo4iOFZmg0PyWCJ_V37v6A6RpIIfhuSve0TbD4gibHlNnNIexdt9fxszcSCGPvxpOLhORK5EQXGYz0CEdWyT6O7E6H9ci2XvnFh3jllBKYLy-_k9YlIFC3MfaJ9_w3y9hGp7nZ_DpiVMsAf0Bbu_zhjGJycJGIV0lRf4P6BZjKd-FQ-Dcxi2Ho5rDXzSWFFvoW-AM1uYGcvsKx6j4ttL82Tas-V8jvB38VvOqP_vLdLuyA";
		String CONTENT = "application/json";
		String p = "4";
		String currency = "USD";
		String Loc = "en-US";
		String reg = "US";
		JsonArray ja = new JsonArray();
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		ArrayList<String> idl = Subscriptions.getId(token);
		for (String id : idl) {
		String cycle = Billing.getBillingCycle(token,id);
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

			String resp = Billing.getCard(token,currency,Loc, reg);
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
			for(BigDecimal d : list){
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
		}
		}
		System.out.println(ja.toString());			}
}