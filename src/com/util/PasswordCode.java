package com.util;

import java.security.MessageDigest;

/**
 * Created by lenovo on 2017/7/9.
 */


public class PasswordCode {

    /**
     * message-digest algorithm 5（信息-摘要算法）
     *
     * md5的长度，默认为128bit，也就是128个 0和1的 二进制串 。
     *
     * 128/4 = 32 换成 16进制 表示后，为32位了。
     */
	public void PasswordCode(){
		
	}
    public static String getMD5(String password){
        String md5Str="";
        try {
            //创建一个提供信息摘要算法的对象，初始化为MD5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            //变成byte数组
            byte[] MD5ofPassword = password.getBytes();

            //计算后获得字节数组
            byte[] buff = md.digest(MD5ofPassword);

            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5Str = bytesToHex(buff);
        }catch(Exception e){
            e.printStackTrace();
        }
        return md5Str;
    }

    /**
     * 二进制转十六进制
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuffer md5Str = new StringBuffer();

        //把数组的每个字节转化成16进制MD5字符串
        int digital;
        for(int i=0;i<bytes.length;i++){
            digital = bytes[i];

            if(digital<0)
                digital+=256;
            if(digital<16)
                md5Str.append("0");
            md5Str.append(Integer.toHexString(digital));
        }
        return md5Str.toString().toUpperCase();
    }
    
    public static void main(String []args){
    	PasswordCode pc = new PasswordCode();
    	String s = pc.getMD5("1234");
    	String t = pc.getMD5("1234");
    	System.out.println(s);
    	System.out.println(t);
    }
}
