package com.joainfo.common.util;

import java.util.Base64;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 문자열을 이용한 다양한 기능을 제공
 * @author 백원태
 * @version 1.0
 */
public class StringUtil {
	public static String nullToBlank(String value) {
		return (value == null) ? "" : value;
	}
	/**
	 * 여러 문자열을 이용하여 키 값 문자열 반환
	 * 만일 키가 하나만 있다면 그 값이 그대로 키가 되어 반환됨
	 * @param keys
	 * @return
	 */
	public static String getKeyValue(LinkedHashMap<String, String> keys){
		String keyValue = "";
		java.util.Iterator<String> iterator = keys.keySet().iterator();

		while (iterator.hasNext()) {
			String key = iterator.next();
			if (keys.size()==1) return (String)keys.get(key);
			keyValue += "_" + (String)keys.get(key);
		  }
		return keyValue;
	}

	/**
	 * 단일 문자열을 이용하여 키 값 문자열 반환
	 * @param key
	 * @return
	 */
	public static String getKeyValue(String key){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("singleKey", key);
		return getKeyValue(keys);
	}

	/**
	 * 두개의 문자열을 이용하여 키 값 문자열 반환
	 * @param key1
	 * @param key2
	 * @return
	 */
	public static String getKeyValue(String key1, String key2){
		LinkedHashMap<String, String> keys = new LinkedHashMap<String, String>();
		keys.put("key1", key1);
		keys.put("key2", key2);
		return getKeyValue(keys);

	}

	 /**
	  * 특수문자를 모두 제거하여 반환
	 * @param str
	 * @return
	 */
	public static String stringReplace(String str){
		if (str==null) return "";
	    String str_imsi   = "";
	    String[] filter_word = {"",
	    		                   "\\.",
	    		                   "\\?",
	    		                   "\\/",
	    		                   "\\~",
	    		                   "\\!",
	    		                   "\\@",
	    		                   "\\#",
	    		                   "\\$",
	    		                   "\\%",
	    		                   "\\^",
	    		                   "\\&",
	    		                   "\\*",
	    		                   "\\(",
	    		                   "\\)",
	    		                   "\\_",
	    		                   "\\+",
	    		                   "\\=",
	    		                   "\\|",
	    		                   "\\\\",
	    		                   "\\}",
	    		                   "\\]",
	    		                   "\\{",
	    		                   "\\[",
	    		                   "\\\"",
	    		                   "\\'",
	    		                   "\\:",
	    		                   "\\;",
	    		                   "\\<",
	    		                   "\\,",
	    		                   "\\>",
	    		                   "\\?",
	    		                   " ",
	    		                   "\\-",
	    		                   "\\/"};
	    for(int i=0;i<filter_word.length;i++){
	        str_imsi = str.replaceAll(filter_word[i],"");
	        str = str_imsi;
	    }
	    return str;
	}
	/**
	 * 특수문자를 모두 제거하여 반환(점 제외)
	 * @param str
	 * @return
	 */
	public static String stringReplaceExceptPoint(String str){
		if (str==null) return "";
		String str_imsi   = "";
		String[] filter_word = {"",
//				"\\.",
				"\\?",
				"\\/",
				"\\~",
				"\\!",
				"\\@",
				"\\#",
				"\\$",
				"\\%",
				"\\^",
				"\\&",
				"\\*",
				"\\(",
				"\\)",
				"\\_",
				"\\+",
				"\\=",
				"\\|",
				"\\\\",
				"\\}",
				"\\]",
				"\\{",
				"\\[",
				"\\\"",
				"\\'",
				"\\:",
				"\\;",
				"\\<",
				"\\,",
				"\\>",
				"\\?",
				" ",
//				"\\-",
		"\\/"};
		for(int i=0;i<filter_word.length;i++){
			str_imsi = str.replaceAll(filter_word[i],"");
			str = str_imsi;
		}
		return str;
	}

	 /**
	  * 숫자만 반환
	 * @param str
	 * @return
	 */
	public static String onlyNumber(String str){
		 String result ="";
		 String patternString = "\\d";
		 Pattern pattern = Pattern.compile(patternString);
		 Matcher matcher = pattern.matcher(str);
		 while(matcher.find()){
			 result += matcher.group(0);
		 }
		 return result;
	 }

	// 오늘과의 날짜 차이를 계산하여 반환
	public static int dateDifference(String baseString){
		if ("".equals(baseString) || baseString==null) return 0;
		Date today = new Date();
		String dateString = stringReplace(baseString);
		SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd");
		Date date;
		try {
			date = dateFormat.parse(dateString);
		} catch (ParseException e) {
			System.out.println("com.joainfo.common.util.StringUtil.dateDifference(): 날짜형식(yyyyMMdd)이 맞지 않습니다. (" + dateString + ")");
			return 0;
		}
		int diff =(int)((today.getTime() - date.getTime()) / (1000*60*60*24));
		return diff;
	}

	/**
	 * 월 더하기. month가 음수면 빼기
	 * @param dateString
	 * @param month
	 * @return
	 */
	public static String addMonth(String dateString, int month){
		String dateStr = stringReplace(dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
			Date date  = dateFormat.parse(dateStr);
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
			c.setTime(date);
			c.add(Calendar.MONTH, month);    // 월 증가
			return dateFormat2.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return dateString;
	}

	/**
	 * 일 더하기. day가 음수면 빼기
	 * @param dateString
	 * @param day
	 * @return
	 */
	public static String addDay(String dateString, int day){
		if (dateString==null) return "";
		if("".equals(dateString)) return "";
		String dateStr = stringReplace(dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
        try {
			Date date  = dateFormat.parse(dateStr);
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
			c.setTime(date);
			c.add(Calendar.DATE, day);    // 일 증가
			return dateFormat2.format(c.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return dateString;
	}

	/**
	 * 요일 이름 반환
	 * @param dateString
	 * @return
	 */
	public static String getDayName(String dateString){
		//Calandar클래스 이용, 1부터 7까지
		String[] dateArray = {"", "일", "월", "화", "수", "목", "금", "토"};
		String dateStr = stringReplace(dateString);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        try {
			Date date  = dateFormat.parse(dateStr);
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
			c.setTime(date);
			return dateArray[c.get(Calendar.DAY_OF_WEEK)];
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 날짜 형식을 yyyy-MM-dd 형식으로 변경하여 반환
	 * @param str null이거나 "" 이면 오늘 날짜 반환
	 * @param format
	 * @return
	 */
	public static String dateFormatStr(String str){
		return dateFormatStr(str, "-");
	}

	/**
	 * 날짜 형식을 yyyy-MM-dd 형식으로 변경하여 반환
	 * @param str null이거나 "" 이면 오늘 날짜 반환
	 * @param format
	 * @return
	 */
	public static String dateFormatStr(String str, String delimiter){
		if (("".equals(str)) || (str==null)) {
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
			String month = "" + (c.get(Calendar.MONTH)+1);
			if (month.length() == 1) month = "0" + month;
			String day = "" + c.get(Calendar.DAY_OF_MONTH);
			if (day.length() == 1) day = "0" + day;
			return "" + c.get(Calendar.YEAR) + delimiter + month + delimiter + day;
		} else{
			String dateStr = stringReplace(str);
			return dateStr.substring(0,4) + delimiter + dateStr.substring(4,6) + delimiter + dateStr.substring(6,8);
		}
	}

	/**
	 * @return 오늘 일시를 yyyyMMddHHmmSS 형식으로
	 */
	public static String nowDateTimeStr(){
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"));
		String month = "" + (c.get(Calendar.MONTH)+1);
		if (month.length() == 1) month = "0" + month;
		String day = "" + c.get(Calendar.DAY_OF_MONTH);
		if (day.length() == 1) day = "0" + day;
		String hour =  "" + c.get(Calendar.HOUR_OF_DAY);
		if (hour.length() == 1) hour = "0" + hour;
		String minute =  "" + c.get(Calendar.MINUTE);
		if (minute.length() == 1) minute = "0" + minute;
		String second =  "" + c.get(Calendar.SECOND);
		if (second.length() == 1) second = "0" + second;
		return "" + c.get(Calendar.YEAR) + month + day + hour + minute + second;
	}

	/**
	 * 금액표시 3자리마다 쉼표찍기
	 * @param str
	 * @return
	 */
	public static String moneyFormatStr(String str){
		String moneyStr = stringReplaceExceptPoint(str);
		if (("".equals(str)) || str==null) {
			str = "0";
		}
		float money;
		try{
			money = new Float(moneyStr).floatValue();
		} catch (Exception e){
			return "";
		}
		if (money == 0) return "0";
		DecimalFormat format = new DecimalFormat();
		return format.format(money);
	}

	public static String convertCustomerCodeToTankFormat(String customerCode) {
		if (customerCode == null || customerCode.length() < 4) return customerCode;

		String customerCodeForTank = customerCode;

		if (!"000".equals(customerCode.substring(0, 3))) {
			customerCodeForTank = customerCode.substring(0, 3) + "-00000";
		}

		return customerCodeForTank;
	}

	public static String decodeBase64(String encode) {
		if (encode != null && !encode.isEmpty()) {
			try {
				return new String(Base64.getDecoder().decode(encode));
			} catch(Exception ex) {
				// Nothing
			}
		}

		return encode;


	}

	public static void main(String[] args){
		System.out.println(onlyNumber("가나2아다라8카마타994-+ㅇadfkdd098ds"));
		System.out.println(stringReplace("가나2아다라8카마타994-+ㅇadfkdd098ds"));
		System.out.println(stringReplaceExceptPoint("3899.02"));
		System.out.println(moneyFormatStr("3899.02"));
		System.out.println(dateDifference("2012-08-07"));
		System.out.println(addMonth("2012/08/07", -1));
		System.out.println(dateFormatStr("2012/08/07"));
		System.out.println(moneyFormatStr("0"));
	}
}
