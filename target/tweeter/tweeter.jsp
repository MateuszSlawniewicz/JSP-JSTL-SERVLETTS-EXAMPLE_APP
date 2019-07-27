<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mateu
  Date: 21.07.2019
  Time: 09:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Tweeter</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h1>Tweeter</h1>
<c:if test="${param.submit == 'add'}">
    <jsp:useBean id="tweet" class="sd.tweets.model.Tweet" scope="request"/>
    <jsp:setProperty name="tweet" property="*"/>
    <jsp:forward page="/tweeter.jsp"/>
</c:if>


<c:forEach items="${requestScope.get('tweets')}" var="tweet">
    <div>
        <c:out value="${tweet.user.login}"/>
        <p>Text: </p>
        <div><c:out value="${tweet.message}"/></div>
        <c:out value="${tweet.date}"/>
        <br>
        <img src="${tweet.image}" alt="No Image"/>
        <c:if test="${{tweet.user.login}.equals({sessionScope.get('login')})}">
            <form method="get" action="edit_servlet">
                <input name="submit" type="submit" value="edit">
            </form>
            <form method="get" action="delete_servlet">
                <input name="submit" type="submit" value="delete">
            </form>
        </c:if>
    </div>
</c:forEach>


<div>

    <h4>New Tweet: </h4>
    <c:import url="tweetform.jsp"/>
</div>


</body>
</html>
