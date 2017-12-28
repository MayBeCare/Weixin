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
	<title>打卡记录</title>
</head>
<style>
  img{width: 14px;cursor: pointer;}
  .nowDate{color: #db524a;display:inline;font-family:Verdana, Arial, Helvetica, sans-serif;}
</style>
<body>
    <p>${id}</p>
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
  </c:forEach>
</body>
</html>



