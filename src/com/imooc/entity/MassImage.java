package com.imooc.entity;

import java.util.List;
import java.util.Map;

/**
 * 根据openId群发图片消息
 * @author asus
 *
 */
public class MassImage {
	
	private List<String> touser;
	private Map<String,Object> image;
	private String msgtype;
	
	public List<String> getTouser() {
		return touser;
	}
	public void setTouser(List<String> touser) {
		this.touser = touser;
	}
	public Map<String, Object> getImage() {
		return image;
	}
	public void setImage(Map<String, Object> image) {
		this.image = image;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	
	
	

}
