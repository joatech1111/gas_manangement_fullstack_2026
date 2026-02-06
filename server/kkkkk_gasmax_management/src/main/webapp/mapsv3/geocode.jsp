<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%><%@
page import="java.io.BufferedReader" %><%@
page import="java.io.DataOutputStream" %><%@
page import="java.io.InputStreamReader" %><%@
page import="java.net.HttpURLConnection" %><%@
page import="java.net.URLConnection" %><%@
page import="java.net.URLDecoder" %><%@
page import="java.net.URLEncoder" %><%@
page import="java.net.URL" %><%
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if(request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
	request.setCharacterEncoding("UTF-8");
%><%

	String mapClientId = "4h79g5bh8u";
	String mapClientSecret = "ZWaV70Kv9xOtLR0AoFh95CsxlQbqQy7tpCAJNNiF";

	String query = null;
	try{
		query = request.getParameter("query");
		
		if (query == null || query.trim().length() == 0) {
			out.print("Invalid parameter");
		} else {
			String newQuery = new String(query.getBytes("8859_1"), "UTF-8");
			//String newQuery2 = URLEncoder.encode(query, "ISO-8859-1");
			//newQuery2 = URLDecoder.decode(query, "UTF-8");
			
			//String testString = URLDecoder.decode(newQuery, "UTF-8");
			
			//URL url = new URL("https://openapi.naver.com/v1/map/geocode?query=" + URLEncoder.encode(newQuery, "UTF-8"));
			//URL url = new URL("https://openapi.naver.com/v1/map/geocode?query=" + newQuery);
			URL url = new URL("https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + URLEncoder.encode(newQuery, "UTF-8"));
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			
			con.setRequestMethod("GET");
			con.setRequestProperty("Access-Control-Allow-Origin", "*");
			con.setRequestProperty("Access-Control-Allow-Methods", "GET, POST");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "application/json");
			con.setUseCaches(false);
		    con.setDoInput(true);
			con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", mapClientId);
			con.setRequestProperty("X-NCP-APIGW-API-KEY", mapClientSecret);
	
			InputStreamReader isr = null;
			int responseCode = con.getResponseCode();
			if (responseCode >= 400) {
				isr = new InputStreamReader(con.getErrorStream(), "UTF-8");
			} else {
				isr = new InputStreamReader(con.getInputStream(), "UTF-8");
			}
			
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			StringBuffer responseBuffer = new StringBuffer();
			
			while((inputLine = in.readLine()) != null) {
				responseBuffer.append(inputLine);
			}
			in.close();
	
			//out.print(newQuery);
			out.print(responseBuffer.toString());
		}
	} catch (Exception e){
		e.printStackTrace();
	}
%>