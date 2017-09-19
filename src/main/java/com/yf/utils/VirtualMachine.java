package com.yf.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.hof.pool.jdbc.metadata.NuoDBMetaData;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VirtualMachine {
	static Logger LOGGER = Logger.getLogger(VirtualMachine.class.getName());
	static int numdays = 14;

	public static int[] getIndex(String token) {
		String[] type = Resources.getType(token);
		int i, k = 0;
		int ind[] = new int[2];
		for (i = 0; i < type.length; i++) {
			if (type[i].equals("Microsoft.Compute/virtualMachines")) {
				ind[k] = i;
				k++;
			}
		}
		return ind;
	}

	public static String[] getvm(String token, String resid) {
		final String CONTENT = "application/json";
		int index[] = getIndex(token);
		String req[] = new String[7];
		for (int i = 0; i < index.length; i++) {
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
					req[j] = ja.toString();
				}
			} catch (Exception e) {
				return null;
			}
		}
		return req;
	}

	public static String getDetails(String token) {
		final String CONTENT = "application/json";
		int index[] = getIndex(token);
		for (int i = 0; i < index.length; i++) {
			String resid = Resources.getResid(token, index[i]);
			String tok = "Bearer " + token;

			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url("https://management.azure.com" + resid + "?api-version=2017-03-30")
					.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
			try {
				Response response = client.newCall(request).execute();

				Gson gson = new GsonBuilder().create();

				JsonObject job = gson.fromJson(response.body().string(), JsonObject.class);
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
				String[] par = getvm(token, resid);
				JsonElement percpu = new JsonParser().parse(par[0]);
				JsonArray ja = percpu.getAsJsonArray();
				JsonElement netin = new JsonParser().parse(par[1]);
				JsonArray ja1 = netin.getAsJsonArray();
				JsonElement netout = new JsonParser().parse(par[2]);
				JsonArray ja2 = netout.getAsJsonArray();
				JsonElement diskread = new JsonParser().parse(par[3]);
				JsonArray ja3 = diskread.getAsJsonArray();
				JsonElement diskwrite = new JsonParser().parse(par[4]);
				JsonArray ja4 = diskwrite.getAsJsonArray();
				JsonElement diskrop = new JsonParser().parse(par[5]);
				JsonArray ja5 = diskrop.getAsJsonArray();
				JsonElement diskwop = new JsonParser().parse(par[6]);
				JsonArray ja6 = diskwop.getAsJsonArray();
				for (int k = 0; k < numdays * 24; k++) {
					JsonObject jo = new JsonObject();
					jo.addProperty("VmID", vm);
					jo.addProperty("Resource Type", str);
					jo.addProperty("OS type", os);
					jo.addProperty("VMSize", vmSz);
					jo.addProperty("Location", loc);
					jo.addProperty("Timestamp", ja.get(k).getAsJsonObject().get("timeStamp").getAsString());
					try {
						jo.addProperty("PercentageCPU", ja.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("PercentageCPU", 0);
					}
					try {
						jo.addProperty("Network In", ja1.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Network In", 0);
					}
					try {
						jo.addProperty("Network Out", ja2.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Network Out", 0);
					}
					try {
						jo.addProperty("", ja3.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Average", 0);
					}
					try {
						jo.addProperty("PercentageCPU", ja4.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Average", 0);
					}
					try {
						jo.addProperty("PercentageCPU", ja.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Average", 0);
					}
					try {
						jo.addProperty("PercentageCPU", ja5.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Average", 0);
					}
					try {
						jo.addProperty("PercentageCPU", ja5.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Average", 0);
					}
					ja1.add(jo);
				}

			}

			catch (Exception e) {
				return null;
			}
		}
		return ja1.toString();
	}

}