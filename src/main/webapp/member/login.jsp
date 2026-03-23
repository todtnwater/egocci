<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../header.jsp" %>

<script>
	function goPassword(){
		document.getElementById('password').focus();
	}
	
	function memberLogin() {
	    const errorInput = document.getElementById('errorMsg');
	    
	    const userId = document.getElementById('email').value;
	    const userPw = document.getElementById('password').value;
	    
	    if (!userId) {
	        errorInput.value = '이메일을 입력해주세요.';
	        errorInput.style.color = 'red';
	        document.getElementById('email').focus();
	        return;
	    }
	    
	    if (!userPw) {
	        errorInput.value = '비밀번호를 입력해주세요.';
	        errorInput.style.color = 'red';
	        document.getElementById('password').focus();
	        return;
	    }
	    
	    errorInput.value = '로그인 처리중...';
	    errorInput.style.color = 'blue';
	    errorInput.style.fontWeight = 'bold';
	    
	    $.ajax({
	        url: 'Member',
	        type: 'POST',
	        data: {
	        	't_gubun': 'memberLogin',
	            't_id': userId,
	            't_pw': userPw
	        },
	        success: function(data) {
	            if(data.includes('환영합니다')) {  // success → 환영합니다
	                errorInput.value = '로그인 성공!';
	                errorInput.style.color = 'green';
	                
	                setTimeout(function() {
	                    location.href = 'Index';
	                });
	            } else {
	                errorInput.value = data;  // 서버에서 온 메시지 그대로 표시
	                errorInput.style.color = 'red';
	            }
	        },
	        error: function(xhr, status, error) {
	            errorInput.value = '로그인 중 오류 발생';
	            errorInput.style.color = 'red';
	        }
	    });
	}
	
	function memberJoin() {
	    document.mem.t_gubun.value = "join";
	    document.mem.method = "post";
	    document.mem.action = "Member";
	    document.mem.submit();
	}
</script>

<div class="content-row">
     <!-- Main Content -->
        <main class="main-content">
			<div class="login-title">
				<h2>LOGIN</h2>
			</div>
				<div class="login-form">
					<form name="mem">
						<input type="hidden" name="t_gubun" value="login">
						<div class="fl-clear2"><br></div>
						<div class="fl-clear">Email<br><input name="t_id" class="login-input" id="email" type="text" onkeypress="if( event.keyCode==13 ){goPassword()}"></div>
						<div class="fl-clear">비밀번호<br><input name="t_pw" class="login-input" id="password" type="password" onkeypress="if( event.keyCode==13 ){memberLogin()}"></div>
						<div class="fl-clear2"><input name="error" class="error-input" id="errorMsg" type="text" readonly></div>
						<div class="login-form2">
							<a class="login-box" href="javascript:memberLogin()">로그인</a>
							<a class="join-box" href="javascript:memberJoin()">회원가입</a>
						</div>
					</form>
				</div>
        </main>
<%@ include file="../footer.jsp" %>