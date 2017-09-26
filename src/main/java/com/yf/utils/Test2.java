package com.yf.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {
	public static void main(String[] args) {

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA2NDU1MzAxLCJuYmYiOjE1MDY0NTUzMDEsImV4cCI6MTUwNjQ1OTIwMSwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQXlXbHZZTkE0bXlyR3hKWml4L2gzL1JZNDQ2S0tjZVk0SEVFVi9jcWxKM2c9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6IjFvM1h0UU0zeDBpN2ItRHZOeUJMQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.Gneafq_7IJ0MuojZT0XQw-uYnX3eHqSTE7KnwCzglr-X6Enidxptb4-IzBjq_ODFY16I5Gemn4x2pGcd_9TdaNwkEAMsjmAtRgm3fU2HJAb09Z44q08rYCalA33CXYg5ZfIUoSjvAYn1EQ7fP_jMZkkiJrE_zW8Ljzid29TNQo5jX91pSqnR8dv46dbPDP-sDfOUEGxWXQ_NuUtnVeBbbKL2vkbSgrwTOS0Uj-h97bB5cPtMeOdwB3xD7yAR0h6mym2XLIKsTDiutnXis0dFJe67CjwJOr0Pb7-8Qdj8SxJ53OsbxsSqatwGvgkQmq6nnsf3pCPxoZfoiDbUhVe8WA";
/*		String CONTENT = "application/json";
		JsonArray ja = new JsonArray();
		String id = Subscriptions.getId(token);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/RateCard?api-version=2016-08-31-preview&$filter=OfferDurableId eq 'MS-AZR-0003p' and Currency eq 'AUD' and Locale eq 'en-AU' and RegionInfo eq 'AU'")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("Meters");
			Map<String, Float> hm = new HashMap<String, Float>();
			JsonObject jo1 = new JsonObject();
		    Float MeterRates = 0.0f;
			for (int j = 0; j < ja1.size(); j++) {
				String MeterId = ja1.get(j).getAsJsonObject().get("MeterId").getAsString();
				try{
				MeterRates = ja1.get(j).getAsJsonObject().get("MeterRates").getAsJsonObject().get("0").getAsFloat();
				}
				catch (Exception e) {
			    MeterRates = 0.0f;
				}
		        Float rates = hm.containsKey(MeterId) ? hm.get(MeterId) : 0;
		        rates += MeterRates;
		        hm.put(MeterId, rates);
			}			
			Iterator<Entry<String, Float>> itr=  hm.entrySet().iterator();
            while(itr.hasNext())
            {
                jo1.addProperty("MeterId", itr.next().getKey());
                jo1.addProperty("MeterRates", itr.next().getValue());
                ja.add(jo1);
            }

		} catch (Exception e) {
			return;
		}
		System.out.println(ja.toString());*/
		
		
		
		
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
			Map<String, BigDecimal> hm = new HashMap<String, BigDecimal>();

			String resp = Billing.getCard(token);
			JsonElement match = new JsonParser().parse(resp);
			JsonArray ja2 = match.getAsJsonArray();
			for (int j = 0; j < ja1.size(); j++) {
				JsonObject jo1 = new JsonObject();
				String i = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("meterId")
						.getAsString();
				BigDecimal MeterQ = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("quantity")
							.getAsBigDecimal();
				
		        BigDecimal rates = (BigDecimal) (hm.containsKey(i) ? hm.get(i) : BigDecimal.ZERO);
		        rates = rates.add(MeterQ);
		        hm.put(i, rates);
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
