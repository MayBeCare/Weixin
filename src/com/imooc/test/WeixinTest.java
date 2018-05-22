package com.imooc.test;

import com.imooc.entity.AccessToken;
import com.imooc.util.MessageUtil;
import com.imooc.util.WeixinUtil;

import net.sf.json.JSONObject;

public class WeixinTest {
	public static void main(String[] args) {
		try {
			AccessToken token = WeixinUtil.getAccessToken();
			System.out.println("票据:"+token.getToken());
			System.out.println("有效时间:"+token.getExpiresIn());
//			
//			String path = "D:/touxiang.jpg";
//			String mediaId = WeixinUtil.upload(path, token.getToken(), "image");  //thumb
//			String mediaId = WeixinUtil.upload(path, token.getToken(), "thumb");  //thumb
//			System.out.println(mediaId);
//			
			String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
			int result = WeixinUtil.createMenu(token.getToken(), menu);
			if(result == 0){
				System.out.println("创建菜单成功");
			}else{
				System.out.println("错误码:"+result);
			}
			
//			JSONObject jsonObject = WeixinUtil.queryMenu(token.getToken());
//			System.out.println(jsonObject);
			
//			String result = WeixinUtil.translate("百度翻译");
//			System.out.println(result);
			
//			JSONObject jsonObject = WeixinUtil.getUserInfo(token.getToken(),"oNBaExOt67SzKoTQ0mkTwSwxcymo");
//			System.out.println(jsonObject);
			
			
//			String outStr = MessageUtil.initMassImage(mediaId);
//			JSONObject jsonObject = WeixinUtil.sendMass(token.getToken(), outStr);
//			System.out.println(jsonObject);
			
//			String outStr = MessageUtil.initMassText();
//			JSONObject jsonObject = WeixinUtil.sendMass(token.getToken(), outStr);
//			System.out.println(jsonObject);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
