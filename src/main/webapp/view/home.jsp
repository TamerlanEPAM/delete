<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<html>
<head>
    <title>Home</title>
</head>
<body>
<ol class="game-list">
    <c:forEach var="game" items="${games}">
        <c:out value="${game.getGameId}"/>
    </c:forEach>
</ol>
Hello
</body>
</html>
