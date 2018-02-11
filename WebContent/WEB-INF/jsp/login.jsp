<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1.0,user-scalable=no" />
	<script type="text/javascript" src="<%=basePath%>js/jquery-1.11.1.min.js"></script>
    <title>登录</title>
</head>
<style>

.content{
    /* background-color: rgba(255, 255, 255, 0.95);
    width: 420px;
    height: 300px;
    border: 1px solid #000000;
    border-radius: 6px;
    padding: 10px; */
    margin-top: 20%;
    text-align: center;
  /*   margin-left: auto;
    margin-right: auto;
    display: block; */
}
   .login-input-box{
    margin-top: 12px;
    width: 100%;
    margin-left: auto;
    margin-right: auto;
    display: inline-block;
}

.login-input-box input{
    width: 250px;
    height: 35px;
    margin-left: 18px;
    border: 1px solid #dcdcdc;
    border-radius: 4px;
    padding-left: 15px;
}
.login-button-box{
    margin-top: 12px;
    width: 100%;
    margin-left: auto;
    margin-right: auto;
    display: inline-block;
}

.login-button-box button{
    background-color: #39867E;
    color: #ffffff;
    font-size: 16px;
    width: 270px;
    height: 40px;
    margin-left: 18px;
    border: 1px solid #39867E;
    border-radius: 4px;
}
.login_img{display:block;width:100px;margin:90px auto 30px;}
</style>
<body>
   <img src="<%=basePath%>image/login.png" class="login_img"/>
  <div class="content">
      <input type="text" id="openId" value='${openId}' style='display: none;'>
      <div class="login-input-box">
          <input type="text" id="userCode" placeholder="输入工号">
      </div>
      <div class="login-input-box">
           <input type="password" id="userPassWord" placeholder="输入密码">
      </div>
      <div class="login-button-box">
           <button type="button" id="login">登录</button>
      </div>
      <!-- <input type="checkbox" id="checked" checked disabled>
        <label for="checked">绑定微信登陆</label> -->
   </div>
  
</body>
</html>
<script type="text/javascript">
   $("#login").click(function(){
	   
	   var userCode = $("#userCode").val().trim();
	   var userPassWord = $("#userPassWord").val().trim();
	   var openId = $("#openId").val();
	   
	   if(userCode ==(null||"") || userPassWord ==(null||"")){
		  alert("工号或密码不能为空");
		  return false;
	   }
	   
	   $.ajax({
	       url : "userLogin",  
           type : "post",
           data:{
             "userCode":userCode,
             "userPassWord":userPassWord,
             "openId":openId
           },
           
           success:function(data){
              if(data == "1"){
            	  window.location.href= "wxjs_login?openId="+openId;
              }else if(data == "0"){
                  alert("工号或密码错误");
              }
           },
	    });
   })
</script>



