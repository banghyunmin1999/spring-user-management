<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>LOGIN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/form.css" />
</head>
<body>
    <div class="form-container">
        <h1>로그인</h1>
        <form action="login_action" method="post">
            
            <div class="form-group">
                <label for="id">아이디</label>
                <input type="text" id="id" name="id" placeholder="ID 입력" class="form-control" required />
            </div>
            
            <div class="form-group">
                <label for="pwd">비밀번호</label>
                <input type="password" id="pwd" name="pwd" placeholder="PASSWORD 입력" class="form-control" required />
            </div>
            
            <button type="submit" class="submit-btn">확인</button>
        </form>

        <a href="${pageContext.request.contextPath}/join" class="home-link">아직 회원이 아니신가요? <b>회원가입</b></a>
    </div>

    <script>
        <c:if test="${param.error == 'true'}">
            alert("아이디 또는 비밀번호가 일치하지 않습니다.");
        </c:if>
    </script>

</body>
</html>