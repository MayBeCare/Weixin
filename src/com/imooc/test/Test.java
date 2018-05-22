package com.imooc.test;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Test {
	
	public static void main(String[] args) {
		String time1 = "2018-01-10";
		String time2 = "2018-01-11";
		int time = time1.compareTo(time2);
		System.out.println(time);
		if(time <= 0){
			System.out.println("生效");
		}
		
		DecimalFormat formater = new DecimalFormat("#0.##");
		formater.setRoundingMode(RoundingMode.FLOOR);
		System.out.println(formater.format(14.310687662731988));
		
		
	  System.out.println(Test.GetDistance(39.92982,116.28769,39.90793,116.45304));  
	}
	
	
    private static double EARTH_RADIUS = 6371.393;  
    private static double rad(double d)  
    {  
       return d * Math.PI / 180.0;  
    }  
  
    /** 
     * 计算两个经纬度之间的距离 
     * @param lat1 
     * @param lng1 
     * @param lat2 
     * @param lng2 
     * @return 
     */  
    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)  
    {  
       double radLat1 = rad(lat1);  
       double radLat2 = rad(lat2);  
       double a = radLat1 - radLat2;      
       double b = rad(lng1) - rad(lng2);  
       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +   
        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
       s = s * EARTH_RADIUS;  
//       s = Math.round(s * 1000);  
       return s;  
    }  

}
