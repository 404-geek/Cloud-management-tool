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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyIsImtpZCI6IjJLVmN1enFBaWRPTHFXU2FvbDd3Z0ZSR0NZbyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA5NzMxNzY4LCJuYmYiOjE1MDk3MzE3NjgsImV4cCI6MTUwOTczNTY2OCwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhHQUFBQU5VNGFuL0RWTmJxYzJQYUtCdEg1N3VVTStORVl2L1U5RUNzSnJFTlkrM3c9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTA2LjIwMC4xOTkuMzMiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6IjMtVlV1Tmhxc0VHTkdESVhrMUlBQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.2dnlDROpNJPKlOxLaYN-WEaFjmHDBuFzGj_ctuf1GLoAgH_9Mh6w0fv0sa7lrkhJ2bCgUhclwbnJ6-bZsf4InHEXNpeMLr6WxoN-rtj9iV9ce3_9g0ZHZ6nfHlXVKrCqGR_7R7EUZDUo1pA5Ss7aFK-a8gUVYefroMYNSgDLaqXK7KA3nCni2nYRCqlYRpABNQN-8htQmqSdoqszZUcd7ouFhKz87OHPhQWEYdw-tF24L_NCV2LL40jVqh89RFCXWbQkmwlOY8_m2jegp3CeYSHmt3Okdwh8xlMx04dnyRpwDUZHoimAdCGDxGA-9xi-WV4QQMp3kbyZ8fz32m-Fcg";
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
			try {
				OkHttpClient client = new OkHttpClient();

				Request request = new Request.Builder()
				  .url("https://management.azure.com"+resid+"?api-version=2014-01-01")
				  .addHeader("Authorization", "Bearer "+token)
				  .build();

				Response response = client.newCall(request).execute();
				JsonElement jer = new JsonParser().parse(response.body().string());
				String status = jer.getAsJsonObject().get("properties").getAsJsonObject().get("status").getAsString();
/*	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();
	            Document doc = builder.parse(new ByteArrayInputStream(response.body().bytes()));
	            XPath xPath = XPathFactory.newInstance().newXPath();
	            String expression = "//d:status/text()";
	            String status = xPath.compile(expression).evaluate(doc, XPathConstants.STRING).toString();
	            System.out.println(status);*/
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
				jo.addProperty("Status", status);
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
				ja1.add(jo);
			} catch (Exception e) {
				return;
			}
		}
		System.out.println(ja1.toString());
	}
}