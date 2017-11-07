package com.yf.utils;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {
	public static void main(String[] args) {

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyIsImtpZCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTEwMDUwMzU0LCJuYmYiOjE1MTAwNTAzNTQsImV4cCI6MTUxMDA1NDI1NCwiYWNyIjoiMSIsImFpbyI6IlkyTmdZRmgwWlcycGhzTE04TitoWjB1NTJvcXJiWi9GYzNQZGltUnRXbVI4N0dhSnlrUUEiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDEuNDYuMTQ2IiwibmFtZSI6Ikp1c3RpbiBIZXdpdHQiLCJvaWQiOiI1Zjg5MDZkMS1lZTJkLTQyMWQtYWE3MS0wNmE4MTA5YzIxMzAiLCJwdWlkIjoiMTAwMzNGRkY4RDI3MjRBQyIsInNjcCI6InVzZXJfaW1wZXJzb25hdGlvbiIsInN1YiI6InByWURpX2wtR0dsT3pqX2lMNk9renBBS3RBb0NkVGVsRzBWdVJzOU5SbVkiLCJ0aWQiOiIyN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTciLCJ1bmlxdWVfbmFtZSI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXBuIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1dGkiOiJhUHFOeTBncnZVU0RRejR2cnFjS0FBIiwidmVyIjoiMS4wIiwid2lkcyI6WyI2MmU5MDM5NC02OWY1LTQyMzctOTE5MC0wMTIxNzcxNDVlMTAiXX0.hpz3bSEuofU6DzVBbrcFc1I_N8WY8_XwYiZolLgJYY3ugd75W1_iWPxbLBFTTKVRPiUrht25OYIXWzdgZyaMjTeGTxhm-4sbTIGMyIvV5-SE5reSEzJRMJnBEmGfeN4wLYrLDd9dStMQw6U6cYGvAuKkq726Kuqgj1eMtFwS4ND1Q8IWkUMZJgUVBHqh2Wu7JCk1eIt0n9CxLiQS5j4rWSQtbXSlPoW0d3EjVNEIEjbM_dupZtNMGpSv3UmNxaSSshJt2qg0xMwoN2jf5AvI7FZix-u2xmkfPQTReseB66TVodAZtMW8qFdbHBecpPyjwzOOy4FeS6Jf2yIATJe0aQ";
		String CONTENT = "application/json";
		String p = "3";
		String currency = "AUD";
		String Loc = "en-AU";
		String reg = "AU";
		String offer = "0003P";
		String tok = "Bearer " + token;
		ArrayList<String> subs = Subscriptions.getId(token);
		System.out.println(subs.size());
		for (int j=1;j <= subs.size();j++) {
	    int k = j-1;
		String id = subs.get(k);
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + id + "/resources?api-version=2017-05-10")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja = jo.getAsJsonArray("value");
			JsonObject jo1 = new JsonObject();
			jo1.add(""+j+"", ja);
			System.out.println(jo1.toString());
			//System.out.println(jo1.size());
		} catch (Exception e) {
			System.out.println("ttttt");
		}		
	}
}
}