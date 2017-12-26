package com.imooc.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import com.imooc.util.CheckUtil;
import com.imooc.util.MessageUtil;
import com.imooc.util.WeixinUtil;

@WebServlet("/wx")
public class WeixinServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			                                  throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		System.out.println("ceshi");
		PrintWriter out = resp.getWriter();
		if(CheckUtil.checkSignature(signature, timestamp, nonce)){
			out.print(echostr);
		}

	}
	
	
	/*
	 * 在确保开发模式打开的情况下，微信后台会把用户消息发到我们的服务器上，也就是我们微信后台配置的URL
	 * 微信后台发送消息是一个POST请求，URL会带上signature、timestamp、nonce这3个参数
	 * 标准的POST参数是从HTTP BODY中解析的
	 * 
	 * 在回复的XML中，把接收的ToUserName和FromUserName交换，
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
//		String qs = req.getQueryString();
//		System.out.println(qs);
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);
			String fromUserName = map.get("FromUserName");
			String toUserName = map.get("ToUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			String msgId = map.get("MsgId");
			String picUrl = map.get("PicUrl");
			String mediaId = map.get("MediaId");
			String format = map.get("Format");
			System.out.println(picUrl);
			System.out.println(mediaId);
			System.out.println(format);
			System.out.println("消息类型为======>>>>>>>>>>>"+msgType);
			System.out.println("消息内容为======>>>>>>>>>>>"+content);
			System.out.println("消息ID为======>>>>>>>>>>>"+msgId);
			String msg = null;
			if (MessageUtil.MSGTYPE_TEXT.equals(msgType)) {            //文本消息
				if("1".equals(content)){
					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.firstMenu());
				}else if("2".equals(content)){
					msg = MessageUtil.initNewsMessage(toUserName, fromUserName);
				}else if("3".equals(content)){
					msg = MessageUtil.initImageMessage(toUserName, fromUserName);
				}else if("4".equals(content)){
					msg = MessageUtil.initMusicMessage(toUserName, fromUserName);
				}else if("5".equals(content)){
					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transMenu());
				}else if(content.startsWith("翻译")){
					String word = content.replaceAll("^翻译", "").trim();
					if("".equals(word)){
						msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transMenu());
					}else{
						msg = MessageUtil.initText(toUserName, fromUserName, WeixinUtil.translate(word));
					}
				}else if("？".equals(content) || "?".equals(content)){
					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
				}else{
					msg = MessageUtil.initText(toUserName, fromUserName, "输入的指令无效哦(′⌒`)");
                }
				/*TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setMsgType("text");
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setContent("您发送的消息为:" + content);
				msg = MessageUtil.textMessageToXml(textMessage);*/
				
			}else if(MessageUtil.MSGTYPE_EVENT.equals(msgType)){   //如果是事件
				String eventType = map.get("Event");//事件类型
				if(MessageUtil.EVENT_SUBSCRIBE.equals(eventType)){     //关注
					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
                }else if(MessageUtil.EVENT_CLICK.equals(eventType)){   //点击事件
 					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
 				}else if(MessageUtil.EVENT_VIEW.equals(eventType)){   // 跳转URL用户点击view
					String url = map.get("EventKey");
					System.out.println("======<<<<<<<<<>>>>>>>>>" + url);
					msg = MessageUtil.initText(toUserName, fromUserName, url);
				}else if(MessageUtil.EVENT_SCANCODE_PUSH.equals(eventType)){ //扫码事件
					String key = map.get("EventKey");
					msg = MessageUtil.initText(toUserName, fromUserName, key);
				}
			}else if(MessageUtil.MSGTYPE_LOCATION.equals(msgType)){      //位置
				String label = map.get("Label");
				msg = MessageUtil.initText(toUserName, fromUserName,label);
			}else if(MessageUtil.MSGTYPE_IMAGE.equals(msgType)){    //图片消息
				String imageMediaId = map.get("MediaId");
				msg = MessageUtil.replyImageMessage(toUserName, fromUserName,imageMediaId);
			}else if(MessageUtil.MSGTYPE_VOICE.equals(msgType)){    //语音消息
				String voiceMediaId = map.get("MediaId");
				msg = MessageUtil.replyVoiceMessage(toUserName, fromUserName,voiceMediaId);
			}
			System.out.println(msg);
			writer.print(msg);            //返回给微信后台
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

}






