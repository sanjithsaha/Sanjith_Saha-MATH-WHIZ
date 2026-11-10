<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:if test="${empty sessionScope.questions}">
  <c:redirect url="/index.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>${initParam.appTitle} - Game</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
  <h1 class="title">${initParam.appTitle}</h1>

  <div class="card">
    <h2>Good luck, <c:out value="${sessionScope.playerName}"/>!</h2>

    <c:if test="${not empty errors}">
      <div class="error">Please fix the highlighted answers and submit again.</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/submit" method="post" autocomplete="off">
      <c:forEach var="q" items="${sessionScope.questions}" varStatus="st">
        <div class="question">
          <span class="marks">${q.marks} marks</span>
          <div><span class="qno">Q${st.index + 1}.</span> <c:out value="${q.text}"/></div>
          <input type="text" name="answer${st.index}" placeholder="Your answer"
                 value="<c:out value='${given[st.index]}'/>">
          <div class="hint"><c:out value="${q.hint}"/></div>
          <c:if test="${not empty errors[st.index]}">
            <div class="field-error"><c:out value="${errors[st.index]}"/></div>
          </c:if>
        </div>
      </c:forEach>
      <button class="btn" type="submit">Submit Answers</button>
    </form>
  </div>

  <div class="footer">Student ID: ${initParam.studentId}</div>
</body>
</html>
