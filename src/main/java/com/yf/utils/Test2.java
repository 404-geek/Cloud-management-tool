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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA2NDI3MTA2LCJuYmYiOjE1MDY0MjcxMDYsImV4cCI6MTUwNjQzMTAwNiwiYWNyIjoiMSIsImFpbyI6IlkyVmdZR2crNjVxWi9rUHdmWDNGK1NtTUxoVW1sMlE0UFFOT0hHRStyekU3L0h0OTJIWUEiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoiUGRMS2dPQl9iMFdTczE3YzBqWWdBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.wJ7k__WncfvlLY5wKG1ej8VDXWbXxmSiAOfld2_ZgIPeQFTMKbalrBZbic9CnTGObHBPBnywQ2sobtsFpcb1xxN11t33KaL_ScETn7Yqwvz6cviHYI0egYuIk9eFyrzgOzRema5jyte4VuJsZSzSWGB6E5Er15a8Gn-PHTo1AJbqKHqa6APruj8qNFOHazqRRkDJ-hYkr6y55X83pwWYy37QzINZjFAVsENpRoImgGjuDIXQmpTPkFiupwA_uuibD3ID6sYEpSCtOJTKiH8B-7sZeIkL1LtQJwUc-rsy3VkokFNx12X0-1xIsPp7cFroA79sBtOCfeQ_14dbqAnx-Q";
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

			String resp = Billing.getCard(token);
			JsonElement match = new JsonParser().parse(resp);
			JsonArray ja2 = match.getAsJsonArray();
			for (int j = 0; j < ja1.size(); j++) {
				JsonObject jo1 = new JsonObject();
				String i = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("meterId")
						.getAsString();
				for (int k = 0; k < ja2.size(); k++) {
					String temp = ja2.get(k).getAsJsonObject().get("MeterId").getAsString();
					if (i.equals(temp)) {
						rate = ja2.get(k).getAsJsonObject().get("MeterRates").getAsBigDecimal();
					}
				}
				// jo1.addProperty("id",
				// ja1.get(j).getAsJsonObject().get("id").getAsString());
				jo1.addProperty("Name", ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject()
						.get("meterName").getAsString());
				jo1.addProperty("Resource Id", i);
				BigDecimal quantity = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("quantity")
						.getAsBigDecimal();
				jo1.addProperty("Units", ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("quantity")
						.getAsBigDecimal());
				jo1.addProperty("BillAmount(AUD)", rate.multiply(quantity));
				jo1.addProperty("UsageStartTime", ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject()
						.get("usageStartTime").getAsString());
				ja.add(jo1);
				// System.out.println(ja.toString());
			}

		} catch (Exception e) {
			System.out.println("hhhhh");
		}
		System.out.println(ja.toString());
	}
}
