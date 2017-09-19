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
	SimpleDateFormat sdfgmt = new SimpleDateFormat("YYYY-MM-DD'T'HH:MM:SS'Z'");
        sdfgmt.setTimeZone(TimeZone.getTimeZone("UTC"));

        SimpleDateFormat sdfmad = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        sdfmad.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));

        //String inpt = "2017-09-19T23:00:00Z";
        System.out.println("Asia/Kolkata:  " + sdfmad.format(sdfgmt.parse(inpt)));

}
}

