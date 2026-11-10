<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${initParam.appTitle} - Result</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <h1 class="title">${initParam.appTitle}</h1>

  <div class="card">
    <h2 class="center">Result for <c:out value="${playerName}"/></h2>
    <div class="score">${score} / 10</div>

    <table>
      <tr><th>Q</th><th>Your answer</th><th>Correct answer</th><th>Marks</th></tr>
      <c:forEach var="q" items="${questions}" varStatus="st">
        <tr>
          <td>Q${st.index + 1}</td>
          <td><c:out value="${given[st.index]}"/></td>
          <td><c:out value="${expected[st.index]}"/></td>
          <td>
            <c:choose>
              <c:when test="${correct[st.index]}"><span class="good">+${q.marks}</span></c:when>
              <c:otherwise><span class="bad">0</span></c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:forEach>
    </table>

    <div class="center">
      <a class="btn" href="${pageContext.request.contextPath}/index.jsp">Play Again</a>
      <a class="btn secondary" href="${pageContext.request.contextPath}/leaderboard">Leaderboard</a>
    </div>
  </div>

  <div class="footer">Student ID: ${initParam.studentId}</div>
</body>
</html>
