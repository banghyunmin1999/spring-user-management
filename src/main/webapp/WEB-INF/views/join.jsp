<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>회원 가입</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/form.css" />
</head>
<body>

    <div class="form-container">
        <h1>회원가입</h1>
        <form action="join_action" method="post" id="joinForm">
            
            <div class="form-group">
                <label for="name">이름</label>
                <input type="text" id="name" name="name" placeholder="이름 입력" class="form-control" required />
            </div>
            
            <div class="form-group">
                <label for="id">아이디</label>
                <input type="text" id="id" name="id" placeholder="ID 입력" class="form-control" required />
            </div>
            
            <div class="form-group">
                <label for="pwd">비밀번호</label>
                <input type="password" id="pwd" name="pwd" placeholder="PASSWORD 입력" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="pwd2">비밀번호 확인</label>
                <input type="password" id="pwd2" name="pwd2" placeholder="PASSWORD 확인값 입력" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="phone">연락처</label>
                <input type="text" id="phone" name="phone" placeholder="연락처" class="form-control" required />
            </div>

            <div class="form-group">
                <label for="address">주소</label>
                <input type="text" id="address" name="address" placeholder="주소" class="form-control" required />
            </div>

            <button type="submit" class="submit-btn">확인</button>
        </form>
        
        <a href="${pageContext.request.contextPath}/" class="home-link">홈으로 돌아가기</a>
    </div>

    <script>
        const form = document.getElementById('joinForm');

        form.addEventListener('submit', function(event) {
            const pwd = document.getElementById('pwd').value;
            const pwd2 = document.getElementById('pwd2').value;

            // 두 비밀번호 값이 일치하지 않는 경우
            if (pwd !== pwd2) {
                // 사용자에게 경고창을 보여줍니다.
                alert('비밀번호가 일치하지 않습니다.');
                
                // form의 기본 제출 동작을 막습니다. (서버로 전송되는 것을 중단)
                event.preventDefault(); 
            }
        });
    </script>

</body>
</html>