<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>회원 리스트</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/list.css" />
</head>
<body>
    <div class="list-container">
        <h1>회원 리스트</h1>
        
        <div class="user-card-list">
            <c:forEach var="user" items="${userList}">
                <div class="user-card">
                    <div class="user-card-info">
                        <div class="user-card-name">${user.name}</div>
                        <div class="user-card-details">
                            <span>📞 ${user.phone}</span>
                            <span>🏠 ${user.address}</span>
                        </div>
                    </div>
                    <div class="user-card-actions">
                        <a href="${pageContext.request.contextPath}/user_edit?idx=${user.idx}" class="edit-btn">수정</a>
                        <form action="${pageContext.request.contextPath}/delete" method="post">
                            <input type="hidden" name="idx" value="${user.idx}" />
                            <button type="submit" class="delete-btn" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</button>
                        </form>
                    </div>
                </div>
            </c:forEach>
        </div>

        <a href="${pageContext.request.contextPath}/" class="action-button">메인 화면으로</a>
    </div>
</body>
</html>