package com.imooc.servlet;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.imooc.entity.AccessToken;
import com.imooc.entity.Wxjs_Ticket;
import com.imooc.service.Wxjs_Service;

import net.sf.json.JSONObject;

/*
 * 此类,在静态方法中使用Service
 */
@Component
public class WeiXinJs_SDKUtil {

	public static final String APPID = "wx0fd51bf2fc843efb";
	private static final String APPSECRET = "bb1fd7bba658544573040dc852c96c63";
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static final String jsapi_ticket_url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	
	private final static Logger logger = LoggerFactory.getLogger(WeiXinJs_SDKUtil.class);
	
	@Autowired
    private Wxjs_Service wxjs_Service;
	
	private static WeiXinJs_SDKUtil weiXinJsUtil;
	
	 @PostConstruct  
     public void init() {           //静态方法里面使用注入的Service 
		 weiXinJsUtil = this; 
		 weiXinJsUtil.wxjs_Service = this.wxjs_Service; 
     } 

	
	/**
	 * 生成指定个数的随机数
	 * @param length
	 * @return
	 */
	public static String Random(int length){
		String val = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
		   // 输出字母还是数字
		   String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num"; 
		   // 字符串
		   if ("char".equalsIgnoreCase(charOrNum)) {
		    // 取得大写字母还是小写字母
		    int choice = random.nextInt(2) % 2 == 0 ? 65 : 97; 
		    val += (char) (choice + random.nextInt(26));
		    
		   } else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
		      val += String.valueOf(random.nextInt(10));
		   }
		 }
		  return val;
	 }
	
	/**
	 * get请求
	 * @param url
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		HttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = JSONObject.fromObject(result);
		}
		return jsonObject;
	  }
	
	/**
	 * 获取accessToken
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static AccessToken getAccessToken() throws ParseException, IOException{
		AccessToken token = new AccessToken();
		String url = ACCESS_TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if(jsonObject!=null){
			token.setToken(jsonObject.getString("access_token"));
			token.setExpiresIn(jsonObject.getInt("expires_in"));
		}
		return token;
	}
	
	/**
	 * 获取jsapi_ticket
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String getJsTicket() throws ParseException, IOException{
		//查询ticket
		Wxjs_Ticket wt = weiXinJsUtil.wxjs_Service.findTicket();
		
		if(wt != null){
			long nowTime = System.currentTimeMillis();
			
			if(nowTime - wt.getTime() < 7000*1000){          //判断是否已过期
				return wt.getTicket();	
			}
			weiXinJsUtil.wxjs_Service.deleteTicket(wt.getId());
		}
		
		String ticket = "";
		AccessToken token = getAccessToken();
		String url = jsapi_ticket_url.replace("ACCESS_TOKEN", token.getToken());
		JSONObject jsonObject = doGetStr(url);
	    
		if(jsonObject!=null){
			ticket = jsonObject.getString("ticket");
		}
		
		String id = Random(6);
		long creTime = System.currentTimeMillis();
		
		Wxjs_Ticket newTicket = new Wxjs_Ticket();
		newTicket.setId(id);
		newTicket.setTicket(ticket);
		newTicket.setTime(creTime);
		weiXinJsUtil.wxjs_Service.addNewTicket(newTicket);
		
		logger.info("执行了token=====>>>>>>>>>{}",ticket);
		
		return ticket;
	}
	
	/**
	 * sha1 加密
	 * @param str
	 * @return
	 */
	public static String getSha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		System.out.println(Random(16));
//		getJsTicket();
	}
	
  }





