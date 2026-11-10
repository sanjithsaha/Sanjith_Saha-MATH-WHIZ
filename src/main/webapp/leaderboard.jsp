<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${initParam.appTitle} - Leaderboard</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <h1 class="title">${initParam.appTitle}</h1>

  <div class="card">
    <h2>Top 10 Leaderboard</h2>
    <table>
      <tr><th>#</th><th>Player</th><th>Score</th><th>Difficulty</th><th>Date / Time</th></tr>
      <c:forEach var="s" items="${scores}" varStatus="st">
        <tr>
          <td>${st.count}</td>
          <td><c:out value="${s.playerName}"/></td>
          <td>${s.score}</td>
          <td>${s.difficulty}</td>
          <td><fmt:formatDate value="${s.playedAt}" pattern="dd MMM yyyy, HH:mm:ss"/></td>
        </tr>
      </c:forEach>
      <c:if test="${empty scores}">
        <tr><td colspan="5" class="center">No scores yet. Be the first to play!</td></tr>
      </c:if>
    </table>
    <div class="center">
      <a class="btn" href="${pageContext.request.contextPath}/index.jsp">Play Game</a>
    </div>
  </div>

  <div class="footer">Student ID: ${initParam.studentId}</div>
</body>
</html>
