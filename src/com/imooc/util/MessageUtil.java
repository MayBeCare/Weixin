package com.imooc.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.entity.Image;
import com.imooc.entity.ImageMessage;
import com.imooc.entity.MassImage;
import com.imooc.entity.MassText;
import com.imooc.entity.Music;
import com.imooc.entity.MusicMessage;
import com.imooc.entity.News;
import com.imooc.entity.NewsMessage;
import com.imooc.entity.TextMessage;
import com.imooc.entity.Voice;
import com.imooc.entity.VoiceMessage;
import com.thoughtworks.xstream.XStream;

public class MessageUtil {
	
	public static final String MSGTYPE_TEXT = "text";
	public static final String MSGTYPE_NEWS = "news";
	public static final String MSGTYPE_IMAGE = "image";
	public static final String MSGTYPE_VOICE = "voice";
	public static final String MSGTYPE_MUSIC = "music";
	public static final String MSGTYPE_LOCATION = "location";
	public static final String MSGTYPE_LINK = "link";
	public static final String MSGTYPE_EVENT = "event";
	public static final String EVENT_SUBSCRIBE = "subscribe";
	public static final String EVENT_SCAN = "SCAN";
	public static final String EVENT_LOCATION = "location_select";
	public static final String EVENT_CLICK = "CLICK";
	public static final String EVENT_VIEW = "VIEW";
    public static final String EVENT_SCANCODE_PUSH = "scancode_push";
	
	/**
     * xml转map
     * @param req
     * @return
     * @throws IOException
     * @throws DocumentException
     */
    @SuppressWarnings("unchecked")
	public static Map<String,String> xmlToMap(HttpServletRequest req) throws IOException, DocumentException {
        HashMap<String, String> map = new HashMap<String, String>();
        SAXReader saxReader = new SAXReader();
        ServletInputStream inputStream = req.getInputStream();        //获取输入流
        Document document = saxReader.read(inputStream);

        Element rootElement = document.getRootElement();     //获取xml根元素

        List <Element> elements = rootElement.elements();

        for (Element el : elements){
            map.put(el.getName(),el.getText());
        }
        inputStream.close();
        return map;
    }
    
    /**
     * 文本消息对象转换为xml
     * @param textMessage
     * @return
     */
    public static String textMessageToXml (TextMessage textMessage){
        XStream xStream = new XStream();
        xStream.alias("xml",textMessage.getClass());    //将根节点转换为xml  <com.imooc.entity.TextMessage>
        return xStream.toXML(textMessage);
    }
    
    /**
	 * 组装文本消息
	 * @param toUser
	 * @param fromUser
	 * @param content
	 * @return
	 */
	public static String initText(String toUser,String fromUser,String content){
		TextMessage tm = new TextMessage();
		tm.setFromUserName(toUser);
		tm.setToUserName(fromUser);
		tm.setMsgType(MessageUtil.MSGTYPE_TEXT);
		tm.setCreateTime(new Date().getTime());
		tm.setContent(content);
		return MessageUtil.textMessageToXml(tm);
    }
    
    
    /**
     * 主菜单
     * @return
     */
    public static String mainMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎光临,客官里面请\n");
		sb.append("1、文本信息\n");
		sb.append("2、图文消息\n");
		sb.append("3、图片消息\n");
		sb.append("4、音乐消息\n");
		sb.append("5、翻译\n");
		sb.append("回复？显示此菜单");
		return sb.toString();
    }
    
    public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("你好!Hello World");
		return sb.toString();
    }
    
    public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎远方客人的到来...");
		return sb.toString();
    }
    
    public static String transMenu(){
    	StringBuffer sb = new StringBuffer();
		sb.append("词组翻译使用指南\n");
		sb.append("使用示例：\n");
		sb.append("翻译足球\n");
		sb.append("回复？显示主菜单");
		return sb.toString();
    }
    
    
    
    /**
	 * 图文消息转为xml
	 * @param newsMessage
	 * @return
	 */
	public static String newsMessageToXml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new News().getClass());
		return xstream.toXML(newsMessage);
	}
	
	/**
	 * 图文消息的组装
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initNewsMessage(String toUserName,String fromUserName){
		String message = null;
		List<News> newsList = new ArrayList<News>();
		NewsMessage newsMessage = new NewsMessage();
		
		News news = new News();
		news.setTitle("慕课网介绍");
		news.setDescription("慕课网是垂直的互联网IT技能免费学习网站。以独家视频教程、在线编程工具、学习计划、问答社区为核心特色。在这里，你可以找到最好的互联网技术牛人，也可以通过免费的在线公开视频课程学习国内领先的互联网IT技术。慕课网课程涵盖前端开发、PHP、Html5、Android、iOS、Swift等IT前沿技术语言，包括基础课程、实用案例、高级分享三大类型，适合不同阶段的学习人群。");
		news.setPicUrl("http://wx.com.ngrok.xiaomiqiu.cn/Weixin/image/imooc.jpg");       //图片链接
		news.setUrl("www.imooc.com");              //点击图文消息跳转链接
		 
		newsList.add(news);
		
		newsMessage.setToUserName(fromUserName);
		newsMessage.setFromUserName(toUserName);
		newsMessage.setCreateTime(new Date().getTime());
		newsMessage.setMsgType(MSGTYPE_NEWS);
		newsMessage.setArticles(newsList);
		newsMessage.setArticleCount(newsList.size());
		
		message = newsMessageToXml(newsMessage);
		
		return message;
	}
	
	/**
	 * 图片消息转为xml
	 * @param imageMessage
	 * @return
	 */
	public static String imageMessageToXml(ImageMessage imageMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	
	/**
	 * 组装图片消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initImageMessage(String toUserName,String fromUserName){
		String message = null;
		Image image = new Image();
		image.setMediaId("E9rQquO1wM7Jo44ItmZ_S6PPy8Ujwbf-vYVyFXhvbqA36X7EDj35TY2U0UyW61e4");
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(MSGTYPE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		message = imageMessageToXml(imageMessage);
		return message;
	}
	/**
	 * 回复图片消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String replyImageMessage(String toUserName,String fromUserName,String mediaId){
		String message = null;
		Image image = new Image();
		image.setMediaId(mediaId);
		ImageMessage imageMessage = new ImageMessage();
		imageMessage.setFromUserName(toUserName);
		imageMessage.setToUserName(fromUserName);
		imageMessage.setMsgType(MSGTYPE_IMAGE);
		imageMessage.setCreateTime(new Date().getTime());
		imageMessage.setImage(image);
		message = imageMessageToXml(imageMessage);
		return message;
	}
	
	/**
	 * 语音消息转为xml
	 * @param imageMessage
	 * @return
	 */
	public static String voiceMessageToXml(VoiceMessage voiceMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", voiceMessage.getClass());
		return xstream.toXML(voiceMessage);
	}
	
	/**
	 * 回复语音消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String replyVoiceMessage(String toUserName,String fromUserName,String mediaId){
		String message = null;
		Voice voice = new Voice();
		voice.setMediaId(mediaId);
		VoiceMessage voiceMessage = new VoiceMessage();
		voiceMessage.setFromUserName(toUserName);
		voiceMessage.setToUserName(fromUserName);
		voiceMessage.setMsgType(MSGTYPE_VOICE);
		voiceMessage.setCreateTime(new Date().getTime());
		voiceMessage.setVoice(voice);
		message = voiceMessageToXml(voiceMessage);
		return message;
	}
	
	/**
	 * 音乐消息转为xml
	 * @param musicMessage
	 * @return
	 */
	public static String musicMessageToXml(MusicMessage musicMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", musicMessage.getClass());
		return xstream.toXML(musicMessage);
	}
	
	/**
	 * 组装音乐消息
	 * @param toUserName
	 * @param fromUserName
	 * @return
	 */
	public static String initMusicMessage(String toUserName,String fromUserName){
		String message = null;
		Music music = new Music();
		music.setThumbMediaId("oeVOCzVQwF0ChgtJYRsKqX0wcyP44Q6S98oWhN7TMCr9jL0GbEvXekF1MjNcJ6Hl");
		music.setTitle("see you again");
		music.setDescription("速7片尾曲");
		music.setMusicUrl("http://wx.com.ngrok.xiaomiqiu.cn/Weixin/resource/See You Again.mp3");
		music.setHQMusicUrl("http://wx.com.ngrok.xiaomiqiu.cn/Weixin/resource/See You Again.mp3");
		
		MusicMessage musicMessage = new MusicMessage();
		musicMessage.setFromUserName(toUserName);
		musicMessage.setToUserName(fromUserName);
		musicMessage.setMsgType(MSGTYPE_MUSIC);
		musicMessage.setCreateTime(new Date().getTime());
		musicMessage.setMusic(music);
		message = musicMessageToXml(musicMessage);
		return message;
	}
	
	/**
	 * 组装群发图片消息
	 * @return
	 */
	public static String initMassImage(String mediaId) {
		MassImage massImage = new MassImage();
		List<String> list = new ArrayList<String>();
		list.add("oNBaExOt67SzKoTQ0mkTwSwxcymo");
		list.add("oNBaExPvHIa8wq2juVah97EGdiMk");
		massImage.setTouser(list);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("media_id", mediaId);
		massImage.setImage(map);
		
		massImage.setMsgtype("image");
		
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String str = gson.toJson(massImage);
		System.out.println("str:" + str);
		return str;

	}
	
	/**
	 * 组装群发文本消息
	 * @return
	 */
	public static String initMassText() {
		MassText massText = new MassText();
		List<String> list = new ArrayList<String>();
		list.add("oNBaExOt67SzKoTQ0mkTwSwxcymo");
		list.add("oNBaExPvHIa8wq2juVah97EGdiMk");
		massText.setTouser(list);
		
		massText.setMsgtype("text");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("content", "Hello,<a href='https://www.baidu.com'>点我去百度了</a>");
		massText.setText(map);

		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String str = gson.toJson(massText);
		System.out.println("str:" + str);
		return str;

	}

}
