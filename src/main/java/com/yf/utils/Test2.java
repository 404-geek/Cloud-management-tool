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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA3MDI0NTE0LCJuYmYiOjE1MDcwMjQ1MTQsImV4cCI6MTUwNzAyODQxNCwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQTJ6L05vb1YxRi9id1pMWHFJNmoyOVBTajMyaXhSNnhOSVdDK3JZWllXQ0k9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6InNOd1Q3NkR5ZGtlaTFzR2d2R2dyQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.JTaQGEPTViEhAnS06Atex3fVy8ACinbDIypSJqOkWWscUAFUJRxuOiHJb9FVoXhS7wnnn2vAroNsHCj9i91cu-u5zkrngPD3-5TY1xMdgf-EOb1_MKF0zypinph447wFX7j_uE1K7zA0ffDqo_s7JYcR4PZzkUvv0mI4cfGzKRR__ffXsoOrPg6_g1tYkX4L9d_g7fMEj2Gr08EZJipY0YxPYfGiGPplXD_hz2-DeFSfDLCqMytWetehPYMmr7Y0kBTCdZzy2t-ORHshxWkOzGDpJ4nA5o4C7nHJz2YkyxlrGyQvCZOo_3qbfRny5ztB7UDBFMTwHxIL0A5Hj_YqWg";
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
/*				BigDecimal u = entry.getValue().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				//double o = u.setScale(0, RoundingMode.CEILING).doubleValue(); 
				System.out.println(entry.getKey()+"             "+u);*/
				for (int j = 0; j < ja2.size(); j++) {
					String ko = entry.getKey();
					String mo = ja2.get(j).getAsJsonObject().get("MeterId").getAsString();
					if (ko.equals(mo)) {
						BigDecimal po = ja2.get(j).getAsJsonObject().get("MeterRates").getAsBigDecimal();
								//.setScale(2, BigDecimal.ROUND_HALF_EVEN);
						BigDecimal lo = entry.getValue().setScale(2, BigDecimal.ROUND_HALF_EVEN);
						re = po.multiply(lo).setScale(2, BigDecimal.ROUND_HALF_EVEN);
						list.add(re);
					}
				}
			}
			BigDecimal sum = new BigDecimal(0);
			for(BigDecimal d : list){
			  sum = sum.add(d);
			 //System.out.println(d);
			}
			JsonObject jo1 = new JsonObject();
			jo1.addProperty("ReportedStartedTime", dstart);
			jo1.addProperty("ReportedEndTime", dend);
			jo1.addProperty("Bill", sum);
			ja.add(jo1);
		} catch (Exception e) {
			System.out.println("gggg");
		}
		}
		System.out.println(ja.toString());		
		}
}