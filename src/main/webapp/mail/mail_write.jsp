<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String sessionId = (String)session.getAttribute("sessionId");
String userName = (String)session.getAttribute("userName");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>메일 보내기</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/mail.css">
<script>
function goSend(){
    var f = document.mailForm;

    if(f.t_subject.value.trim() == ""){
        alert("제목을 입력하세요.");
        f.t_subject.focus();
        return;
    }

    if(f.t_content.value.trim() == ""){
        alert("내용을 입력하세요.");
        f.t_content.focus();
        return;
    }

    f.submit();
}

function goClose(){
    history.back();
}
</script>
</head>
<body class="mail-write-body">
    <div class="mail-write-wrap">
        <div class="mail-close" onclick="goClose()">×</div>
        <div class="mail-write-title">Send Email</div>

        <div class="mail-image-box">
            <img src="<%=request.getContextPath()%>/images/default-song.jpg" alt="mail image">
        </div>

        <% if(sessionId != null && userName != null) { %>
        <form name="mailForm" method="post" action="<%=request.getContextPath()%>/Mail" class="mail-form-box">
            <input type="hidden" name="t_gubun" value="send">

            <input type="text" value="korangemgmt@gmail.com" readonly>
            <input type="text" name="t_sender_name" value="<%=userName%>" readonly>
            <input type="email" name="t_sender_email" value="<%=sessionId%>" readonly>
            <input type="text" name="t_subject" placeholder="Subject">
            <textarea name="t_content" placeholder="Your Message"></textarea>

            <button type="button" class="mail-send-btn" onclick="goSend()">Send Message</button>
        </form>
        <% } else { %>
        <div class="mail-login-guide">
            메일 전송은 로그인 후 이용 가능합니다.<br>
            <a href="<%=request.getContextPath()%>/Member" class="mail-login-btn">Login</a>
        </div>
        <% } %>
    </div>
</body>
</html>