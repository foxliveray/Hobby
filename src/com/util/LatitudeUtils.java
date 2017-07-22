package com.util;

/**
 * @author 钱洋
 * @date 2017年7月21日 下午11:26:44
 */
import java.io.BufferedReader;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.net.URL;  
import java.net.URLEncoder;  
import java.util.HashMap;  
import java.util.Map;  
  
  
import org.apache.commons.lang.StringUtils;  
  
  
public class LatitudeUtils {  
  
  
    public static final String KEY_1 = "55kYzrUl9kuGQrH4nxQZy1SI8a6cScxU";  
  
  
  
  
    /** 
     * 返回输入地址的经纬度坐标 
     * key lng(经度),lat(纬度) 
     */  
    public static Map<String,String> getGeocoderLatitude(String address){  
        BufferedReader in = null;  
        try {  
            //将地址转换成utf-8的16进制  
            address = URLEncoder.encode(address, "UTF-8");  
            //URL tirc = new URL("http://api.map.baidu.com/geocoder?address="+ address +"&output=json&key="+ KEY_1);  
            URL tire = new URL("http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak="+KEY_1+"&callback=showLocation");
  
            in = new BufferedReader(new InputStreamReader(tire.openStream(),"UTF-8"));  
            String res;  
            StringBuilder sb = new StringBuilder("");  
            while((res = in.readLine())!=null){  
                sb.append(res.trim());  
            }  
            String str = sb.toString();  
            Map<String,String> map = null;  
            if(StringUtils.isNotEmpty(str)){  
                int lngStart = str.indexOf("lng\":");  
                int lngEnd = str.indexOf(",\"lat");  
                int latEnd = str.indexOf("},\"precise");  
                if(lngStart > 0 && lngEnd > 0 && latEnd > 0){  
                    String lng = str.substring(lngStart+5, lngEnd);  
                    String lat = str.substring(lngEnd+7, latEnd);  
                    map = new HashMap<String,String>();  
                    map.put("lng", lng);  
                    map.put("lat", lat);  
                    return map;  
                }  
            }  
        }catch (Exception e) {  
            e.printStackTrace();  
        }finally{  
            try {  
                in.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return null;  
    }  
    
    private static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
     }
      
    public static double[] getLat(String address){
    	Map<String, String> json = LatitudeUtils.getGeocoderLatitude(address);  
    	 System.out.println("lng : " + json.get("lng"));  
         System.out.println("lat : " + json.get("lat"));  
         double[] gd = new double[2];
         gd = GPSUtil.bd09_To_gps84(Double.parseDouble(json.get("lng")),Double.parseDouble(json.get("lat")));
         System.out.println(gd[0]);
         System.out.println(gd[1]);
//         String lng = String.valueOf(gd[0]);
//         String lat = String.valueOf(gd[1]);
//         String mix = lng + ","+lat;
		return gd;
    }
    public static void main(String args[]){  
//        String mix = LatitudeUtils.getLat("北京交通大学");
//        System.out.println(mix);
    }  
  
}  