package com.yf.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

public class Test
{
  public static void main(String[] args)
  {
/*    DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    
    Date date = null;
    try
    {
      date = utcFormat.parse("2016-03-01T00:00:00Z");
    }
    catch (ParseException e)
    {
      e.printStackTrace();
    }
    DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    pstFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
    Timestamp ts = Timestamp.valueOf(pstFormat.format(date));
    
    System.out.println(ts);*/
/*	  String ds1 = "2017-08-25";
	  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	  SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
	  String ds2 = null;
	try {
		ds2 = sdf2.format(sdf1.parse(ds1));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  System.out.println(ds2);*/
	    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA3MTk3NTYxLCJuYmYiOjE1MDcxOTc1NjEsImV4cCI6MTUwNzIwMTQ2MSwiYWNyIjoiMSIsImFpbyI6IlkyVmdZS2c2YXJYUngxQythSEZDb0o2WHhMWlFPVmtOS2VkU1I1VTNLVEw4UXBkMVFnRT0iLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoiSVIyNVg2UjNnMHFXQTF6eFJGa01BQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.yCnOao-MKoluHCTMZiryqzcCo9ODOCB74OJ1bVZDC4wjI58n3t3qJJLcYl4ahN6cKA0RssTsJ4GDSzs2hCyfrQBrJAw3EIXUWQznLhylyeq6zbF6_rtTIPRmeRRsx0IcHTVZJFEmJYgUZ25erFp23Nvr8DhHft4QUrM_WWLhRNegJ4BRm4yiXIAYaXObOYS8_cjOyIas0WHpPUj7pOcj7ci4BDY5nUBI12sdIFEaBOFtkGpQNsY7asPsYaxyB4M1xccRPiDNFCCga5ntJ5Lxl-1BXqkCoJq4tdQdfZaIl1TQqMQfwU2I1nlIVfdQItsPVOUKMOCv50F1DqYPrj-EyQ";
	    int arr = 0;
	    LinkedHashMap<String, String> type = Resources.getType(token);
		ArrayList<String> list = new ArrayList<String>();
		try {
			for (Map.Entry<String, String> entry : type.entrySet()) {
				list.add(entry.getValue());
			}
			System.out.println(list.get(arr));
		} catch (Exception e) {
			return;
		}

  }
}
