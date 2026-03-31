<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.BusinessMailDto" %>
<%
String sessionLevel = (String)session.getAttribute("sessionLevel");
if(sessionLevel == null || !sessionLevel.equals("top")) {
    response.sendRedirect(request.getContextPath() + "/Index");
    return;
}

ArrayList<BusinessMailDto> list = (ArrayList<BusinessMailDto>)request.getAttribute("t_dtos");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 메일 목록</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/mail.css">
</head>
<body class="mail-admin-body">
    <div class="mail-admin-wrap mail-admin-list-wrap">
        <h2 class="mail-admin-title">관리자 메일 목록</h2>

        <table class="mail-list-table">
            <tr>
                <th>번호</th>
                <th>보낸 사람</th>
                <th>보낸 이메일</th>
                <th>제목</th>
                <th>상태</th>
                <th>작성일</th>
                <th>발송일</th>
            </tr>
            <%
            if(list != null){
                for(BusinessMailDto dto : list){
            %>
            <tr onclick="location.href='<%=request.getContextPath()%>/Mail?t_gubun=adminView&t_mail_id=<%=dto.getMailId()%>'">
                <td><%=dto.getMailId()%></td>
                <td><%=dto.getSenderName()%></td>
                <td><%=dto.getSenderEmail()%></td>
                <td><%=dto.getSubject()%></td>
                <td><%=dto.getSendStatus()%></td>
                <td><%=dto.getCreatedAt()%></td>
                <td><%=dto.getSentAt() == null ? "-" : dto.getSentAt()%></td>
            </tr>
            <%
                }
            }
            %>
        </table>

        <div class="mail-btn-area">
            <a href="<%=request.getContextPath()%>/Manager?t_gubun=list" class="mail-btn">관리자 페이지로</a>
        </div>
    </div>
</body>
</html>