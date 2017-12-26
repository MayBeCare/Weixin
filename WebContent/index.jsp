<%@ page language="java" import="java.util.*" contentType = "text/html" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1.0,user-scalable=no" />
		<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
		<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.1.min.js"></script>
		<title>微信js_sdk</title>
	</head>
	<body>
	    <%  
	        String appId = (String)request.getAttribute("appId"); 
            String date = (String)request.getAttribute("time"); 
	        String nonceStr = (String)request.getAttribute("nonceStr"); 
	        String signature = (String)request.getAttribute("signature"); 
        %>
        <p>提交时的appId为：  
            <%=appId %>  
        </p> 
        <p>提交时的系统时间为：  
            <%=date %>  
        </p>
        <p>提交时的随机串为：  
            <%=nonceStr %>  
        </p>
        <p>提交时的签名为：  
            <%=signature %>  
        </p>   
	  <script type="text/javascript">
	    wx.config({
	        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
	        appId: '<%=appId %>', // 必填，公众号的唯一标识
	        timestamp:'<%=date %>', // 必填，生成签名的时间戳
	        nonceStr: '<%=nonceStr %>', // 必填，生成签名的随机串
	        signature: '<%=signature %>',// 必填，签名，见附录1
	        jsApiList: [
	           'getLocation',
	           'chooseImage'
	         ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
	    });
	    
	    wx.ready(function(){
	        // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
	    });
	    
	    wx.error(function(res){
	        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
	    });
	    
	    function getAdress(){
	    	wx.getLocation({
	    		type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
	    		success: function (res) {
	    		var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
	    		var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
	    		var speed = res.speed; // 速度，以米/每秒计
	    		var accuracy = res.accuracy; // 位置精度
	    		
	    		$.ajax({
			        url : "showLocation",  
		            type : "post",
		            data:{
		              "latitude":latitude,
		              "longitude":longitude,
		            },  
		            success:function(data){
                       if(data == "1"){
                    	   alert("获取位置成功");
                       }
		              
		            },
			    });
	    	}
	      });
	    }
	    
	    function chooseImage(){
	    	wx.chooseImage({
	    		count: 1, // 默认9
	    		sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
	    		sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
	    		success: function (res) {
	    		var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
	    	 }
	      });
	    }
	    
	    function findRecord(){
	    	window.location.href = "findRecord";
	    }
	  </script>
	    
	    
	    <button onclick = "getAdress();">获取当前位置打卡</button>
	    <button onclick = "chooseImage();">选择图片</button>
	    <button onclick = "findRecord();">查看打卡记录</button>
	    
	</body>
</html>






