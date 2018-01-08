package com.imooc.controller;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.entity.Wxjs_Address;
import com.imooc.entity.Wxjs_Record;
import com.imooc.service.Wxjs_Service;

@Controller
public class Wxjs_Controller {
	
	private final static Logger logger = LoggerFactory.getLogger(Wxjs_Controller.class);
	
	@Autowired
    private Wxjs_Service wxjs_Service;
	
	@RequestMapping(value="/showLocation")
	@ResponseBody
	public String main(String latitude,String longitude){
		
//		latitude = "39.906647";
//		longitude = "116.447";
		
		System.out.println("...纬度..."+latitude  +"...经度..."+longitude);
		
		List<Object> cardList = new ArrayList<Object>();
		
		List<Wxjs_Address> addressList = wxjs_Service.findCardAddress();
		for (Wxjs_Address address : addressList) {
			double s = GetDistance(latitude,longitude,address.getLatitude(),address.getLongitude());
//			double s = getDistance(Double.valueOf(latitude),Double.valueOf(longitude),Double.valueOf(address.getLatitude()),Double.valueOf(address.getLongitude()));
//			System.out.println(address.getName()+"=======>>>>>>>"+s);
			logger.info("{}=======>>>>>>{}", address.getName(),s);
			if(s <= 0.5){
				cardList.add("1");	
			}else{
				cardList.add("0");	
			}
		}
		
		if(!cardList.contains("1")){
			return "0";
		}
		
		//先计算查询点的经纬度范围  
        /*double r = 6371;//地球半径千米  
        double dis = 0.5;//0.5千米距离  
        double dlng =  2*Math.asin(Math.sin(dis/(2*r))/Math.cos(Double.valueOf(latitude)*Math.PI/180));  
        dlng = dlng*180/Math.PI;//角度转为弧度  
        double dlat = dis/r;  
        dlat = dlat*180/Math.PI;          
        double minlat = Double.valueOf(latitude)-dlat;  
        double maxlat = Double.valueOf(latitude)+dlat;  
        double minlng = Double.valueOf(longitude) -dlng;  
        double maxlng = Double.valueOf(longitude) + dlng;  
        
        System.out.println(minlng+","+maxlng+","+minlat+","+maxlat);*/
		
		
		String id = "13000100";
		
//		System.out.println(Wxjs_List);
		
		/*获取当前打卡时间*/
		Date date=new Date();  
        DateFormat format=new SimpleDateFormat("yyyy年MM月dd日");  
        String nowDate = format.format(date);
        
        Wxjs_Record newStartRecord = null;
        Wxjs_Record newEndRecord = null;
        
        Wxjs_Record wxjs_Record = wxjs_Service.getRecordByDate(nowDate);   
//        System.out.println(wxjs_Record);
        if(wxjs_Record == null){                         //当前未打卡
        	newStartRecord = new Wxjs_Record();
        	newStartRecord.setId(id);
        	newStartRecord.setClockDate(nowDate);
        	/*获取当前打卡具体时间*/
        	DateFormat startTime=new SimpleDateFormat("HH:mm:ss");  
            String nowStartTime = startTime.format(date);
            
            newStartRecord.setStartTime(nowStartTime);
            newStartRecord.setEndTime(nowStartTime);
        	
        	wxjs_Service.addNewRecord(newStartRecord);
        }else{                                               //当前已打卡，则更新终止打卡时间
        	newEndRecord = new Wxjs_Record();
        	newEndRecord.setClockDate(nowDate);
        	/*获取当前时间*/
        	DateFormat endTime=new SimpleDateFormat("HH:mm:ss");  
            String nowEndTime = endTime.format(date);
            newEndRecord.setEndTime(nowEndTime);
            wxjs_Service.updateRecord(newEndRecord);
        }
        
//        Wxjs_Record wxjs_Record1 = wxjs_Service.getRecordByDate(nowDate);
//        System.out.println(wxjs_Record1);
        
		
		return "1";
		

	}
	
	
	@RequestMapping("/findRecord")
	public String findRecord(Model model){
		
		Calendar c = Calendar.getInstance();//可以对每个时间域单独修改   
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);   //0-11表示
		
		//当前月为month+1
		int nowmonth = month + 1;
		
		List<String> list = new ArrayList<String>();
		String key = "";
		if(nowmonth < 10){
			key = year +"年0"+nowmonth+"月";
		}else{
			key = year +"年"+nowmonth+"月";
		}
		list.add(key);
		//上一月
		int lastmonth = nowmonth - 1;
		
		if(lastmonth == 0){
			year = year - 1;
			lastmonth = 12;
			key = year +"年"+lastmonth+"月";
		}else{
			if(lastmonth < 10){
				key = year +"年0"+lastmonth+"月";
			}
			key = year +"年"+lastmonth+"月";
		}
		list.add(key);
		
		//上上月
		int beforemonth = lastmonth - 1;
		if(beforemonth <= 0){
			year = year - 1;
			beforemonth = 12;
			key = year +"年"+beforemonth+"月";
		}else{
			if(beforemonth < 10){
				key = year +"年0"+beforemonth+"月";
			}
			key = year +"年"+beforemonth+"月";
		}
		list.add(key);
		
		List<Wxjs_Record> Wxjs_List = null;
		Map<String,Object> map = new HashMap<String,Object>();
		for (String k : list) {
			Wxjs_List =  wxjs_Service.getRecordList(k);
			if(Wxjs_List.size()>0){
				map.put(k, Wxjs_List);
			}
		}
		
		Map<String, Object> resultMap = sortMapByKey(map);    //按Key进行排序
		
		model.addAttribute("id","13000100");
		model.addAttribute("recordList",resultMap);
		return "/record";
		
	}
	
	/**
	 * map按照key排序
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, Object> returnMap  = new TreeMap<String, Object>(new MapKeyComparator());

        returnMap .putAll(map);

        return returnMap;
    }
	
	
	//比较器类  
	public static class MapKeyComparator implements Comparator<String>{

	    public int compare(String str1, String str2) {
	    	//降序排序
	        return str2.compareTo(str1);
	        //升序
            //return str1.compareTo(str2);
	    }
	}
	
	/**
	 * 1. Lng1 Lat1  表示A点经纬度，Lng2 Lat2  表示B点经纬度；
	   2. a=Lat1 – Lat2 为两点纬度之差  b=Lng1 -Lng2 为两点经度之差；
	   3. 6378.137为地球半径，单位为千米；
		计算出来的结果单位为千米。
	 */
	public static double GetDistance(String lat1, String lng1, String lat2, String lng2)  {  
		double EARTH_RADIUS = 6378.137;  
	    double radLat1 = rad(lat1);  
	    double radLat2 = rad(lat2);  
	    double a = radLat1 - radLat2;  
	    double b = rad(lng1) - rad(lng2);  
	    double s = 2 * Math.asin(
	    		            Math.sqrt(
	    		                 Math.pow(Math.sin(a/2),2) +   
	                                  Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	    s = s * EARTH_RADIUS; 
//	    logger.info("相差公里数为<<<<<<>>>>>>>>>{}公里", s);
//	    s = Math.round(s * 10000)/10000; 
//	    s = s * 1000;     //转化成米
//	    logger.info("相差米数为<<<<<<>>>>>>>>>{}米", s);
	    //保留两位(不四舍五入)
	    DecimalFormat formater = new DecimalFormat("#0.##");
		formater.setRoundingMode(RoundingMode.FLOOR);
		s = Double.valueOf(formater.format(s));

	    return s; 
	    
	}  
	
	//把经纬度转为度（°） 
	private static double rad(String d){  
	    return Double.valueOf(d) * Math.PI / 180;  
	}  
	
	
	public static double getDistance( double lat1,double longt1, double lat2, double longt2){
//		 final double PI = 0 ; //圆周率
	    final double R = 6378.137;              //地球的半径
	   
        double x,y,distance;
        
        x=(longt2-longt1)*Math.PI*R*Math.cos( ((lat1+lat2)/2) *Math.PI/180)/180;
        y=(lat2-lat1)*Math.PI*R/180;
        distance=Math.hypot(x,y);
        return distance;
	}
	
}





