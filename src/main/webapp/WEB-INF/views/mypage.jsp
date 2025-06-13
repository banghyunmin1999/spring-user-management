<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/form.css" />
</head>
<body>
    <div class="form-container">
        <h1>마이페이지</h1>
        <p class="mypage-intro">내 정보를 확인하고 수정할 수 있습니다.</p>
        
        <form action="${pageContext.request.contextPath}/mypage_action" method="post">
            
            <div class="form-group">
                <label>아이디</label>
                <input type="text" value="${user.id}" class="form-control" readonly />
            </div>

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

            <button type="submit" class="submit-btn">정보 수정</button>
            <a href="${pageContext.request.contextPath}/" class="cancel-btn">메인으로</a>
        </form>
    </div>
    
    <style>
        .form-control[readonly] {
            background-color: #f1f1f1;
            cursor: not-allowed;
        }
        .mypage-intro {
            color: #555;
            margin-top: -1.5rem;
            margin-bottom: 2rem;
        }
    </style>
</body>
</html>