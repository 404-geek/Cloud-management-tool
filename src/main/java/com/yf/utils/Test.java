package com.yf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Test {

	public static void main(String [] args) {
		// TODO Auto-generated method stub
	SimpleDateFormat sdfgmt = new SimpleDateFormat("YYYY-MM-DD'T'HH:MM:SS'Z'");
        sdfgmt.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat sdfmad = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        sdfmad.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        String inpt = "2017-09-19T23:00:00Z";
        try {
			System.out.println(sdfmad.format(sdfgmt.parse(inpt)));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}
}

