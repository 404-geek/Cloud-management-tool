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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA3NjE1NDM1LCJuYmYiOjE1MDc2MTU0MzUsImV4cCI6MTUwNzYxOTMzNSwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQTY0dnJhZVdQN0M4ZWE3S2NXMVJqbHFLaGd0NzdWdUZoMUZKa2NwZFRHL009IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6IlY5a2Z3YWdXMjBTdWpQTC12ajFCQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.hxrn34nGZKto-RXtO4eTJTBGtuF92BwBnpyPnPbz9On74iRt7ks8eEGWBVs7hKdt112Bfj_XaFw2JhrrfKN2wJvomYEzv2pkOdsmHKnBiQ8Dia_lf8qmvD0HUlO_z6S0hgcpfr7S7yxJA_HeOyLq3jQbOVJ8Jv5JrKd6tJVwxOIdDCCrEISdgbYeze4rqg_jiLJe5hxMJsaaIy4P83tH3QfKA75fHHDHHwaeMX0QW-94VDtsqOuYClAAdde9Uw2ZTeDaYnSZXXO4MNFbLB3oRy2oEXmJ-CZ4NnT7nbpe8-E61FDAv7oUMzJmAMGP9lRQvO3GJMaKn7_ygWCpCEHTnA";
		String CONTENT = "application/json";
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
				try{
				String ds1 = jaa.get(0).getAsJsonObject().get("InvoicePeriodStartDate").getAsString();
				String ds2 = jaa.get(0).getAsJsonObject().get("InvoicePeriodEndDate").getAsString();
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

					for (int j = 0; j < ja1.size(); j++) {
						String i = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("meterId")
								.getAsString();
						BigDecimal MeterQ = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject()
								.get("quantity").getAsBigDecimal();

						BigDecimal rates = (BigDecimal) (hm.containsKey(i) ? hm.get(i) : BigDecimal.ZERO);
						rates = rates.add(MeterQ);
						hm.put(i, rates);
					}
					String resp = Billing.getCard(token, currency, Loc, reg, offer);
					JsonElement match = new JsonParser().parse(resp);
					JsonArray ja2 = match.getAsJsonArray();
					BigDecimal re = BigDecimal.ZERO;
					for (Map.Entry<String, BigDecimal> entry : hm.entrySet()) {
						for (int j = 0; j < ja2.size(); j++) {
							String ko = entry.getKey();
							String mo = ja2.get(j).getAsJsonObject().get("MeterId").getAsString();
							if (ko.equals(mo)) {
								BigDecimal po = ja2.get(j).getAsJsonObject().get("MeterRates").getAsBigDecimal();
								String name = ja2.get(j).getAsJsonObject().get("Metername").getAsString();
								String sub = ja2.get(j).getAsJsonObject().get("MeterSub").getAsString();
								BigDecimal lo = entry.getValue().setScale(2, BigDecimal.ROUND_HALF_EVEN);
								re = po.multiply(lo).setScale(2, BigDecimal.ROUND_HALF_EVEN);
								JsonObject jo1 = new JsonObject();
								jo1.addProperty("Subscription Id", id);
								jo1.addProperty("Resource Name", name+"-"+sub);
								jo1.addProperty("Id", ko);
								jo1.addProperty("Consumed Units", lo);
								jo1.addProperty("Billable Units", lo);
								jo1.addProperty("Pre-Tax Cost", re);
								ja.add(jo1);
							}
							
						}
					}
				} catch (Exception e) {
					return;
				}
				}catch (Exception e) {
					BigDecimal re = BigDecimal.ZERO;
					JsonObject jo1 = new JsonObject();
					jo1.addProperty("Subscription Id", id);
					jo1.addProperty("Resource Name", "No Data");
					jo1.addProperty("Id", "No Data");
					jo1.addProperty("Consumed Units", re);
					jo1.addProperty("Billable Units", re);
					jo1.addProperty("Pre-Tax Cost", re);
					ja.add(jo1);
				}
			}
		System.out.println(ja.toString());
	}
}