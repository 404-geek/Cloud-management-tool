package com.yf.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VirtualMachine {
	 static Logger LOGGER = Logger.getLogger(VirtualMachine.class.getName());
		public static int[] getIndex(String token) {
			String[] type = Resources.getType(token);
			int i,k=0;
			int ind[]=new int[2];
			for(i=0; i < type.length; i++){
				if(type[i].equals("Microsoft.Compute/virtualMachines")){
					ind[k]=i;
					k++;
				}
			}
			return ind;
		}

		public static String[] getvm(String token) {
		final String CONTENT = "application/json";
		int index[] = getIndex(token);
		String req[]= new String[20];
		for(int i=0;i<index.length;i++){
		String resid = Resources.getResid(token,index[i]);
		String tok = "Bearer "+token;
		LocalDate currentDate = LocalDate.now();
		LocalDate day = LocalDate.now().minus(1, ChronoUnit.DAYS);
		OkHttpClient client = new OkHttpClient();	
		Request request = new Request.Builder()
			    .url("https://management.azure.com"+resid+"/providers/microsoft.insights/metrics?api-version=2016-09-01&$filter=(name.value eq 'Percentage CPU' or name.value eq 'Network In' or name.value eq 'Network Out' or name.value eq 'Disk Read Bytes' or name.value eq 'Disk Write Bytes' or name.value eq 'Disk Read Operations/Sec' or name.value eq 'Disk Write Operations/Sec') and timeGrain eq duration'PT1H' and startTime eq "+day+" and endTime eq "+currentDate)
                .addHeader("Authorization", tok)
                .addHeader("Content-type", CONTENT)
                .build();
		try {
           Response response = client.newCall(request).execute();
           req[i] = response.body().string();
			return req;
			
			
			}
		catch (Exception e){
			return null; 
		}
		}
		return req;
       }
		
		
		public static String getvmDetails(String token) {
	    final String CONTENT = "application/json";
		int index[] = getIndex(token);
 		JSONArray arr = new JSONArray();
		JSONArray array = new JSONArray();
		JSONObject name = new JSONObject();
		HashMap<String, JSONObject> map = new HashMap<String, JSONObject>();
		for(int i=0;i<index.length;i++){
			
			String resid = Resources.getResid(token,index[i]);
			System.out.println(resid);
			String tok = "Bearer "+token;
			OkHttpClient client = new OkHttpClient();	
			Request request = new Request.Builder()
				    .url("https://management.azure.com"+resid+"?api-version=2017-03-30")
	                .addHeader("Authorization", tok)
	                .addHeader("Content-type", CONTENT)
	                .build();
	       

         try {
     		
        	Response response = client.newCall(request).execute();
     	    JSONObject jsonObj = new JSONObject();
			Gson gson = new GsonBuilder().create();
			
			   JsonObject job = gson.fromJson(response.body().string(), JsonObject.class);
	           JsonObject obj= job.get("properties").getAsJsonObject();
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
	            jsonObj.put("vmID", vm);
	            jsonObj.put("Resource Type",str);
	            jsonObj.put("OS Type",os);
	            jsonObj.put("VM Size",vmSz);
	            jsonObj.put("Location",loc);
	    		JSONObject jo1 = new JSONObject();
	    		jo1.put("Percentage CPU", "21");
	            jo1.put("Network In", "121");
	            jo1.put("Network Out", "323");
	            jo1.put("Disk Read", "3231");
	            jo1.put("Disk Write", "3124");
	            jo1.put("Disk Read Operation","4324");
	            jo1.put("Disk Write Operation", "4324");
	            array.put(jo1);
	            jsonObj.put("Last30days", array);
	            map.put("json",jsonObj);
	            arr.put(map.get("json"));
     		}

		
		catch (Exception e){
			return null;
		}

      }        
		name.put("vmdetails", arr);
		//System.out.println(name.toString());
		return name.toString();

    }
		
		
}
