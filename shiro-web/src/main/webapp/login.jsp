<%--
  Created by IntelliJ IDEA.
  User: cp7093
  Date: 2021-2-3
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h6>这里是login.jsp</h6>
<%--<a href="<c:url value="http://www.baidu.com"/>">发送登录认证请求</a><br>--%>
<a href="<c:url value="/authen?username=pomelo&password=123456"/>">发送登录认证请求</a><br>
<a href="<c:url value="/authen?username=pomelo&password=12"/>">模拟发送错误的帐号密码请求</a><br>
</body>
</html>
