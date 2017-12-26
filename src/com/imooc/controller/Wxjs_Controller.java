package com.imooc.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
        	/*获取当前时间*/
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
		
		if(month <= 0){
			int lastyeah = year - 1;
		}
		
		List<Wxjs_Record> Wxjs_List =  wxjs_Service.getRecordList();
		model.addAttribute("id","13000100");
		model.addAttribute("recordList",Wxjs_List);
		return "/record";
		
	}

}
