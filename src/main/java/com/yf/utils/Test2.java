package com.yf.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA2NTE1Mjg1LCJuYmYiOjE1MDY1MTUyODUsImV4cCI6MTUwNjUxOTE4NSwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQXhkYUs1VFlMUUNKUDk5VTdoUWt0NXI1UXg1TFExZ1UzS2NFOHNqZnlzelU9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6ImxSQ19IODZZR1VlNjJ6S1VLZFFzQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.UkCeAzL_FeF6bm2ronwz2C2rvih5sr77M0CIQSRhwoLJnqhjO89N50BaRw3Gw0nuVpRY2_WmtFCMxFPrDI6FDDkQwXtZP8Pb1bh94nftdKALLLWk0I5wbf24A0hEXpbxP6lk1gjORwMKB7jMbhIZF62mMqv88ArEpcqsAuZLuF0Dqi8RNpWcmHHRwNCJOSRbADEyhplzK7oGVVlN-Dpf_7slwAxSbRoxDB0TB9FZ2DbPegTyQriEMYj-lJwFfQtztBL3SL5XdMQfdUh6rZcXyLXCUbtN9Jib2zDEkEV1TgnifcP10_iiapHwJidl8OKq7R6AVUe7J8II0omTYyPdOg";
		String CONTENT = "application/json";
		JsonArray ja = new JsonArray();
		String id = Subscriptions.getId(token);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		LocalDate localDate = LocalDate.now();
		LocalDate ago = LocalDate.now().minusDays(30);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
		Request request = new Request.Builder()
/*				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/UsageAggregates?api-version=2015-06-01-preview&reportedStartTime="
						+ ago.format(formatter) + "&reportedEndTime=" + localDate.format(formatter)
						+ "&aggregationGranularity=Daily&showDetails=True")*/
				.url("https://management.azure.com/subscriptions/5744cda7-2ba6-4f43-b407-acf9a0be2822/providers/Microsoft.Commerce/UsageAggregates?api-version=2015-06-01-preview&reportedStartTime=8/26/2017&reportedEndTime=9/25/2017&aggregationGranularity=Daily&showDetails=True")
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
				String i = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("meterId")
						.getAsString();
				BigDecimal MeterQ = ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject().get("quantity")
						.getAsBigDecimal();

				BigDecimal rates = (BigDecimal) (hm.containsKey(i) ? hm.get(i) : BigDecimal.ZERO);
				rates = rates.add(MeterQ);
				hm.put(i, rates);
			}
			String sr = "";
			BigDecimal re = BigDecimal.ZERO;
			for (Map.Entry<String, BigDecimal> entry : hm.entrySet()) {
				JsonObject jo1 = new JsonObject();
				//System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				jo1.addProperty("MeterId", entry.getKey());
				for (int j = 0; j < ja2.size(); j++) {
					String ko = entry.getKey();
					String mo = ja2.get(j).getAsJsonObject().get("MeterId").getAsString();
					if (ko.equals(mo)) {
						BigDecimal po = ja2.get(j).getAsJsonObject().get("MeterRates").getAsBigDecimal();
						BigDecimal lo = entry.getValue();
						re = po.multiply(lo);
						sr = ja2.get(j).getAsJsonObject().get("Metername").getAsString();
					}
				}
				//System.out.println(entry.getKey()+"     "+re);
				jo1.addProperty("Name", sr);
				jo1.addProperty("MeterRates", re);
				ja.add(jo1);
			}
			
		} catch (Exception e) {
			System.out.println("hhhhh");
		}
	   System.out.println(ja.toString());
	}
}