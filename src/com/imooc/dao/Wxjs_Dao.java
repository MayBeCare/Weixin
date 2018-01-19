package com.imooc.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.entity.Wxjs_Address;
import com.imooc.entity.Wxjs_Record;
import com.imooc.entity.Wxjs_Ticket;
import com.imooc.entity.Wxjs_User;

public interface Wxjs_Dao {
	
	List<Wxjs_Record> queryRecord(@Param("clockDate")String time,@Param("openId")String openId);
	
	Wxjs_Record queryByDate(@Param("nowDate")String nowDate,@Param("id")String id);
	
	int addNewRecord(Wxjs_Record newRecord);
	
	int updateRecord(Wxjs_Record updateRecord);
	
	List<Wxjs_Address> findAddress();
	
	Wxjs_User findByOpenId(String openId);
	
	int updateUser(Wxjs_User user);
	
	Wxjs_Ticket findTicket();
	
	int deleteTicket(String id);
	
	int addNewTicket(Wxjs_Ticket ticket);

}
