package com.yf.utils;

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
		 String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA1ODEzMDU3LCJuYmYiOjE1MDU4MTMwNTcsImV4cCI6MTUwNTgxNjk1NywiYWNyIjoiMSIsImFpbyI6IlkyVmdZUGl3L01EcFJzdS9BY0VaczhWTkJldk83RTB2OXBqOFBPOG1YemxEcWFQZWYxY0EiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoiYktIX1oxbUpkMC1CVXFZQlJJUXNBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.vsaMWdMJ-n12uOlZ6e6EytdIKIGmu4PLa-JUtxuvZRNR4xzSb7NnpitwLCBlU7Wkd6f35VkLU9gOeq1OqB2z82ZKWGHsruZjW28Q7FcEZy1voWYXCW4nKoWyOBpom-RizBZ1qYutjLn3jpyUkX66BbdaRQ_to9p1Z_0cuo_juk-izs57xW6ns4KU18FTHcSCiE15oE-VHojfDhi1SHh82WUx9KbcvAar0EjHnppon-nfwz7xOho0NEojoM61SD0mxlJq-Hss3UJIOdDJh9d6n9t2EIc5YFU0xBbo23kM3eAd8S0J-G9z2ShTQr6eiqwrOq7nfNx9NxOtYHSsGeI2cA";
		final String CONTENT = "application/json";
		int index[] = VirtualMachine.getIndex(token);

		JsonArray ja1 = new JsonArray();
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
				String[] par = VirtualMachine.getvm(token, resid);
				for (int j = 0; j < par.length; j++) {
					JsonElement je = new JsonParser().parse(par[j]);
					JsonArray ja = je.getAsJsonArray();
					for (int k = 0; k < numdays * 24; k++) {
						JsonObject jo = new JsonObject();
						jo.addProperty("VmID", vm);
						jo.addProperty("Resource Type", str);
						jo.addProperty("OS type", os);
						jo.addProperty("VMSize", vmSz);
						jo.addProperty("Location", loc);
						jo.addProperty("Timestamp", ja.get(k).getAsJsonObject().get("timeStamp").getAsString());
						try {
							jo.addProperty("Average", ja.get(k).getAsJsonObject().get("average").getAsBigDecimal());
						} catch (Exception e) {
							jo.addProperty("Average", 0);
						}
						ja1.add(jo);
					}
				}
			}

			catch (Exception e) {
				return;
			}
		}
		System.out.println(ja1.toString());
	

}
}