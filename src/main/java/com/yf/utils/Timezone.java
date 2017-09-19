package com.yf.utils;

import java.util.ArrayList;
import java.util.logging.Logger;

public class Timezone {
	static Logger LOGGER = Logger.getLogger(Timezone.class.getName());
	public static ArrayList<String> getTimeZone(){
		String[] ids = TimeZone.getAvailableIDs();
		 ArrayList<String> list=new ArrayList<String>();
		for (String id : ids) {
			list.add(id);
		}
		return list;
		
	}
	
	
}
