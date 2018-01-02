<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1.0,user-scalable=no" />
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.1.min.js"></script>
	<title>打卡记录</title>
</head>
<style>
    .nowDate{color: #db524a;display:inline;font-family:Verdana, Arial, Helvetica, sans-serif;}
  	*{margin:0;padding:0;}
	ul,li{list-style: none;}
	.sroll{width:100%;height:100%;overflow-x: hidden;}
	ul{width:300%;height:100%;}
	ul li{float:left;}
	h2{width:100%;text-align: center;line-height:50px;}
	.table{width:100%;border-collapse: collapse;}
	.table tr td,.table tr th{text-align:center;padding:8px 10px;font-size:14px;border:1px solid #ddd;}
	.top{position:relative;}
	.top p{width:100%;line-height:40px;text-align:center;font-size: 18px;font-weight:900;}
	.top img{display: block;width:20px;height: 20px;position:absolute;cursor:pointer;}
	.top img:nth-of-type(1){left:20px;top:10px;}
	.top img:nth-of-type(2){right:20px;top:10px;} 
</style>
<body>
	<h2>${id}</h2>
	<div class="sroll">
	   <ul>
		 <c:forEach items="${recordList }" var="list">
		    <li style="text-align:center;"> 
		    	<div class="top">
				   <img class="upper" alt="" src="<%=basePath%>image/left.png">   
			        <p class="nowDate">${list.key }</p>  
			       <img class="down" alt="" src="<%=basePath%>image/right.png"> 
				</div>
		     <table align="center" class="table">
		     	<thead>
				<tr>
					<th style="background:#eee">日期</th>
					<th style="background:#eee">开始时间</th>
					<th style="background:#eee">结束时间</th>
				</tr>
				</thead>
				<tbody>
				 <c:forEach items="${list.value }" var="v">
					<tr>
						<td>${fn:substring(v.clockDate, 5,16 ) }</td>
						<td>${v.startTime }</td>
						<td>${v.endTime }</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
			</li> 
		  </c:forEach>	
		</ul>
	</div>





   <%--  <p>${id}</p>
    <c:forEach items="${recordList }" var="list">
    <div style="text-align:center;">  
       <img alt="" src="<%=basePath%>image/left.png">   
        <p class="nowDate">${list.key }</p>  
       <img alt="" src="<%=basePath%>image/right.png"> 
     <table align="center">
		<tr>
			<th style="background:#eee">日期</th>
			<th style="background:#eee">开始时间</th>
			<th style="background:#eee">结束时间</th>
		</tr>
		 <c:forEach items="${list.value }" var="v">
			<tr>
				<td>${fn:substring(v.clockDate, 5,16 ) }</td>
				<td>${v.startTime }</td>
				<td>${v.endTime }</td>
			</tr>
		</c:forEach>
	</table>
	</div> 
  </c:forEach> --%>
</body>
</html>
<script type="text/javascript">
var w = document.documentElement.clientWidth; 
$(".sroll ul li").width( w );
	
$(function(){
	$(".upper").on("click",function(){
		var width = $(this).parents("li").width();
		var index = $(this).parents("li").index();
		if(index < 3){
			var wid = width * (index+1) + $(".sroll").scrollLeft();
			$(".sroll").scrollLeft(wid);
		}else{
			return 
		}
	})
	
	$(".down").on("click",function(){
		var width = $(this).parents("li").width();
		var index = $(this).parents("li").index();
		if(index > 0){
			var wid = $(".sroll").scrollLeft() - width ;
			$(".sroll").scrollLeft(wid);
		}else{
			return 
		}
	})
})
</script>




