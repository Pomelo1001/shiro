<%--
  Created by IntelliJ IDEA.
  User: cp7093
  Date: 2021-2-4
  Time: 16:30
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>主页</title>
</head>
<body>
您已经登录成功<br>
<a href="<c:url value="/index.jsp"/>">回到首页</a><br>
<a href="<c:url value="/actions/admin"/> ">进入管理员页面</a><br>
<a href="<c:url value="/actions/logout"/> ">退出，此请求会被shiro的退出拦截器捕获</a>
</body>
<br>
欢迎<shiro:principal /><br>
欢迎<shiro:principal /><br>
欢迎<shiro:principal /><br>
</html>