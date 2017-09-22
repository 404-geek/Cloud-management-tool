package com.yf.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {
	public static void main(String[] args) {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA2MDg0Mzk5LCJuYmYiOjE1MDYwODQzOTksImV4cCI6MTUwNjA4ODI5OSwiYWNyIjoiMSIsImFpbyI6IlkyVmdZUERWVWwwUzU1eHhmZU5DaDc2ZnJ6d2ZuOXBpcDFUQThDdnZWWXZkNFdjaHM4TUIiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoic0I3NE8xSGk5VXl5TGplZ0V3NFVBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.j4Iqt4LJAlEEBUexYRcsUkUzgUMp31jdW5jqr-sqJG5qI17uP7VqC-aPbvWvJQuWrSAvmDXXTfkr9NFGK4jMBV0-dfs6ApyWvddMa98t9fsYY_FrEs13IDzRAQWLd-cBffbZu6g90xM8sKKYvxwx9MoYwis7qDA-1ed2ey8qP8SO7G-USz8gwX9TAvnNmakNqfDhf-tDC1DidopDNHhNzzTQaakMiD2w_5RtmvUSCfu4_YopU0SgNQvCQOGJOn5ym0pD1SF9k4lSAqvCwp3_VdoNtlMikVEboByRwZ3KNfFhzQypJyDHdF6CGDM82nsqK1Tl3qWChGAq9vLI082ahw";
		String CONTENT = "application/json";
		BigDecimal rate = new BigDecimal(123);
		JsonArray ja = new JsonArray();
		String id = Subscriptions.getId(token);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		LocalDate localDate = LocalDate.now();
		LocalDate ago = LocalDate.now().minusDays(1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/UsageAggregates?api-version=2015-06-01-preview&reportedStartTime="
						+ ago.format(formatter) + "&reportedEndTime=" + localDate.format(formatter)
						+ "&aggregationGranularity=Hourly&showDetails=True")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("value");
			for (int j = 0; j < ja1.size(); j++) {
				JsonObject jo1 = new JsonObject();
				String resp = Billing.getCard(token);
				JsonElement match = new JsonParser().parse(resp);
				JsonArray ja2 = match.getAsJsonArray();
				String i = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("meterId")
						.getAsString();
				for (int k = 0; k < ja2.size(); k++) {
					String temp = ja2.get(k).getAsJsonObject().get("MeterId").getAsString();
					if (i.equals(temp)) {
						rate = ja2.get(k).getAsJsonObject().get("MeterRates").getAsBigDecimal();
					}
				}
				jo1.addProperty("id", ja1.get(j).getAsJsonObject().get("id").getAsString());
				jo1.addProperty("SubscriptionId", ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject()
						.get("subscriptionId").getAsString());
				BigDecimal quantity = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("quantity")
						.getAsBigDecimal();
				jo1.addProperty("BillAmount(USD)", rate.multiply(quantity));
				ja.add(jo1);
				// System.out.println(ja.toString());
			}

		} catch (Exception e) {
			System.out.println("hhhhh");
		}
		System.out.println(ja.toString());
	}
}
