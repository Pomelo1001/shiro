<%--
  Created by IntelliJ IDEA.
  User: cp7093
  Date: 2021-2-3
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h6>这里是login.jsp</h6>
<a href="<c:url value="/authen?username=jay&password=123456"/>">发送登录认证请求</a><br>
<a href="<c:url value="/authen?username=jay&password=12"/>">模拟发送错误的帐号密码请求</a><br>
</body>
</html>
