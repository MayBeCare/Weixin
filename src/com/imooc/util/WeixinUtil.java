package com.imooc.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.imooc.entity.AccessToken;
import com.imooc.menu.Button;
import com.imooc.menu.ClickButton;
import com.imooc.menu.Menu;
import com.imooc.menu.ViewButton;
import com.imooc.trans.Trans;
import com.imooc.trans.TransApi;
import com.imooc.trans.TransResult;


/**
 * 微信工具类
 * @author Stephen
 *
 */
public class WeixinUtil {
//	private static final String APPID = "wx811a35ce636e0234";
//	private static final String APPSECRET = "71ecefb123d08f7097dea02cf2bd05a3";
	
	public static final String APPID = "wx0fd51bf2fc843efb";
	public static final String APPSECRET = "bb1fd7bba658544573040dc852c96c63";
	
	private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	
	private static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	private static final String QUERY_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	
	private static final String DELETE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	
	private static final String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";
	
	
	//外网映射域名
	public static final String DOMAIN_NAME = "http://wx.com.tunnel.qydev.com/Weixin/";
		
	//根据openID群发
	public static final String Mass_Image_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
	
	//上传图文消息内的图片获取URL
	public static final String Upload_Mass_Image = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token=ACCESS_TOKEN";
	
	/*
	 * 百度翻译
	 */
	private static final String APP_ID = "20171112000094618";
    private static final String SECURITY_KEY = "HdCwLuplNj8xnavViq4w";

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
	 * POST请求
	 * @param url
	 * @param outStr
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url,String outStr) throws ParseException, IOException{
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(url);
		JSONObject jsonObject = null;
		httpost.setEntity(new StringEntity(outStr,"UTF-8"));    //提交的数据
		HttpResponse response = client.execute(httpost);
		String result = EntityUtils.toString(response.getEntity(),"UTF-8");
		jsonObject = JSONObject.fromObject(result);
		return jsonObject;
	}
	
	/**
	 * 文件上传
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 */
	public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}

		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
//		String url = Upload_Mass_Image.replace("ACCESS_TOKEN", accessToken);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST"); 
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);       //忽略缓存

		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		//设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);

		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		JSONObject jsonObj = JSONObject.fromObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
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
	 * 组装菜单
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static Menu initMenu() throws UnsupportedEncodingException{
		Menu menu = new Menu();
		ClickButton button11 = new ClickButton();
		button11.setName("click菜单");
		button11.setType("click");
		button11.setKey("11");
		
		ViewButton button21 = new ViewButton();
		button21.setName("view菜单");
		button21.setType("view");
//		button21.setUrl("http://www.imooc.com");
//		button21.setUrl("http://wx.com.ngrok.xiaomiqiu.cn/Weixin/wxjs_sdk");
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
                + "appid=" + APPID
                + "&redirect_uri=" + URLEncoder.encode(DOMAIN_NAME+"showLogin","utf-8") 
                + "&response_type=code"
                + "&scope=snsapi_base"
                + "&state=STATE"
                + "#wechat_redirect";
		button21.setUrl(url);
		
		ClickButton button31 = new ClickButton();
		button31.setName("扫码事件");
		button31.setType("scancode_push");
		button31.setKey("31");
		
		ClickButton button32 = new ClickButton();
		button32.setName("地理位置");
		button32.setType("location_select");
		button32.setKey("32");
		
		Button button = new Button();
		button.setName("菜单");
		button.setSub_button(new Button[]{button31,button32});
		
		menu.setButton(new Button[]{button11,button21,button});
		return menu;
	}
	
	public static int createMenu(String token,String menu) throws ParseException, IOException{
		int result = 0;
		String url = CREATE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doPostStr(url, menu);
		if(jsonObject != null){
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	public static JSONObject queryMenu(String token) throws ParseException, IOException {
		String url = QUERY_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
	}

	public static int deleteMenu(String token) throws ParseException, IOException {
		String url = DELETE_MENU_URL.replace("ACCESS_TOKEN", token);
		JSONObject jsonObject = doGetStr(url);
		int result = 0;
		if (jsonObject != null) {
			result = jsonObject.getInt("errcode");
		}
		return result;
	}
	
	public static JSONObject getUserInfo(String token,String openId) throws ParseException, IOException{
		String url = USER_INFO.replace("ACCESS_TOKEN", token).replace("OPENID", openId);
		JSONObject jsonObject = doGetStr(url);
		return jsonObject;
		
	}
	
	public static String translate(String query) {
		
        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        String result =  api.getTransResult(query, "auto", "auto");
        JSONObject json = JSONObject.fromObject(result);
        Trans trans = (Trans) JSONObject.toBean(json, Trans.class);
        TransResult transResult = trans.getTrans_result()[0];
        String dst = transResult.getDst();
        System.out.println(dst);
		return dst;
	}
	
	
	/**
	 *  根据code获取用户的openid
	 * @param code
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	public static String getOpenId(String code) throws ParseException, IOException{
		
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                + "appid=" + APPID
                + "&secret=" + APPSECRET
                + "&code=" + code
                + "&grant_type=authorization_code";
		
		JSONObject jsonObject = WeixinUtil.doGetStr(url);
		
		String openId = jsonObject.getString("openid");
		
		return openId;
	}
	
	
	/**
	 * 根据openID群发
	 * @param token
	 * @param outStr
	 * @return
	 */
	public static JSONObject sendMass(String token, String outStr) {
		
		String url = Mass_Image_URL.replace("ACCESS_TOKEN", token);
		
		JSONObject jsonObject = doPostMass(url,outStr);
		
		return jsonObject;
		
	}
	
	/**
	 * 群发post请求
	 * @param url
	 * @param outStr
	 * @return
	 */
	public static JSONObject doPostMass(String url, String outStr) {
		
		JSONObject jsonObject = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		
		try {
			httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "UTF-8");
			jsonObject = JSONObject.fromObject(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
}
