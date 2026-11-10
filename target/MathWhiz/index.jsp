<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${initParam.appTitle}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <h1 class="title">${initParam.appTitle}</h1>

  <div class="card">
    <h2>Welcome, challenger!</h2>
    <p>Answer four maths questions (Prime, Modulus and two Trigonometry) and
       earn up to <strong>10 marks</strong>. Your score goes to the Top 10 leaderboard.</p>

    <c:if test="${not empty error}">
      <div class="error"><c:out value="${error}"/></div>
    </c:if>

    <form action="${pageContext.request.contextPath}/start" method="post">
      <label for="playerName">Player Name</label>
      <input type="text" id="playerName" name="playerName" maxlength="100"
             placeholder="Enter your name" autofocus required>
      <button class="btn" type="submit">Start Game</button>
      <a class="btn secondary" href="${pageContext.request.contextPath}/leaderboard">Leaderboard</a>
    </form>
  </div>

  <div class="footer">Student ID: ${initParam.studentId} &middot; Difficulty: NORMAL</div>
</body>
</html>
