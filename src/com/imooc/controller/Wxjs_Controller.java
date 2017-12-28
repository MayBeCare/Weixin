package com.imooc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.entity.Wxjs_Record;
import com.imooc.service.Wxjs_Service;

@Controller
public class Wxjs_Controller {
	
	@Autowired
    private Wxjs_Service wxjs_Service;
	
	@RequestMapping(value="/showLocation")
	@ResponseBody
	public String main(String latitude,String longitude){
		
		System.out.println(latitude  +"......"+longitude);
		
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
		
		model.addAttribute("id","13000100");
		model.addAttribute("recordList",map);
		return "/record";
		
	}

}
