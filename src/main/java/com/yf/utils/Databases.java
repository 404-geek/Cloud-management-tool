package com.yf.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Databases {
	static Logger LOGGER = Logger.getLogger(Databases.class.getName());
	static int numdays = 14;
	static String CONTENT = "application/json";

	public static ArrayList<Integer> getIndexdb(String token) {
		LinkedHashMap<String, String> type = Resources.getType(token);
		ArrayList<Integer> list = new ArrayList<Integer>();
		int i = 0;
		for (Map.Entry<String, String> entry : type.entrySet()) {
			if (entry.getValue().equals("Microsoft.Sql/servers/databases")) {
				list.add(Integer.valueOf(i));
			}
			i++;
		}
		return list;
	}

	public static String[] getdb(String token, String resid) {
		String[] req = new String[4];
		int k = 0;
		String tok = "Bearer " + token;
		LocalDate currentDate = LocalDate.now();
		LocalDate day = LocalDate.now().minus(numdays, ChronoUnit.DAYS);
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + resid
						+ "/providers/microsoft.insights/metrics?api-version=2016-09-01&$filter=(name.value eq 'dtu_consumption_percent' or name.value eq 'cpu_percent' or name.value eq 'log_write_percent' or name.value eq 'physical_data_read_percent') and startTime eq "
						+ day + " and endTime eq " + currentDate + " and timeGrain eq duration'PT1H'")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("value");
			for (int j = 0; j < ja1.size(); j++) {
				JsonArray ja = jo.getAsJsonArray("value").get(j).getAsJsonObject().getAsJsonArray("data");
				req[(k++)] = ja.toString();
			}
		} catch (Exception e) {
			return null;
		}
		return req;
	}
	
	public static String[] getdbLive(String token, String resid) {
		String[] req = new String[4];
		int k = 0;
		String tok = "Bearer " + token;
		LocalDate currentDate = LocalDate.now();
		LocalDate day = LocalDate.now().plus(1L, ChronoUnit.DAYS);
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + resid
						+ "/providers/microsoft.insights/metrics?api-version=2016-09-01&$filter=(name.value eq 'dtu_consumption_percent' or name.value eq 'cpu_percent' or name.value eq 'log_write_percent' or name.value eq 'physical_data_read_percent') and startTime eq "
						+ currentDate + " and endTime eq " + day + " and timeGrain eq duration'PT1H'")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("value");
			for (int j = 0; j < ja1.size(); j++) {
				JsonArray ja = jo.getAsJsonArray("value").get(j).getAsJsonObject().getAsJsonArray("data");
				req[(k++)] = ja.toString();
			}
		} catch (Exception e) {
			return null;
		}
		return req;
	}

	public static String getDetails(String token) {
		String str = "";
		ArrayList<Integer> sqlist = getIndexdb(token);
		JsonArray ja1 = new JsonArray();
		for (int i = 0; i < sqlist.size(); i++) {
			String resid = Resources.getResid(token, ((Integer) sqlist.get(i)).intValue());
			try {
				String[] det = getdb(token, resid);
				JsonElement je = new JsonParser().parse(det[0]);
				JsonArray dtu = je.getAsJsonArray();
				je = new JsonParser().parse(det[1]);
				JsonArray percpu = je.getAsJsonArray();
				je = new JsonParser().parse(det[2]);
				JsonArray perlog = je.getAsJsonArray();
				je = new JsonParser().parse(det[3]);
				JsonArray perdata = je.getAsJsonArray();
				for (int j = 0; j < numdays * 24; j++) {
					JsonObject jo = new JsonObject();
					jo.addProperty("Resource ID", resid);
					Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
					Matcher m = pat.matcher(resid);
					while (m.find()) {
						str = m.group(1);
					}
					jo.addProperty("Resource Group", str);
					jo.addProperty("Timestamp", dtu.get(j).getAsJsonObject().get("timeStamp").getAsString());
					try {
						jo.addProperty("DTU Percentage",
								Float.valueOf(dtu.get(j).getAsJsonObject().get("average").getAsFloat()));
					} catch (Exception e) {
						jo.addProperty("DTU Percentage", Integer.valueOf(0));
					}
					try {
						jo.addProperty("CPU percentage",
								Float.valueOf(percpu.get(j).getAsJsonObject().get("average").getAsFloat()));
					} catch (Exception e) {
						jo.addProperty("CPU percentage", Integer.valueOf(0));
					}
					try {
						jo.addProperty("Log IO percentage",
								Float.valueOf(perlog.get(j).getAsJsonObject().get("average").getAsFloat()));
					} catch (Exception e) {
						jo.addProperty("Log IO percentage", Integer.valueOf(0));
					}
					try {
						jo.addProperty("Data IO percentage",
								Float.valueOf(perdata.get(j).getAsJsonObject().get("average").getAsFloat()));
					} catch (Exception e) {
						jo.addProperty("Data IO percentage", Integer.valueOf(0));
					}
					ja1.add(jo);
				}
			} catch (Exception e) {
				return null;
			}
		}
		return ja1.toString();
	}
	
	public static String getLiveDetails(String token) {
		String str = "";
		ArrayList<Integer> index = getIndexdb(token);
		JsonArray ja1 = new JsonArray();
		for (int i = 0; i < index.size(); i++) {
			String resid = Resources.getResid(token, ((Integer) index.get(i)).intValue());
			try {
				OkHttpClient client = new OkHttpClient();

				Request request = new Request.Builder()
				  .url("https://management.azure.com"+resid+"?api-version=2014-01-01")
				  .addHeader("Authorization", "Bearer "+token)
				  .build();

				Response response = client.newCall(request).execute();
				JsonElement jer = new JsonParser().parse(response.body().string());
				String status = jer.getAsJsonObject().get("properties").getAsJsonObject().get("status").getAsString();
				String[] det = getdbLive(token, resid);
				JsonElement je = new JsonParser().parse(det[0]);
				JsonArray dtu = je.getAsJsonArray();
				je = new JsonParser().parse(det[1]);
				JsonArray percpu = je.getAsJsonArray();
				je = new JsonParser().parse(det[2]);
				JsonArray perlog = je.getAsJsonArray();
				je = new JsonParser().parse(det[3]);
				JsonArray perdata = je.getAsJsonArray();
				ArrayList<Integer> list = new ArrayList<Integer>();
				for (int j = 0; j < 24; j++) {
					String per = percpu.get(j).getAsJsonObject().get("timeStamp").getAsString();
					DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

					Date date = null;
					try {
						date = utcFormat.parse(per);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					pstFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
					Timestamp ts = Timestamp.valueOf(pstFormat.format(date));
					LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
					Timestamp current = Timestamp.valueOf(ldt);
					if (ts.before(current)) {
						list.add(Integer.valueOf(j));
					}
				}
				int size = list.size() - 1;
				JsonObject jo = new JsonObject();
				jo.addProperty("Resource ID", resid);
				jo.addProperty("Status", status);
				Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
				Matcher m = pat.matcher(resid);
				while (m.find()) {
					str = m.group(1);
				}
				jo.addProperty("Resource Group", str);
				jo.addProperty("Timestamp", dtu.get(size).getAsJsonObject().get("timeStamp").getAsString());
				try {
					jo.addProperty("DTU Percentage",
							Float.valueOf(dtu.get(size).getAsJsonObject().get("average").getAsFloat()));
				} catch (Exception e) {
					jo.addProperty("DTU Percentage", Integer.valueOf(0));
				}
				try {
					jo.addProperty("CPU percentage",
							Float.valueOf(percpu.get(size).getAsJsonObject().get("average").getAsFloat()));
				} catch (Exception e) {
					jo.addProperty("CPU percentage", Integer.valueOf(0));
				}
				try {
					jo.addProperty("Log IO percentage",
							Float.valueOf(perlog.get(size).getAsJsonObject().get("average").getAsFloat()));
				} catch (Exception e) {
					jo.addProperty("Log IO percentage", Integer.valueOf(0));
				}
				try {
					jo.addProperty("Data IO percentage",
							Float.valueOf(perdata.get(size).getAsJsonObject().get("average").getAsFloat()));
				} catch (Exception e) {
					jo.addProperty("Data IO percentage", Integer.valueOf(0));
				}
				ja1.add(jo);
			} catch (Exception e) {
				return null;
			}
		}
		return ja1.toString();
	}

}
