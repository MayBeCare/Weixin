package com.imooc.dao;

import java.util.List;

import com.imooc.entity.Wxjs_Address;
import com.imooc.entity.Wxjs_Record;

public interface Wxjs_Dao {
	
	List<Wxjs_Record> queryAll(String k);
	
	Wxjs_Record queryByDate(String nowDate);
	
	int addNewRecord(Wxjs_Record newRecord);
	
	int updateRecord(Wxjs_Record updateRecord);
	
	List<Wxjs_Address> findAddress();

}
