<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.MemberDto" %>
<%
    String level = (String)session.getAttribute("sessionLevel");
    if(!"top".equals(level)) {
        response.sendRedirect("Index");
        return;
    }

    ArrayList<MemberDto> list = (ArrayList<MemberDto>)request.getAttribute("t_dtos");
%>

<%@ include file="../header.jsp" %>

<div class="content-row">
    <main class="main-content">
        <div class="content-title"><h2>회원 관리</h2></div>

        <table border="1" width="100%" cellpadding="10" cellspacing="0" style="background:#111;color:#fff;border-collapse:collapse;">
            <tr>
                <th>No</th>
                <th>Email</th>
                <th>Name</th>
                <th>Role</th>
                <th>탈퇴여부</th>
                <th>가입일</th>
            </tr>
            <%
            if(list != null) {
                for(MemberDto dto : list) {
            %>
            <tr style="cursor:pointer;" onclick="location.href='Manager?t_gubun=view&t_email=<%=dto.getEmail()%>'">
                <td><%=dto.getNo()%></td>
                <td><%=dto.getEmail()%></td>
                <td><%=dto.getName()%></td>
                <td><%=dto.getRole()%></td>
                <td><%=dto.getExited()%></td>
                <td><%=dto.getCreated_at()%></td>
            </tr>
            <%
                }
            }
            %>
        </table>
    </main>
</div>

<%@ include file="../footer.jsp" %>