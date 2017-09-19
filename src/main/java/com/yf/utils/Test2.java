package com.yf.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {

	public static void main(String [] args) {
		// TODO Auto-generated method stub
		 int numdays = 14;
		 String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA1ODE3NTM2LCJuYmYiOjE1MDU4MTc1MzYsImV4cCI6MTUwNTgyMTQzNiwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQTY0dnJhZVdQN0M4ZWE3S2NXMVJqbHFLaGd0NzdWdUZoMUZKa2NwZFRHL009IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6IldlNnJwZF9oeFUyVG12aWhRU3d1QUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.SjPgmhGhXditUTWoZ8CHKOiVzE7ibOJdxlDzHj9USwrFFVE1ibWYA86CpLlsJRrhN2ayvEIE22CN7GJcLsOBkb5tXXzlhJdhGVLWSCI6wDAzGhFqya-iY5uft2J12eqDHHIHlxWnLr9vmL5wtdVILO2z1IVJ_iwsdQKPcCWtMjVICkY0pwgIFDcxfcfr_xldcC5MmeGOMV9Hsl4Xu6k5N9wHe4bFhEtKSq2O_JhA_Fb3fMeSULBhZyS1mub2Y7SdpP-uoQr_VSKw5O1x0c9ezCjheBWTB7SiA9sBMGxxAUFVLe1d2oVlpP6H3XXMuwwy5cFUL-Q3z4M5MEqN6AKiFA";	
		 
		 		final String CONTENT = "application/json";
		int index[] = VirtualMachine.getIndex(token);

		JsonArray jaf = new JsonArray();
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
				String[] par = VirtualMachine.getvmLive(token, resid);
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
				for (int k = 0; k < 24; k++) {
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
						jo.addProperty("Disk Read Bytes",
								ja3.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Disk Read Bytes", 0);
					}
					try {
						jo.addProperty("Disk Write Bytes",
								ja4.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Disk Write Bytes", 0);
					}
					try {
						jo.addProperty("Disk Read Operations",
								ja5.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Disk Read Operations", 0);
					}
					try {
						jo.addProperty("Disk Write Operations",
								ja6.get(k).getAsJsonObject().get("average").getAsBigDecimal());
					} catch (Exception e) {
						jo.addProperty("Disk Write Operations", 0);
					}
					jaf.add(jo);
				}

			} catch (Exception e) {
				return;
			}
		}
		System.out.println(jaf.toString());
	

}
}