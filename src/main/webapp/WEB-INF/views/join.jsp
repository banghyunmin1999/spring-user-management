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

            <div class="form-group">
                <label>사용자 유형</label>
                <div class="radio-group">
                    <input type="radio" id="guest" name="userType" value="guest" checked />
                    <label for="guest">일반 사용자</label>
                    <input type="radio" id="admin" name="userType" value="admin" />
                    <label for="admin">관리자</label>
                </div>
            </div>

            <div class="form-group" id="adminPasswordGroup" style="display: none;">
                <label for="adminPassword">관리자 비밀번호</label>
                <input type="password" id="adminPassword" name="adminPassword" placeholder="관리자 등록을 위한 비밀번호" class="form-control" />
            </div>

            <button type="submit" class="submit-btn">확인</button>
        </form>
        
        <a href="${pageContext.request.contextPath}/" class="home-link">홈으로 돌아가기</a>
    </div>

    <script>
        const form = document.getElementById('joinForm');
        const adminRadio = document.getElementById('admin');
        const guestRadio = document.getElementById('guest');
        const adminPasswordGroup = document.getElementById('adminPasswordGroup');
        const adminPasswordInput = document.getElementById('adminPassword');

        // 라디오 버튼 변경 시 관리자 비밀번호 필드 표시/숨김
        function toggleAdminPassword() {
            if (adminRadio.checked) {
                adminPasswordGroup.style.display = 'block';
                adminPasswordInput.required = true;
            } else {
                adminPasswordGroup.style.display = 'none';
                adminPasswordInput.required = false;
            }
        }

        adminRadio.addEventListener('change', toggleAdminPassword);
        guestRadio.addEventListener('change', toggleAdminPassword);

        form.addEventListener('submit', function(event) {
            const pwd = document.getElementById('pwd').value;
            const pwd2 = document.getElementById('pwd2').value;

            // 두 비밀번호 값이 일치하지 않는 경우
            if (pwd !== pwd2) {
                alert('비밀번호가 일치하지 않습니다.');
                event.preventDefault();
            }
        });

        // 에러 메시지 표시
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('error') === 'admin') {
            alert('관리자 비밀번호가 일치하지 않습니다.');
        }
    </script>

</body>
</html>