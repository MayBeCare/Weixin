package com.imooc.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.dao.Wxjs_Dao;
import com.imooc.entity.Wxjs_Address;
import com.imooc.entity.Wxjs_Record;
import com.imooc.service.Wxjs_Service;

@Service("Wxjs_Service")
public class Wxjs_ServiceImpl implements Wxjs_Service{
	
	@Autowired 
    private Wxjs_Dao wxjs_Dao;

	public List<Wxjs_Record> getRecordList(String k) {
		
		return wxjs_Dao.queryAll(k);
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

}
