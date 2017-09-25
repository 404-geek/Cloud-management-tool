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
		
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA2MzQzNDUxLCJuYmYiOjE1MDYzNDM0NTEsImV4cCI6MTUwNjM0NzM1MSwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQUt0a0lTbXU5QXlmV2U5SzhTaTJpY0ZUUjNzY1Z4U0taZGZyUm1KZ0oxcWs9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6ImNnYUpzbzhaRVVHdDUtenN6Rm9VQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.AQfDbkRNN56peIR7lcumYoFmVVst5fhNOuW1oqy050K267nWvM_6xYAdFRDYp87-JdFAGqFCJ4fa_It76LQSjbGSpi2XV2FAdtOamV1gHEY5_oZ52lFg0eCUoShaOrXQztYxEybjEoUsJQpZgRgiiAH38h3Hp3kk3-gLu78-fpHcvuMVXhMhKjb7BxZWqUll6XsXLBWWGTIjhfMzzp_xXdz2Vy9Cw4_emcVNWQVUi2GzK9kKeemlf__VHk2Qze72yS-AnNzL3xyCmz79AtufKkvIV81dGvxSxX9EcfsOr8aUu8iYWnQ3PN8a0968z0Q1IC7eGhFM1dflrVL_cftKdQ";
		String CONTENT = "application/json";
		BigDecimal rate = new BigDecimal(123);
		JsonArray ja = new JsonArray();
		String id = Subscriptions.getId(token);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		LocalDate localDate = LocalDate.now();
		LocalDate ago = LocalDate.now().minusDays(30);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/UsageAggregates?api-version=2015-06-01-preview&reportedStartTime="
						+ ago.format(formatter) + "&reportedEndTime=" + localDate.format(formatter)
						+ "&aggregationGranularity=Daily&showDetails=True")
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
