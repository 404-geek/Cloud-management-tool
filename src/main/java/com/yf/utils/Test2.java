package com.yf.utils;

import java.util.ArrayList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Test2
{
  public static void main(String[] args)
  {
		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyIsImtpZCI6IkhIQnlLVS0wRHFBcU1aaDZaRlBkMlZXYU90ZyJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTA1OTgxOTYwLCJuYmYiOjE1MDU5ODE5NjAsImV4cCI6MTUwNTk4NTg2MCwiYWNyIjoiMSIsImFpbyI6IkFTUUEyLzhGQUFBQThiWTFwRWkrNEpuMGpXLyt2K3owSXpIdVQzVkdYYldESm94d0tBYTFwUms9IiwiYW1yIjpbInB3ZCJdLCJhcHBpZCI6ImIzMTQ0ZmUxLTYzMzgtNDNhMC04OTE4LWMwNTI5MWY1MTE3MCIsImFwcGlkYWNyIjoiMSIsImZhbWlseV9uYW1lIjoiSGV3aXR0IiwiZ2l2ZW5fbmFtZSI6Ikp1c3RpbiIsImdyb3VwcyI6WyJhYzc5Njc0MC1mZjVjLTQ3ZjctODlkZS1mZTkzM2JkZjUwOGQiXSwiaXBhZGRyIjoiMTQuMTQyLjExMC4yMjIiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6ImRRVGFVaXpvTFVHNjBIY0pBMFlJQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.pzsSQ6WubsAVA1YnZD4JD5Ndk-41cgXmHwMevDiUkuDZmkNrRTmp6zIyL6y8_kjZWmBJG1S1P5fcYKuTSgfgRH9hrMEifL0iQCXjJWWQkunADYa6c7ygxyXpwKE_8zxtGK6-qJ_IZImTs6HHRS_sXUzLWwj_rzHvR07a1Xxs3FsdJAoVo3RcneTRRk_H2kK81FJofAT1-4ZT1JTNfL7IFqFNil4eQ8vNFLdKNPwFyXHzLv3ZWVheebnnNGt2bRN7oD7gBSdxf0rn0GHGi5cBq5Dwe5iJSs75jnNIdsZS8Iz4ZKECweC2ITfjO2cfPZYEq-rKVLj6Qvm6LMX8zuCw6g";
		String CONTENT = "application/json";
		JsonArray ja = new JsonArray();
		String id = Subscriptions.getId(token);
		String tok = "Bearer " + token;
		OkHttpClient client = new OkHttpClient();

		Request request = new Request.Builder()
				.url("https://management.azure.com" + id
						+ "/providers/Microsoft.Commerce/RateCard?api-version=2016-08-31-preview&$filter=OfferDurableId eq 'MS-AZR-0003p' and Currency eq 'USD' and Locale eq 'en-US' and RegionInfo eq 'US'")
				.addHeader("Authorization", tok).addHeader("Content-type", CONTENT).build();
		try {
			Response response = client.newCall(request).execute();
			JsonElement je = new JsonParser().parse(response.body().string());
			JsonObject jo = je.getAsJsonObject();
			JsonArray ja1 = jo.getAsJsonArray("Meters");
			String currency = jo.get("Currency").getAsString();
			String tax = jo.get("IsTaxIncluded").getAsString();
			String locale = jo.get("Locale").getAsString();
			//String temp = ja1.get(19).getAsJsonObject().get("MeterRegion").getAsString();
			//System.out.println(ja1.size());
			for (int j = 0; j < ja1.size(); j++) {
				JsonObject jo1 = new JsonObject();
				jo1.addProperty("Currency", currency);
				jo1.addProperty("Tax", tax);
				jo1.addProperty("Locale", locale);
				jo1.addProperty("EffectiveDate", ja1.get(j).getAsJsonObject().get("EffectiveDate").getAsString());
   			    jo1.addProperty("IncludedQuantity", ja1.get(j).getAsJsonObject().get("IncludedQuantity").getAsInt());
   			    try{
				jo1.addProperty("MeterCategory", ja1.get(j).getAsJsonObject().get("MeterCategory").getAsString());
   			    }
   			    catch (Exception e) {
					jo1.addProperty("MeterCategory", "NULL");
				}
   			    jo1.addProperty("MeterRegion", ja1.get(j).getAsJsonObject().get("MeterRegion").getAsString());
				jo1.addProperty("MeterStatus", ja1.get(j).getAsJsonObject().get("MeterStatus").getAsString());
      			jo1.addProperty("MeterSubCategory", ja1.get(j).getAsJsonObject().get("MeterSubCategory").getAsString());
      			try {
         		jo1.addProperty("MeterTags", ja1.get(j).getAsJsonObject().get("MeterTags").getAsString());
      			}
			    catch (Exception e) {
				jo1.addProperty("MeterTags", "NULL");
			    }
				jo1.addProperty("Unit", ja1.get(j).getAsJsonObject().get("Unit").getAsString());
				try {
					jo1.addProperty("MeterRates",
							ja1.get(j).getAsJsonObject().get("MeterRates").getAsJsonObject().get("0").getAsFloat());
				} catch (Exception e) {
					jo1.addProperty("MeterRates", Integer.valueOf(0));
				}
				ja.add(jo1);
			}

		} catch (Exception e) {
			System.out.println("jjjj");
		}
		System.out.println(ja.toString());
	}
  }

