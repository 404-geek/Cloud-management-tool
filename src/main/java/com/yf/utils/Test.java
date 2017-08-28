package com.yf.utils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
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

public class Test {

	public static void main(String [] args) {
		// TODO Auto-generated method stub

		final String CONTENT = "application/json";
		 String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IlZXVkljMVdEMVRrc2JiMzAxc2FzTTVrT3E1USIsImtpZCI6IlZXVkljMVdEMVRrc2JiMzAxc2FzTTVrT3E1USJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTAzOTI0NzgzLCJuYmYiOjE1MDM5MjQ3ODMsImV4cCI6MTUwMzkyODY4MywiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhFQUFBQTJKeWYvcnEwZmxFbE84R3BpM1g4L3R6OUdnWHdGV2ZXSGVkWXNVTFZ4VlU9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6ImNnZ0hta0tuazBHdExkWHhndmtEQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.czwKbIlBmNz8XY2dw6zTlT38gmP-kNWSdFyyJvTh120xP9P89da78zME7Ft034BdRWBOvXOaMP5stzTgBm5RmaMCDFFgNtp9wGib5SKX-hiiWDC0qlo33Qbikx6hazGW4R6dPJGcg-V-mekvFOjora0bVDhdV2ZhGdYAm_yN5HOYDRjzByNV1GsBmE5VvPBsmddr-SduL-Y8JEGiqH93cEUZeAqV7lF1UnU-VQA5KGi4jipgh44nETzfMQLOnsFt8OwXyw9c5cGsogIVJcxaIzmVAGQp89jQnPveAfrz7NpT0Jl73xcwIxOO1p1gG8IN3UiyDYVMx2mqxaC6twFpYA";
		int index[] = VirtualMachine.getIndex(token);
		String time[]= new String[20];
		String str = "";
		BigDecimal avg[]= new BigDecimal[20];
		JSONArray arr = new JSONArray();
		JSONArray array = new JSONArray();
		JSONObject name = new JSONObject();
		HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
        for(int i=0;i<index.length;i++){
        JSONObject jo1=new JSONObject();
		String resid = Resources.getResid(token,index[i]);
		String tok = "Bearer "+token;
		LocalDate currentDate = LocalDate.now();
		LocalDate day = LocalDate.now().minus(7, ChronoUnit.DAYS);
		OkHttpClient client = new OkHttpClient();	
		Request request = new Request.Builder()
			    .url("https://management.azure.com"+resid+"/providers/microsoft.insights/metrics?api-version=2016-09-01&$filter=(name.value eq 'Percentage CPU' or name.value eq 'Network In' or name.value eq 'Network Out' or name.value eq 'Disk Read Bytes' or name.value eq 'Disk Write Bytes' or name.value eq 'Disk Read Operations/Sec' or name.value eq 'Disk Write Operations/Sec') and timeGrain eq duration'PT1H' and startTime eq "+day+" and endTime eq "+currentDate)
                .addHeader("Authorization", tok)
                .addHeader("Content-type", CONTENT)
                .build();
                
		try {
	       Response response = client.newCall(request).execute();
	      //System.out.println(response.body().string());
           JSONObject jo = new JSONObject(response.body().string());
	       
	       JSONArray ja = jo.getJSONArray("value");
	       //System.out.println(ja.length());
           for(int j=0;j<ja.length();j++){
	       JSONObject je= ja.getJSONObject(j);
	       JSONArray ja1 = je.getJSONArray("data");
	       String id = je.getString("id");
           
	       for(int k=0;k<ja1.length();k++){ 
	    	   System.out.println(ja1.length());
	    	   JSONObject je1= ja1.getJSONObject(k);
	    	    time[k] = je1.getString("timeStamp");
	    	    avg[k] = je1.getBigDecimal("average");
	            Pattern pat = Pattern.compile("metrics/(.*?)");
	            Matcher m = pat.matcher(id);
	             while(m.find())
	                        {
	             str= m.group(1);
	                        }
	             System.out.println(time[k]);
	             System.out.println(avg[k]);     
/*    		jo1.put(str, time[k]);
    		jo1.put(str, avg[k]);
            array.put(jo1);
            System.out.println(array);
            map.put("json",jo1);
            arr.put(map.get("json"));
            System.out.println(arr);*/
	       }
		   }
		}
		catch (Exception e){
			
			return;
		}
	}
   //name.put("VMdetails", arr);
   //System.out.println(name.toString());

}
}

