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

public class VirtualMachine {
	static Logger LOGGER = Logger.getLogger(VirtualMachine.class.getName());
	static int numdays = 14;
	static String CONTENT = "application/json";
	/* This function returns the number of virtual machines from azure */

	public static ArrayList<Integer> getIndex(String token) {
		LinkedHashMap<String, String> type = Resources.getType(token);
		ArrayList<Integer> list = new ArrayList<Integer>();
		int i = 0;
		for (Map.Entry<String, String> entry : type.entrySet()) {
			if (entry.getValue().equals("Microsoft.Compute/virtualMachines")) {
				list.add(Integer.valueOf(i));
			}
			i++;
		}
		return list;
	}

	/* This function returns the metrics of azure virtual machine's' */

	public static String[] getvm(String token, String resid) {
		String[] req = new String[7];
		int k = 0;
		String tok = "Bearer " + token;
		LocalDate currentDate = LocalDate.now();
		LocalDate day = LocalDate.now().minus(numdays, ChronoUnit.DAYS);
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + resid
						+ "/providers/microsoft.insights/metrics?api-version=2016-09-01&$filter=(name.value eq 'Percentage CPU' or name.value eq 'Network In' or name.value eq 'Network Out' or name.value eq 'Disk Read Bytes' or name.value eq 'Disk Write Bytes' or name.value eq 'Disk Read Operations/Sec' or name.value eq 'Disk Write Operations/Sec') and timeGrain eq duration'PT1H' and startTime eq "
						+ day + " and endTime eq " + currentDate)
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

	public static String[] getvmLive(String token, String resid) {
		String[] req = new String[7];
		int k = 0;
		String tok = "Bearer " + token;
		LocalDate currentDate = LocalDate.now();
		LocalDate day = LocalDate.now().plus(1L, ChronoUnit.DAYS);
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + resid
						+ "/providers/microsoft.insights/metrics?api-version=2016-09-01&$filter=(name.value eq 'Percentage CPU' or name.value eq 'Network In' or name.value eq 'Network Out' or name.value eq 'Disk Read Bytes' or name.value eq 'Disk Write Bytes' or name.value eq 'Disk Read Operations/Sec' or name.value eq 'Disk Write Operations/Sec') and timeGrain eq duration'PT1H' and startTime eq "
						+ currentDate + " and endTime eq " + day)
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
		ArrayList<Integer> index = getIndex(token);
		JsonArray ja1 = new JsonArray();
		for (int i = 0; i < index.size(); i++) {
			String resid = Resources.getResid(token, ((Integer) index.get(i)).intValue());
			String tok = "Bearer " + token;

			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url("https://management.azure.com" + resid + "?api-version=2017-03-30")
					.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
			try {
				Response response = client.newCall(request).execute();

				Gson gson = new GsonBuilder().create();

				JsonObject job = (JsonObject) gson.fromJson(response.body().string(), JsonObject.class);
				JsonObject obj = job.get("properties").getAsJsonObject();

				JsonObject obj3 = job.get("properties").getAsJsonObject().getAsJsonObject().get("storageProfile")
						.getAsJsonObject().get("osDisk").getAsJsonObject().get("managedDisk").getAsJsonObject();
				JsonObject obj1 = job.get("properties").getAsJsonObject().get("hardwareProfile").getAsJsonObject();

				JsonObject obj2 = job.get("properties").getAsJsonObject().getAsJsonObject().get("storageProfile")
						.getAsJsonObject().get("osDisk").getAsJsonObject();
				String vm = obj.get("vmId").getAsString();
				String vmSz = obj1.get("vmSize").getAsString();
				String os = obj2.get("osType").getAsString();
				String loc = job.get("location").getAsString();
				String str = obj3.get("id").getAsString();
				Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
				Matcher m = pat.matcher(str);
				while (m.find()) {
					str = m.group(1);
				}
				String[] det = getvm(token, resid);
				JsonElement je = new JsonParser().parse(det[0]);
				JsonArray percentageCPU = je.getAsJsonArray();
				je = new JsonParser().parse(det[1]);
				JsonArray NetworkIN = je.getAsJsonArray();
				je = new JsonParser().parse(det[2]);
				JsonArray NetworkOut = je.getAsJsonArray();
				je = new JsonParser().parse(det[3]);
				JsonArray DiskReadBytes = je.getAsJsonArray();
				je = new JsonParser().parse(det[4]);
				JsonArray DiskWriteBytes = je.getAsJsonArray();
				je = new JsonParser().parse(det[5]);
				JsonArray DiskReadOp = je.getAsJsonArray();
				je = new JsonParser().parse(det[6]);
				JsonArray DiskWriteOp = je.getAsJsonArray();
				for (int j = 0; j < numdays * 24; j++) {
					JsonObject jo = new JsonObject();
					jo.addProperty("VmID", vm);
					jo.addProperty("Resource Type", str);
					jo.addProperty("OS type", os);
					jo.addProperty("VMSize", vmSz);
					jo.addProperty("Location", loc);
					jo.addProperty("Timestamp", percentageCPU.get(j).getAsJsonObject().get("timeStamp").getAsString());
					try {
						jo.addProperty("Percentage CPU",
								percentageCPU.get(j).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Percentage CPU", Integer.valueOf(0));
					}
					try {
						jo.addProperty("NetworkIN",
								NetworkIN.get(j).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("NetworkIN", Integer.valueOf(0));
					}
					try {
						jo.addProperty("NetworkOut",
								NetworkOut.get(j).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("NetworkOut", Integer.valueOf(0));
					}
					try {
						jo.addProperty("Disk Read Bytes",
								DiskReadBytes.get(j).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Disk Read Bytes", Integer.valueOf(0));
					}
					try {
						jo.addProperty("Disk Write Bytes",
								DiskWriteBytes.get(j).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Disk Write Bytes", Integer.valueOf(0));
					}
					try {
						jo.addProperty("Disk Read Operation",
								DiskReadOp.get(j).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Disk Read Operation", Integer.valueOf(0));
					}
					try {
						jo.addProperty("Disk Write Operation",
								DiskWriteOp.get(j).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Disk Write Operation", Integer.valueOf(0));
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
		ArrayList<Integer> index = getIndex(token);
		JsonArray ja1 = new JsonArray();
		for (int i = 0; i < index.size(); i++) {
			String resid = Resources.getResid(token, ((Integer) index.get(i)).intValue());
			String tok = "Bearer " + token;

			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder()
					.url("https://management.azure.com" + resid + "?api-version=2017-03-30&$expand=instanceView")
					.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
			try {
				Response response = client.newCall(request).execute();

				Gson gson = new GsonBuilder().create();

				JsonObject job = (JsonObject) gson.fromJson(response.body().string(), JsonObject.class);
				JsonObject obj = job.get("properties").getAsJsonObject();

				JsonObject obj3 = job.get("properties").getAsJsonObject().getAsJsonObject().get("storageProfile")
						.getAsJsonObject().get("osDisk").getAsJsonObject().get("managedDisk").getAsJsonObject();
				JsonObject obj1 = job.get("properties").getAsJsonObject().get("hardwareProfile").getAsJsonObject();

				JsonObject obj2 = job.get("properties").getAsJsonObject().getAsJsonObject().get("storageProfile")
						.getAsJsonObject().get("osDisk").getAsJsonObject();
				String stats = job.get("properties").getAsJsonObject().get("instanceView")
						.getAsJsonObject().get("statuses").getAsJsonArray().get(1).getAsJsonObject().get("displayStatus").getAsString();
				String vm = obj.get("vmId").getAsString();
				String vmSz = obj1.get("vmSize").getAsString();
				String os = obj2.get("osType").getAsString();
				String loc = job.get("location").getAsString();
				String str = obj3.get("id").getAsString();
				Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
				Matcher m = pat.matcher(str);
				while (m.find()) {
					str = m.group(1);
				}
				String[] det = getvmLive(token, resid);
				JsonElement je = new JsonParser().parse(det[0]);
				JsonArray percentageCPU = je.getAsJsonArray();
				je = new JsonParser().parse(det[1]);
				JsonArray NetworkIN = je.getAsJsonArray();
				je = new JsonParser().parse(det[2]);
				JsonArray NetworkOut = je.getAsJsonArray();
				je = new JsonParser().parse(det[3]);
				JsonArray DiskReadBytes = je.getAsJsonArray();
				je = new JsonParser().parse(det[4]);
				JsonArray DiskWriteBytes = je.getAsJsonArray();
				je = new JsonParser().parse(det[5]);
				JsonArray DiskReadOp = je.getAsJsonArray();
				je = new JsonParser().parse(det[6]);
				JsonArray DiskWriteOp = je.getAsJsonArray();
				ArrayList<Integer> list = new ArrayList<Integer>();
				for (int j = 0; j < 24; j++) {
					String per = percentageCPU.get(j).getAsJsonObject().get("timeStamp").getAsString();
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
				jo.addProperty("VmID", vm);
				jo.addProperty("Resource Type", str);
				jo.addProperty("OS type", os);
				jo.addProperty("VMSize", vmSz);
				jo.addProperty("Location", loc);
				jo.addProperty("Status", stats);
				jo.addProperty("Timestamp", percentageCPU.get(size).getAsJsonObject().get("timeStamp").getAsString());
				try {
					jo.addProperty("Percentage CPU",
							percentageCPU.get(size).getAsJsonObject().get("average").getAsBigDecimal());
				} catch (Exception e) {
					jo.addProperty("Percentage CPU", Integer.valueOf(0));
				}
				try {
					jo.addProperty("NetworkIN", NetworkIN.get(size).getAsJsonObject().get("average").getAsBigDecimal());
				} catch (Exception e) {
					jo.addProperty("NetworkIN", Integer.valueOf(0));
				}
				try {
					jo.addProperty("NetworkOut",
							NetworkOut.get(size).getAsJsonObject().get("average").getAsBigDecimal());
				} catch (Exception e) {
					jo.addProperty("NetworkOut", Integer.valueOf(0));
				}
				try {
					jo.addProperty("Disk Read Bytes",
							DiskReadBytes.get(size).getAsJsonObject().get("average").getAsBigDecimal());
				} catch (Exception e) {
					jo.addProperty("Disk Read Bytes", Integer.valueOf(0));
				}
				try {
					jo.addProperty("Disk Write Bytes",
							DiskWriteBytes.get(size).getAsJsonObject().get("average").getAsBigDecimal());
				} catch (Exception e) {
					jo.addProperty("Disk Write Bytes", Integer.valueOf(0));
				}
				try {
					jo.addProperty("Disk Read Operation",
							DiskReadOp.get(size).getAsJsonObject().get("average").getAsBigDecimal());
				} catch (Exception e) {
					jo.addProperty("Disk Read Operation", Integer.valueOf(0));
				}
				try {
					jo.addProperty("Disk Write Operation",
							DiskWriteOp.get(size).getAsJsonObject().get("average").getAsBigDecimal());
				} catch (Exception e) {
					jo.addProperty("Disk Write Operation", Integer.valueOf(0));
				}
				ja1.add(jo);
			} catch (Exception e) {
				return null;
			}
		}
		return ja1.toString();
	}
}
