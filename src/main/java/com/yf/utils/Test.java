package com.yf.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	  String ds1 = "2017-08-25";
	  SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	  SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
	  String ds2 = null;
	try {
		ds2 = sdf2.format(sdf1.parse(ds1));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  System.out.println(ds2);
  }
}
