package com.yf.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Databases
{
  static Logger LOGGER = Logger.getLogger(Databases.class.getName());
  static int numdays = 14;
  
  public static ArrayList<Integer> getIndexdb(String token)
  {
    String[] type = Resources.getType(token);
    ArrayList<Integer> list = new ArrayList();
    int i = 0;
    for (i = 0; i < type.length; i++) {
      if (type[i].equals("Microsoft.Sql/servers/databases")) {
        list.add(Integer.valueOf(i));
      }
    }
    return list;
  }
  
  public static String[] getdb(String token, String resid)
  {
    String CONTENT = "application/json";
    String[] req = new String[4];
    int k = 0;
    String tok = "Bearer " + token;
    LocalDate currentDate = LocalDate.now();
    LocalDate day = LocalDate.now().minus(numdays, ChronoUnit.DAYS);
    OkHttpClient client = new OkHttpClient();
    
    Request request = new Request.Builder().url("https://management.azure.com" + resid + "/providers/microsoft.insights/metrics?api-version=2016-09-01&$filter=(name.value eq 'dtu_consumption_percent' or name.value eq 'cpu_percent' or name.value eq 'log_write_percent' or name.value eq 'physical_data_read_percent') and startTime eq " + day + " and endTime eq " + currentDate + " and timeGrain eq duration'PT1H'").addHeader("Authorization", tok).addHeader("Content-type", "application/json").build();
    try
    {
      Response response = client.newCall(request).execute();
      JsonElement je = new JsonParser().parse(response.body().string());
      JsonObject jo = je.getAsJsonObject();
      JsonArray ja1 = jo.getAsJsonArray("value");
      for (int j = 0; j < ja1.size(); j++)
      {
        JsonArray ja = jo.getAsJsonArray("value").get(j).getAsJsonObject().getAsJsonArray("data");
        req[(k++)] = ja.toString();
      }
    }
    catch (Exception e)
    {
      return null;
    }
    int j;
    return req;
  }
  
  public static String getDetails(String token)
  {
    String str = "";
    ArrayList<Integer> sqlist = getIndexdb(token);
    JsonArray ja1 = new JsonArray();
    for (int i = 0; i < sqlist.size(); i++)
    {
      String resid = Resources.getResid(token, ((Integer)sqlist.get(i)).intValue());
      try
      {
        String[] det = getdb(token, resid);
        JsonElement je = new JsonParser().parse(det[0]);
        JsonArray dtu = je.getAsJsonArray();
        je = new JsonParser().parse(det[1]);
        JsonArray percpu = je.getAsJsonArray();
        je = new JsonParser().parse(det[2]);
        JsonArray perlog = je.getAsJsonArray();
        je = new JsonParser().parse(det[3]);
        JsonArray perdata = je.getAsJsonArray();
        for (int j = 0; j < numdays * 24; j++)
        {
          JsonObject jo = new JsonObject();
          jo.addProperty("Resource ID", resid);
          Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
          Matcher m = pat.matcher(resid);
          while (m.find()) {
            str = m.group(1);
          }
          jo.addProperty("Resource Group", str);
          jo.addProperty("Timestamp", dtu.get(j).getAsJsonObject().get("timeStamp").getAsString());
          try
          {
            jo.addProperty("DTU Percentage", Float.valueOf(dtu.get(j).getAsJsonObject().get("average").getAsFloat()));
          }
          catch (Exception e)
          {
            jo.addProperty("DTU Percentage", Integer.valueOf(0));
          }
          try
          {
            jo.addProperty("CPU percentage", Float.valueOf(percpu.get(j).getAsJsonObject().get("average").getAsFloat()));
          }
          catch (Exception e)
          {
            jo.addProperty("CPU percentage", Integer.valueOf(0));
          }
          try
          {
            jo.addProperty("Log IO percentage", 
              Float.valueOf(perlog.get(j).getAsJsonObject().get("average").getAsFloat()));
          }
          catch (Exception e)
          {
            jo.addProperty("Log IO percentage", Integer.valueOf(0));
          }
          try
          {
            jo.addProperty("Data IO percentage", 
              Float.valueOf(perdata.get(j).getAsJsonObject().get("average").getAsFloat()));
          }
          catch (Exception e)
          {
            jo.addProperty("Data IO percentage", Integer.valueOf(0));
          }
          ja1.add(jo);
        }
      }
      catch (Exception e)
      {
        return null;
      }
    }
    return ja1.toString();
  }
}
