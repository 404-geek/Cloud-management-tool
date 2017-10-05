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
	  SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	    Date parsed = null;
	    try {
	        parsed = sdf.parse("08/26/2017");
	    } catch (ParseException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	    }
	    java.sql.Date data = new java.sql.Date(parsed.getTime());
	    System.out.println(data);
  }
}
