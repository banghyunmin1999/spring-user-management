<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>프로젝트</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resources/index.css" />
</head>
<body>
	<div class="user-info">
		<c:choose>
			<c:when test="${isLogin}">
				<span><b>${userId}</b> 님 환영합니다!</span>
				<a href="${pageContext.request.contextPath}/mypage"
					class="mypage-link">마이페이지</a>
				<a href="${pageContext.request.contextPath}/logout">로그아웃</a>
			</c:when>
			<c:otherwise>
				<a href="${pageContext.request.contextPath}/login">로그인</a>
			</c:otherwise>
		</c:choose>
	</div>

	<main class="main-container">
		<h1>프로젝트 홈</h1>

		<div class="stats-container">
			<div class="stat-card">
				<div class="stat-value">${totalUsers}</div>
				<div class="stat-label">총 회원수</div>
			</div>
			<div class="stat-card">
				<div class="stat-value">${todayUsers}</div>
				<div class="stat-label">금일 가입자수</div>
			</div>
		</div>

		<div class="menu-container">
			<div class="menu-card">
				<a href="${pageContext.request.contextPath}/join">회원 가입</a>
			</div>
			<div class="menu-card">
				<a href="${pageContext.request.contextPath}/user_list">회원 리스트</a>
			</div>
		</div>
	</main>
</body>
</html>