package com.yf.utils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2 {
	public static void main(String[] args) {

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA3NTMyODU3LCJuYmYiOjE1MDc1MzI4NTcsImV4cCI6MTUwNzUzNjc1NywiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQVBsTEttSjRNT3YrZE12MW90bEtEOVoxamNaazliMnhvak11OEdScnp0Wkk9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6ImxJOFdkTWRKcWt5SkpUSUIyZ05yQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.0VtXV_wblefn9RXPeFBe8acgKnE2WMqw2Sv_y35cJlstY1L64whATXIcC7VBy_rgcJvnLhvdmmedOWUHiife_Bo-9z7SG9Che1TeFZEYBKmNfS0QGVziEdc1S0ncajya2IceUV86XP1G4KSg6nLgE8s_38D5pSxp4Eo3hSjmhE50hsKYpUpjBoIYlS5MjXwxbBkqgMdILebN_HuaH7m8TtLo8ZSe-X4aUBn_T4mgKqN71-gKDSM9_BHsPADgn2LzujFSV_ki7X38uGad3KL6-LvvxrhPBHEkW2cZ4D9wA_rgHUAqjbtYlOdmg2H0afjZgZi-wSflbl1BxeLUCqBn1w";
		String CONTENT = "application/json";
		String str = "";
		ArrayList<Integer> index = Databases.getIndexdb(token);
		JsonArray ja1 = new JsonArray();
		for (int i = 0; i < index.size(); i++) {
			String resid = Resources.getResid(token, ((Integer) index.get(i)).intValue());
			try {
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
				ja1.add(jo);
			} catch (Exception e) {
				return;
			}
		}
		System.out.println(ja1.toString());
	}
}