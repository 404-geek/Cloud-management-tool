package com.yf.utils;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {

	public static void main(String [] args) {
		// TODO Auto-generated method stub

		final String CONTENT = "application/json";
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IlZXVkljMVdEMVRrc2JiMzAxc2FzTTVrT3E1USIsImtpZCI6IlZXVkljMVdEMVRrc2JiMzAxc2FzTTVrT3E1USJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTAzODkyMzEzLCJuYmYiOjE1MDM4OTIzMTMsImV4cCI6MTUwMzg5NjIxMywiYWNyIjoiMSIsImFpbyI6IlkyRmdZRkF1MVpzZ2Q5Sncydjhnd3czUENybjQ5ODh2V0NzMHBXYU9rbSt0K0o2Y0Zia0EiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoiZ0RxSXBlRHI3ME9YTlkyUzVXSW1BQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.FW2c7w6I_0p8pr3SdxqdCBJelTXa_AY1ZcEZg4O4TsoGyQGtaIoj3GqzRNcH93SkcHytISxOQcw3OYTiGfdK117xFB8V-S6Hd5A52tjM7JxGbDwKoEiSWYKvb9PuULr4XWB1C8OjIQMLPpX3x0i7laW8u469-EprFZDVV25IStezliNEZv4KZBHjw4EW3C2qUdWXHJ2KCMS9lLkSTfaAdJJLBjw6e5-pPi6mJDo3sDI7hwZzN1PW54539b4tSJ1UWFnZg3bLUO_s4LtS9WeNJNXQEraG8bV18e6wxzNvAJOy8IQgvp9FSpJEN7Q92rvVvbG6TD1m51E0eZZtx5yybw";
		int index[] = VirtualMachine.getIndex(token);
		JSONArray arr = new JSONArray();
		JSONArray array = new JSONArray();
		JSONObject name = new JSONObject();
		HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
        for(int i=0;i<index.length;i++){
        	
        //System.out.println(index.length);
        JSONObject json=new JSONObject();
		String resid = Resources.getResid(token,index[i]);
		String tok = "Bearer "+token;
		OkHttpClient client = new OkHttpClient();	
		Request request = new Request.Builder()
			    .url("https://management.azure.com"+resid+"?api-version=2017-03-30")
                .addHeader("Authorization", tok)
                .addHeader("Content-type", CONTENT)
                .build();

		try {
	       Response response = client.newCall(request).execute();
           Gson gson = new GsonBuilder().create();
           JsonObject job = gson.fromJson(response.body().string(), JsonObject.class);
           JsonObject obj= job.get("properties").getAsJsonObject();
        		   //.get("vmId").getAsJsonObject();
           JsonObject obj3= job.get("properties").getAsJsonObject().getAsJsonObject().get("storageProfile").getAsJsonObject().get("osDisk").getAsJsonObject().get("managedDisk").getAsJsonObject();
           JsonObject obj1= job.get("properties").getAsJsonObject().get("hardwareProfile").getAsJsonObject();
           JsonObject obj2= job.get("properties").getAsJsonObject().getAsJsonObject().get("storageProfile").getAsJsonObject().get("osDisk").getAsJsonObject();
           String vm = obj.get("vmId").getAsString(); 
           String vmSz = obj1.get("vmSize").getAsString(); 
           String os = obj2.get("osType").getAsString();
           String loc = job.get("location").getAsString();
           String str = obj3.get("id").getAsString();
           Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
           Matcher m = pat.matcher(str);
            while(m.find())
                       {
            str= m.group(1);
                       }
            json.put("vmID", vm);
            json.put("Resource Type",str);
            json.put("OS Type",os);
            json.put("VM Size",vmSz);
            json.put("Location",loc);
    		JSONObject jo1 = new JSONObject();
    		jo1.put("Percentage CPU", "21");
            jo1.put("Network In", "121");
            jo1.put("Network Out", "323");
            jo1.put("Disk Read", "3231");
            jo1.put("Disk Write", "3124");
            jo1.put("Disk Read Operation","4324");
            jo1.put("Disk Write Operation", "4324");
            array.put(jo1);
            json.put("Last30days", array);
            map.put("json",json);
            arr.put(map.get("json"));
		   }
		catch (Exception e){
			return;
		}
	}
        name.put("VMdetails", arr);
    System.out.println(name.toString());

}
}