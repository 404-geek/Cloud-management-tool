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

		String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6IlNTUWRoSTFjS3ZoUUVEU0p4RTJnR1lzNDBRMCIsImtpZCI6IlNTUWRoSTFjS3ZoUUVEU0p4RTJnR1lzNDBRMCJ9.eyJhdWQiOiJodHRwczovL21hbmFnZW1lbnQuY29yZS53aW5kb3dzLm5ldC8iLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yN2M5NTA0YS1jNDY3LTQzNzYtYjI2MS04OWIxNWU4ZWJiMTcvIiwiaWF0IjoxNTE5MDIyMTYyLCJuYmYiOjE1MTkwMjIxNjIsImV4cCI6MTUxOTAyNjA2MiwiYWNyIjoiMSIsImFpbyI6IlkyTmdZSENUTGNuYi9iRjBza2ZocC9teG1ROTF3OWZQZTNDUFU3RE1ORzJPcjhYTlFHNEEiLCJhbXIiOlsicHdkIl0sImFwcGlkIjoiMDVjZDU5ZjktNmUyMC00OTI4LTg3OGItYTgwYjcwMzM0OTJhIiwiYXBwaWRhY3IiOiIxIiwiZmFtaWx5X25hbWUiOiJIZXdpdHQiLCJnaXZlbl9uYW1lIjoiSnVzdGluIiwiZ3JvdXBzIjpbImFjNzk2NzQwLWZmNWMtNDdmNy04OWRlLWZlOTMzYmRmNTA4ZCJdLCJpcGFkZHIiOiIxLjIzLjE4NS4xNTQiLCJuYW1lIjoiSnVzdGluIEhld2l0dCIsIm9pZCI6IjVmODkwNmQxLWVlMmQtNDIxZC1hYTcxLTA2YTgxMDljMjEzMCIsInB1aWQiOiIxMDAzM0ZGRjhEMjcyNEFDIiwic2NwIjoidXNlcl9pbXBlcnNvbmF0aW9uIiwic3ViIjoicHJZRGlfbC1HR2xPempfaUw2T2t6cEFLdEFvQ2RUZWxHMFZ1UnM5TlJtWSIsInRpZCI6IjI3Yzk1MDRhLWM0NjctNDM3Ni1iMjYxLTg5YjE1ZThlYmIxNyIsInVuaXF1ZV9uYW1lIjoianVzdGluLmhld2l0dEB5ZWxsb3dmaW4uYmkiLCJ1cG4iOiJqdXN0aW4uaGV3aXR0QHllbGxvd2Zpbi5iaSIsInV0aSI6Ijl4Rk5VSUlvWmt5cm5nVGVGckloQUEiLCJ2ZXIiOiIxLjAiLCJ3aWRzIjpbIjYyZTkwMzk0LTY5ZjUtNDIzNy05MTkwLTAxMjE3NzE0NWUxMCJdfQ.cnAZuFE8jvPi3zOy0lZli0O3g6WGVipTX4tYNCEnHslMKyqDSoH9VfXuvo8IF0m8gJHdg68voC3eE4W6zZzzfrT-uzOybnIZhOvzUfAJhphXkvlcyN8rOxR-yefhmWRU-Hop21OcA60TZYgRlL11Xlcd1bg2nUoh7j-13J5j954pM1MNM6x4l_NxQO7bjkdyalHkvBtXcD8JZmJjbOLeJGRhqB-xn4sNtqolGCzdEf5E7LuxuvDs3SAxIpXQUCHv7th89qfeCp8swyVrruDpGm5R1PICuMrbbOp_AdWN-5tCbijX9rOgAN74I-yP3ldwWw_p3FhClb4wzJsW7VPxcQ";
		String CONTENT = "application/json";
		String p = "22";
		//String currency = "AUD";
		String Loc = "en-AU";
		String reg = "AU";
		String offer = "0003P";
		//String tok = "Bearer " + token;
		
		
		
		
}
}