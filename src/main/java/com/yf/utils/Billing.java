package com.yf.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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

	public static String getCard(String token, String currency, String Loc, String reg, String offer) {
		String CONTENT = "application/json";
		JsonArray ja = new JsonArray();
		ArrayList<String> idl = Subscriptions.getId(token);
		String id = idl.get(0);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/RateCard?api-version=2016-08-31-preview&$filter=OfferDurableId eq 'MS-AZR-" + offer + "' and Currency eq '"
						+ currency + "' and Locale eq '" + Loc + "' and RegionInfo eq '" + reg + "'")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("Meters");
			for (int j = 0; j < ja1.size(); j++) {
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("MeterId", ja1.get(j).getAsJsonObject().get("MeterId").getAsString());
				jo1.addProperty("Metername", ja1.get(j).getAsJsonObject().get("MeterName").getAsString());
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
	
	public static int getCode(String token, String currency, String Loc, String reg, String offer) {
		String CONTENT = "application/json";
		ArrayList<String> idl = Subscriptions.getId(token);
		String id = idl.get(0);
		int responseCode = 999;
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/RateCard?api-version=2016-08-31-preview&$filter=OfferDurableId eq 'MS-AZR-" + offer + "' and Currency eq '"
						+ currency + "' and Locale eq '" + Loc + "' and RegionInfo eq '" + reg + "'")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			return response.code();

		} catch (Exception e) {
			return responseCode;
		}
	}
	
	public static String getStr(String token, String currency, String Loc, String reg, String offer) {
		String CONTENT = "application/json";
		ArrayList<String> idl = Subscriptions.getId(token);
		String id = idl.get(0);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/RateCard?api-version=2016-08-31-preview&$filter=OfferDurableId eq 'MS-AZR-" + offer + "' and Currency eq '"
						+ currency + "' and Locale eq '" + Loc + "' and RegionInfo eq '" + reg + "'")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			String ret = je.getAsJsonObject().get("Message").getAsString();
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getBillingCycle(String token, String id) {
		String CONTENT = "application/json";
		JsonArray ja = new JsonArray();
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Billing/invoices?api-version=2017-04-24-preview")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("value");
			for (int j = 0; j < ja1.size(); j++) {
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("Subscription Id", id);
				jo1.addProperty("InvoicePeriodEndDate", ja1.get(j).getAsJsonObject().get("properties").getAsJsonObject()
						.get("invoicePeriodEndDate").getAsString());
				jo1.addProperty("InvoicePeriodStartDate", ja1.get(j).getAsJsonObject().get("properties")
						.getAsJsonObject().get("invoicePeriodStartDate").getAsString());
				ja.add(jo1);
			}

		} catch (Exception e) {
			return null;
		}
		return ja.toString();
	}

	public static String getBilling(String token, String currency, String p, String Loc, String reg, String offer) {
		String CONTENT = "application/json";
		JsonArray ja = new JsonArray();
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();
		ArrayList<String> idl = Subscriptions.getId(token);
		for (String id : idl) {
			String cycle = Billing.getBillingCycle(token, id);
			JsonElement je1 = new JsonParser().parse(cycle);
			JsonArray jaa = je1.getAsJsonArray();
			for (int k = 0; k < Integer.parseInt(p); k++) {
				try{
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
					return null;
				}
				}catch (Exception e) {
					BigDecimal re = BigDecimal.ZERO;
					JsonObject jo1 = new JsonObject();
					jo1.addProperty("Subscription Id", id);
					jo1.addProperty("ReportedStartedTime", "0");
					jo1.addProperty("ReportedEndTime", "0");
					jo1.addProperty("Bill", re);
					ja.add(jo1);
				}
			}
		}
		return ja.toString();
	}
}
