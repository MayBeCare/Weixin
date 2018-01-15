package com.imooc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.dao.Wxjs_Dao;
import com.imooc.entity.Wxjs_Address;
import com.imooc.entity.Wxjs_Record;
import com.imooc.entity.Wxjs_Ticket;
import com.imooc.entity.Wxjs_User;
import com.imooc.service.Wxjs_Service;

@Service("Wxjs_Service")
public class Wxjs_ServiceImpl implements Wxjs_Service{
	
	@Autowired 
    private Wxjs_Dao wxjs_Dao;

	public List<Wxjs_Record> getRecordList(String time,String openId) {
		return wxjs_Dao.queryAll(time,openId);
	}

	public Wxjs_Record getRecordByDate(String nowDate) {
		return wxjs_Dao.queryByDate(nowDate);
	}

	public int addNewRecord(Wxjs_Record newRecord) {
		return wxjs_Dao.addNewRecord(newRecord);
	}

	public int updateRecord(Wxjs_Record updateRecord) {
		return wxjs_Dao.updateRecord(updateRecord);
	}

	public List<Wxjs_Address> findCardAddress() {
		return wxjs_Dao.findAddress();
	}

	public Wxjs_User findByOpenId(String openId) {
		return wxjs_Dao.findByOpenId(openId);
	}

	public int updateUser(Wxjs_User user) {
		return wxjs_Dao.updateUser(user);
	}

	public Wxjs_Ticket findTicket() {
		return wxjs_Dao.findTicket();
	}

	public int deleteTicket(String id) {
		return wxjs_Dao.deleteTicket(id);
	}

	public int addNewTicket(Wxjs_Ticket ticket) {
		return wxjs_Dao.addNewTicket(ticket);
	}

}
