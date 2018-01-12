package com.imooc.test;

import com.imooc.entity.AccessToken;
import com.imooc.util.WeixinUtil;

import net.sf.json.JSONObject;

public class WeixinTest {
	public static void main(String[] args) {
		try {
			AccessToken token = WeixinUtil.getAccessToken();
			System.out.println("Ʊ��:"+token.getToken());
			System.out.println("��Чʱ��:"+token.getExpiresIn());
			
//			String path = "D:/touxiang.jpg";
//			String mediaId = WeixinUtil.upload(path, token.getToken(), "image");  //thumb
//			String mediaId = WeixinUtil.upload(path, token.getToken(), "thumb");  //thumb
//			System.out.println(mediaId);
//			
			String menu = JSONObject.fromObject(WeixinUtil.initMenu()).toString();
			int result = WeixinUtil.createMenu(token.getToken(), menu);
			if(result == 0){
				System.out.println("�����˵��ɹ�");
			}else{
				System.out.println("������:"+result);
			}
			
//			JSONObject jsonObject = WeixinUtil.queryMenu(token.getToken());
//			System.out.println(jsonObject);
			
//			String result = WeixinUtil.translate("�ٶȷ���");
//			System.out.println(result);
			
//			JSONObject jsonObject = WeixinUtil.getUserInfo(token.getToken(),"oNBaExOt67SzKoTQ0mkTwSwxcymo");
//			System.out.println(jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
