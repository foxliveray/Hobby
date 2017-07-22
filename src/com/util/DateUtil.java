package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

	public final static String PATTERN = "yyyy-MM-dd HH:mm:ss";

	public final static String PATTERN2 = "MM月dd日  HH时mm分";
	
	public final static String PATTERN3 = "yyyy-MM-dd";
	
	public final static String PATTERN4 = "yyyy年MM月";
	
	public final static String PATTERN5 = "yyyyMMdd";
	
	public static void main(String[] args){
		Calendar cal = Calendar.getInstance();
		
		System.out.println(cal.get(Calendar.DAY_OF_WEEK));
	} 
	
	public static Date parse(String date) {
		return parse(date, PATTERN);
	}

	public static Date parse(String date, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		try {
			return format.parse(date);
		} catch (Exception e) {
			return null;
		}
	}
	 public static Date parse(String str, Locale locale) {
	        if(str == null || PATTERN == null) {
	            return null;
	        }
	        try {
	            return new SimpleDateFormat(PATTERN, locale).parse(str);
	        } catch (ParseException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }

	public static String format(Date date) {
		return format(date, PATTERN);
	}

	public static String format(Date date, String pattern) {
		if (date == null)
			return "";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * 获取当前小时（24小时制）
	 * 
	 * @return
	 */
	public static int get24Hour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}
	
	
	/**
	 * 获取当前月份
	 * 
	 * @param m
	 * @return
	 */
	public static int getMonth() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.MONTH) + 1;
	}
	
	/**
	 * 获取当前年份
	 * @return
	 */
	public static int getYear(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}
	
	/**
	 * 获取星期
	 * @return
	 */
	public static int getWeekDay(){
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.DAY_OF_WEEK);
	}
	
	public static Date getDate(int month) {
		Calendar c = Calendar.getInstance();
		int nowMonth = c.get(Calendar.MONTH) + 1;
		c.add(Calendar.MONTH, month - nowMonth);
		return c.getTime();
	}
	
	/**
	 * 当前日期前几日日期
	 * @param day
	 * @return
	 */
	public static String lastDayDate(int day){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day);
		return format(cal.getTime(), PATTERN3);
	}
	/**
	 * 取得当前日期之后N天的日期
	 * @param n
	 * @return
	 */
	public static Date afterNDay(int n) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, n);
		return c.getTime();
	}
}
