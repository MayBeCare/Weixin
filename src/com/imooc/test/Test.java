package com.imooc.test;

public class Test {
	
	public static void main(String[] args) {
		String time1 = "2017��12��08";
		String time2 = "2018��01��01";
		int time = time1.compareTo(time2);
		System.out.println(time);
		if(time <= 0){
			System.out.println("��Ч");
		}
		
		
	  System.out.println(Test.GetDistance(39.92982,116.28769,39.90793,116.45304));  
	}
	
	
    private static double EARTH_RADIUS = 6371.393;  
    private static double rad(double d)  
    {  
       return d * Math.PI / 180.0;  
    }  
  
    /** 
     * ����������γ��֮��ľ��� 
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
