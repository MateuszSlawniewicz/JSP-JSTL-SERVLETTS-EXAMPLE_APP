<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"
             isELIgnored="false"
%>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="style.css">

</head>
<body>


<h1>Login Page</h1>
<c:if test="${param.get('submit').equalsIgnoreCase('login')}" >
      <c:out value="NIEPOPRAWNE DANE"/>
</c:if>
<form method="get" action="login_servlet">
    <div>
        <label>user</label>
        <input type="text" name="login"/>
    </div>
    <div>
        <label>password</label>
        <input type="password" name="password">
        <label></label>
        <input name="submit" type="submit" value="login">
    </div>
</form>



</body>
</html>