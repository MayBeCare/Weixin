<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1.0,user-scalable=no" />
	<title>打卡记录</title>
</head>
<body>
    <p>${id}</p>
     <table>
		<tr>
			<th style="background:#eee">日期</th>
			<th style="background:#eee">开始时间</th>
			<th style="background:#eee">结束时间</th>
		</tr>
		 <c:forEach items="${recordList }" var="list">
			<tr>
				<td>${list.clockDate }</td>
				<td>${list.startTime }</td>
				<td>${list.endTime }</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>