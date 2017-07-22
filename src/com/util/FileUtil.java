package com.util;

import java.io.File;

/**
 * 文件工具类
 * @author ZZ
 * @date 2015-1-22 上午8:44:01
 */
public class FileUtil {
	
	/**
	 * 获取文件后缀名
	 * @param fileName
	 * @return
	 */
	public static String getFileExtension(String fileName){
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
	}
	
	/**
	 * 获取文件大小
	 * @param size 字节
	 * @return
	 */
	public static  String getFileSize(long size) 
	{
		if (size <= 0)	return "0";
		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		float temp = (float)size / 1024;
		if (temp >= 1024) 
		{
			return df.format(temp / 1024) + "M";
		}
		else 
		{
			return df.format(temp) + "K";
		}
	}
	
	/**
     * 判断路径是否存在，不存在创建此路径
     * @param path
     */
    public static String createPath(String path){
    	File file = new File(path);
    	if(!file.exists()){
    		file.mkdirs();
    	}
    	return path;
    }
}
