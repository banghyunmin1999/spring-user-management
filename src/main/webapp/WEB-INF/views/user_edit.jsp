<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원정보 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/form.css" />
</head>
<body>
    <div class="form-container">
        <h1>회원 정보 수정</h1>
        <form action="${pageContext.request.contextPath}/user_edit_action" method="post">
            <input type="hidden" name="idx" value="${user.idx}" />

            <div class="form-group">
                <label for="name">이름</label>
                <input type="text" id="name" name="name" value="${user.name}" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="phone">연락처</label>
                <input type="text" id="phone" name="phone" value="${user.phone}" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="address">주소</label>
                <input type="text" id="address" name="address" value="${user.address}" class="form-control" required />
            </div>

            <button type="submit" class="submit-btn">수정 완료</button>
            <a href="${pageContext.request.contextPath}/user_list" class="cancel-btn">취소</a>
        </form>
    </div>
</body>
</html>