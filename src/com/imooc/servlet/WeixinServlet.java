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
	 * ��ȷ������ģʽ�򿪵�����£�΢�ź�̨����û���Ϣ�������ǵķ������ϣ�Ҳ��������΢�ź�̨���õ�URL
	 * ΢�ź�̨������Ϣ��һ��POST����URL�����signature��timestamp��nonce��3������
	 * ��׼��POST�����Ǵ�HTTP BODY�н�����
	 * 
	 * �ڻظ���XML�У��ѽ��յ�ToUserName��FromUserName������
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
			System.out.println("��Ϣ����Ϊ======>>>>>>>>>>>"+msgType);
			System.out.println("��Ϣ����Ϊ======>>>>>>>>>>>"+content);
			System.out.println("��ϢIDΪ======>>>>>>>>>>>"+msgId);
			String msg = null;
			if (MessageUtil.MSGTYPE_TEXT.equals(msgType)) {            //�ı���Ϣ
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
				}else if(content.startsWith("����")){
					String word = content.replaceAll("^����", "").trim();
					if("".equals(word)){
						msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.transMenu());
					}else{
						msg = MessageUtil.initText(toUserName, fromUserName, WeixinUtil.translate(word));
					}
				}else if("��".equals(content) || "?".equals(content)){
					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
				}else{
					msg = MessageUtil.initText(toUserName, fromUserName, "�����ָ����ЧŶ(���`)");
                }
				/*TextMessage textMessage = new TextMessage();
				textMessage.setToUserName(fromUserName);
				textMessage.setFromUserName(toUserName);
				textMessage.setMsgType("text");
				textMessage.setCreateTime(new Date().getTime());
				textMessage.setContent("�����͵���ϢΪ:" + content);
				msg = MessageUtil.textMessageToXml(textMessage);*/
				
			}else if(MessageUtil.MSGTYPE_EVENT.equals(msgType)){   //������¼�
				String eventType = map.get("Event");//�¼�����
				if(MessageUtil.EVENT_SUBSCRIBE.equals(eventType)){     //��ע
					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
                }else if(MessageUtil.EVENT_CLICK.equals(eventType)){   //����¼�
 					msg = MessageUtil.initText(toUserName, fromUserName, MessageUtil.mainMenu());
 				}else if(MessageUtil.EVENT_VIEW.equals(eventType)){   // ��תURL�û����view
					String url = map.get("EventKey");
					System.out.println("======<<<<<<<<<>>>>>>>>>" + url);
					msg = MessageUtil.initText(toUserName, fromUserName, url);
				}else if(MessageUtil.EVENT_SCANCODE_PUSH.equals(eventType)){ //ɨ���¼�
					String key = map.get("EventKey");
					msg = MessageUtil.initText(toUserName, fromUserName, key);
				}
			}else if(MessageUtil.MSGTYPE_LOCATION.equals(msgType)){      //λ��
				String label = map.get("Label");
				msg = MessageUtil.initText(toUserName, fromUserName,label);
			}else if(MessageUtil.MSGTYPE_IMAGE.equals(msgType)){    //ͼƬ��Ϣ
				String imageMediaId = map.get("MediaId");
				msg = MessageUtil.replyImageMessage(toUserName, fromUserName,imageMediaId);
			}else if(MessageUtil.MSGTYPE_VOICE.equals(msgType)){    //������Ϣ
				String voiceMediaId = map.get("MediaId");
				msg = MessageUtil.replyVoiceMessage(toUserName, fromUserName,voiceMediaId);
			}
			System.out.println(msg);
			writer.print(msg);            //���ظ�΢�ź�̨
		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			writer.close();
		}
	}

}






