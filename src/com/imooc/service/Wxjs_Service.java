package com.imooc.service;

import java.util.List;

import com.imooc.entity.Wxjs_Record;

public interface Wxjs_Service {
	
	List<Wxjs_Record> getRecordList(String k);
	
	Wxjs_Record getRecordByDate(String nowDate);
	
	int addNewRecord(Wxjs_Record newRecord);
	
	int updateRecord(Wxjs_Record updateRecord);

}
