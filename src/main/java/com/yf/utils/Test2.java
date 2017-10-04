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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA3MDk3MDU2LCJuYmYiOjE1MDcwOTcwNTYsImV4cCI6MTUwNzEwMDk1NiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQUdqTTR3bXNnU3lTb1hhcTZ4Yi9yV2lXT2tkVHZqdmpOK0RnUlhzaG5HKzA9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6IkhKRkN3dDREdjBlTDlVZnpDWVlyQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.ZDSNat1pnJDn67BhXSG-VYt1GUSp3GModEzl2WraYKgudcE46pdC4a3hUyky-G3MF8nrRKCrcA8NIQjL6GPYxrPzHDeDB9tKNySI6EYBWF22oQIlrf_GZIoL_GVushbU-kU0dX0_VkzFk9sqTo3w1M-50aWFUGR17aoOHpfs0heWKnqjItN1Bs0qc3nfguVh0EBPaT7-YL9jIgQeE-mqygq2t_1zjxplkLPxDaOKoOL6Ojs1cHaiv0kHD1lErdtCpkndBL0xFpO0d53ZS1IEIPCxXoHGya3VehjCMCyMa0BBAxqBAQ-F69rHm2h6Lv2eYOjbPdbetkghNgx0VvyUQA";
		String CONTENT = "application/json";
		String p = "4";
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
				BigDecimal u = entry.getValue().setScale(2, BigDecimal.ROUND_HALF_EVEN);
				//double o = u.setScale(0, RoundingMode.CEILING).doubleValue(); 
				System.out.println(entry.getKey()+"             "+u);
/*				for (int j = 0; j < ja2.size(); j++) {
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
					
				}*/
			}
			System.out.println("            ");
/*			BigDecimal sum = new BigDecimal(0);
			for(BigDecimal d : list){
			  sum = sum.add(d);
			 //System.out.println(d);
			}
			JsonObject jo1 = new JsonObject();
			jo1.addProperty("ReportedStartedTime", dstart);
			jo1.addProperty("ReportedEndTime", dend);
			jo1.addProperty("Bill", sum);
			ja.add(jo1);*/
		} catch (Exception e) {
			System.out.println("gggg");
		}
		}
		//System.out.println(ja.toString());		
		}
}