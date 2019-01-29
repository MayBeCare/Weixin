package com.imooc.entity;


/**
 * Created by asus on 2017/10/18.
 */
public class Wxjs_Record {
	
    private String userId;
    private String clockDate;
    private String startTime;
    private String endTime;
	
    private String userName;
    
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getClockDate() {
		return clockDate;
	}
	public void setClockDate(String clockDate) {
		this.clockDate = clockDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
