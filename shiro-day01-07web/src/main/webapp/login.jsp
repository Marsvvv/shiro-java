<%--
  Created by IntelliJ IDEA.
  User: Tobu
  Date: 2020/12/18
  Time: 8:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/login" method="post">
    <table>
        <tr>
            <th>用户名</th>
            <td><input type="text" name="loginName"/></td>
        </tr>
        <tr>
            <th>密码</th>
            <td><input type="password" name="password"/></td>
        </tr>
        <tr>
            <td><input type="button" value="提交"></td>
        </tr>
    </table>
</form>
</body>
</html>
