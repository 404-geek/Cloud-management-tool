package com.yf.utils;

import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Billing {
	 static Logger LOGGER = Logger.getLogger(Billing.class.getName());
	public static String getCard(String token) {
		String CONTENT = "application/json";
		JsonArray ja = new JsonArray();
		String id = Subscriptions.getId(token);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/RateCard?api-version=2016-08-31-preview&$filter=OfferDurableId eq 'MS-AZR-0003p' and Currency eq 'USD' and Locale eq 'en-US' and RegionInfo eq 'US'")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("Meters");
			String currency = jo.get("Currency").getAsString();
			String tax = jo.get("IsTaxIncluded").getAsString();
			String locale = jo.get("Locale").getAsString();
			for (int j = 0; j < ja1.size(); j++) {
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("Currency", currency);
				jo1.addProperty("Tax", tax);
				jo1.addProperty("Locale", locale);
				jo1.addProperty("EffectiveDate", ja1.get(j).getAsJsonObject().get("EffectiveDate").getAsString());
				jo1.addProperty("IncludedQuantity", ja1.get(j).getAsJsonObject().get("IncludedQuantity").getAsInt());
				try {
					jo1.addProperty("MeterCategory", ja1.get(j).getAsJsonObject().get("MeterCategory").getAsString());
				} catch (Exception e) {
					jo1.addProperty("MeterCategory", "NULL");
				}
				jo1.addProperty("MeterRegion", ja1.get(j).getAsJsonObject().get("MeterRegion").getAsString());
				jo1.addProperty("MeterStatus", ja1.get(j).getAsJsonObject().get("MeterStatus").getAsString());
				jo1.addProperty("MeterSubCategory", ja1.get(j).getAsJsonObject().get("MeterSubCategory").getAsString());
				try {
					jo1.addProperty("MeterTags", ja1.get(j).getAsJsonObject().get("MeterTags").getAsString());
				} catch (Exception e) {
					jo1.addProperty("MeterTags", "NULL");
				}
				jo1.addProperty("Unit", ja1.get(j).getAsJsonObject().get("Unit").getAsString());
				try {
					jo1.addProperty("MeterRates",
							ja1.get(j).getAsJsonObject().get("MeterRates").getAsJsonObject().get("0").getAsFloat());
				} catch (Exception e) {
					jo1.addProperty("MeterRates", Integer.valueOf(0));
				}
				ja.add(jo1);
			}

		} catch (Exception e) {
			return null;
		}
		return ja.toString();
	}
}
