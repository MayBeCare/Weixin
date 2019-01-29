package com.imooc.controller;

import java.io.IOException;
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
import javax.servlet.http.HttpServletRequest;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.imooc.entity.Wxjs_Address;
import com.imooc.entity.Wxjs_Record;
import com.imooc.entity.Wxjs_User;
import com.imooc.service.Wxjs_Service;
import com.imooc.servlet.WeiXinJs_SDKUtil;
import com.imooc.util.WeixinUtil;


@Controller
public class Wxjs_Controller {
	
	private final static Logger logger = LoggerFactory.getLogger(Wxjs_Controller.class);
	
	@Autowired
    private Wxjs_Service wxjs_Service;
	
	/**
	 * 进行打卡
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	@RequestMapping(value="/showLocation")
	@ResponseBody
	public String main(String latitude,String longitude,String openId){
		
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

		Wxjs_User user = wxjs_Service.findByOpenId(openId);
//		String id = "13000100";
		
		/*获取当前打卡时间*/
		Date date=new Date();  
        DateFormat format=new SimpleDateFormat("yyyy年MM月dd日");  
        String nowDate = format.format(date);
        
        Wxjs_Record newStartRecord = null;
        Wxjs_Record newEndRecord = null;
        
        Wxjs_Record wxjs_Record = wxjs_Service.getRecordByDate(nowDate,user.getUserCode());   
        
        if(wxjs_Record == null){                         //当前未打卡
        	newStartRecord = new Wxjs_Record();
        	newStartRecord.setUserId(user.getUserCode());
        	newStartRecord.setClockDate(nowDate);
        	/*获取当前打卡具体时间*/
        	DateFormat startTime=new SimpleDateFormat("HH:mm:ss");  
            String nowStartTime = startTime.format(date);
            
            newStartRecord.setStartTime(nowStartTime);
            newStartRecord.setEndTime(nowStartTime);
        	
        	wxjs_Service.addNewRecord(newStartRecord);
        }else{                                               //当前已打卡，则更新终止打卡时间
        	newEndRecord = new Wxjs_Record();
        	newEndRecord.setUserId(user.getUserCode());
        	newEndRecord.setClockDate(nowDate);
        	/*获取当前时间*/
        	DateFormat endTime=new SimpleDateFormat("HH:mm:ss");  
            String nowEndTime = endTime.format(date);
            newEndRecord.setEndTime(nowEndTime);
            wxjs_Service.updateRecord(newEndRecord);
        }
		
		return "1";
		

	}
	
	/**
	 * 查看打卡记录
	 * @param model
	 * @return
	 */

	@RequestMapping("/findRecord")
	@SuppressWarnings("unchecked")
	public String findRecord(Model model,String openId) throws IOException{
		
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
			}else{
				key = year +"年"+lastmonth+"月";
			}
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
			}else{
				key = year +"年"+beforemonth+"月";
			}
		}
		list.add(key);
		
		List<Wxjs_Record> Wxjs_List = null;
		
		Map<String,Object> map = new HashMap<String,Object>();
		
		for (String time : list) {
			Wxjs_List =  wxjs_Service.getRecordList(time,openId);
			if(Wxjs_List.size()>0){
				map.put(time, Wxjs_List);
			}
		}
		
		Map<String, Object> resultMap = sortMapByKey(map);    //按Key进行排序
		
		String userName = "";
		if(resultMap != null){
			
			List<Wxjs_Record> recordList = (List<Wxjs_Record>) getRandomValueFromMap(resultMap);
			
			userName = recordList.get(0).getUserName();    //用户姓名
		}
		
		model.addAttribute("userName",userName);
		model.addAttribute("recordList",resultMap);
		return "/record";
	
	}
	
	/** 
     * 从map中随机取得一个value 
     * @param map 
     * @return 
     */  
    public static <K, V> V getRandomValueFromMap(Map<K, V> map) {  
        int rn = (int) (Math.random()*map.size());  
        int i = 0;  
        for (V value : map.values()) {  
            if(i==rn){  
                return value;  
            }  
            i++;  
        }  
        return null;  
    } 
    
	
	
	
	/**
	 * 判断用户和微信是否已绑定
	 * @param request
	 * @param model
	 * @throws ParseException
	 * @throws IOException
	 */
	@RequestMapping("/showLogin")
	public String showLogin(HttpServletRequest request,Model model,RedirectAttributes attr) throws ParseException, IOException{
		
		String code = request.getParameter("code");
		
		String openId = WeixinUtil.getOpenId(code);
		
//		String openId = "oNBaExOt67SzKoTQ0mkTwSwxcymo";
		
		Wxjs_User user  = wxjs_Service.findByOpenId(openId);
		if(user == null){
			model.addAttribute("openId",openId);
			return "/login";             //到页面
		}
		
//		attr.addAttribute("openId", openId);   //传值到另一个，相当于get请求用?拼接
//		return "redirect:/wxjs_sdk";     //跳转到方法
		
		String re_url = WeixinUtil.DOMAIN_NAME + "showLogin?code="+code+"&state=STATE";
		String timestamp = String.valueOf(System.currentTimeMillis()/1000); // 必填，生成签名的时间戳,时间戳(timestamp)值要记住精确到秒，不是毫秒。
		String nonceStr= WeiXinJs_SDKUtil.Random(16); // 必填，生成签名的随机串
		String ticket = WeiXinJs_SDKUtil.getJsTicket();
		String string1 = "jsapi_ticket="+ticket+
				         "&noncestr="+nonceStr+
				         "&timestamp="+timestamp+
				         "&url="+re_url;
		
//		System.out.println(ticket +"....."+nonceStr +"......"+timestamp+"...."+url);
		
//		System.out.println(string1);
		
		String signature = WeiXinJs_SDKUtil.getSha1(string1);
		
		model.addAttribute("appId", WeiXinJs_SDKUtil.APPID);
		model.addAttribute("time", timestamp);
		model.addAttribute("nonceStr", nonceStr);
		model.addAttribute("signature", signature);
		model.addAttribute("openId", openId);
		
		return "/index";
	}
		
	
	
	
	/**
	 * 用户登录
	 * @param user
	 * @return
	 */
	@RequestMapping("/userLogin")
	@ResponseBody
	public String userLogin(Wxjs_User user){
		int a = wxjs_Service.updateUser(user);
		return String.valueOf(a);
		
	}
	
	/*
	 * 登录成功跳转
	 */
	@RequestMapping("/wxjs_login")
	public String login(HttpServletRequest request,Model model) throws ParseException, IOException{
		
		String openId = request.getParameter("openId");
		
		String log_url = WeixinUtil.DOMAIN_NAME + "wxjs_login?openId="+openId;
		
		String timestamp = String.valueOf(System.currentTimeMillis()/1000); // 必填，生成签名的时间戳,时间戳(timestamp)值要记住精确到秒，不是毫秒。
		String nonceStr= WeiXinJs_SDKUtil.Random(16); // 必填，生成签名的随机串
		String ticket = WeiXinJs_SDKUtil.getJsTicket();
		String string1 = "jsapi_ticket="+ticket+
				         "&noncestr="+nonceStr+
				         "&timestamp="+timestamp+
				         "&url="+log_url;
		
//		System.out.println(ticket +"....."+nonceStr +"......"+timestamp+"...."+url);
		
//		System.out.println(string1);
		
		String signature = WeiXinJs_SDKUtil.getSha1(string1);
		
		model.addAttribute("appId", WeiXinJs_SDKUtil.APPID);
		model.addAttribute("time", timestamp);
		model.addAttribute("nonceStr", nonceStr);
		model.addAttribute("signature", signature);
		model.addAttribute("openId", openId);
		
		return "/index";
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

        returnMap.putAll(map);

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





