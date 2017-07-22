package com.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfinal.plugin.activerecord.Model;

/** 
 * 字符串操作工具包
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class StrUtils 
{
	private final static Pattern emailer = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private final static SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat dateFormater2 = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 将字符串转位日期类型
	 * @param sdate
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * 欢迎时间 早上 下午 e.g.
	 * @return
	 */
	public static String welcomeTime() {
		String welcome = "";
		int hour = DateUtil.get24Hour();
		if (hour < 6) {
			welcome = "凌晨";
		} else if (hour < 12) {
			welcome = "上午";
		} else if (hour < 18) {
			welcome = "下午";
		} else {
			welcome = "晚上";
		}
		return welcome;
	}
	
	/**
	 * 以友好的方式显示时间
	 * @param sdate
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if(time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//判断是否是同一天
		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
		}
		else if(days == 1){
			ftime = "昨天";
		}
		else if(days == 2){
			ftime = "前天";
		}
		else if(days > 2 && days <= 10){ 
			ftime = days+"天前";			
		}
		else if(days > 10){			
			ftime = dateFormater2.format(time);
		}
		return ftime;
	}
	
	/**
	 * 判断给定字符串时间是否为今日
	 * @param sdate
	 * @return boolean
	 */
	public static boolean isToday(String sdate){
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if(time != null){
			String nowDate = dateFormater2.format(today);
			String timeDate = dateFormater2.format(time);
			if(nowDate.equals(timeDate)){
				b = true;
			}
		}
		return b;
	}
	
	/**
	 * 判断给定字符串是否空白串。
	 * 空白串是指由空格、制表符、回车符、换行符组成的字符串
	 * 若输入字符串为null或空字符串，返回true
	 * @param input
	 * @return boolean
	 */
	public static boolean isEmpty( String input ) 
	{
		if ( input == null || "".equals( input ) )
			return true;
		
		for ( int i = 0; i < input.length(); i++ ) 
		{
			char c = input.charAt( i );
			if ( c != ' ' && c != '\t' && c != '\r' && c != '\n' )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断是不是一个合法的电子邮件地址
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email){
		if(email == null || email.trim().length()==0) 
			return false;
	    return emailer.matcher(email).matches();
	}
	/**
	 * 判断是不是一个合法的网址
	 * @param str
	 * @return
	 */
	public static boolean isURL(String str){
        //转换为小写
        str = str.toLowerCase();
        String regex = "^((https|http|ftp|rtsp|mms)?://)"  
                + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@  
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184  
                + "|" // 允许IP和DOMAIN（域名） 
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.  
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名  
                + "[a-z]{2,6})" // first level domain- .com or .museum  
                + "(:[0-9]{1,4})?" // 端口- :80  
                + "((/?)|" // a slash isn't required if there is no file name  
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";  
        return Pattern.compile(regex).matcher(str).matches(); //match(regex ,str);
    }
	/**
	 * 字符串转整数
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try{
			return Integer.parseInt(str);
		}catch(Exception e){}
		return defValue;
	}
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if(obj==null) return 0;
		return toInt(obj.toString(),0);
	}
	/**
	 * 对象转整数
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static long toLong(String obj) {
		try{
			return Long.parseLong(obj);
		}catch(Exception e){}
		return 0;
	}
	/**
	 * 字符串转布尔值
	 * @param b
	 * @return 转换异常返回 false
	 */
	public static boolean toBool(String b) {
		try{
			return Boolean.parseBoolean(b);
		}catch(Exception e){}
		return false;
	}
	/**
	 * 指定字符连接数组
	 * @param array
	 * @param separator
	 * @return
	 */
	public static String join(Object[] array, char separator) {
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);
        StringBuffer buf = new StringBuffer(bufSize);

        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
	
	/**
	 * 字符串转数组
	 * @param str
	 * @param separator
	 * @return
	 */
	public static Integer[] toIntgerArray(String str, String separator){
		String[] strs = str.split(separator);
		Integer[] intArray = new Integer[strs.length];
		for(int i=0;  i< strs.length; i++){
			intArray[i] = Integer.valueOf(strs[i]);
		}
		return intArray;
	}
	
	/**
	 * 指定字符连接Model ID
	 * @param array
	 * @param separator
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String joinIds(List<? extends Model> models, char separator) {
        if (models == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder("0");
        for (int i = 0; i < models.size(); i++) {
        	sb.append(separator);
        	sb.append(models.get(i).get("id"));
        }
        return sb.toString();
    }
	
	/**
	 * 判断字符串str是否在数组strs中
	 * @param str
	 * @param strs
	 * @return
	 */
	public static boolean haveStr(String str, String[] strs){
		for(String s : strs){
			if(s.equals(str)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 格式化weeks数据
	 * @param weeks
	 * @return
	 */
	public static String formatWeeks(String weeks){
		Pattern pattren = Pattern.compile("^\\d(,\\d+)*");
		Matcher matcher = pattren.matcher(weeks);
		if(matcher.matches()){
			String[] ws = weeks.split(",");
			String str = ws[0];
			if(ws.length>1){
				if((Integer.valueOf(ws[1])-Integer.valueOf(ws[0]))==1){
					str += "-";
					if(ws.length<3 || (Integer.valueOf(ws[2])-Integer.valueOf(ws[1]))>1){
						str += ws[1];
					}
				}
				for(int i=1; i<ws.length; i++){				
					if((Integer.valueOf(ws[i])-Integer.valueOf(ws[i-1]))>1){
						str += ","+ws[i];
					}				
					if(i>1 && i<(ws.length-1) && (Integer.valueOf(ws[i])-Integer.valueOf(ws[i-1]))==1
					  && (Integer.valueOf(ws[i+1])-Integer.valueOf(ws[i]))==1
					  && (Integer.valueOf(ws[i-1])-Integer.valueOf(ws[i-2]))>1){
						str += "-";					
					}
					
					if(i>1 && i<(ws.length-1) && (Integer.valueOf(ws[i])-Integer.valueOf(ws[i-1]))==1
					   && (Integer.valueOf(ws[i+1])-Integer.valueOf(ws[i]))>1){
						if(i>2 && (Integer.valueOf(ws[i-1])-Integer.valueOf(ws[i-2]))>1){
							str += "-";
						}
						str += ws[i];
					}
				}
				
				if(ws.length>2 && (Integer.valueOf(ws[ws.length-1])-Integer.valueOf(ws[ws.length-2]))==1){
					if(ws.length>3 && (Integer.valueOf(ws[ws.length-2])-Integer.valueOf(ws[ws.length-3]))>1)
						str += "-";
					str += ws[ws.length-1];
				}				
			}
			return str;
		}else{
			return "";
		}		
	}
}