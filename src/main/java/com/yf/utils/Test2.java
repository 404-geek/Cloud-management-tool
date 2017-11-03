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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {
	public static void main(String[] args) {

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyIsImtpZCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA5NzEwMzQyLCJuYmYiOjE1MDk3MTAzNDIsImV4cCI6MTUwOTcxNDI0MiwiYWNyIjoiMSIsImFpbyI6IlkyTmdZRGdqMk5BZXR1aDVoTDVEOW9wSEg3cGU4TndOZlJsUm9LRVM4c295cCtUeG56QUEiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiYjMxNDRmZTEtNjMzOC00M2EwLTg5MTgtYzA1MjkxZjUxMTcwIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxNC4xNDIuMTEwLjIyMiIsIm5hbWUiOiJKdXN0aW4gSGV3aXR0Iiwib2lkIjoiNWY4OTA2ZDEtZWUyZC00MjFkLWFhNzEtMDZhODEwOWMyMTMwIiwicHVpZCI6IjEwMDMzRkZGOEQyNzI0QUMiLCJzY3AiOiJ1c2VyX2ltcGVyc29uYXRpb24iLCJzdWIiOiJwcllEaV9sLUdHbE96al9pTDZPa3pwQUt0QW9DZFRlbEcwVnVSczlOUm1ZIiwidGlkIjoiMjdjOTUwNGEtYzQ2Ny00Mzc2LWIyNjEtODliMTVlOGViYjE3IiwidW5pcXVlX25hbWUiOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInVwbiI6Imp1c3Rpbi5oZXdpdHRAeWVsbG93ZmluLmJpIiwidXRpIjoiYmQ1UW9sZlE4MHFLQWUzVC1Ec1lBQSIsInZlciI6IjEuMCIsIndpZHMiOlsiNjJlOTAzOTQtNjlmNS00MjM3LTkxOTAtMDEyMTc3MTQ1ZTEwIl19.bW_ZBR8gCIlj2V3Uy3969IWTSvR2-XS-V4_sv708eFoMQEW3v9T_r3Jx91LgFmB50cyj22W_RY2XE77hMzirVPpd1pkBAzqKE2Cpeajg3bl2JoKKghrMPNMFGTy_yZvVacr8jMpIiKm50cWYrpXmsuPu14Fd4Sh3hdMDdXcXLij3iFiOz6X7W6w4L1IPlffwnFfB3KjS1wDuvrWmNm29Ugs9WNnOmV66J7G4Ll9N-UlQcTsKJi6eg6UH8S5XrApvTiWTP-AdtVPcyeK750cIgJ6RTJQ9IOb6u1FCDOSzxO5unlN9aCLpoQfOl-M6YmZxv5t_orevWRyYDvsSoY3rFA";
		String CONTENT = "application/json";
/*		String currency = "AUD";
		String Loc = "en-AU";
		String reg = "AU";
		String offer = "0003P";*/
		String str = "";
		ArrayList<Integer> index = Databases.getIndexdb(token);
		JsonArray ja1 = new JsonArray();
		for (int i = 0; i < index.size(); i++) {
			String resid = Resources.getResid(token, ((Integer) index.get(i)).intValue());
			System.out.println(resid);
			try {
				OkHttpClient client = new OkHttpClient();

				Request request = new Request.Builder()
				  .url("https://management.azure.com"+resid+"?api-version=2014-01-01")
				  .addHeader("Authorization", "Bearer "+token)
				  .build();

				Response response = client.newCall(request).execute();
				System.out.println(response.body().string());
/*	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document doc = builder.parse(new ByteArrayInputStream(response.body().bytes()));
	            XPath xPath = XPathFactory.newInstance().newXPath();
	            String expression = "//d:status/text()";
	            String status = xPath.compile(expression).evaluate(doc, XPathConstants.STRING).toString();
	            System.out.println(status);
				String[] det = Databases.getdbLive(token, resid);
				JsonElement je = new JsonParser().parse(det[0]);
				JsonArray dtu = je.getAsJsonArray();
				je = new JsonParser().parse(det[1]);
				JsonArray percpu = je.getAsJsonArray();
				je = new JsonParser().parse(det[2]);
				JsonArray perlog = je.getAsJsonArray();
				je = new JsonParser().parse(det[3]);
				JsonArray perdata = je.getAsJsonArray();
				ArrayList<Integer> list = new ArrayList<Integer>();
				for (int j = 0; j < 24; j++) {
					String per = percpu.get(j).getAsJsonObject().get("timeStamp").getAsString();
					DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
					utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

					Date date = null;
					try {
						date = utcFormat.parse(per);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					DateFormat pstFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					pstFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
					Timestamp ts = Timestamp.valueOf(pstFormat.format(date));
					LocalDateTime ldt = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
					Timestamp current = Timestamp.valueOf(ldt);
					if (ts.before(current)) {
						list.add(Integer.valueOf(j));
					}
				}
				int size = list.size() - 1;
				JsonObject jo = new JsonObject();
				jo.addProperty("Resource ID", resid);
				Pattern pat = Pattern.compile("resourceGroups/(.*?)/providers");
				Matcher m = pat.matcher(resid);
				while (m.find()) {
					str = m.group(1);
				}
				jo.addProperty("Resource Group", str);
				jo.addProperty("Timestamp", dtu.get(size).getAsJsonObject().get("timeStamp").getAsString());
				try {
					jo.addProperty("DTU Percentage",
							Float.valueOf(dtu.get(size).getAsJsonObject().get("average").getAsFloat()));
				} catch (Exception e) {
					jo.addProperty("DTU Percentage", Integer.valueOf(0));
				}
				try {
					jo.addProperty("CPU percentage",
							Float.valueOf(percpu.get(size).getAsJsonObject().get("average").getAsFloat()));
				} catch (Exception e) {
					jo.addProperty("CPU percentage", Integer.valueOf(0));
				}
				try {
					jo.addProperty("Log IO percentage",
							Float.valueOf(perlog.get(size).getAsJsonObject().get("average").getAsFloat()));
				} catch (Exception e) {
					jo.addProperty("Log IO percentage", Integer.valueOf(0));
				}
				try {
					jo.addProperty("Data IO percentage",
							Float.valueOf(perdata.get(size).getAsJsonObject().get("average").getAsFloat()));
				} catch (Exception e) {
					jo.addProperty("Data IO percentage", Integer.valueOf(0));
				}
				ja1.add(jo);*/
			} catch (Exception e) {
				return;
			}
		}
		//System.out.println(ja1.toString());
	}
}