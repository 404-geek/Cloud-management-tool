package com.yf.utils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Test {
	public static void main(String[] args) {
		/*
		 * DateFormat utcFormat = new
		 * SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		 * utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		 * 
		 * Date date = null; try { date =
		 * utcFormat.parse("2016-03-01T00:00:00Z"); } catch (ParseException e) {
		 * e.printStackTrace(); } DateFormat pstFormat = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * pstFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		 * Timestamp ts = Timestamp.valueOf(pstFormat.format(date));
		 * 
		 * System.out.println(ts);
		 */
		/*
		 * String monthBegin = LocalDate.now().withDayOfMonth(1).toString();
		 * 
		 * SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		 * SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy"); String
		 * ds2 = null; try { ds2 = sdf2.format(sdf1.parse(monthBegin)); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } System.out.println(ds2);
		 */
		PdfReader reader = null;
		try {
			reader = new PdfReader(
					"https://billinginsightsstore05.blob.core.windows.net/invoices/5744cda7-2ba6-4f43-b407-acf9a0be2822-201711-417044150449226.pdf?sv=2014-02-14&sr=b&sig=qjHU3eZZOqm%2F%2BgSMMtxkZ%2F4Foi1fXHZnJ78Q4yMnnPc%3D&se=2017-11-07T07%3A23%3A03Z&sp=r");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String str = null;
		try {
			str = PdfTextExtractor.getTextFromPage(reader, 1);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		reader.close();
		String str1 = str.replaceAll("[\r\n]+", " ");
		//System.out.println(str1);
		final String regex = "(?:\\S+\\s+\\S+\\s)?\\S*Total Amount";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(str1);
        String match ="";
		while (matcher.find()) {
			match = matcher.group(0);
		}
		
		final String regex1 = "[1-9]\\d*(\\.\\d+)";
		final String string = match;

		final Pattern pattern1 = Pattern.compile(regex1);
		final Matcher matcher1 = pattern1.matcher(string);

		while (matcher1.find()) {
		    System.out.println(matcher1.group(0));}
		
		final String regex2 = "^\\w+";
		final String string2 = match;

		final Pattern pattern2 = Pattern.compile(regex2);
		final Matcher matcher2 = pattern2.matcher(string2);

		while (matcher2.find()) {
		    System.out.println(matcher2.group(0));}
	}
}
