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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA3MjgyMTA5LCJuYmYiOjE1MDcyODIxMDksImV4cCI6MTUwNzI4NjAwOSwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQTJNTWFjWWp3b1Rnd2hNaVJDWU5XZWE0aGFJVXk1ZUUzdU5oL3BUVFAxcVk9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6InVURy1TbDR2RGsySlA5QUZBNjQtQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.wAfnuHHH0EGrmR2pwKEUEH8OC6DaljlQM1JyPN6bHnsNrIWdVkaVUqLq0jPeZ7RaKJ3-xyrJ--nZLlq0TWR3FL0RAwoPuVebHDSuP4SzZlncEEJMv4bThnANyBlkv1Izfp0lXSFj2YWM8MmL5sKPhPuO_4Yq3mLQHgt5XPVMuFN0tMtgmFye51ofy33yQXnMN2kaXyERWzd7jLVdCFakaRwKWYhwr7rQXtzcSxVmR5P8v1axx1zi3uofCM37jbLaUYI5kLM455a65tkEom3fl6r9T9lJxKi6qRAhdRPcSd0Vz6OIj7S_l8rkkkX1UvIASLauBpNeGbHJl8wSUsVrVA";
		String CONTENT = "application/json";
		String p = "12";
		String currency = "USD";
		String Loc = "en-US";
		String reg = "US";
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
				try {
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
				} catch (Exception e) {
					JsonObject jo1 = new JsonObject();
					jo1.addProperty("Subscription Id", id);
					jo1.addProperty("ReportedStartedTime", "0");
					jo1.addProperty("ReportedEndTime", "0");
					jo1.addProperty("Bill", "0");
					ja.add(jo1);
				}
			}

		}
		System.out.println(ja.toString());
	}
}