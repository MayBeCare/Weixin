package com.imooc.service;

import java.util.List;

import com.imooc.entity.Wxjs_Address;
import com.imooc.entity.Wxjs_Record;
import com.imooc.entity.Wxjs_Ticket;
import com.imooc.entity.Wxjs_User;

public interface Wxjs_Service {
	
	List<Wxjs_Record> getRecordList(String time,String openId);
	
	Wxjs_Record getRecordByDate(String nowDate,String id);
	
	int addNewRecord(Wxjs_Record newRecord);
	
	int updateRecord(Wxjs_Record updateRecord);
	
	List<Wxjs_Address> findCardAddress();
	
	Wxjs_User findByOpenId(String openId);
	
	int updateUser(Wxjs_User user);
	
	Wxjs_Ticket findTicket();
	
	int deleteTicket(String id);
	
	int addNewTicket(Wxjs_Ticket ticket);

}
