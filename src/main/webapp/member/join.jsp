<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../header.jsp" %>

<script>
	let isIdChecked = false;
	let isIdAvailable = false;
	
	function checkId() {
	    const errorInput = document.getElementById('errorMsg');
	    const userId = document.getElementById('email').value;
	    const resultInput = document.querySelector('input[name="t_id_result"]');
	    
	    isIdChecked = false;
	    isIdAvailable = false;
	    
	    if (!userId) {
	        errorInput.value = '이메일을 입력하세요';
	        errorInput.style.color = 'red';
	        document.getElementById('email').focus();
	        return;
	    }
	    
	    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	    if (!emailPattern.test(userId)) {
	        errorInput.value = '올바른 이메일 형식이 아님!';
	        errorInput.style.color = 'red';
	        document.getElementById('email').focus();
	        return;
	    }
	    
	    resultInput.value = '확인 중...';
	    resultInput.style.color = 'blue';
	    
	    $.ajax({
	        url: 'MemberCheckId',
	        type: 'POST',
	        data: {
	            't_id': userId
	        },
	        success: function(data) {
	            isIdChecked = true;
	            
	            if(data.includes('사용가능')) {
	                isIdAvailable = true;
	                resultInput.value = '✅ 사용 가능';
	                resultInput.style.color = 'green';
	            } else {
	                isIdAvailable = false;
	                resultInput.value = '❌ 사용 불가';
	                resultInput.style.color = 'red';
	            }
	        },
	        error: function() {
	            isIdChecked = false;
	            isIdAvailable = false;
	            resultInput.value = '⚠️ 오류 발생';
	            resultInput.style.color = 'red';
	        }
	    });
	}
	
	function resetIdCheck() {
	    isIdChecked = false;
	    isIdAvailable = false;
	    const resultInput = document.querySelector('input[name="t_id_result"]');
	    resultInput.value = '';
	}
		
	function memberJoin() {
	    const errorInput = document.getElementById('errorMsg');
	    
	    const userId = document.getElementById('email').value;  
	    const userPw = document.getElementById('password').value; 
	    const userPwchk = document.getElementById('passwordChk').value;  
	    const userNa = document.getElementById('name').value;
	    
	    const phone1 = document.getElementById('phone1').value;
	    const phone2 = document.getElementById('phone2').value;
	    const phone3 = document.getElementById('phone3').value;
	    
	    if (!userId) {
	        errorInput.value = '이메일을 입력하세요';
	        errorInput.style.color = 'red';
	        document.getElementById('email').focus();
	        return;
	    }
	    
	    if (!isIdChecked) {
	        errorInput.value = 'ID 중복 체크 필요';
	        errorInput.style.color = 'red';
	        return;
	    }
	    
	    if (!isIdAvailable) {
	        errorInput.value = '사용 가능한 이메일로 변경';
	        errorInput.style.color = 'red';
	        document.getElementById('email').focus();
	        return;
	    }
	    
	    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
	    if (!emailPattern.test(userId)) {
	        errorInput.value = '올바른 이메일 형식 아님';
	        errorInput.style.color = 'red';
	        document.getElementById('email').focus();
	        return;
	    }
	    
	    if (!userPw) {
	        errorInput.value = '비밀번호를 입력하세요';
	        errorInput.style.color = 'red';
	        document.getElementById('password').focus();
	        return;
	    }
	    
	    if (userPw.length < 6) {
	        errorInput.value = '비밀번호 6자리 이상';
	        errorInput.style.color = 'red';
	        document.getElementById('password').focus();
	        return;
	    }
	    
	    if (!userPwchk) {
	        errorInput.value = '비밀번호 확인 입력';
	        errorInput.style.color = 'red';
	        document.getElementById('passwordChk').focus();
	        return;
	    }
	    
	    if (userPw !== userPwchk) {
	        errorInput.value = '비밀번호 불일치';
	        errorInput.style.color = 'red';
	        document.getElementById('password').focus();
	        return;
	    }
	    
	    if (!userNa) {
	        errorInput.value = '이름을 입력하세요';
	        errorInput.style.color = 'red';
	        document.getElementById('name').focus();
	        return;
	    }
	    
	    const hasAnyPhone = phone1 || phone2 || phone3;
	    if (hasAnyPhone) {
	        if (!phone1 || !phone2 || !phone3) {
	            errorInput.value = '전화번호 전체 입력';
	            errorInput.style.color = 'red';
	            if (!phone1) document.getElementById('phone1').focus();
	            else if (!phone2) document.getElementById('phone2').focus();
	            else if (!phone3) document.getElementById('phone3').focus();
	            return;
	        }
	        
	        if (!/^\d{2,3}$/.test(phone1) || !/^\d{3,4}$/.test(phone2) || !/^\d{4}$/.test(phone3)) {
	            errorInput.value = '전화번호 형식 오류';
	            errorInput.style.color = 'red';
	            document.getElementById('phone1').focus();
	            return;
	        }
	    }
	    
	    errorInput.value = '처리중...';
	    errorInput.style.color = 'blue';
	    errorInput.style.fontWeight = 'bold';
	    
	    $.ajax({
	        url: 'Member',
	        type: 'POST',
	        data: {
	            't_gubun': 'joinsave',
	            't_id': userId,
	            't_pw': userPw,
	            't_pwchk': userPwchk,
	            't_name': userNa,
	            't_phone1': phone1,
	            't_phone2': phone2,
	            't_phone3': phone3,
	            't_gender': document.getElementById('gender').value
	        },
	        success: function(data) {
	            if(data.includes('success') || data.includes('회원가입')) {
	                errorInput.value = '가입 성공!';
	                errorInput.style.color = 'green';
	                
	                setTimeout(function() {
	                    document.mem.t_gubun.value = "login";
	                    document.mem.method = "post";
	                    document.mem.action = "Member";
	                    document.mem.submit();
	                }, 1500);
	            } else {
	                errorInput.value = '가입 실패';
	                errorInput.style.color = 'red';
	            }
	        },
	        error: function(xhr, status, error) {
	            errorInput.value = '오류 발생';
	            errorInput.style.color = 'red';
	            console.error('AJAX Error:', error);
	        }
	    });
	}
	
	function cancelJoin() {
	    document.mem.t_gubun.value = "login";
	    document.mem.method = "post";
	    document.mem.action = "Member";
	    document.mem.submit();
	}
</script>

<div class="content-row">
     <!-- Main Content -->
        <main class="main-content">
         <!-- 콘텐츠 영역 1번 새로운 노래 유튜브영상/설명  -->
			<div class="login-title">
				<h2>Sign up</h2>
			</div>
				<div class="login-form">
				  <form name="mem">
				    <input type="hidden" name="t_gubun" value="join">
				    <div class="fl-clear2"><br></div>
				
				    <!-- Email -->
				    <div class="fl-clear">
				      <label for="email">Email*<br></label>
				      <input name="t_id" class="login-input" id="email" type="email" required onchange="resetIdCheck()">
				      <input type="button" onclick="checkId()" value="ID중복검사" class="join-box">
				      <input type="text" name="t_id_result" disabled class="error-input" >
				    </div>
				
				    <!-- Password -->
				    <div class="fl-clear">
				      <label for="password">비밀번호*<br></label>
				      <input name="t_pw" class="login-input" id="password" type="password" required>
				    </div>
				
				    <!-- Password 확인 -->
				    <div class="fl-clear">
				      <label for="passwordChk">비밀번호확인*<br></label>
				      <input name="t_pwchk" class="login-input" id="passwordChk" type="password" required>
				    </div>
				
				    <!-- 이름 -->
				    <div class="fl-clear">
				      <label for="name">이름*<br></label>
				      <input name="t_name" class="login-input" id="name" type="text" required>
				    </div>
				
				    <!-- 전화번호 -->
				    <div class="fl-clear">
				      <label for="phone1">전화번호<br></label>
				      <input name="t_phone1" class="loginp-input" id="phone1" type="text" maxlength="3">
						-
				      <input name="t_phone2" class="loginp-input" id="phone2" type="text" maxlength="4">
						-
				      <input name="t_phone3" class="loginp-input" id="phone3" type="text" maxlength="4">
				    </div>
				
				    <!-- 성별 -->
				    <div class="fl-clear">
				      <label for="gender">성별<br></label>
				      <select name="t_gender" class="login-input" id="gender">
				        <option value="M">남성</option>
				        <option value="F">여성</option>
				      </select>
				    </div>
				
				    <!-- 에러 메시지 -->
				    <div class="fl-clear2">
				    	<input name="error" class="error-input" id="errorMsg" type="text" readonly>
				    </div>
				
				    <!-- 버튼 -->
				    <div class="login-form2">
				      <a class="join-box" href="javascript:memberJoin()">회원가입</a>
				      <a class="login-box" href="javascript:cancelJoin()">취소</a>
				    </div>
				  </form>
				</div>
        </main>
<%@ include file="../footer.jsp" %>