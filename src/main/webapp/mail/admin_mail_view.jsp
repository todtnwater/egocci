<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.BusinessMailDto" %>
<%
String sessionLevel = (String)session.getAttribute("sessionLevel");
if(sessionLevel == null || !sessionLevel.equals("top")) {
    response.sendRedirect(request.getContextPath() + "/Index");
    return;
}

BusinessMailDto dto = (BusinessMailDto)request.getAttribute("t_dto");
if(dto == null) {
    response.sendRedirect(request.getContextPath() + "/Mail?t_gubun=adminList");
    return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 메일 상세</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/mail.css">
<script>
function goDelete(mailId){
    if(confirm("이 메일 기록을 삭제 처리하시겠습니까?")){
        location.href = "<%=request.getContextPath()%>/Mail?t_gubun=adminDelete&t_mail_id=" + mailId;
    }
}
</script>
</head>
<body class="mail-admin-body">
    <div class="mail-admin-wrap">
        <h2 class="mail-admin-title">관리자 메일 상세</h2>

        <table class="mail-view-table">
            <tr>
                <th>번호</th>
                <td><%=dto.getMailId()%></td>
            </tr>
            <tr>
                <th>보낸 사람 이름</th>
                <td><%=dto.getSenderName()%></td>
            </tr>
            <tr>
                <th>보낸 사람 이메일</th>
                <td><%=dto.getSenderEmail()%></td>
            </tr>
            <tr>
                <th>받는 사람 이메일</th>
                <td><%=dto.getRecipientEmail()%></td>
            </tr>
            <tr>
                <th>제목</th>
                <td><%=dto.getSubject()%></td>
            </tr>
            <tr>
                <th>상태</th>
                <td><%=dto.getSendStatus()%></td>
            </tr>
            <tr>
                <th>실패 사유</th>
                <td><%=dto.getFailReason() == null ? "-" : dto.getFailReason()%></td>
            </tr>
            <tr>
                <th>작성일</th>
                <td><%=dto.getCreatedAt()%></td>
            </tr>
            <tr>
                <th>발송일</th>
                <td><%=dto.getSentAt() == null ? "-" : dto.getSentAt()%></td>
            </tr>
            <tr>
                <th>내용</th>
                <td>
                    <div class="mail-view-content"><%=dto.getContent()%></div>
                </td>
            </tr>
        </table>

        <div class="mail-btn-area">
            <a href="<%=request.getContextPath()%>/Mail?t_gubun=adminList" class="mail-btn mail-btn-gray">목록</a>
            <button type="button" class="mail-btn" onclick="goDelete('<%=dto.getMailId()%>')">삭제</button>
        </div>
    </div>
</body>
</html>